package com.xdl.jjg.web.controller.wap.trade;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.system.api.model.domain.EsReturnReasonDO;
import com.shopx.system.api.model.domain.vo.EsReturnReasonVO;
import com.shopx.system.api.service.IEsReturnReasonService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.*;
import com.shopx.trade.api.model.domain.dto.*;
import com.shopx.trade.api.model.domain.vo.EsRefundVO;
import com.shopx.trade.api.model.domain.vo.EsWapRefundCountVO;
import com.shopx.trade.api.model.domain.vo.EsWapRefundOrderItemsVO;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.BuyerRefundForm;
import com.shopx.trade.web.request.query.EsWapAfterSaleQueryForm;
import com.shopx.trade.web.request.query.EsWapAfterSaleRecordQueryForm;
import com.shopx.trade.web.request.query.WapRefundApplyForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器-移动端-售后相关接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-20
 */
@RestController
@Api(value = "/wap/trade/afterSale",tags = "移动端-售后相关接口")
@RequestMapping("/wap/trade/afterSale")
public class EsWapAfterSaleController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAfterSaleService afterSaleService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsReturnReasonService iEsReturnReasonService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderItemsService iEsOrderItemsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundService iEsRefundService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService orderService;


    @ApiOperation(value = "可申请售后服务的订单列表",response = EsWapRefundOrderItemsVO.class)
    @GetMapping(value = "/getEsServiceOrderList")
    @ResponseBody
    public ApiResponse getEsServiceOrderList(EsWapAfterSaleQueryForm form){
        EsReFundQueryBuyerDTO esReFundQueryBuyerDTO = new EsReFundQueryBuyerDTO();
        // 当前会员ID
        esReFundQueryBuyerDTO.setMemberId(ShiroKit.getUser().getId());
        esReFundQueryBuyerDTO.setKeyword(form.getKeyword());
        //esReFundQueryBuyerDTO.setTime(form.getTime());
        // 订单已收货/已完成状态可申请售后
        // 订单售后状态为 未申请
        DubboPageResult<EsWapRefundOrderItemsDO> result = iEsOrderItemsService.getEsWapOrderItemsList(esReFundQueryBuyerDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()){
            List<EsWapRefundOrderItemsDO> list = result.getData().getList();
            List<EsWapRefundOrderItemsVO> wapRefundOrderItemsVOS = BeanUtil.copyList(list, EsWapRefundOrderItemsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),wapRefundOrderItemsVOS);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "根据售后类型获取原因列表")
    @ApiImplicitParam(name = "type", value = "售后类型", required =true, dataType = "String" ,paramType="path")
    @GetMapping(value = "/getReasonList/{type}")
    public ApiResponse getReasonList(@PathVariable String type) {
        DubboPageResult<EsReturnReasonDO> result = iEsReturnReasonService.getByType(type);
        if (result.isSuccess()){
            List<EsReturnReasonVO> esReturnReasonVOS = BeanUtil.copyList(result.getData().getList(), EsReturnReasonVO.class);
            return ApiResponse.success(esReturnReasonVOS);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "申请售后",response = WapRefundApplyForm.class)
    @PostMapping(value = "/refunds/apply")
    @ResponseBody
    public ApiResponse refund(@RequestBody @Valid WapRefundApplyForm refundApply) {
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(ShiroKit.getUser().getId());
        refundApplyDTO.setMemberName(ShiroKit.getUser().getName());
        BeanUtils.copyProperties(refundApply,refundApplyDTO);
        //设置 维权类型
        refundApplyDTO.setRefuseType(refundApply.getRefuseType());
        //设置 售后类型
        refundApplyDTO.setRefundType(refundApply.getRefundType());

        if(!StringUtil.isEmpty(refundApply.getUrl())){
            String original = refundApply.getUrl();
            String[] split = original.split(",");
            List<BuyerUrlDTO> list=new ArrayList<>();
            for (String s:split) {
                BuyerUrlDTO url=new BuyerUrlDTO();
                url.setUrl(s);
                list.add(url);
            }
            refundApplyDTO.setUrlList(list);
        }
        refundApplyDTO.setSkuIdList(Arrays.asList(refundApply.getSkuId()));
        DubboResult<EsServiceOrderDO> result = afterSaleService.applyRefund(refundApplyDTO);
        return ApiResponse.success(result.getData());
    }

    @ApiOperation(value = "取消售后申请", response = EsRefundVO.class)
    @ApiImplicitParam(name = "sn", value = "售后单编号", required =true, dataType = "String" ,paramType="path")
    @PostMapping(value = "/updateCancelAfterSale/{sn}")
    public ApiResponse updateCancelAfterSale(@PathVariable("sn") String sn) {
        Long memberId = ShiroKit.getUser().getId();
        // 取消操作 删除退款单数据，更改订单以及订单项售后状态
        DubboResult dubboResult = iEsRefundService.updateCancelAfterSale(sn, memberId);
        if (dubboResult.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
    }

    @GetMapping(value = "/getAfterSalesRecords")
    @ApiOperation(value = "处理中/已完成售后单列表",response = EsRefundVO.class)
    @ResponseBody
    public ApiResponse getAfterSalesRecords(@Valid EsWapAfterSaleRecordQueryForm form){
        EsWapAfterSaleRecordQueryDTO dto = new EsWapAfterSaleRecordQueryDTO();
        // 当前会员ID
        dto.setMemberId(ShiroKit.getUser().getId());
        //设置状态
        dto.setStatus(form.getStatus());
        DubboPageResult<EsRefundDO> refundList = iEsRefundService.getAfterSalesRecords(dto, form.getPageSize(), form.getPageNum());
        if (refundList.isSuccess()){
            List<EsRefundVO> esRefundVOS = BeanUtil.copyList(refundList.getData().getList(), EsRefundVO.class);
            esRefundVOS.stream().forEach(esRefundVO -> {
                List<BuyerUrlDTO> urlDTOS = JsonUtil.jsonToList(esRefundVO.getUrl(), BuyerUrlDTO.class);
                DubboResult<EsOrderDO> result = orderService.getEsOrderInfo(esRefundVO.getOrderSn());
                if (result.isSuccess()){
                    EsOrderDO order = result.getData();
                    esRefundVO.setShipProvince(order.getShipProvince());
                    esRefundVO.setShipCity(order.getShipCity());
                    esRefundVO.setShipCounty(order.getShipCounty());
                    esRefundVO.setShipTown(order.getShipTown());
                    esRefundVO.setShipAddr(order.getShipAddr());
                    esRefundVO.setShipMobile(order.getShipMobile());
                }
                esRefundVO.setUrlList(urlDTOS);
            });
            return ApiPageResponse.pageSuccess(refundList.getData().getTotal(),esRefundVOS);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(refundList));
    }

    @ApiOperation(value = "查看售后单详情", response = EsRefundVO.class)
    @ApiImplicitParam(name = "sn", value = "售后单编号", required =true, dataType = "String" ,paramType="path")
    @GetMapping(value = "/getCancelOrderDetail/{sn}")
    public ApiResponse getCancelOrderDetail(@PathVariable String sn) {
        Long memberId = ShiroKit.getUser().getId();
        DubboResult<EsRefundDO> result = iEsRefundService.getServiceDetail(sn, memberId);
        if (result.isSuccess()){
            EsRefundVO esRefundVO = new EsRefundVO();
            BeanUtils.copyProperties(result.getData(),esRefundVO);
            return ApiResponse.success(esRefundVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "售后数量",response = EsWapRefundCountVO.class)
    @GetMapping(value = "/getCount")
    @ResponseBody
    public ApiResponse getCount(){
        DubboResult<EsWapRefundCountVO> result = iEsRefundService.getCount(ShiroKit.getUser().getId());
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "买家未发货状态申请退款",response = BuyerRefundForm.class)
    @PostMapping(value = "/refunds/notShipApply")
    @ResponseBody
    public ApiResponse refund(@Valid @RequestBody BuyerRefundForm refundApply) {
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(ShiroKit.getUser().getId());
        refundApplyDTO.setMemberName(ShiroKit.getUser().getName());
        BeanUtils.copyProperties(refundApply, refundApplyDTO);
        //设置 维权类型
        refundApplyDTO.setRefuseType(refundApply.getRefuseType());
        //设置 售后类型
        refundApplyDTO.setRefundType(refundApply.getRefundType());
        if (CollectionUtils.isEmpty(refundApply.getSkuId())) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请选中商品项！");
        }
        refundApplyDTO.setSkuIdList(refundApply.getSkuId());

        DubboResult<EsServiceOrderDO> result = afterSaleService.cancelOrder(refundApplyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
