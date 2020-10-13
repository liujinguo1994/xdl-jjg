package com.xdl.jjg.web.controller.pc.after;

import com.alibaba.fastjson.JSON;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.system.api.model.domain.EsReturnReasonDO;
import com.shopx.system.api.model.domain.vo.EsReturnReasonVO;
import com.shopx.system.api.service.IEsReturnReasonService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsRefundDO;
import com.shopx.trade.api.model.domain.EsServiceOrderDO;
import com.shopx.trade.api.model.domain.EsServiceOrderItemsDO;
import com.shopx.trade.api.model.domain.dto.*;
import com.shopx.trade.api.model.domain.vo.EsRefundVO;
import com.shopx.trade.api.model.domain.vo.EsServiceOrderItemsVO;
import com.shopx.trade.api.model.domain.vo.EsServiceOrderVO;
import com.shopx.trade.api.model.enums.ProcessStatusEnum;
import com.shopx.trade.api.model.enums.RefundTypeEnum;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.BuyerRefundApplyForm;
import com.shopx.trade.web.request.BuyerRefundForm;
import com.shopx.trade.web.request.SellerRefundApprovalForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zjp
 * @version v7.0
 * @Description 售后相关API
 * @ClassName AfterSaleController
 * @since v7.0 下午8:10 2018/5/9
 */
