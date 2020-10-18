package com.xdl.jjg.web.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jjg.system.model.domain.EsRegionsDO;
import com.jjg.system.model.dto.EsRegionsDTO;
import com.jjg.system.model.form.EsRegionsForm;
import com.jjg.system.model.vo.EsRegionsVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRegionsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器-地区
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esRegions")
@Api(value = "/esRegions", tags = "地区")
public class EsRegionsController {

    @Autowired
    private IEsRegionsService iEsRegionsService;

    @GetMapping(value = "/children/{id}")
    @ApiOperation(value = "获取某地区的子地区", response = EsRegionsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @ResponseBody
    public ApiResponse getChildrenById(@PathVariable Long id) {
        DubboResult<List<EsRegionsDO>> result = iEsRegionsService.getChildrenById(id);
        if (result.isSuccess()) {
            List<EsRegionsDO> data = result.getData();
            List<EsRegionsVO> esRegionsVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esRegionsVOList = data.stream().map(esRegionsDO -> {
                    EsRegionsVO esRegionsVO = new EsRegionsVO();
                    BeanUtil.copyProperties(esRegionsDO, esRegionsVO);
                    return esRegionsVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esRegionsVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加地区")
    @PostMapping(value = "/insertRegions")
    @ResponseBody
    public ApiResponse insertRegions(@Valid @RequestBody @ApiParam(name = "地区form对象", value = "form") EsRegionsForm form) {
        EsRegionsDTO esRegionsDTO = new EsRegionsDTO();
        BeanUtil.copyProperties(form, esRegionsDTO);
        DubboResult result = iEsRegionsService.insertRegions(esRegionsDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改地区")
    @PutMapping(value = "/updateRegions")
    @ResponseBody
    public ApiResponse updateRegions(@Valid @RequestBody @ApiParam(name = "地区form对象", value = "form") EsRegionsForm form) {
        EsRegionsDTO esRegionsDTO = new EsRegionsDTO();
        BeanUtil.copyProperties(form, esRegionsDTO);
        DubboResult result = iEsRegionsService.updateRegions(esRegionsDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteRegions/{id}")
    @ResponseBody
    public ApiResponse deleteRegions(@PathVariable Long id) {
        DubboResult result = iEsRegionsService.deleteRegions(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}
