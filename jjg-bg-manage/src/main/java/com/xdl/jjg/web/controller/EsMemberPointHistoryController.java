package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsMemberAdminDO;
import com.xdl.jjg.model.domain.EsMemberPointHistoryDO;
import com.xdl.jjg.model.dto.EsMemberDTO;
import com.xdl.jjg.model.dto.EsMemberPointHistoryDTO;
import com.xdl.jjg.model.form.EsMemberPointHistoryQueryForm;
import com.xdl.jjg.model.form.EsMemberPointQueryForm;
import com.xdl.jjg.model.vo.EsMemberAdminVO;
import com.xdl.jjg.model.vo.EsMemberPointHistoryVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberPointHistoryService;
import com.xdl.jjg.web.service.IEsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-会员积分明细
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMemberPointHistory")
@Api(value = "/esMemberPointHistory", tags = "会员积分明细")
public class EsMemberPointHistoryController {

    @Autowired
    private IEsMemberPointHistoryService iesMemberPointHistoryService;

    @Autowired
    private IEsMemberService memberService;

    @ApiOperation(value = "分页查询会员积分列表", response = EsMemberAdminVO.class)
    @GetMapping(value = "/getMemberPointList")
    @ResponseBody
    public ApiResponse getMemberPointList(EsMemberPointQueryForm form) {
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(form, esMemberDTO);
        DubboPageResult<EsMemberAdminDO> result = memberService.getMemberList(esMemberDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberAdminDO> data = result.getData().getList();
            List<EsMemberAdminVO> voList = BeanUtil.copyList(data, EsMemberAdminVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询会员积分明细列表", response = EsMemberPointHistoryVO.class)
    @GetMapping(value = "/getMemberPointHistoryList")
    @ResponseBody
    public ApiResponse getMemberPointHistoryList(@Valid EsMemberPointHistoryQueryForm form) {
        EsMemberPointHistoryDTO dto = new EsMemberPointHistoryDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsMemberPointHistoryDO> result = iesMemberPointHistoryService.getMemberPointHistoryList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberPointHistoryDO> data = result.getData().getList();
            List<EsMemberPointHistoryVO> voList = BeanUtil.copyList(data, EsMemberPointHistoryVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

