package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsParametersForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p>
 *  前端控制器-参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-21 15:46:00
 */
@Api(value = "/esParameters",tags = "参数")
@RestController
@RequestMapping("/esParameters")
public class EsParametersController {

    @Autowired
    private IEsParametersService iEsParametersService;

    @PostMapping(value = "/insertParameters")
    @ResponseBody
    @ApiOperation(value = "新增参数信息")
    public ApiResponse insertParameters(@Valid @RequestBody @ApiParam(name="参数form对象",value="form") EsParametersForm form) {
        EsParametersDTO parametersDTO = new EsParametersDTO();
        BeanUtil.copyProperties(form, parametersDTO);
        DubboResult result = iEsParametersService.insertParameters(parametersDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateParameters/{id}")
    @ResponseBody
    @ApiOperation(value = "编辑参数信息")
    public ApiResponse updateParameters(@Valid @RequestBody @ApiParam(name="参数form对象",value="form") EsParametersForm form, @PathVariable Long id) {
        EsParametersDTO parametersDTO = new EsParametersDTO();
        BeanUtil.copyProperties(form, parametersDTO);
        DubboResult result = iEsParametersService.updateParameters(parametersDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteParameters/{id}")
    @ResponseBody
    @ApiOperation(value = "删除参数信息")
    @ApiImplicitParam(name = "id", value = "参数id", required = true, dataType = "long", paramType = "path",example = "1")
    public ApiResponse deleteParameters(@PathVariable Long id) {
        DubboResult result = iEsParametersService.deleteParameters(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "参数上移或者下移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sortType", value = "排序类型，上移 up，下移down", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "参数id", required = true, paramType = "path", dataType = "long"), })
    @PutMapping(value = "/sortParameters/{id}/{sortType}")
    @ResponseBody
    public ApiResponse sortParameters(@PathVariable("id") Long id, @PathVariable String sortType) {
        DubboResult result = iEsParametersService.sortParameters(id, sortType);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }



}

