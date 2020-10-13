package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.ArrayUtil;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.service.IEsGoodsSkuService;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCartNumDO;
import com.shopx.member.api.model.domain.EsCommercelItemsDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.*;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序-购物车 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-30
 */
@Api(value = "/applet/member/cart", tags = "小程序-购物车")
@RestController
@RequestMapping("/applet/member/cart")
public class EsAppletCartController {

    @Autowired
    private CartManager cartManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsSkuService iEsGoodsSkuService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCartService cartService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCommercelItemsService iEsCommercelItemsService;


    @ApiOperation(value = "加入购物车")
    @ResponseBody
    @PostMapping("/add")
    public ApiResponse add(@RequestBody @Valid EsAppletAddCartForm cartForm) {
        try {
            //获取当前用户
            DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(cartForm.getSkey());
            if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
            }
            Long memberId = dubboResult.getData().getId();
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
            this.cartManager.addV2(skuId, buyerNum,cartForm.getSkey());
            return ApiResponse.success();
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }


    @ApiOperation(value = "获取购物车列表", response = CartVO.class)
    @GetMapping()
    @ResponseBody
    public ApiResponse cartQuery(@Valid EsAppletGetCartForm getCartForm) {
        // 校验权限
        this.isAuth(getCartForm.getSkey());

        // 检测是否有商家在商品加入购物车后，变动过商品价格。
        this.cartManager.reviewGoods(getCartForm.getSkey());

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
                List<CartVO> list = this.cartManager.getCartListCheck(getCartForm.getSkey());

                if (StringUtil.notEmpty(tab) && priceDown.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.priceDownCartList(list,getCartForm.getSkey());
                    }
                }else if (StringUtil.notEmpty(tab) && stockShortage.equals(tab)){
                    if (CollectionUtils.isNotEmpty(list)){
                        list = this.cartManager.stockShortageCartList(list,getCartForm.getSkey());
                    }
                }
                this.Transformation(list);
                return ApiResponse.success(list);
            }

            // 读取选中的列表
            if (!StringUtil.isEmpty(showType) && checked.equals(showType)) {
                // 获取选中商品
                List<CartVO> list = this.cartManager.getCheckedItems(getCartForm.getSkey());
                // 商品 活动层级切换
                this.Transformation(list);
                return ApiResponse.success(list);
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(new ArrayList());
    }

    @ApiOperation(value = "清空购物车")
    @DeleteMapping("/del/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    public ApiResponse clean(@PathVariable String skey) {
        // 校验权限
        this.isAuth(skey);
        try{
            cartManager.clean(skey);
            tradePriceManager.cleanPrice(skey);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "获取购物车总价",response =PriceDetailVO.class )
    @GetMapping(value = "/price/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    public ApiResponse totalPrice(@PathVariable String skey) {
        try {
            // 校验权限
            this.isAuth(skey);
            PriceDetailVO priceDetail = tradePriceManager.getTradePrice(skey);
            return ApiResponse.success(priceDetail);
        }catch (ArgumentException e){
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新购物车中产品的数量或选中状态")
    @ResponseBody
    @PostMapping(value = "/sku/checked")
    public ApiResponse update(@RequestBody @Valid EsAppletCheckCartForm checkCartForm) {

        Integer checked = checkCartForm.getChecked();
        Long skuId = checkCartForm.getSkuId();
        Integer num = checkCartForm.getNum();
        List<CartVO> cartList = new ArrayList<>();
        try {
            this.isAuth(checkCartForm.getSkey());
            if (checked != null) {
                Long[] skuIds = new Long[1];
                skuIds[0] = skuId;
                cartList = this.cartManager.checked(skuIds, checked,checkCartForm.getSkey());

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
                cartList = this.cartManager.updateNum(skuId, num,checkCartForm.getSkey());
            }
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.success(cartList);
    }

    @ApiOperation(value = "删除或批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuIds", value = "skuId数组", required = true, dataType = "int", paramType = "path", allowMultiple = true),
            @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    })
    @DeleteMapping(value = "/sku/del/{skuIds}/{skey}")
    public ApiResponse delete(@PathVariable Integer[] skuIds, @PathVariable String skey) {

        try{
            this.isAuth(skey);
            List<CartVO> delete = this.cartManager.delete(skuIds,skey);
            return ApiResponse.success(delete);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "全选或全不选")
    @ResponseBody
    @PostMapping(value = "/checked")
    public ApiResponse updateAll(@RequestBody @Valid EsAppletCheckedCartForm form) {
        this.isAuth(form.getSkey());
        this.cartManager.checkedAll(form.getChecked(),form.getTab(),form.getSkey());
        return ApiResponse.success();
    }

    @ApiOperation(value = "立即购买")
    @ResponseBody
    @PostMapping("/buy")
    public ApiResponse buy(@RequestBody @Valid EsAppletAddCartForm cartForm) {
        this.isAuth(cartForm.getSkey());
        Long skuId = cartForm.getSkuId();
        Integer num=cartForm.getNum();
        DubboResult<EsGoodsSkuCO> goodsSku = this.iEsGoodsSkuService.getGoodsSku(skuId);
        Integer enableQuantity = goodsSku.getData().getEnableQuantity();
        if (enableQuantity < num){
            throw  new ArgumentException(TradeErrorCode.OVERSELL_ERROR.getErrorCode(),TradeErrorCode.OVERSELL_ERROR.getErrorMsg());
        }
        try {
            this.cartManager.buy(skuId, num,cartForm.getSkey());
        } catch (Exception e) {
            return ApiResponse.fail(e.hashCode(),e.getMessage());
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "获取tab页商品数量",response = EsTabCountVO.class)
    @GetMapping(value = "/getTabCount/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getTabCount(@PathVariable String skey) {
        this.isAuth(skey);
        EsTabCountVO esTabCountVO = this.cartManager.getCartTabCount(skey);
        return ApiResponse.success(esTabCountVO);
    }

    @ApiOperation(value = "再次购买")
    @PostMapping("/buyAgain")
    @ResponseBody
    public ApiResponse buyAgain(@Valid EsAppletBuyAgainForm form) {
        try {
            this.cartManager.buyAgainPC(form.getOrderSn(),form.getSkey());
            return ApiResponse.success();
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }

    }

    /**
     * 校验权限
     */
    private void isAuth(String skey){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            throw new ArgumentException(TradeErrorCode.NOT_LOGIN.getErrorCode(),TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
    }

    //商品活动数据转换 用于前端展示
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




