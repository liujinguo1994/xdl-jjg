package com.xdl.jjg.web.controller.pc.trade;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.*;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.model.domain.vo.EsGoodsVO;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCartNumDO;
import com.shopx.member.api.model.domain.EsCommercelItemsDO;
import com.shopx.member.api.model.domain.EsMemberCollectionGoodsDO;
import com.shopx.member.api.model.domain.dto.EsMemberCollectionGoodsDTO;
import com.shopx.member.api.service.*;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: CartController
 * @Description: 购物车模块
 * @Author: libw  981087977@qq.com
 * @Date: 6/10/2019 20:15
 * @Version: 1.0
 */
@Api(value = "/cart", tags = "购物车模块控制层")
@RestController
@RequestMapping("/cart")
public class CartController {
    private static Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private CartManager cartManager;
    @Autowired
    private TradePriceManager tradePriceManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSearchKeyWordService iEsSearchKeyWordService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService iEsGoodsSkuService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCommercelItemsService iEsCommercelItemsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCartService cartService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 10000, check = false)
    private IEsMemberCollectionGoodsService memberCollectionGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;



    @ApiOperation(value = "向购物车中添加一个产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "产品ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "此产品的购买数量", required = true, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse add(@NotNull(message = "产品id不能为空") Long skuId, @NotNull(message = "购买数量不能为空") String num) {
        try {
            DubboResult<EsGoodsSkuCO> goodsSku = this.iEsGoodsSkuService.getGoodsSku(skuId);
            // 验证商品库存
            if (!goodsSku.isSuccess()) {
                throw new ArgumentException(goodsSku.getCode(), goodsSku.getMsg());
            }
            Integer enableQuantity = goodsSku.getData().getEnableQuantity() == null ? 0 : goodsSku.getData().getEnableQuantity();

            if(!ArrayUtil.isNumeric(num)|| "0".equals(num)){
                return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode() ,"请确认购买数量是否正确");
            }
            // 获取操作人id（会员id）
            Long memberId = ShiroKit.getUser().getId();
            DubboResult<EsCartNumDO> byMemberId = cartService.getByMemberId(memberId);
            if (!byMemberId.isSuccess()) {
                throw new ArgumentException(byMemberId.getCode(), byMemberId.getMsg());
            }
            EsCartNumDO data = byMemberId.getData();
            Integer buyGoodsNum = 0;
            Integer buyerNum = Integer.parseInt(num);
            // 如果购物车项中有添加商品 则 需要验证加上购物车项的商品数量 进行商品库存验证
            if(data != null){
                DubboResult<EsCommercelItemsDO> itemsBySkuId = iEsCommercelItemsService.getItemsBySkuId(skuId, data.getCartId());
                if (!itemsBySkuId.isSuccess()){
                    throw new ArgumentException(itemsBySkuId.getCode(), itemsBySkuId.getMsg());
                }
                Integer quantity = 0;
                if (itemsBySkuId.getData() != null){
                    quantity = itemsBySkuId.getData().getQuantity();
                }
                //添加购物车中的商品和已经添加购物车商品数量之和
                buyGoodsNum = MathUtil.add(quantity, buyerNum).intValue();
            }else {
                buyGoodsNum = buyerNum;
            }

            if (buyGoodsNum > enableQuantity){
                throw  new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
            }
            this.cartManager.addV2(skuId, buyerNum,null);
            return ApiResponse.success();
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

    }

    @ApiOperation(value = "获取购物车列表", notes = "通过showType来控制显示方式，空或all为获取所有，checked为只获取选中的",response = CartVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "showType", value = "显示方式", required = false, dataType = "String", paramType =
                    "query", allowableValues = "all,checked"),
            @ApiImplicitParam(name = "tab", value = "TAB页显示", required = false, dataType = "String", paramType =
                    "query", allowableValues = "priceDown,stockShortage"),
    })
    @GetMapping()
    public ApiResponse cartQuery(String showType,String tab) {
        // 校验权限
        this.isAuth();

        // 检测是否有商家在商品加入购物车后，变动过商品价格。
        this.cartManager.reviewGoods(null);

        // 全部商品

        String all = "all";
        //选中商品
        String checked = "checked";
        //tab页面降价商品
        String priceDown = "priceDown";
        //tab页面库存紧张
        String stockShortage = "stockShortage";

        try {
            // 读取所有的购物列表
            if (StringUtil.isEmpty(showType) || all.equals(showType)) {
//                List<CartVO> list = this.cartManager.getCartList();
                List<CartVO> list = this.cartManager.getCartListCheck(null);

                if (StringUtil.notEmpty(tab) && priceDown.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.priceDownCartList(list,null);
                    }
                }else if (StringUtil.notEmpty(tab) && stockShortage.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.stockShortageCartList(list,null);
                    }
                }
                // 商品 活动层级切换
                this.Transformation(list);
                return ApiResponse.success(list);
            }

            // 读取选中的列表
            if (!StringUtil.isEmpty(showType) && checked.equals(showType)) {
                // 获取选中商品
                List<CartVO> list = this.cartManager.getCheckedItems(null);
                // 商品 活动层级切换
                this.Transformation(list);
                return ApiResponse.success(list);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(new ArrayList());
    }

    @ApiOperation(value = "获取购物车列表选中和未选中后调用",response = CartVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "showType", value = "显示方式", required = false, dataType = "String", paramType =
                    "query", allowableValues = "all,checked"),
            @ApiImplicitParam(name = "tab", value = "TAB页显示", required = false, dataType = "String", paramType =
                    "query", allowableValues = "priceDown,stockShortage"),
    })
    @GetMapping(value = "/cartQueryCheck")
    public ApiResponse cartQueryCheck(String showType,String tab) {
        // 校验权限
        this.isAuth();

        // 检测是否有商家在商品加入购物车后，变动过商品价格。
        this.cartManager.reviewGoods(null);

        // 全部商品
        String all = "all";
        //选中商品
        String checked = "checked";
        //tab页面降价商品
        String priceDown = "priceDown";
        //tab页面库存紧张
        String stockShortage = "stockShortage";

        try {
            // 读取所有的购物列表
            if (StringUtil.isEmpty(showType) || all.equals(showType)) {
                List<CartVO> list = this.cartManager.getCartListCheck(null);

                if (StringUtil.notEmpty(tab) && priceDown.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.priceDownCartList(list,null);
                    }
                }else if (StringUtil.notEmpty(tab) && stockShortage.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.stockShortageCartList(list,null);
                    }
                }
                // 商品 活动层级切换
                this.Transformation(list);
                return ApiResponse.success(list);
            }

            // 读取选中的列表
            if (!StringUtil.isEmpty(showType) && checked.equals(showType)) {
                // 获取选中商品
                List<CartVO> list = this.cartManager.getCheckedItems(null);
                // 商品 活动层级切换
                this.Transformation(list);
                return ApiResponse.success(list);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(new ArrayList());
    }


    /**
     * @Description: 商品活动数据转换 用于前端展示
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/4/30 14:44
     */
    private void Transformation(List<CartVO> list) {
        // 根据活动组装商品数据（分组）
        list.stream().forEach(cartVO -> {
            // 根据加入购物车的时间进行降序 保证新加入商品在上面
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList().stream()
                    .sorted(Comparator.comparing(CartItemsVO::getLastModify).reversed()).collect(Collectors.toList());
            // 通过活动类型分组
            LinkedHashMap<String, List<CartItemsVO>> collect1 = cartItemsList.stream()
                    .collect(Collectors.groupingBy(CartItemsVO::getPromotionType,LinkedHashMap::new,Collectors.toList()));

            List<PreferentialMessageVO> groupPromotionVoList = new ArrayList<>();
            for (String type:collect1.keySet()) {
                // 如果是满减组合活动 则把所有的满减活动商品放到活动集合内
                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(type)){
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                    // 根据选中状态降序
//                    List<CartItemsVO> collect = collect1.get(type).stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                    preferentialMessageVO.setSkuList(collect1.get(type));
                    collect1.get(type).forEach(cartItemsVO -> {
                        List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                        promotionList.stream().forEach(tradePromotionGoodsVO -> {
                            String content = tradePromotionGoodsVO.getContent();
                            tradePromotionGoodsVO.setTitle(content);
                        });
                        BeanUtil.copyProperties(cartItemsVO.getPreferentialMessage(),preferentialMessageVO);
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());

                    });
                    groupPromotionVoList.add(preferentialMessageVO);
                }else if (PromotionTypeEnum.NO.name().equals(type)){
                    // 没有活动则 把所有的未参加活动的商品数据合并
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                   preferentialMessageVO.setSkuList(collect1.get(type));
                    collect1.get(type).forEach(cartItemsVO -> {
                        List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                        promotionList.stream().forEach(tradePromotionGoodsVO -> {
                            String content = tradePromotionGoodsVO.getContent();
                            tradePromotionGoodsVO.setTitle(content);
                        });
                        BeanUtil.copyProperties(cartItemsVO.getPreferentialMessage(),preferentialMessageVO);
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());
                    });
                    groupPromotionVoList.add(preferentialMessageVO);
                }else {
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                    // 根据选中状态降序
//                    List<CartItemsVO> collect = collect1.get(type).stream().sorted(Comparator.comparing(CartItemsVO::getChecked).reversed()).collect(Collectors.toList());
                    List<CartItemsVO> cartItemsVOS = collect1.get(type);
                    preferentialMessageVO.setSkuList(cartItemsVOS);
                    collect1.get(type).forEach(cartItemsVO -> {
                        List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                        promotionList.stream().forEach(tradePromotionGoodsVO -> {
                            String content = tradePromotionGoodsVO.getContent();
                            tradePromotionGoodsVO.setTitle(content);
                        });
                        BeanUtil.copyProperties(cartItemsVO.getPreferentialMessage(),preferentialMessageVO);
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());
                        Double total = 0.0;
                        Double price = 0.0;
                        for (CartItemsVO cartItemsVO1 : cartItemsVOS) {
                            PreferentialMessageVO preferentialMessage = cartItemsVO1.getPreferentialMessage();
                            Double v1 = preferentialMessage.getPreferentialTotal() == null ? 0.0 : preferentialMessage.getPreferentialTotal();
                            Double v2 = preferentialMessage.getPreferentialPrice() == null ? 0.0 : preferentialMessage.getPreferentialPrice();
                            total = total + v1;
                            price = price + v2;
                        }
                        preferentialMessageVO.setPreferentialPrice(price);
                        preferentialMessageVO.setPreferentialTotal(total);

                    });
                    groupPromotionVoList.add(preferentialMessageVO);
                }
            }

            cartVO.setPreferentialList(groupPromotionVoList);
