package com.xdl.jjg.web.controller.pc.trade;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.vo.CancelVO;
import com.shopx.trade.api.model.domain.vo.RogVO;
import com.shopx.trade.api.model.enums.OrderPermission;
import com.shopx.trade.api.service.IEsOrderOperateService;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 订单流程操作
 * Description: zhuox-shop-trade
 * <p>
 * Created by LJG on 2019/8/2 11:08
 */
@Api(value = "orderOperate",tags = "订单流程操作")
@RestController
@RequestMapping(value = "/orderOperate")
public class EsOrderOperateController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderOperateService iEsOrderOperateService;

    @ApiOperation(value = "确认收货")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "orderSn",value = "订单编号",required = true,dataType = "string",paramType = "query")
    )
    @PostMapping("/confirmReceipt")
    @ResponseBody
    public ApiResponse confirmReceipt(@ApiIgnore String orderSn){
        RogVO rogVO = new RogVO();
        rogVO.setOrderSn(orderSn);
        rogVO.setOperator(ShiroKit.getUser().getName());
        DubboResult<EsOrderDO> rog = this.iEsOrderOperateService.rog(rogVO, OrderPermission.buyer);
        return ApiResponse.success(rog);
    }

    @ApiOperation(value = "取消交易订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, dataType = "string",paramType = "query"),
    })
    @PostMapping("/cancel")
    @ResponseBody
    public ApiResponse cancel(@ApiIgnore String orderSn) {
        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator(ShiroKit.getUser().getName());
        cancelVO.setOrderSn(orderSn);
//        cancelVO.setReason(reason);
        DubboResult<EsOrderDO> result = this.iEsOrderOperateService.cancelOrder(cancelVO,OrderPermission.buyer);
        return ApiResponse.success(result);
    }

    @ApiOperation(value = "取消交易单 整个交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeSn", value = "交易编号", required = true, dataType = "string",paramType = "query"),
    })
    @PostMapping("/cancelTrade")
    @ResponseBody
    public ApiResponse cancelTrade(@ApiIgnore String tradeSn) {
        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator(ShiroKit.getUser().getName());
        cancelVO.setOrderSn(tradeSn);
        cancelVO.setReason("用户主动取消");
        DubboResult<EsOrderDO> result = this.iEsOrderOperateService.cancelOrder(cancelVO,OrderPermission.buyer);
        return ApiResponse.success(result);
    }
}
