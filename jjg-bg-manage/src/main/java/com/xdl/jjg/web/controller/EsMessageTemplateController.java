package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.EsMessageTemplateDO;
import com.jjg.system.model.dto.EsMessageTemplateDTO;
import com.jjg.system.model.form.EsMessageTemplateForm;
import com.jjg.system.model.form.EsMessageTemplateQueryForm;
import com.jjg.system.model.vo.EsMessageTemplateVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMessageTemplateService;
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
 * 前端控制器-消息模板
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMessageTemplate")
@Api(value = "/esMessageTemplate", tags = "消息模板")
public class EsMessageTemplateController {

    @Autowired
    private IEsMessageTemplateService iEsMessageTemplateService;

    @ApiOperation(value = "分页查询消息模板列表", response = EsMessageTemplateVO.class)
    @GetMapping(value = "/getMessageTemplateList")
    @ResponseBody
    public ApiResponse getMessageTemplateList(@Valid EsMessageTemplateQueryForm form) {
        EsMessageTemplateDTO esMessageTemplateDTO = new EsMessageTemplateDTO();
        BeanUtil.copyProperties(form, esMessageTemplateDTO);
        DubboPageResult<EsMessageTemplateDO> result = iEsMessageTemplateService.getMessageTemplateList(esMessageTemplateDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMessageTemplateDO> data = result.getData().getList();
            List<EsMessageTemplateVO> esMessageTemplateVOList = BeanUtil.copyList(data, EsMessageTemplateVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esMessageTemplateVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改消息模板")
    @PutMapping(value = "/updateMessageTemplate")
    @ResponseBody
    public ApiResponse updateMessageTemplate(@Valid @RequestBody @ApiParam(name = "消息模板form对象", value = "form") EsMessageTemplateForm form) {
        EsMessageTemplateDTO esMessageTemplateDTO = new EsMessageTemplateDTO();
        BeanUtil.copyProperties(form, esMessageTemplateDTO);
        DubboResult result = iEsMessageTemplateService.updateMessageTemplate(esMessageTemplateDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加消息模板")
    @PostMapping(value = "/insertMessageTemplate")
    @ResponseBody
    public ApiResponse insertMessageTemplate(@Valid @RequestBody @ApiParam(name = "消息模板form对象", value = "form") EsMessageTemplateForm form) {
        EsMessageTemplateDTO esMessageTemplateDTO = new EsMessageTemplateDTO();
        BeanUtil.copyProperties(form, esMessageTemplateDTO);
        DubboResult result = iEsMessageTemplateService.insertMessageTemplate(esMessageTemplateDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteMessageTemplate/{id}")
    @ResponseBody
    @ApiOperation(value = "删除消息模板")
    @ApiImplicitParam(name = "id", value = "消息模板id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteMessageTemplate(@PathVariable Long id) {
        DubboResult result = iEsMessageTemplateService.deleteMessageTemplate(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
