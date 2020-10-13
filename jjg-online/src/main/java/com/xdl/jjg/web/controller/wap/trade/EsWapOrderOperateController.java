package com.xdl.jjg.web.controller.wap.trade;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.member.api.model.domain.dto.EsShopNoticeLogDTO;
import com.shopx.member.api.service.IEsShopNoticeLogService;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.vo.CancelVO;
import com.shopx.trade.api.model.domain.vo.RogVO;
import com.shopx.trade.api.model.enums.OrderPermission;
import com.shopx.trade.api.service.IEsOrderOperateService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsWapShopNoticeLogForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  移动端-订单操作
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-28
 */
@Api(value = "/wap/trade/orderOperate",tags = "移动端-订单操作")
@RestController
@RequestMapping(value = "/wap/trade/orderOperate")
public class EsWapOrderOperateController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderOperateService iEsOrderOperateService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsShopNoticeLogService esShopNoticeLogService;


    @ApiOperation(value = "取消整个交易单(未付款)")
    @ApiImplicitParam(name = "tradeSn", value = "交易号", required = true, dataType = "string", paramType = "path")
    @PostMapping("/cancelTrade/{tradeSn}")
    @ResponseBody
    public ApiResponse cancelTrade(@PathVariable String tradeSn) {
        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator(ShiroKit.getUser().getName());
        cancelVO.setOrderSn(tradeSn);
        cancelVO.setReason("用户主动取消");
        DubboResult<EsOrderDO> result = this.iEsOrderOperateService.cancelOrder(cancelVO,OrderPermission.buyer);
        return ApiResponse.success(result);
    }

    @ApiOperation(value = "确认收货")
    @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, dataType = "string", paramType = "path")
    @PostMapping("/confirmReceipt/{orderSn}")
    @ResponseBody
    public ApiResponse confirmReceipt(@PathVariable String orderSn){
        RogVO rogVO = new RogVO();
        rogVO.setOrderSn(orderSn);
        rogVO.setOperator(ShiroKit.getUser().getName());
        DubboResult<EsOrderDO> rog = iEsOrderOperateService.rog(rogVO, OrderPermission.buyer);
        return ApiResponse.success(rog);
    }

    @ApiOperation(value = "提醒发货")
    @PostMapping("/remindDelivery")
    @ResponseBody
    public ApiResponse remindDelivery(@RequestBody @Valid EsWapShopNoticeLogForm form){
        EsShopNoticeLogDTO dto = new EsShopNoticeLogDTO();
        dto.setShopId(form.getShopId());
        String orderSn = form.getOrderSn();
        dto.setNoticeContent("订单号为:"+orderSn+"的用户提醒您尽快发货");
        dto.setSendTime(System.currentTimeMillis());
        dto.setMemberIds(ShiroKit.getUser().getId());
        dto.setOrderSn(orderSn);
        DubboResult result = esShopNoticeLogService.insertShopNoticeLog(dto);
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
