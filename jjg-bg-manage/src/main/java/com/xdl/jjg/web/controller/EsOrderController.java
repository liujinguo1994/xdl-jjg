package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsOrderDO;
import com.xdl.jjg.model.domain.EsSellerOrderDO;
import com.xdl.jjg.model.dto.EsAdminOrderQueryDTO;
import com.xdl.jjg.model.enums.OrderStatusEnum;
import com.xdl.jjg.model.form.EsOrderQueryForm;
import com.xdl.jjg.model.vo.EsAdminOrderVO;
import com.xdl.jjg.model.vo.EsOrderVO;
import com.xdl.jjg.model.vo.LabelValueBeanVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器-订单管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@Api(value = "/esOrder", tags = "订单管理")
@RestController
@RequestMapping("/esOrder")
public class EsOrderController {

    @Autowired
    private IEsOrderService orderService;

    @ApiOperation(value = "订单信息分页查询", response = EsOrderVO.class)
    @GetMapping(value = "/getEsAdminOrderList")
    @ResponseBody
    public ApiResponse getEsAdminOrderList(EsOrderQueryForm form) {
        EsAdminOrderQueryDTO dto = new EsAdminOrderQueryDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setOrderState(form.getOrder_status());
        DubboPageResult<EsOrderDO> result = orderService.getEsAdminOrderList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsOrderVO> esOrderVOList = BeanUtil.copyList(result.getData().getList(), EsOrderVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esOrderVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "获取订单详情", response = EsAdminOrderVO.class)
    @GetMapping("/getEsAdminOrderInfo/{orderSn}")
    @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "string", paramType = "path", required = true)
    @ResponseBody
    public ApiResponse getEsAdminOrderInfo(@PathVariable("orderSn") String orderSn) {
        DubboResult<EsSellerOrderDO> result = orderService.getEsAdminOrderInfo(orderSn);
        if (result.isSuccess()) {
            EsAdminOrderVO adminOrderVO = new EsAdminOrderVO();
            BeanUtil.copyProperties(result.getData(), adminOrderVO);
            return ApiResponse.success(adminOrderVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getOrderStatusList")
    @ApiOperation(value = "获取订单状态下拉别表", response = LabelValueBeanVO.class)
    @ResponseBody
    public ApiResponse getOrderStatusList() {
        List<LabelValueBeanVO> list = new ArrayList<>();
        LabelValueBeanVO bean = null;
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            bean = new LabelValueBeanVO();
            if ("NEW".equals(e.value()) || "INTO_DB_ERROR".equals(e.value())) {
                continue;
            }
            if ("CONFIRM".equals(e.value())) {
                bean.setKey(e.value());
                bean.setValue("待付款");
            } else if ("PAID_OFF".equals(e.value())) {
                bean.setKey(e.value());
                bean.setValue("待发货");
            } else if ("SHIPPED".equals(e.value())) {
                bean.setKey(e.value());
                bean.setValue("待收货");
            } else {
                bean.setKey(e.value());
                bean.setValue(e.description());
            }
            list.add(bean);
        }
        return ApiResponse.success(list);
    }
}
