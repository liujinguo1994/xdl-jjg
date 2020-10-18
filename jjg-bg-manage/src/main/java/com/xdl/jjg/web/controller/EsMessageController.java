package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.EsMessageDO;
import com.jjg.system.model.dto.EsMessageDTO;
import com.jjg.system.model.form.EsMessageForm;
import com.jjg.system.model.form.EsQueryPageForm;
import com.jjg.system.model.vo.EsMessageVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-站内消息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMessage")
@Api(value = "/esMessage", tags = "站内消息")
public class EsMessageController {

    @Autowired
    private IEsMessageService messageService;

    @ApiOperation(value = "分页查询站内消息", response = EsMessageVO.class)
    @GetMapping(value = "/getMessageList")
    @ResponseBody
    public ApiResponse getMessageList(EsQueryPageForm form) {
        EsMessageDTO dto = new EsMessageDTO();
        DubboPageResult<EsMessageDO> result = messageService.getMessageList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMessageVO> list = BeanUtil.copyList(result.getData().getList(), EsMessageVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加站内消息")
    @PostMapping(value = "/insertMessage")
    @ResponseBody
    public ApiResponse insertMessage(@Valid @RequestBody @ApiParam(name = "站内消息form对象", value = "form") EsMessageForm form) {
        EsMessageDTO dto = new EsMessageDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setAdminId(ShiroKit.getUser().getId());
        dto.setAdminName(ShiroKit.getUser().getUsername());
        dto.setSendTime(System.currentTimeMillis());
        DubboResult result = messageService.insertMessage(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}
