package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.EsReturnReasonDO;
import com.jjg.system.model.dto.EsReturnReasonDTO;
import com.jjg.system.model.form.EsQueryPageForm;
import com.jjg.system.model.form.EsReturnReasonForm;
import com.jjg.system.model.vo.EsReturnReasonVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsReturnReasonService;
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
 * 前端控制器-售后申请原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
@Api(value = "/esReturnReason", tags = "售后申请原因")
@RestController
@RequestMapping("/esReturnReason")
public class EsReturnReasonController {

    @Autowired
    private IEsReturnReasonService returnReasonService;

    @ApiOperation(value = "分页查询售后申请原因", response = EsReturnReasonVO.class)
    @GetMapping(value = "/getReturnReasonList")
    @ResponseBody
    public ApiResponse getReturnReasonList(EsQueryPageForm form) {
        EsReturnReasonDTO dto = new EsReturnReasonDTO();
        DubboPageResult<EsReturnReasonDO> result = returnReasonService.getReturnReasonList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsReturnReasonDO> data = result.getData().getList();
            List<EsReturnReasonVO> list = BeanUtil.copyList(data, EsReturnReasonVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertReturnReason")
    @ResponseBody
    public ApiResponse insertReturnReason(@Valid @RequestBody @ApiParam(name = "原因form对象", value = "form") EsReturnReasonForm form) {
        EsReturnReasonDTO dto = new EsReturnReasonDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = returnReasonService.insertReturnReason(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/updateReturnReason")
    @ResponseBody
    public ApiResponse updateReturnReason(@Valid @RequestBody @ApiParam(name = "原因form对象", value = "form") EsReturnReasonForm form) {
        EsReturnReasonDTO dto = new EsReturnReasonDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = returnReasonService.updateReturnReason(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteReturnReason/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteReturnReason(@PathVariable Long id) {
        DubboResult result = returnReasonService.deleteReturnReason(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
