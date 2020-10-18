package com.xdl.jjg.web.controller;


import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.jjg.member.model.dto.EsMemberLevelConfigDTO;
import com.jjg.member.model.dto.EsQueryMemberLevelConfigDTO;
import com.jjg.member.model.vo.EsMemberLevelConfigVO;
import com.jjg.system.model.form.EsMemberLevelConfigForm;
import com.jjg.system.model.form.EsMemberLevelQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.MemberLevelConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 前端控制器-会员等级配置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-04 09:42:03
 */
@RestController
@Api(value = "/esMemberLevelConfig", tags = "会员等级配置")
@RequestMapping("/esMemberLevelConfig")
public class EsMemberLevelConfigController {

    @Autowired
    private MemberLevelConfigService memberLevelConfigService;

    @ApiOperation(value = "分页查询会员等级列表", response = EsMemberLevelConfigVO.class)
    @GetMapping(value = "/getMemberLevelConfigList")
    @ResponseBody
    public ApiResponse getMemberLevelConfigList(EsMemberLevelQueryForm form) {
        EsQueryMemberLevelConfigDTO dto = new EsQueryMemberLevelConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsMemberLevelConfigDO> result = memberLevelConfigService.getMemberLevelConfigList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberLevelConfigDO> data = result.getData().getList();
            List<EsMemberLevelConfigVO> esMemberLevelConfigVOList = BeanUtil.copyList(data, EsMemberLevelConfigVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esMemberLevelConfigVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加会员等级")
    @PostMapping(value = "/insertMemberLevelConfig")
    @ResponseBody
    public ApiResponse insertMemberLevelConfig(@Valid @RequestBody @ApiParam(name = "会员等级form对象", value = "form") EsMemberLevelConfigForm form) {
        EsMemberLevelConfigDTO esMemberLevelConfigDTO = new EsMemberLevelConfigDTO();
        BeanUtil.copyProperties(form, esMemberLevelConfigDTO);
        DubboResult result = memberLevelConfigService.insertMemberLevelConfig(esMemberLevelConfigDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改会员等级")
    @PutMapping(value = "/updateMemberLevelConfig/{id}")
    @ResponseBody
    public ApiResponse updateMember(@Valid @RequestBody @ApiParam(name = "会员等级form对象", value = "form") EsMemberLevelConfigForm form, @PathVariable Long id) {
        EsMemberLevelConfigDTO esMemberLevelConfigDTO = new EsMemberLevelConfigDTO();
        BeanUtil.copyProperties(form, esMemberLevelConfigDTO);
        DubboResult result = memberLevelConfigService.updateMemberLevelConfig(esMemberLevelConfigDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除会员等级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的会员等级id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteMemberLevelConfig/{id}")
    @ResponseBody
    public ApiResponse deleteMemberLevelConfig(@PathVariable Long id) {
        DubboResult result = memberLevelConfigService.deleteMemberLevelConfig(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

