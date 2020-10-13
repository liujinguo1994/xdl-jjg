package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsCommentConfigForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 前端控制器-商品好评设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-24
 */
@Api(value = "/esCommentSortConfig", tags = "商品好评设置")
@RestController
@RequestMapping("/esCommentSortConfig")
public class EsCommentSortConfigController {

    @Autowired
    private IEsCommentSortConfigService commentSortConfigService;

    @Autowired
    private IEsGradeWeightConfigService gradeWeightConfigService;

    @PutMapping(value = "/saveConfig")
    @ApiOperation(value = "保存设置")
    @ResponseBody
    public ApiResponse saveConfig(@Valid @RequestBody @ApiParam(name = "设置form对象", value = "form") EsCommentConfigForm form) {
        EsCommentConfigDTO esCommentConfigDTO = new EsCommentConfigDTO();
        List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList = BeanUtil.copyList(form.getGradeWeightConfigFormList(), EsGradeWeightConfigDTO.class);
        esCommentConfigDTO.setGradeWeightConfigDTOList(gradeWeightConfigDTOList);
        List<EsCommentSortConfigDTO> commentSortConfigDTOList = BeanUtil.copyList(form.getCommentSortConfigFormList(), EsCommentSortConfigDTO.class);
        esCommentConfigDTO.setCommentSortConfigDTOList(commentSortConfigDTOList);
        DubboResult result = commentSortConfigService.insertCommentSortConfig(esCommentConfigDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "查询权重设置", response = EsGradeWeightConfigVO.class)
    @GetMapping(value = "/getGradeWeightConfigList")
    @ResponseBody
    public ApiResponse getGradeWeightConfigList() {
        DubboPageResult<EsGradeWeightConfigDO> result = gradeWeightConfigService.getGradeWeightConfigList();
        if (result.isSuccess()) {
            List<EsGradeWeightConfigDO> data = result.getData().getList();
            List<EsGradeWeightConfigVO> esGradeWeightConfigVOList = BeanUtil.copyList(data, EsGradeWeightConfigVO.class);
            return ApiResponse.success(esGradeWeightConfigVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询好中差评设置")
    @GetMapping(value = "/getCommentSortConfigList")
    @ResponseBody
    public ApiResponse getCommentSortConfigList() {
        DubboPageResult<EsCommentSortConfigDO> result = commentSortConfigService.getCommentSortConfigList();
        if (result.isSuccess()) {
            List<EsCommentSortConfigDO> data = result.getData().getList();
            List<EsCommentSortConfigVO> esCommentSortConfigVOList = BeanUtil.copyList(data, EsCommentSortConfigVO.class);
            return ApiResponse.success(esCommentSortConfigVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