//            cartVO.setCartItemsList(null);
        });
    }

    @ApiOperation(value = "清空购物车")
    @DeleteMapping()
    public ApiResponse clean() {
        // 验证权限
        this.isAuth();
        try{
            cartManager.clean(null);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

        this.tradePriceManager.cleanPrice(null);
        return ApiResponse.success();
    }

    @ApiOperation(value = "获取购物车总价")
    @GetMapping(value = "/price")
    public ApiResponse totalPrice() {
        try {
            // 验证权限
            this.isAuth();
            PriceDetailVO priceDetail = tradePriceManager.getTradePrice(null);
            return ApiResponse.success(priceDetail);
        }catch (ArgumentException e){
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }catch (Exception e){
             return ApiResponse.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "获取设置过优惠券信息后的购物车总价")
    @GetMapping(value = "/getSetCouponPrice")
    public ApiResponse getSetCouponPrice() {
        try {
            // 验证权限
            this.isAuth();

            PriceDetailVO priceDetail = tradePriceManager.getTradePrice(null);
            return ApiResponse.success(priceDetail);
        }catch (ArgumentException e){
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新购物车中的多个产品", notes = "更新购物车中的多个产品的数量或选中状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku_id", value = "产品id数组", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "checked", value = "是否选中", dataType = "int", paramType = "query", allowableValues = "0,1"),
            @ApiImplicitParam(name = "num", value = "产品数量", dataType = "int", paramType = "query"),
    })
    @ResponseBody
    @PostMapping(value = "/sku/{sku_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse update(@ApiIgnore @NotNull(message = "产品id不能为空") @PathVariable(name = "sku_id") Long skuId,
                              @Min(value = 0) @Max(value = 1) Integer checked, @Min(value = 0)Integer num) {

        List<CartVO> cartList = new ArrayList<>();
        try {
            this.isAuth();
            if (checked != null) {
                Long[] skuIds = new Long[1];
                skuIds[0] = skuId;
                cartList = this.cartManager.checked(skuIds, checked,null);

            } else if (num != null) {
                DubboResult<EsGoodsSkuCO> goodsSku = this.iEsGoodsSkuService.getGoodsSku(skuId);
                Integer enableQuantity = goodsSku.getData().getEnableQuantity();
                if (num <= 0){
                    throw  new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(),"您的输入不合法！");
                }
                if (enableQuantity < num){
                   throw  new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
                }
                cartList = this.cartManager.updateNum(skuId, num,null);
                // 商品 活动层级切换
                this.Transformation(cartList);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

        return ApiResponse.success(cartList);
    }

    @ApiOperation(value = "删除购物车中的一个或多个产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku_ids", value = "产品id，多个产品可以用英文逗号：(,) 隔开", required = true, dataType = "int", paramType = "path", allowMultiple = true),
    })
    @DeleteMapping(value = "/{sku_ids}/sku")
    public ApiResponse delete(@PathVariable(name = "sku_ids") Integer[] skuIds) {

        try{
            this.isAuth();
            List<CartVO> delete = this.cartManager.delete(skuIds,null);
            return ApiResponse.success(delete);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "设置全部商为选中或不选中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "checked", value = "是否选中", required = true, dataType = "int", paramType = "query", allowableValues = "0,1"),
            @ApiImplicitParam(name = "tab", value = "入口标识(全部商品，priceDown 降价商品，stockShortage 库存紧缺)", required = false, dataType = "String", paramType =
                    "query", allowableValues = "priceDown,stockShortage"),
    })
    @ResponseBody
    @PostMapping(value = "/checked", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateAll(@NotNull(message = "必须指定是否选中") @Min(value = 0, message = "是否选中参数异常") @Max(value = 1,message = "是否选中参数异常") Integer checked,
                                 String tab) {
        this.isAuth();
        if (checked != null) {
            this.cartManager.checkedAll(checked,tab,null);
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "批量设置某商家的商品为选中或不选中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "checked", value = "是否选中", required = true, dataType = "int", paramType = "query", allowableValues = "0,1"),
            @ApiImplicitParam(name = "tab", value = "入口标识(全部商品，priceDown 降价商品，stockShortage 库存紧缺)", required = false, dataType = "String", paramType =
                    "query", allowableValues = "priceDown,stockShortage"),
    })
    @ResponseBody
    @PostMapping(value = "/shop/{shop_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateSellerAll(@NotNull(message = "店铺id不能为空") @PathVariable(name = "shop_id") Long shopId,
                                  @NotNull(message = "必须指定是否选中") @Min(value = 0) @Max(value = 1) Integer checked,
                                       String tab) {
        this.isAuth();
        if (checked != null && shopId != null) {
            this.cartManager.checkedShopAll(shopId, checked,tab,null);
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "立即购买")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "产品ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "此产品的购买数量", required = true, dataType = "int", paramType = "query"),
    })
    @ResponseBody
    @PostMapping("/buy")
    public ApiResponse buy(@ApiIgnore @NotNull(message = "产品id不能为空") Long skuId,
                      @ApiIgnore @NotNull(message = "购买数量不能为空") Integer num) {

        DubboResult<EsGoodsSkuCO> goodsSku = this.iEsGoodsSkuService.getGoodsSku(skuId);
        Integer enableQuantity = goodsSku.getData().getEnableQuantity();
        if (enableQuantity < num){
            throw  new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
        }
        try {
            this.cartManager.buy(skuId, num,null);
        } catch (Exception e) {
            return ApiResponse.fail(e.hashCode(),e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     * 校验权限
     */
    private void isAuth(){
        if(ShiroKit.getUser() == null){
            throw new ArgumentException(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),
                    TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
        }
    }

    @ApiOperation(value = "购物车页面 推荐接口",response = EsGoodsVO.class)
    @GetMapping(value = "/getRecommendedForYou")
    @ResponseBody
    public ApiResponse getRecommendedForYouList() {
        // 登录的情况
        if(ShiroKit.getUser() != null){
            Long memberId  = ShiroKit.getUser().getId();
            // 调用搜索历史接口 获取结算页面 获取可使用的优惠券列表word list
            DubboPageResult searchKeyWord = iEsSearchKeyWordService.getSearchKeyWord(memberId);
            List list1 = searchKeyWord.getData().getList();
            String[] goodsName = (String[]) list1.toArray(new String[list1.size()]);
            DubboPageResult<EsGoodsDO> pageResult = iEsGoodsService.getRecommendForYouGoods(goodsName);
            if (pageResult.isSuccess()) {
                List<EsGoodsVO> esGoodsVOS = BeanUtil.copyList(pageResult.getData().getList(), EsGoodsVO.class);
                return ApiResponse.success(esGoodsVOS);
            }else{
                return ApiResponse.fail(ApiStatus.wrapperException(pageResult));
            }
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "购物车页面获取该店铺的优惠券列表V-2",response = EsCouponVO.class)
    @GetMapping(value = "/getEsCouponListByShopId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shop_id", value = "店铺Id", required = true, dataType = "long", paramType = "query")})
    @ResponseBody
    public ApiResponse getEsCouponListByShopId(@ApiIgnore @NotEmpty(message = "店铺ID") Long shop_id) {
        // 登录的情况
        if(ShiroKit.getUser() == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId  = ShiroKit.getUser().getId();
        // 调用会员优惠券列表接口 获取coupon list
        DubboPageResult<EsCouponDO> esCouponList = iEsCouponService.getEsCouponListByShopId(shop_id);
        List<EsCouponDO> couponDOList = esCouponList.getData().getList();

        List<EsCouponVO> esCouponVOS = BeanUtil.copyList(couponDOList, EsCouponVO.class);

        List<Long> couponDOIds = esCouponVOS.stream().map(EsCouponVO::getId).collect(Collectors.toList());

        DubboResult result = iesMemberCouponService.getMemberWhetherCouponIds(memberId, couponDOIds);
        List<Long> list = (List<Long>)result.getData();
        if(null != list){
            esCouponVOS = esCouponVOS.stream().map(e -> {
                if(list.contains(e.getId())){
                    e.setIsReceive(1);
                    return e;
                }
                e.setIsReceive(0);
                return e;
            }).collect(Collectors.toList());
        }
        return ApiResponse.success(esCouponVOS);
    }

    @ApiOperation(value = "购物车页面设置优惠活动V_2",response = CartVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID ", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "skuId", value = "商品项ID skuId", required = false, dataType = "Long", paramType = "query"),
    })
    @ResponseBody
    @PostMapping(value = "/setPromotion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse setPromotion(Long activityId,Long skuId) {
//        this.isAuth();
        try {
            if (activityId != null && skuId != null) {
                List<CartVO> cartVOS = this.cartManager.setPromotion(activityId, skuId,null);
                // 商品 活动层级切换
                this.Transformation(cartVOS);
                return ApiResponse.success(cartVOS);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(new ArrayList());
    }


    @ApiOperation(value = "查询购物车的数量")
    @GetMapping(value = "/cartNum")
    public ApiResponse getCartNum() {
        ShiroUser buyer = ShiroKit.getUser();
        Integer num = 0;
        if (buyer != null) {
            // 通过会员ID 获取购物车数量

            List<CartVO> cacheCartList = cartManager.getCacheCartList(null);
            for (CartVO cartVO : cacheCartList) {
                List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                for (CartItemsVO cartItemsVO : cartItemsList) {
                    num = num + cartItemsVO.getNum();
                }
            }
            return ApiResponse.success(num);
        }
        return ApiResponse.success(num);
    }

    @ApiOperation(value = "加入购物车后跳转加入成功页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "产品ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "此产品的购买数量",dataType = "int", paramType = "query"),
    })
    @ResponseBody
    @GetMapping(value = "/jump")
    public ApiResponse jump(@NotNull(message = "产品id不能为空") Long skuId, Integer num) {
        try {
            Long memberId = ShiroKit.getUser().getId();
            if (null == memberId) {
                return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
            }
            CartItemsVO cartItemsVO1 = new CartItemsVO();
            DubboResult<EsGoodsSkuCO> goodsSku = iEsGoodsSkuService.getGoodsSku(skuId);

            if (goodsSku.isSuccess()) {
                EsGoodsSkuCO data = goodsSku.getData();
                String specs = data.getSpecs();
                List<EsSpecValuesVO> esSpecValuesVOS = JsonUtil.jsonToList(specs, EsSpecValuesVO.class);
                cartItemsVO1.setSpecList(esSpecValuesVOS);
                cartItemsVO1.setGoodsImage(data.getThumbnail());
                cartItemsVO1.setName(data.getGoodsName());
                cartItemsVO1.setNum(num);
                cartItemsVO1.setGoodsId(data.getGoodsId());
                cartItemsVO1.setShopId(data.getShopId());
                cartItemsVO1.setCategoryId(data.getCategoryId());
            }
            return ApiResponse.success(cartItemsVO1);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

    }

    // TODO  需求去确定
    @PostMapping(value = "/collection/insertGoods/{goodsId}")
    @ApiOperation(value = "添加商品收藏")
    @ApiImplicitParam(name = "goodsId",value = "商品ID",dataType = "long",paramType = "path",required = true,example = "1")
    public ApiResponse insertGoods(@PathVariable("goodsId") Long goodsId){
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO = new EsMemberCollectionGoodsDTO();
        esMemberCollectionGoodsDTO.setGoodsId(goodsId);
        esMemberCollectionGoodsDTO.setMemberId(userId);
        DubboResult<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDODubboResult = memberCollectionGoodsService.insertMemberCollectionGood(esMemberCollectionGoodsDTO);
        if(esMemberCollectionGoodsDODubboResult.isSuccess()){
            return ApiResponse.success(esMemberCollectionGoodsDODubboResult.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esMemberCollectionGoodsDODubboResult));
    }
}




