package com.xdl.jjg.web.controller;


import com.jjg.member.model.domain.EsAdminManagerDO;
import com.jjg.member.model.dto.QueryCommentListDTO;
import com.jjg.member.model.vo.EsAdminManagerVO;
import com.jjg.system.model.form.EsMemberCommentForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.MemberCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器-评论管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMemberComment")
@Api(value = "/esMemberComment", tags = "评论管理")
public class EsMemberCommentController {

    @Autowired
    private MemberCommentService iesMemberCommentService;

    @ApiOperation(value = "分页查询评论列表", response = EsAdminManagerVO.class)
    @GetMapping(value = "/getMemberCommentList")
    @ResponseBody
    public ApiResponse getMemberCommentList(EsMemberCommentForm form) {
        QueryCommentListDTO dto = new QueryCommentListDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsAdminManagerDO> result = iesMemberCommentService.getAdminManagerList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsAdminManagerDO> data = result.getData().getList();
            List<EsAdminManagerVO> list = BeanUtil.copyList(data, EsAdminManagerVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除或批量删除评论")
    @ApiImplicitParam(name = "ids", value = "评论id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = iesMemberCommentService.deleteDiscountComment(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

