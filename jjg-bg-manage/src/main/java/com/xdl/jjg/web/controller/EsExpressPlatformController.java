package com.xdl.jjg.web.controller;

import com.jjg.system.model.domain.EsExpressPlatformDO;
import com.jjg.system.model.dto.EsExpressPlatformDTO;
import com.jjg.system.model.form.EsExpressPlatForm;
import com.jjg.system.model.vo.EsExpressPlatformVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsExpressPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 前端控制器-快递平台设置
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-26 10:30:00
 */
@Api(value = "/express", tags = "快递平台设置")
@RestController
@RequestMapping("/express")
public class EsExpressPlatformController {

    @Autowired
    private IEsExpressPlatformService expressPlatformService;

    @GetMapping(value = "/getList")
    @ApiOperation(value = "获取快递平台设置", response = EsExpressPlatformVO.class)
    @ResponseBody
    public ApiResponse getSecuritySetting(int pageSize, int pageNum) {
        DubboPageResult<EsExpressPlatformVO> result = expressPlatformService.getExpressPlatformList(pageSize, pageNum);
        if (result.isSuccess()) {
            List<EsExpressPlatformVO> data = result.getData().getList();
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), data);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/{bean}")
    @ApiOperation(value = "修改快递平台设置")
    @ResponseBody
    public ApiResponse updateExpress(@PathVariable String bean, @RequestBody @ApiIgnore EsExpressPlatForm form) {
        form.setBean(bean);
        EsExpressPlatformDTO esExpressPlatformDTO = new EsExpressPlatformDTO();
        BeanUtil.copyProperties(form, esExpressPlatformDTO);
        esExpressPlatformDTO.setConfig(JsonUtil.objectToJson(form.getConfigItems()));
        DubboResult result = expressPlatformService.updateExpressPlatform(esExpressPlatformDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "获取快递平台的配置", response = String.class)
    @GetMapping("/{id}")
    @ApiImplicitParam(name = "bean", value = "快递平台bean id", required = true, dataType = "String", paramType = "path")
    public ApiResponse getUploadSetting(@PathVariable Long id) {
        DubboResult<EsExpressPlatformDO> result = this.expressPlatformService.getExpressPlatform(id);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "开启某个快递平台方案", response = String.class)
    @PutMapping("/{bean}/open")
    @ApiImplicitParam(name = "bean", value = "bean", required = true, dataType = "String", paramType = "path")
    public ApiResponse open(@PathVariable String bean) {
        this.expressPlatformService.open(bean);
        return ApiResponse.success();
    }


//    @GetMapping(value = "/getList")
//    @ApiOperation(value = "获取快递平台设置",response = EsExpressPlatformVO.class)
//    @ResponseBody
//    public ApiResponse getSecuritySetting(int pageSize, int pageNum) {
//        DubboPageResult<EsExpressPlatformVO> result = expressPlatformService.getExpressPlatformList(pageSize, pageNum);
//        if (result.isSuccess()) {
//            List<EsExpressPlatformVO> data = result.getData().getList();
//            return ApiPageResponse.pageSuccess(result.getData().getTotal(),data);
//        } else {
//            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

}
