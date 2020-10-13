package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.ArrayUtil;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.model.domain.vo.EsGoodsVO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.model.domain.EsCartNumDO;
import com.shopx.member.api.model.domain.EsCommercelItemsDO;
import com.shopx.member.api.service.IEsCartService;
import com.shopx.member.api.service.IEsCommercelItemsService;
import com.shopx.member.api.service.IEsMemberCouponService;
import com.shopx.member.api.service.IEsSearchKeyWordService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsCouponDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.request.EsWapAddCartForm;
import com.shopx.trade.web.request.EsWapCheckCartForm;
import com.shopx.trade.web.request.EsWapGetCartForm;
import com.shopx.trade.web.request.query.EsWapCartSetPromotion;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 移动端-购物车 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/member/cart", tags = "移动端-购物车")
@RestController
@RequestMapping("/wap/member/cart")
public class EsWapCartController {

    @Autowired
    private CartManager cartManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSearchKeyWordService iEsSearchKeyWordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService iEsGoodsSkuService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCartService cartService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCommercelItemsService iEsCommercelItemsService;


    @ApiOperation(value = "向购物车中添加一个产品")
    @ResponseBody
    @PostMapping("/add")
    public ApiResponse add(@Valid @RequestBody EsWapAddCartForm cartForm) {
        try {
            Long skuId = cartForm.getSkuId();
            String num = String.valueOf(cartForm.getNum());
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
    @GetMapping()
    public ApiResponse cartQuery(@Valid EsWapGetCartForm getCartForm) {
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

        String showType = getCartForm.getShowType();
        String tab = getCartForm.getTab();
        try {
            // 读取所有的购物列表
            if (StringUtil.isEmpty(showType) || all.equals(showType)) {
                //List<CartVO> list = this.cartManager.getCartList();
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

    @ApiOperation(value = "选中和未选中后调用获取购物车列表",response = CartVO.class)
    @GetMapping(value = "/cartQueryCheck")
    public ApiResponse cartQueryCheck(EsWapGetCartForm getCartForm) {
        // 校验权限
        this.isAuth();
        this.cartManager.reviewGoods(null);
        // 全部商品
        String all = "all";
        //选中商品
        String checked = "checked";
        //tab页面降价商品
        String priceDown = "priceDown";
        //tab页面库存紧张
        String stockShortage = "stockShortage";

        String showType = getCartForm.getShowType();
        String tab = getCartForm.getTab();
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
                return ApiResponse.success(list);
            }

            // 读取选中的列表
            if (!StringUtil.isEmpty(showType) && checked.equals(showType)) {
                // 获取选中商品
                List<CartVO> list = this.cartManager.getCheckedItems(null);
                return ApiResponse.success(list);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(new ArrayList());
    }

    @ApiOperation(value = "清空购物车")
    @DeleteMapping("/del")
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

    @ApiOperation(value = "获取购物车总价",response =PriceDetailVO.class )
    @GetMapping(value = "/price")
    public ApiResponse totalPrice() {
        try {
            // 验证权限
            this.isAuth();
            PriceDetailVO priceDetail = tradePriceManager.getTradePrice(null);
            return ApiResponse.success(priceDetail);
        }catch (ArgumentException e){
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "获取设置过优惠券信息后的购物车总价",response =PriceDetailVO.class)
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

    @ApiOperation(value = "更新购物车中的多个产品的数量或选中状态")
    @ResponseBody
    @PostMapping(value = "/sku/checked")
    public ApiResponse update(@RequestBody EsWapCheckCartForm checkCartForm) {

        Integer checked = checkCartForm.getChecked();
        Long skuId = checkCartForm.getSkuId();
        Integer num = checkCartForm.getNum();
        List<CartVO> cartList = new ArrayList<>();
        try {
            this.isAuth();
            if (checked != null) {
                Long[] skuIds = new Long[1];
                skuIds[0] = skuId;
                cartList = this.cartManager.checked(skuIds, checked,null);

            }
            if (num != null) {
                DubboResult<EsGoodsSkuCO> goodsSku = this.iEsGoodsSkuService.getGoodsSku(skuId);
                Integer enableQuantity = goodsSku.getData().getEnableQuantity();
                if (num <= 0){
                    throw  new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(),"您的输入不合法！");
                }
                if (enableQuantity < num){
                   throw  new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
                }
                cartList = this.cartManager.updateNum(skuId, num,null);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

        return ApiResponse.success(cartList);
    }

    @ApiOperation(value = "批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuIds", value = "skuId数组", required = true, dataType = "int", paramType = "path", allowMultiple = true)
    })
    @DeleteMapping(value = "/sku/del/{skuIds}")
    public ApiResponse delete(@PathVariable Integer[] skuIds) {

        try{
            this.isAuth();
            List<CartVO> delete = this.cartManager.delete(skuIds,null);
            return ApiResponse.success(delete);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "全选或全不选")
    @ResponseBody
    @PostMapping(value = "/checked")
    public ApiResponse updateAll(@RequestBody EsWapGetCartForm getCartForm) {
        Integer checked = getCartForm.getChecked();
        this.isAuth();
        if (checked != null) {
            this.cartManager.checkedAll(checked,getCartForm.getTab(),null);
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "批量设置某商家的商品为选中或不选中")
    @ResponseBody
    @PostMapping(value = "/shop")
    public ApiResponse updateSellerAll(@RequestBody EsWapGetCartForm getCartForm) {
        this.isAuth();
         Integer checked = getCartForm.getChecked();
         Long shopId=getCartForm.getShopId();
        if (checked != null && shopId != null) {
            this.cartManager.checkedShopAll(shopId, checked,getCartForm.getTab(),null);
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "立即购买")
    @ResponseBody
    @PostMapping("/buy")
    public ApiResponse buy(@RequestBody EsWapAddCartForm cartForm) {

        Long skuId = cartForm.getSkuId();
        Integer num=cartForm.getNum();
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

    @ApiOperation(value = "获取tab页商品数量",response = EsTabCountVO.class)
    @ResponseBody
    @GetMapping(value = "/getTabCount")
    public ApiResponse getTabCount() {
        this.isAuth();
        EsTabCountVO esTabCountVO = this.cartManager.getCartTabCount(null);
        return ApiResponse.success(esTabCountVO);
    }





    @ApiOperation(value = "再次购买")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单号", dataType = "String", paramType = "query")})
    @ResponseBody
    @PostMapping("/buyAgain")
    public ApiResponse buyAgain(String orderSn) {
        try {
           //this.cartManager.buyAgain(orderSn,null);
            this.cartManager.buyAgainPC(orderSn,null);
            return ApiResponse.success();
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
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

    @ApiOperation(value = "购物车页面设置优惠活动V_2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID ", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "skuId", value = "商品项ID skuId", required = false, dataType = "Long", paramType = "query"),
    })
    @ResponseBody
    @PostMapping(value = "/setPromotion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse setPromotion(@RequestBody @Valid EsWapCartSetPromotion setPromotion) {
        this.isAuth();
        try {
            List<CartVO> cartVOS = this.cartManager.setPromotion(setPromotion.getActivityId(), setPromotion.getSkuId(),null);
            // 商品 活动层级切换
            this.Transformation(cartVOS);
            return ApiResponse.success(cartVOS);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    /**
     * @Description: 商品活动数据转换 用于前端展示
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/4/30 14:44
     */
    private void Transformation(List<CartVO> list) {
        // 根据活动组装商品数据（分组）
        list.stream().forEach(cartVO -> {
            List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
            // 通过活动类型分组
            Map<String, List<CartItemsVO>> collect1 = cartItemsList.stream()
                    .collect(Collectors.groupingBy(CartItemsVO::getPromotionType));

            List<PreferentialMessageVO> groupPromotionVoList = new ArrayList<>();
            for (String type:collect1.keySet()) {
                // 如果是满减组合活动 则把所有的满减活动商品放到活动集合内
                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(type)){
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                    preferentialMessageVO.setSkuList(collect1.get(type));
                    collect1.get(type).forEach(cartItemsVO -> {
                        BeanUtil.copyProperties(cartItemsVO.getPreferentialMessage(),preferentialMessageVO);
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());
                    });
                    groupPromotionVoList.add(preferentialMessageVO);
                }else if (PromotionTypeEnum.NO.name().equals(type)){
                    // 没有活动则 把所有的未参加活动的商品数据合并
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                    preferentialMessageVO.setSkuList(collect1.get(type));
                    collect1.get(type).forEach(cartItemsVO -> {
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());
                    });
                    groupPromotionVoList.add(preferentialMessageVO);
                }else{
                    // 如果是单品活动 则每个商品上面展示相应的活动信息  没有活动则不展示
                    List<CartItemsVO> cartItemsVOS = collect1.get(type);
                    cartItemsVOS.forEach(cartItemsVO -> {
                        PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                        List<CartItemsVO> itemsList = new ArrayList<>();
                        itemsList.add(cartItemsVO);
                        BeanUtil.copyProperties(cartItemsVO.getPreferentialMessage(),preferentialMessageVO);
                        preferentialMessageVO.setPromotionType(cartItemsVO.getPromotionType());
                        preferentialMessageVO.setSkuList(itemsList);
                        groupPromotionVoList.add(preferentialMessageVO);
                    });

                }
            }
            cartVO.setPreferentialList(groupPromotionVoList);
            cartVO.setCartItemsList(null);
        });
    }

}




