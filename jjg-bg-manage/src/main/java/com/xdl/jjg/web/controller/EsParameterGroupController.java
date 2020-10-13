package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.dto.EsParameterGroupDTO;
import com.xdl.jjg.model.form.EsParameterGroupForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsParameterGroupService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p>
 * 前端控制器-参数组
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esParameterGroup", tags = "参数组")
@RestController
@RequestMapping("/esParameterGroup")
public class EsParameterGroupController {

    @Autowired
    private IEsParameterGroupService parameterGroupService;

    @PostMapping(value = "/insertEsParameterGroup")
    @ResponseBody
    @ApiOperation(value = "新增参数组信息")
    public ApiResponse insertEsParameterGroup(@Valid @RequestBody @ApiParam(name = "参数组form对象", value = "form") EsParameterGroupForm form) {
        EsParameterGroupDTO parameterGroupDTO = new EsParameterGroupDTO();
        BeanUtil.copyProperties(form, parameterGroupDTO);
        DubboResult result = parameterGroupService.insertParameterGroup(parameterGroupDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateParameterGroup/{id}")
    @ResponseBody
    @ApiOperation(value = "编辑参数组信息")
    public ApiResponse updateParameterGroup(@Valid @RequestBody @ApiParam(name = "参数组form对象", value = "form") EsParameterGroupForm form, @PathVariable Long id) {
        EsParameterGroupDTO parameterGroupDTO = new EsParameterGroupDTO();
        BeanUtil.copyProperties(form, parameterGroupDTO);
        DubboResult result = parameterGroupService.updateParameterGroup(parameterGroupDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteParameterGroup/{id}")
    @ResponseBody
    @ApiOperation(value = "删除参数组信息")
    @ApiImplicitParam(name = "id", value = "参数组id", required = true, dataType = "long", paramType = "path", example = "1")
    public ApiResponse deleteParameterGroup(@PathVariable Long id) {
        DubboResult result = parameterGroupService.deleteParameterGroup(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "参数组上移或者下移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数组id", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "sortType", value = "排序类型，上移 up，下移down", required = true, paramType = "path", dataType = "String")})
    @PutMapping(value = "/groupSort/{id}/{sortType}")
    @ResponseBody
    public ApiResponse groupSort(@PathVariable Long id, @PathVariable String sortType) {
        DubboResult result = parameterGroupService.sortParameterGroup(id, sortType);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}

