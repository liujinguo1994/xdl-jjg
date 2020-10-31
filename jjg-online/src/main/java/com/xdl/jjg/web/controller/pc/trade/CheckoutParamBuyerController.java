package com.xdl.jjg.web.controller.pc.trade;


import com.jjg.trade.model.enums.PaymentTypeEnum;
import com.jjg.trade.model.enums.ReceiptTypeEnum;
import com.jjg.trade.model.form.DeliveryMessageForm;
import com.jjg.trade.model.vo.CheckoutParamVO;
import com.jjg.trade.model.vo.PriceDetailVO;
import com.jjg.trade.model.vo.ReceiptVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.manager.CartManager;
import com.xdl.jjg.manager.CheckoutParamManager;
import com.xdl.jjg.manager.TradePriceManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 结算参数控制器
 *
 * @Author: libw  981087977@qq.com
 * @Date: 7/02/2019 10:40
 * @Version: 1.0
 */
@Api(value = "/checkout-params", tags = "结算参数接口模块")
@RestController
@RequestMapping("/checkout-params")
@Validated
public class CheckoutParamBuyerController {

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Autowired
    private CartManager cartManager;

    @ApiOperation(value = "设置收货地址id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address_id", value = "收货地址id", required = true, dataType = "int", paramType = "path"),
    })
    @PostMapping(value = "/address-id/{address_id}")
    public ApiResponse setAddressId(@NotNull(message = "必须指定收货地址id") @PathVariable(value = "address_id") Long addressId) {

        //读取结算参数
        CheckoutParamVO checkoutParamVO = this.checkoutParamManager.getParam(null);

        Long addressId1 = checkoutParamVO.getAddressId();
        if (addressId1 == null){
            //设置收货地址
            this.checkoutParamManager.setAddressId(addressId,null);
            //读取结算参数
            CheckoutParamVO checkoutParamVO1 = this.checkoutParamManager.getParam(null);
            //如果计算运费出现异常，将收货地址还原设置之前的地址
            try {
                // 重新计算运费
                String where = "address";

                this.cartManager.setShipping(where,0,null);

            } catch (ArgumentException e) {
                this.checkoutParamManager.setAddressId(checkoutParamVO1.getAddressId(),null);
                return ApiResponse.fail(e.getExceptionCode(),e.getMessage());
            }
        }else {
            //设置收货地址
            this.checkoutParamManager.setAddressId(addressId,null);
            //如果计算运费出现异常，将收货地址还原设置之前的地址
            try {
                // 重新计算运费
                String where = "address";
                // 自提只支持自营店铺 14l
                this.checkoutParamManager.setDeliveryFreshPage(2,14l,null);
                this.cartManager.setShipping(where,2,null);
            } catch (ArgumentException e) {
                this.checkoutParamManager.setAddressId(checkoutParamVO.getAddressId(),null);
                return ApiResponse.fail(e.getExceptionCode(),e.getMessage());
            }
        }

        return ApiResponse.success();
    }

    @ApiOperation(value = "设置自提信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isDelivery",value = "是否自提",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "shopId",value = "店铺Id",required = true,dataType = "long",paramType = "query"),
    })
    @PostMapping(value = "/delivery")
    public ApiResponse changeDelivery(@ApiIgnore @NotNull(message = "页面选中买家自提 1 是，2否") Integer isDelivery, Long shopId){
        try {
            this.checkoutParamManager.setDelivery(isDelivery,shopId,null);
        } catch (ArgumentException ae) {
            ae.printStackTrace();
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.hashCode(),e.getMessage());
        }
        return ApiResponse.success("");
    }


    @ApiOperation(value = "设置支付类型")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "paymentType", value = "支付类型 在线支付：ONLINE，货到付款：COD", required = false, dataType = "String", paramType = "query", allowableValues = "ONLINE,COD")
    })
    @PostMapping(value = "/paymentType")
    public ApiResponse setPaymentType(String paymentType) {

        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.valueOf(paymentType.toUpperCase());

        this.checkoutParamManager.setPaymentType(paymentTypeEnum,null);
        return ApiResponse.success();
    }

    @ApiOperation(value = "设置发票信息")
    @PostMapping(value = "/receipt")
    public ApiResponse setReceipt(@Valid ReceiptVO receiptVO) {
        if (StringUtil.isEmpty(receiptVO.getReceiptTitle())) {
            return ApiResponse.fail(TradeErrorCode.RECEIPT_TITLE_NOT_NULL.getErrorCode(),
                    TradeErrorCode.RECEIPT_TITLE_NOT_NULL.getErrorMsg());
        }
        if (StringUtil.isEmpty(receiptVO.getReceiptContent())) {
            return ApiResponse.fail(TradeErrorCode.RECEIPT_CONTENT_NOT_NULL.getErrorCode(),
                    TradeErrorCode.RECEIPT_CONTENT_NOT_NULL.getErrorMsg());
        }
        //如果发票不为个人的时候 需要校验发票税号
        if (!receiptVO.getType().equals(0) && StringUtil.isEmpty(receiptVO.getTaxNo())) {
            return ApiResponse.fail(TradeErrorCode.TAX_NO_NOT_NULL.getErrorCode(), TradeErrorCode.TAX_NO_NOT_NULL.getErrorMsg());
        }
        receiptVO.setReceiptType(ReceiptTypeEnum.VALUE_ADDED_TAX_INVOICE.name());
        this.checkoutParamManager.setReceipt(receiptVO,null);
        return ApiResponse.success();
    }


    @PostMapping(value = "/deliveryReceiveMessage/{shopId}")
    @ResponseBody
    @ApiOperation(value = "设置自提信息",notes = "根据form表单提交")
    public ApiResponse setDeliveryReceiveMessage(@Valid DeliveryMessageForm deliveryMessageForm, @PathVariable("shopId") Long shopId){
        this.checkoutParamManager.setDeliveryReceiveMessage(deliveryMessageForm,shopId,null);
        return ApiResponse.success();
    }

    @ApiOperation(value = "取消发票")
    @DeleteMapping(value = "/receipt")
    public ApiResponse delReceipt() {
        checkoutParamManager.deleteReceipt(null);
        return ApiResponse.success();
    }


    @ApiOperation(value = "设置送货时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receive_time", value = "送货时间", required = true, dataType = "String", paramType = "path"),
    })
    @PostMapping(value = "/receive-time/{receive_time}")
    public ApiResponse setReceiveTime(@ApiIgnore @NotBlank(message = "必须指定送货时间") @PathVariable(value = "receive_time") String receiveTime) {
        this.checkoutParamManager.setReceiveTime(receiveTime,null);
        return ApiResponse.success();
    }


    @ApiOperation(value = "设置订单备注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "订单备注", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/remark")
    public ApiResponse setRemark(Long shopId, String remark) {
        this.checkoutParamManager.setRemark(shopId, remark,null);
        return ApiResponse.success();
    }

    @ApiOperation(value = "设置订单备注V2")
    @ResponseBody
    @PostMapping(value = "/setRemark")
    public ApiResponse setRemarkV2(@RequestBody Map<Long,String> remark) {
        this.checkoutParamManager.setRemarks(remark);
        return ApiResponse.success();
    }

    @ApiOperation(value = "设置余额", response = CheckoutParamVO.class)
    @ResponseBody
    @PostMapping(value = "/balance")
    public ApiResponse setBalance(Double balance) {
        Double needPayMoney;
        PriceDetailVO priceDetailVO = tradePriceManager.getTradePrice(null);
        needPayMoney = MathUtil.subtract(priceDetailVO.getTotalPrice(), balance);
        priceDetailVO.setBalance(balance);
        if (needPayMoney < 0 ) {
            return ApiResponse.fail(TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorCode(),
                    TradeErrorCode.OVERPAYMENT_OF_BALANCE.getErrorMsg());
        }

        priceDetailVO.setNeedPayMoney(needPayMoney);
        tradePriceManager.pushPrice(priceDetailVO,null);
        return ApiResponse.success(priceDetailVO);
    }

    @ApiOperation(value = "获取结算参数", response = CheckoutParamVO.class)
    @ResponseBody
    @GetMapping()
    public ApiResponse get() {
        this.cartManager.refreshCart(null);
        try {
            CheckoutParamVO param = this.checkoutParamManager.getParam(null);
            return ApiResponse.success(param);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

//    @ApiOperation(value = "获取自提信息文本")
//    @ResponseBody
//    @GetMapping(value = "/getDeliveryText")
//    public ApiResponse getDeliveryText(){
//        EsDeliveryMessageVO deliveryText = this.checkoutParamManager.getDeliveryText();
//        return ApiResponse.success(deliveryText);
//    }

}
