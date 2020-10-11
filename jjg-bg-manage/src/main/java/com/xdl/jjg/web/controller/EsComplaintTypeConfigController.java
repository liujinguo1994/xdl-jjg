package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsComplaintTypeConfigForm;
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

/**
 * <p>
 * 前端控制器-投诉类型
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esComplaintTypeConfig",tags = "投诉类型")
@RestController
@RequestMapping("/esComplaintTypeConfig")
public class EsComplaintTypeConfigController {

    @Autowired
    private IEsComplaintTypeConfigService complaintTypeConfigService;

    @ApiOperation(value = "分页查询投诉类型",response = EsComplaintTypeConfigVO.class)
    @GetMapping(value = "/getComplaintTypeConfigList")
    @ResponseBody
    public ApiResponse getComplaintTypeConfigList(EsQueryPageForm form) {
        EsComplaintTypeConfigDTO dto = new EsComplaintTypeConfigDTO();
        DubboPageResult<EsComplaintTypeConfigDO> result = complaintTypeConfigService.getComplaintTypeConfigList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsComplaintTypeConfigDO> data = result.getData().getList();
            List<EsComplaintTypeConfigVO> list = BeanUtil.copyList(data, EsComplaintTypeConfigVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加投诉类型")
    @PostMapping(value = "/insertComplaintTypeConfig")
    @ResponseBody
    public ApiResponse insertComplaintTypeConfig(@Valid @RequestBody @ApiParam(name="投诉类型form对象",value="form") EsComplaintTypeConfigForm form){
        EsComplaintTypeConfigDTO dto = new EsComplaintTypeConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = complaintTypeConfigService.insertComplaintTypeConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改投诉类型")
    @PutMapping(value = "/updateComplaintTypeConfig")
    @ResponseBody
    public ApiResponse updateComplaintTypeConfig(@Valid @RequestBody @ApiParam(name="投诉类型form对象",value="form") EsComplaintTypeConfigForm form){
        EsComplaintTypeConfigDTO dto = new EsComplaintTypeConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = complaintTypeConfigService.updateComplaintTypeConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteComplaintTypeConfig/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse deleteComplaintTypeConfig(@PathVariable Long id){
        DubboResult result = complaintTypeConfigService.deleteComplaintTypeConfig(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
