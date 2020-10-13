package com.xdl.jjg.web.controller.applet.trade;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.vo.EsMemberAddressVO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.model.domain.vo.CheckoutParamVO;
import com.shopx.trade.api.model.enums.PaymentTypeEnum;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.manager.CartManager;
import com.shopx.trade.web.manager.CheckoutParamManager;
import com.shopx.trade.web.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  小程序-结算参数 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-01
 */
@Api(value = "/applet/trade/param", tags = "小程序-结算参数")
@RestController
@RequestMapping("/applet/trade/param")
public class EsAppletCheckoutParamController {

    @Autowired
    private CheckoutParamManager checkoutParamManager;
    @Autowired
    private CartManager cartManager;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @ApiOperation(value = "设置收货地址id")
    @PostMapping(value = "/setAddress")
    @ResponseBody
    public ApiResponse setAddressId(@RequestBody @Valid EsSetAddressForm form) {

        //读取结算参数
        CheckoutParamVO checkoutParamVO = this.checkoutParamManager.getParam(form.getSkey());
        Long addressId1 = checkoutParamVO.getAddressId();
        if (addressId1 == null){
            //设置收货地址
            this.checkoutParamManager.setAddressId(form.getAddressId(),form.getSkey());
            //读取结算参数
            CheckoutParamVO checkoutParamVO1 = this.checkoutParamManager.getParam(form.getSkey());
            //如果计算运费出现异常，将收货地址还原设置之前的地址
            try {
                // 重新计算运费
                String where = "address";
                this.cartManager.setShipping(where,0,form.getSkey());

            } catch (ArgumentException e) {
                this.checkoutParamManager.setAddressId(checkoutParamVO1.getAddressId(),form.getSkey());
                return ApiResponse.fail(e.getExceptionCode(),e.getMessage());
            }
        }else {
            //设置收货地址
            this.checkoutParamManager.setAddressId(form.getAddressId(),form.getSkey());
            //如果计算运费出现异常，将收货地址还原设置之前的地址
            try {
                // 重新计算运费
                String where = "address";
                this.cartManager.setShipping(where,0,form.getSkey());

            } catch (ArgumentException e) {
                this.checkoutParamManager.setAddressId(checkoutParamVO.getAddressId(),form.getSkey());
                return ApiResponse.fail(e.getExceptionCode(),e.getMessage());
            }
        }
        return ApiResponse.success();
    }


    @ApiOperation(value = "设置是否自提")
    @PostMapping(value = "/delivery")
    @ResponseBody
    public ApiResponse changeDelivery(@RequestBody @Valid EsAppletChangeDeliveryForm form){
        try {
            this.checkoutParamManager.setDelivery(form.getIsDelivery(),form.getShopId(),form.getSkey());
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
    @PostMapping(value = "/paymentType")
    @ResponseBody
    public ApiResponse setPaymentType(@RequestBody @Valid EsAppletSetPaymentTypeForm form) {
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.valueOf(form.getPaymentType().toUpperCase());
        this.checkoutParamManager.setPaymentType(paymentTypeEnum,form.getSkey());
        return ApiResponse.success();
    }

/*    @ApiOperation(value = "设置发票信息")
    @PostMapping(value = "/receipt")
    @ResponseBody
    public ApiResponse setReceipt(@RequestBody @Valid EsAppletReceiptForm form) {
        //如果发票不为个人的时候 需要校验发票税号
        if (!form.getType().equals(0) && StringUtil.isEmpty(form.getTaxNo())) {
            return ApiResponse.fail(TradeErrorCode.TAX_NO_NOT_NULL.getErrorCode(), TradeErrorCode.TAX_NO_NOT_NULL.getErrorMsg());
        }
        ReceiptVO receiptVO = new ReceiptVO();
        BeanUtil.copyProperties(form,receiptVO);
        receiptVO.setReceiptType(ReceiptTypeEnum.VALUE_ADDED_TAX_INVOICE.name());
        this.checkoutParamManager.setReceipt(receiptVO,form.getSkey());
        return ApiResponse.success();
    }*/


    @PostMapping(value = "/deliveryReceiveMessage")
    @ApiOperation(value = "设置自提信息")
    @ResponseBody
    public ApiResponse setDeliveryReceiveMessage(@Valid @RequestBody EsAppletDeliveryMessageForm form){
        DeliveryMessageForm messageForm = new DeliveryMessageForm();
        BeanUtil.copyProperties(form,messageForm);
        this.checkoutParamManager.setDeliveryReceiveMessage(messageForm,form.getShopId(),form.getSkey());
        return ApiResponse.success();
    }

/*    @ApiOperation(value = "取消发票")
    @DeleteMapping(value = "/receipt/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse delReceipt(@PathVariable String skey) {
        checkoutParamManager.deleteReceipt(skey);
        return ApiResponse.success();
    }*/


/*    @ApiOperation(value = "设置送货时间")
    @PostMapping(value = "/setReceiveTime")
    public ApiResponse setReceiveTime(@RequestBody @Valid EsAppletSetReceiveTimeForm form) {
        this.checkoutParamManager.setReceiveTime(form.getReceiveTime(),form.getSkey());
        return ApiResponse.success();
    }*/


    @ApiOperation(value = "设置订单备注")
    @PostMapping(value = "/remark")
    public ApiResponse setRemark(@Valid @RequestBody EsAppletRemarkForm form) {
        this.checkoutParamManager.setRemark(form.getShopId(), form.getRemark(),form.getSkey());
        return ApiResponse.success();
    }


    @GetMapping(value = "/getParam/{skey}")
    @ApiOperation(value = "获取结算参数", response = CheckoutParamVO.class)
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getParam(@PathVariable String skey) {
        this.cartManager.refreshCart(skey);
        try {
            CheckoutParamVO param = this.checkoutParamManager.getParam(skey);
            return ApiResponse.success(param);
        } catch (ArgumentException e) {
            return ApiResponse.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询默认收货地址", notes = "查询默认收货地址",response =EsMemberAddressVO.class )
    @GetMapping("/getDefaultAddress/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getDefaultAddress(@PathVariable String skey) {
        //获取当前用户id
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = dubboResult.getData().getId();
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getWapDefaultMemberAddress(memberId);
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
