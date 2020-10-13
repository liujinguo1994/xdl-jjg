package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsComplaintReasonConfigForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-投诉原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esComplaintReasonConfig", tags = "投诉原因")
@RestController
@RequestMapping("/esComplaintReasonConfig")
public class EsComplaintReasonConfigController {

    @Autowired
    private IEsComplaintReasonConfigService complaintReasonConfigService;

    @ApiOperation(value = "分页查询投诉原因", response = EsComplaintReasonConfigVO.class)
    @GetMapping(value = "/getComplaintReasonConfigList")
    @ResponseBody
    public ApiResponse getComplaintReasonConfigList(EsQueryPageForm form) {
        EsComplaintReasonConfigDTO dto = new EsComplaintReasonConfigDTO();
        DubboPageResult<EsComplaintReasonConfigDO> result = complaintReasonConfigService.getComplaintReasonConfigList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsComplaintReasonConfigDO> data = result.getData().getList();
            List<EsComplaintReasonConfigVO> list = BeanUtil.copyList(data, EsComplaintReasonConfigVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加投诉原因")
    @PostMapping(value = "/insertComplaintReasonConfig")
    @ResponseBody
    public ApiResponse insertComplaintReasonConfig(@Valid @RequestBody @ApiParam(name = "投诉原因form对象", value = "form") EsComplaintReasonConfigForm form) {
        EsComplaintReasonConfigDTO dto = new EsComplaintReasonConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = complaintReasonConfigService.insertComplaintReasonConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改投诉原因")
    @PutMapping(value = "/updateComplaintReasonConfig")
    @ResponseBody
    public ApiResponse updateComplaintReasonConfig(@Valid @RequestBody @ApiParam(name = "投诉原因form对象", value = "form") EsComplaintReasonConfigForm form) {
        EsComplaintReasonConfigDTO dto = new EsComplaintReasonConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = complaintReasonConfigService.updateComplaintReasonConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteComplaintReasonConfig/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse batchDel(@PathVariable Long id) {
        DubboResult result = complaintReasonConfigService.deleteComplaintReasonConfig(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}