@Api(description="售后相关API",tags = "售后相关接口")
@RestController
@RequestMapping("/after-sales")
public class AfterSaleBuyerController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAfterSaleService afterSaleService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService iesOrderService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderItemsService iEsOrderItemsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundGoodsService iEsRefundGoodsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundService iEsRefundService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsReturnReasonService iEsReturnReasonService;

    @GetMapping(value = "/getEsCancelOrderInfo")
    @ApiOperation(value = "申请 取消订单 订单项页面",notes = "根据订单编号",response = EsServiceOrderVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "String", paramType = "query")
    })
    @ResponseBody
    public ApiResponse getEsCancelOrderInfo(@ApiIgnore String orderSn){
        EsOrderDTO esOrderDTO = new EsOrderDTO();
        esOrderDTO.setMemberId(ShiroKit.getUser().getId());
        //赋值商品名称
        esOrderDTO.setOrderSn(orderSn);
        //删除状态
        esOrderDTO.setIsDel(0);

        DubboResult<EsServiceOrderDO> esServiceOrderInfo = iesOrderService.getEsCancelOrderInfo(esOrderDTO);
        // 转VO
        if (esServiceOrderInfo.isSuccess()){
            EsServiceOrderDO esServiceOrderDO = esServiceOrderInfo.getData();
            EsServiceOrderVO esServiceOrderVO = new EsServiceOrderVO();
            BeanUtils.copyProperties(esServiceOrderDO,esServiceOrderVO);
            return ApiResponse.success(esServiceOrderVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esServiceOrderInfo));
    }

    @GetMapping(value = "/getEsServiceOrderInfo")
    @ApiOperation(value = "申请 售后 订单项页面",notes = "根据订单编号,商品ID",response = EsServiceOrderVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "skuId", value = "商品skuID", dataType = "Long", paramType = "query")
    })
    @ResponseBody
    public ApiResponse getEsServiceOrderInfo(String orderSn,Long skuId){

        Long memberId = ShiroKit.getUser().getId();

        DubboResult<EsServiceOrderDO> esServiceOrderInfo = iesOrderService.getEsServiceOrderInfo(orderSn,skuId,memberId);
        // 转VO
        if (esServiceOrderInfo.isSuccess()){
            EsServiceOrderDO esServiceOrderDO = esServiceOrderInfo.getData();
            EsServiceOrderVO esServiceOrderVO = new EsServiceOrderVO();
            BeanUtils.copyProperties(esServiceOrderDO,esServiceOrderVO);
            return ApiResponse.success(esServiceOrderVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esServiceOrderInfo));
    }

    @GetMapping(value = "/getEsRefundOrderList")
    @ApiOperation(value = "申请退款订单页面",notes = "根据订单编号",response = EsServiceOrderVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "String", paramType = "query")
    })
    @ResponseBody
    public ApiResponse getEsRefundOrderList(String orderSn){

        Long memberId = ShiroKit.getUser().getId();
        // 根据订单编号查询订单信息
        DubboResult<EsServiceOrderDO> esServiceOrderInfo = iesOrderService.getEsRefundOrderList(orderSn,memberId);
        // 转VO
        if (esServiceOrderInfo.isSuccess()){
            EsServiceOrderDO esServiceOrderDO = esServiceOrderInfo.getData();
            EsServiceOrderVO esServiceOrderVO = new EsServiceOrderVO();
            BeanUtils.copyProperties(esServiceOrderDO,esServiceOrderVO);
            return ApiResponse.success(esServiceOrderVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esServiceOrderInfo));
    }


    @ApiOperation(value = "买家申请退款,发货后申请售后接口",response = BuyerRefundApplyForm.class)
    @PostMapping(value = "/refunds/apply")
    public ApiResponse refund(@RequestBody @ApiParam(name = "refundApply",value = "交易对象",required = true) BuyerRefundApplyForm refundApply) {
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(ShiroKit.getUser().getId());
        refundApplyDTO.setMemberName(ShiroKit.getUser().getName());
        BeanUtils.copyProperties(refundApply,refundApplyDTO);
        //设置 维权类型
        refundApplyDTO.setRefuseType(refundApply.getRefuseType());
        //设置 售后类型
        refundApplyDTO.setRefundType(refundApply.getRefundType());
        refundApplyDTO.setSkuIdList(Arrays.asList(refundApply.getSkuId()));
        DubboResult<EsServiceOrderDO> result = afterSaleService.applyRefund(refundApplyDTO);
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "卖家未发货状态申请退款",response = BuyerRefundForm.class)
    @PostMapping(value = "/refunds/notShipApply")
    public ApiResponse refund(@RequestBody @ApiParam(name = "refundApply",value = "交易对象",required = true) BuyerRefundForm refundApply) {
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(ShiroKit.getUser().getId());
        refundApplyDTO.setMemberName(ShiroKit.getUser().getName());
        BeanUtils.copyProperties(refundApply,refundApplyDTO);
        //设置 维权类型
        refundApplyDTO.setRefuseType(refundApply.getRefuseType());
        //设置 售后类型
        refundApplyDTO.setRefundType(refundApply.getRefundType());
        refundApplyDTO.setSkuIdList(refundApply.getSkuId());
        if (CollectionUtils.isEmpty(refundApply.getSkuId())){
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(),"请选中商品项！");
        }
        DubboResult<EsServiceOrderDO> result = afterSaleService.cancelOrder(refundApplyDTO);
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }


//        DubboResult<EsServiceOrderDO> result = afterSaleService.applyRefund(refundApplyDTO);
//        if(result.isSuccess()){
//            return ApiResponse.success(result.getData());
//        }else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
    }

    @PostMapping(value = "/setOption")
    @ApiOperation(value = "设置退款&退货 / 取消订单 选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "option", value = "售后选项 售后类型 -> RETURN_GOODS:退货,CHANGE_GOODS:换货,REPAIR_GOODS:维修", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "skuIds", value = "skuIds", dataType = "Long", paramType = "query"),
    })
    @ResponseBody
    public ApiResponse setOption(String option,String orderSn,Long[] skuIds) {
        if ("RETURN_GOODS".equals(option) || "CANCEL_ORDER".equals(option)){
            DubboResult<Map<String, Double>> itemsMoney = this.iEsOrderItemsService.getEsOrderItemsMoney(orderSn, skuIds);
            return ApiResponse.success(itemsMoney.getData());
        }else {
            return ApiResponse.success();
        }
    }

