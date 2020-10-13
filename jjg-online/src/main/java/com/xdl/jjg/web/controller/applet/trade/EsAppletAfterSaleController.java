package com.xdl.jjg.web.controller.applet.trade;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.system.api.model.domain.EsReturnReasonDO;
import com.shopx.system.api.model.domain.vo.EsReturnReasonVO;
import com.shopx.system.api.service.IEsReturnReasonService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.EsRefundDO;
import com.shopx.trade.api.model.domain.EsServiceOrderDO;
import com.shopx.trade.api.model.domain.EsWapRefundOrderItemsDO;
import com.shopx.trade.api.model.domain.dto.BuyerRefundApplyDTO;
import com.shopx.trade.api.model.domain.dto.BuyerUrlDTO;
import com.shopx.trade.api.model.domain.dto.EsReFundQueryBuyerDTO;
import com.shopx.trade.api.model.domain.dto.EsWapAfterSaleRecordQueryDTO;
import com.shopx.trade.api.model.domain.vo.EsRefundVO;
import com.shopx.trade.api.model.domain.vo.EsWapRefundCountVO;
import com.shopx.trade.api.model.domain.vo.EsWapRefundOrderItemsVO;
import com.shopx.trade.api.service.IEsAfterSaleService;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.api.service.IEsRefundService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.*;
import com.shopx.trade.web.request.query.EsAppletAfterSaleQueryForm;
import com.shopx.trade.web.request.query.EsAppletAfterSaleRecordQueryForm;
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
 *  前端控制器-小程序-售后相关接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-12
 */
@RestController
@Api(value = "/applet/trade/afterSale",tags = "小程序-售后相关接口")
@RequestMapping("/applet/trade/afterSale")
public class EsAppletAfterSaleController {



    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsReturnReasonService iEsReturnReasonService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderItemsService iEsOrderItemsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsAfterSaleService afterSaleService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundService iEsRefundService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsOrderService orderService;


    @ApiOperation(value = "可申请售后服务的订单列表",response = EsWapRefundOrderItemsVO.class)
    @GetMapping(value = "/getEsServiceOrderList")
    @ResponseBody
    public ApiResponse getEsServiceOrderList(@Valid EsAppletAfterSaleQueryForm form){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsReFundQueryBuyerDTO dto = new EsReFundQueryBuyerDTO();
        dto.setMemberId(dubboResult.getData().getId());
        // 订单已收货/已完成状态可申请售后
        // 订单售后状态为 未申请
        DubboPageResult<EsWapRefundOrderItemsDO> result = iEsOrderItemsService.getEsWapOrderItemsList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()){
            List<EsWapRefundOrderItemsDO> list = result.getData().getList();
            List<EsWapRefundOrderItemsVO> wapRefundOrderItemsVOS = BeanUtil.copyList(list, EsWapRefundOrderItemsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),wapRefundOrderItemsVOS);
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据售后类型获取原因列表")
    @ApiImplicitParam(name = "type", value = "售后类型", required =true, dataType = "String" ,paramType="path")
    @GetMapping(value = "/getReasonList/{type}")
    public ApiResponse getReasonList(@PathVariable String type) {
        DubboPageResult<EsReturnReasonDO> result = iEsReturnReasonService.getByType(type);
        if (result.isSuccess()){
            List<EsReturnReasonVO> esReturnReasonVOS = BeanUtil.copyList(result.getData().getList(), EsReturnReasonVO.class);
            return ApiResponse.success(esReturnReasonVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "申请售后")
    @PostMapping(value = "/refunds/apply")
    @ResponseBody
    public ApiResponse refund(@RequestBody @Valid AppletRefundApplyForm refundApply) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(refundApply.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(memberDO.getId());
        refundApplyDTO.setMemberName(memberDO.getName());
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
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "取消售后申请")
    @PostMapping(value = "/updateCancelAfterSale")
    @ResponseBody
    public ApiResponse updateCancelAfterSale(@RequestBody @Valid AppletCancelAfterSaleForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        // 取消操作 更新退款单数据，更改订单以及订单项售后状态
        DubboResult result = iEsRefundService.updateCancelAfterSale(form.getSn(), memberDO.getId());
        if (result.isSuccess()){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getAfterSalesRecords")
    @ApiOperation(value = "处理中/已完成售后单列表",response = EsRefundVO.class)
    @ResponseBody
    public ApiResponse getAfterSalesRecords(@Valid EsAppletAfterSaleRecordQueryForm form){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        EsWapAfterSaleRecordQueryDTO dto = new EsWapAfterSaleRecordQueryDTO();
        // 当前会员ID
        dto.setMemberId(memberDO.getId());
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
    @GetMapping(value = "/getCancelOrderDetail")
    @ResponseBody
    public ApiResponse getCancelOrderDetail(@Valid AppletAfterSaleDetailForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        DubboResult<EsRefundDO> result = iEsRefundService.getServiceDetail(form.getSn(), memberDO.getId());
        if (result.isSuccess()){
            EsRefundVO esRefundVO = new EsRefundVO();
            BeanUtils.copyProperties(result.getData(),esRefundVO);
            return ApiResponse.success(esRefundVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "售后数量",response = EsWapRefundCountVO.class)
    @GetMapping(value = "/getCount/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getCount(@PathVariable String skey){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsWapRefundCountVO> result = iEsRefundService.getCount(dubboResult.getData().getId());
        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "卖家未发货状态申请退款（取消订单）")
    @PostMapping(value = "/refunds/notShipApply")
    @ResponseBody
    public ApiResponse refund(@Valid @RequestBody AppletBuyerRefundForm refundApply) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(refundApply.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        BuyerRefundApplyDTO refundApplyDTO = new BuyerRefundApplyDTO();
        refundApplyDTO.setMemberId(memberDO.getId());
        refundApplyDTO.setMemberName(memberDO.getName());
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
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
