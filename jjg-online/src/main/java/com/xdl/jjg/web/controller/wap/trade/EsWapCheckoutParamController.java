package com.xdl.jjg.web.controller.wap.trade;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.vo.EsMemberAddressVO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.vo.CheckoutParamVO;
import com.shopx.trade.api.model.domain.vo.ReceiptVO;
import com.shopx.trade.api.model.enums.PaymentTypeEnum;
import com.shopx.trade.api.model.enums.ReceiptTypeEnum;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.manager.CheckoutParamManager;
import com.shopx.trade.web.manager.TradePriceManager;
import com.shopx.trade.web.request.ChangeDeliveryForm;
import com.shopx.trade.web.request.DeliveryMessageForm;
import com.shopx.trade.web.request.EsWapRemarkForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *移动端-结算参数控制器
 *
 * @Author: yuanj  595831329@qq.com
 * @Date: 03/03/2020 10:40
 * @Version: 1.0
 */
@Api(value = "/wap/checkout-params", tags = "移动端-结算参数接口模块")
@RestController
@RequestMapping("/wap/checkout-params")
@Validated
public class EsWapCheckoutParamController {

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private TradePriceManager tradePriceManager;

    @Autowired
    private CartManager cartManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;

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
                this.cartManager.setShipping(where,0,null);

            } catch (ArgumentException e) {
                this.checkoutParamManager.setAddressId(checkoutParamVO.getAddressId(),null);
                return ApiResponse.fail(e.getExceptionCode(),e.getMessage());
            }
        }

        return ApiResponse.success();
    }


    @ApiOperation(value = "设置是否自提")
    @PostMapping(value = "/delivery")
    public ApiResponse changeDelivery(@Valid @RequestBody ChangeDeliveryForm form){
        try {
            this.checkoutParamManager.setDelivery(form.getIsDelivery(),form.getShopId(),null);
        } catch (ArgumentException ae) {
            ae.printStackTrace();
            return ApiResponse.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.hashCode(),e.getMessage());
        }
        return ApiResponse.success();
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
    public ApiResponse setDeliveryReceiveMessage(@Valid @RequestBody DeliveryMessageForm deliveryMessageForm, @PathVariable("shopId") Long shopId){
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
    @PostMapping(value = "/remark")
    public ApiResponse setRemark(@Valid @RequestBody EsWapRemarkForm form) {
        this.checkoutParamManager.setRemark(form.getShopId(), form.getRemark(),null);
        return ApiResponse.success();
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

    @ApiOperation(value = "查询默认收货地址", notes = "查询默认收货地址",response =EsMemberAddressVO.class )
    @GetMapping("/getDefaultAddress")
    @ResponseBody
    public ApiResponse getDefaultAddress() {
        //获取当前用户id
        Long userId = ShiroKit.getUser().getId();
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getWapDefaultMemberAddress(userId);
        if (result.isSuccess()) {
            EsMemberAddressVO esMemberAddressVO = new EsMemberAddressVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberAddressVO);
            }
            return ApiResponse.success(esMemberAddressVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}
