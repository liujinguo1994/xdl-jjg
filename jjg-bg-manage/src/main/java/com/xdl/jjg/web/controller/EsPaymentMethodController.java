package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.PaymentPluginDO;
import com.xdl.jjg.model.dto.EsPaymentMethodDTO;
import com.xdl.jjg.model.form.EsPaymentMethodForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.PaymentPluginVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-支付方式
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esPaymentMethod")
@Api(value = "/esPaymentMethod", tags = "支付方式")
public class EsPaymentMethodController {

    @Autowired
    private IEsPaymentMethodService iesPaymentMethodService;

    @ApiOperation(value = "分页查询支付方式列表")
    @GetMapping(value = "/getPaymentMethodList")
    @ResponseBody
    public ApiResponse getPaymentMethodList(EsQueryPageForm form) {
        EsPaymentMethodDTO esPaymentMethodDTO = new EsPaymentMethodDTO();
        DubboPageResult result = iesPaymentMethodService.getPaymentMethodList(esPaymentMethodDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<PaymentPluginVO> voList = result.getData().getList();
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询一个支付方式")
    @GetMapping(value = "/getByPlugin/{pluginId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pluginId", value = "要查询的支付插件id", required = true, dataType = "string", paramType = "path")
    })
    @ResponseBody
    public ApiResponse getByPlugin(@PathVariable("pluginId") String pluginId) {
        DubboResult result = iesPaymentMethodService.getPaymentMethod(pluginId);
        if (result.isSuccess()) {
            PaymentPluginDO data = (PaymentPluginDO) result.getData();
            PaymentPluginVO paymentPluginVO = new PaymentPluginVO();
            BeanUtil.copyProperties(data, paymentPluginVO);
            return ApiResponse.success(paymentPluginVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改支付方式")
    @PutMapping("/updatePaymentMethod/{pluginId}")
    @ResponseBody
    public ApiResponse updatePaymentMethod(@Valid @RequestBody EsPaymentMethodForm form, @PathVariable("pluginId") String pluginId) {
        EsPaymentMethodDTO dto = new EsPaymentMethodDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iesPaymentMethodService.updatePaymentMethod(dto, pluginId);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

