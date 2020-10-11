package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsCustomCategoryForm;
import com.xdl.jjg.model.form.EsCustomCategoryQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 自定义分类 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Api(value = "/esCustomCategory",tags = "自定义分类")
@RestController
@RequestMapping("/esCustomCategory")
public class EsCustomCategoryController {

    @Autowired
    private IEsCustomCategoryService iesCustomCategoryService;
    @Autowired
    private IEsZoneService zoneService;

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsCustomCategory")
    @ResponseBody
    public ApiResponse insertEsCustomCategory(@RequestBody @ApiParam(name="自定义分类form对象",value="form") @Valid EsCustomCategoryForm form){
        EsCustomCategoryDTO dto=new EsCustomCategoryDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iesCustomCategoryService.insertCustomCategory(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsCustomCategory")
    @ResponseBody
    public ApiResponse editEsCustomCategory(@RequestBody @ApiParam(name="自定义分类form对象",value="form") @Valid EsCustomCategoryForm form){
        EsCustomCategoryDTO dto = new EsCustomCategoryDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iesCustomCategoryService.updateCustomCategory(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @DeleteMapping(value = "/deleteEsCustomCategory/{id}")
    @ResponseBody
    public ApiResponse deleteEsCustomCategory(@PathVariable Long id) {
        DubboResult result = iesCustomCategoryService.deleteCustomCategory(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表",response = EsCustomCategoryVO.class)
    @GetMapping(value = "/getEsCustomCategory")
    @ResponseBody
    public ApiResponse getEsCustomCategory(EsCustomCategoryQueryForm form) {
        EsCustomCategoryDTO dto = new EsCustomCategoryDTO();
        BeanUtil.copyProperties(form,dto);
        DubboPageResult<EsCustomCategoryDO> result = iesCustomCategoryService.getCustomCategoryList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsCustomCategoryDO> data = result.getData().getList();
            List<EsCustomCategoryVO> voList = BeanUtil.copyList(data, EsCustomCategoryVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "常买清单与发现好货的自定义分类下拉框",response = EsCustomCategoryVO.class)
    @GetMapping(value = "/getEsCustomCategoryList/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1是常买清单，2发现好货", required = true, dataType = "int", paramType = "path")
    })
    @ResponseBody
    public ApiResponse getEsCustomCategoryList(@PathVariable Integer type) {
        EsCustomCategoryDTO dto = new EsCustomCategoryDTO();
        dto.setZoneId(type.longValue());
        DubboPageResult<EsCustomCategoryDO> result = iesCustomCategoryService.getCustomCategoryList(dto, 100, 1);
        if (result.isSuccess()) {
            List<EsCustomCategoryDO> data = result.getData().getList();
            List<EsCustomCategoryVO> voList = BeanUtil.copyList(data, EsCustomCategoryVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

