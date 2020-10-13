package com.xdl.jjg.web.controller.applet.trade;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsShopNoticeLogDTO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsShopNoticeLogService;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.vo.CancelVO;
import com.shopx.trade.api.model.domain.vo.RogVO;
import com.shopx.trade.api.model.enums.OrderPermission;
import com.shopx.trade.api.service.IEsOrderOperateService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAppletCancelTradeForm;
import com.shopx.trade.web.request.EsAppletConfirmReceiptForm;
import com.shopx.trade.web.request.EsAppletShopNoticeLogForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  小程序-订单操作
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-28
 */
@Api(value = "/applet/trade/orderOperate",tags = "小程序-订单操作")
@RestController
@RequestMapping(value = "/applet/trade/orderOperate")
public class EsAppletOrderOperateController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderOperateService iEsOrderOperateService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsShopNoticeLogService esShopNoticeLogService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @ApiOperation(value = "确认收货")
    @PostMapping("/confirmReceipt")
    @ResponseBody
    public ApiResponse confirmReceipt(@RequestBody @Valid EsAppletConfirmReceiptForm form){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        RogVO rogVO = new RogVO();
        rogVO.setOrderSn(form.getOrderSn());
        rogVO.setOperator(memberDO.getName());
        DubboResult<EsOrderDO> result = iEsOrderOperateService.rog(rogVO, OrderPermission.buyer);
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "提醒发货")
    @PostMapping("/remindDelivery")
    @ResponseBody
    public ApiResponse remindDelivery(@RequestBody @Valid EsAppletShopNoticeLogForm form){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        EsShopNoticeLogDTO dto = new EsShopNoticeLogDTO();
        dto.setShopId(form.getShopId());
        String orderSn = form.getOrderSn();
        dto.setNoticeContent("订单号为:"+orderSn+"的用户提醒您尽快发货");
        dto.setSendTime(System.currentTimeMillis());
        dto.setMemberIds(memberDO.getId());
        dto.setOrderSn(orderSn);
        DubboResult result = esShopNoticeLogService.insertShopNoticeLog(dto);
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "取消整个交易单(未付款)")
    @PostMapping("/cancelTrade")
    @ResponseBody
    public ApiResponse cancelTrade(@RequestBody @Valid EsAppletCancelTradeForm form) {
        // 获取操作人
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator(dubboResult.getData().getName());
        cancelVO.setOrderSn(form.getTradeSn());
        cancelVO.setReason("用户主动取消");
        DubboResult<EsOrderDO> result = this.iEsOrderOperateService.cancelOrder(cancelVO,OrderPermission.buyer);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
