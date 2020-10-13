package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsCommentLabelForm;
import com.xdl.jjg.model.form.EsCommentLabelQueryForm;
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

import java.util.List;

/**
 * <p>
 * 前端控制器-商品评价标签
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-24
 */
@Api(value = "/esCommentLabel", tags = "商品评价标签")
@RestController
@RequestMapping("/esCommentLabel")
public class EsCommentLabelController {

    @Autowired
    private IEsCommentLabelService iesCommentLabelService;


    @ApiOperation(value = "分页查询商品评价标签列表", response = EsCommentLabelVO.class)
    @GetMapping(value = "/getCommentLabelList")
    @ResponseBody
    public ApiResponse getCommentLabelList(EsCommentLabelQueryForm form) {
        EsCommentLabelDTO esCommentLabelDTO = new EsCommentLabelDTO();
        BeanUtil.copyProperties(form, esCommentLabelDTO);
        esCommentLabelDTO.setCommentLabel(form.getKeyword());
        DubboPageResult<EsCommentLabelDO> result = iesCommentLabelService.getCommentLabelList(esCommentLabelDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsCommentLabelDO> data = result.getData().getList();
            List<EsCommentLabelVO> escommentlabeVOList = BeanUtil.copyList(data, EsCommentLabelVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), escommentlabeVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加商品评价标签")
    @PostMapping(value = "/insertCommentLabel")
    @ResponseBody
    public ApiResponse insertCommentLabel(@RequestBody @ApiParam(name = "商品评价标签form对象", value = "form") EsCommentLabelForm form) {
        EsCommentLabelDTO esCommentLabelDTO = new EsCommentLabelDTO();
        BeanUtil.copyProperties(form, esCommentLabelDTO);
        DubboResult result = iesCommentLabelService.insertCommentLabel(esCommentLabelDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改商品评价标签")
    @PutMapping(value = "/updateCommentLabel/{id}")
    @ResponseBody
    public ApiResponse updateCommentLabel(@RequestBody @ApiParam(name = "商品评价标签form对象", value = "form") EsCommentLabelForm form, @PathVariable Long id) {
        EsCommentLabelDTO esCommentLabelDTO = new EsCommentLabelDTO();
        BeanUtil.copyProperties(form, esCommentLabelDTO);
        esCommentLabelDTO.setId(id);
        DubboResult result = iesCommentLabelService.updateCommentLabel(esCommentLabelDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "批量删除商品评论标签")
    @ApiImplicitParam(name = "ids", value = "评论标签id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = iesCommentLabelService.deleteEsCommentLabelBatch(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}