//    @ApiOperation(value = "买家取消订单",response = BuyerRefundApplyForm.class)
//    @PostMapping(value = "/cancelOrder")
//    public ApiResponse cancelOrder(@Valid BuyerCancelOrderForm cancelOrderForm) {
//        BuyerCancelOrderDTO cancelOrderDTO = new BuyerCancelOrderDTO();
//        cancelOrderDTO.setMemberId(ShiroKit.getUser().getId());
//        cancelOrderDTO.setMemberName(ShiroKit.getUser().getName());
//        BeanUtils.copyProperties(cancelOrderForm,cancelOrderDTO);
//        DubboResult<EsServiceOrderDO> result = afterSaleService.cancelOrder(cancelOrderDTO);
//        if(result.isSuccess()){
//            return ApiResponse.success(result.getData());
//        }else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//
//}

    @GetMapping(value = "/getEsServiceOrderList")
    @ApiOperation(value = "可申请售后服务的订单列表",notes = "根据时间和筛选条件",response = EsServiceOrderItemsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "近几个月",dataType = "int",paramType = "query",required = true,example = "1"),
            @ApiImplicitParam(name = "keyWords", value = "店铺名/商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "页数",dataType = "int",paramType = "query",required = true,example = "10"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int",paramType = "query",required = true,example = "1")
    })
    @ResponseBody
    public ApiResponse getEsServiceOrderList( int time,
                                        String keyWords,
                                        @NotEmpty(message = "页数不能为空") int pageSize,
                                        @NotEmpty(message = "页码不能为空") int pageNum){
        EsReFundQueryBuyerDTO esReFundQueryBuyerDTO = new EsReFundQueryBuyerDTO();
        // 当前会员ID
        esReFundQueryBuyerDTO.setMemberId(ShiroKit.getUser().getId());
        esReFundQueryBuyerDTO.setKeyword(keyWords);
        esReFundQueryBuyerDTO.setTime(time);

        // 订单已收货/已完成状态可申请售后
        // 订单售后状态为 未申请
        DubboPageResult<EsServiceOrderItemsDO> itemsList = this.iEsOrderItemsService.getEsServiceOrderItemsList(esReFundQueryBuyerDTO, pageSize, pageNum);
        if (itemsList.isSuccess()){
            List<EsServiceOrderItemsDO> list = itemsList.getData().getList();
            List<EsServiceOrderItemsVO> esServiceOrderItemsVOS = BeanUtil.copyList(list, EsServiceOrderItemsVO.class);

            return ApiPageResponse.pageSuccess(itemsList.getData().getTotal(),esServiceOrderItemsVOS);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(itemsList));
    }

    @GetMapping(value = "/getAfterSalesRecords")
    @ApiOperation(value = "已经申请过售后的 售后单 列表（包括未发货前取消订单的退款单）",notes = "根据时间和筛选条件",response = EsRefundVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "近几个月",dataType = "int",paramType = "query",required = true,example = "1"),
            @ApiImplicitParam(name = "keyWords", value = "订单编号/商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "页数",dataType = "int",paramType = "query",required = true,example = "10"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int",paramType = "query",required = true,example = "1")
    })
    @ResponseBody
    public ApiResponse getAfterSalesRecords( int time,
                                         String keyWords,
                                         @NotEmpty(message = "页数不能为空") int pageSize,
                                         @NotEmpty(message = "页码不能为空") int pageNum){
        EsReFundQueryDTO esReFundQueryDTO = new EsReFundQueryDTO();
        // 当前会员ID
        esReFundQueryDTO.setMemberId(ShiroKit.getUser().getId());
        esReFundQueryDTO.setKeyword(keyWords);
        esReFundQueryDTO.setTime(time);

        DubboPageResult<EsRefundDO> refundList = this.iEsRefundService.getServiceRefundList(esReFundQueryDTO, pageSize, pageNum);
        if (refundList.isSuccess()){
            List<EsRefundVO> esRefundVOS = BeanUtil.copyList(refundList.getData().getList(), EsRefundVO.class);

            return ApiPageResponse.pageSuccess(refundList.getData().getTotal(),esRefundVOS);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(refundList));
    }

    @ApiOperation(value = "买家查看售后单详细", response = EsRefundVO.class)
    @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="query")
    @GetMapping(value = "/getCancelOrderDetail")
    public ApiResponse getCancelOrderDetail(String sn) {
        Long memberId = ShiroKit.getUser().getId();

        DubboResult<EsRefundDO> serviceDetail = this.iEsRefundService.getServiceDetail(sn, memberId);
        if (serviceDetail.isSuccess()){
            EsRefundVO esRefundVO = new EsRefundVO();
            BeanUtils.copyProperties(serviceDetail.getData(),esRefundVO);
            esRefundVO.setProcessStatusList(getProcess(esRefundVO.getRefundType()));
            return ApiResponse.success(esRefundVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(serviceDetail));
    }

    @ApiOperation(value = "根据售后类型获取原因列表")
    @ApiImplicitParam(name = "type", value = "售后类型", required =true, dataType = "String" ,paramType="query")
    @GetMapping(value = "/getReasonList")
    public ApiResponse getReasonList(String type) {

        DubboPageResult<EsReturnReasonDO> byType = this.iEsReturnReasonService.getByType(type);

        if (byType.isSuccess()){
            List<EsReturnReasonVO> esReturnReasonVOS = BeanUtil.copyList(byType.getData().getList(), EsReturnReasonVO.class);
            return ApiResponse.success(esReturnReasonVOS);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(byType));
    }

    @ApiOperation(value = "买家取消退款/售后操作", response = EsRefundVO.class)
    @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="query")
    @GetMapping(value = "/updateCancelAfterSale")
    public ApiResponse updateCancelAfterSale(String sn) {
        Long memberId = ShiroKit.getUser().getId();
        // 取消操作 删除退款单数据，更改订单以及订单项售后状态
        DubboResult dubboResult = this.iEsRefundService.updateCancelAfterSale(sn, memberId);
        if (dubboResult.isSuccess()){

            return ApiResponse.success(dubboResult);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
    }

    @ApiOperation(value = "卖家审核退款/退货")
    @PostMapping(value = "/audits/{sn}")
    @ApiImplicitParam(name = "sn", value = "退款单sn", required =true, dataType = "String" ,paramType="path")
    public ApiResponse audit(@Valid SellerRefundApprovalForm refundApproval, @PathVariable("sn") String sn) {
        refundApproval.setSn(sn);

        SellerRefundApprovalDTO refundApprovalDTO = new SellerRefundApprovalDTO();
        BeanUtils.copyProperties(refundApproval,refundApprovalDTO);

        afterSaleService.approval(refundApprovalDTO);

        return ApiResponse.success();
    }
    public List<String> getProcess(String type){
        //获取操作类型
        String json = JsonUtil.objectToJson(ProcessStatusEnum.values());
        List<String> list= JSON.parseArray(json,String.class);
        //退货
        if(StringUtils.equals(type, RefundTypeEnum.RETURN_GOODS.value())){
            list.remove("WAIT_SHIP");
        }else if(StringUtils.equals(type, RefundTypeEnum.CHANGE_GOODS.value())){
            list.remove("WAIT_IN_STORAGE");

        }else if(StringUtils.equals(type, RefundTypeEnum.REPAIR_GOODS.value())){
            list.remove("WAIT_SHIP");
            list.remove("WAIT_IN_STORAGE");
            list.remove("WAIT_REFUND");
        }else{
            list.remove("WAIT_IN_STORAGE");
            list.remove("WAIT_SHIP");
        }
        list.remove("REFUND_FAIL");
        return list;

    }
}
