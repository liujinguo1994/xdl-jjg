package com.xdl.jjg.web.controller;


import com.jjg.system.model.form.EsQueryPageForm;
import com.jjg.system.model.form.EsSelfDateForm;
import com.jjg.trade.model.domain.EsSelfDateDO;
import com.jjg.trade.model.dto.EsSelfDateDTO;
import com.jjg.trade.model.dto.EsSelfTimeDTO;
import com.jjg.trade.model.vo.EsSelfDateVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.trade.SelfDateService;
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
 * 前端控制器-自提日期
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Api(value = "/esSelfDate", tags = "自提日期")
@RestController
@RequestMapping("/esSelfDate")
public class EsSelfDateController {

    @Autowired
    private SelfDateService selfDateService;


    @ApiOperation(value = "分页查询自提日期列表", response = EsSelfDateVO.class)
    @GetMapping(value = "/getSelfDateList")
    @ResponseBody
    public ApiResponse getSelfDateList(EsQueryPageForm form) {
        EsSelfDateDTO esSelfDateDTO = new EsSelfDateDTO();
        DubboPageResult<EsSelfDateDO> result = selfDateService.getSelfDateList(esSelfDateDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSelfDateDO> data = result.getData().getList();
            List<EsSelfDateVO> esSelfDateVOList = BeanUtil.copyList(data, EsSelfDateVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esSelfDateVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加自提日期")
    @PostMapping(value = "/insertSelfDate")
    @ResponseBody
    public ApiResponse insertSelfDate(@Valid @RequestBody @ApiParam(name = "自提日期form对象", value = "form") EsSelfDateForm form) {
        EsSelfDateDTO esSelfDateDTO = new EsSelfDateDTO();
        BeanUtil.copyProperties(form, esSelfDateDTO);
        List<EsSelfTimeDTO> selfTimeDTOList = BeanUtil.copyList(form.getSelfTimeFormList(), EsSelfTimeDTO.class);
        esSelfDateDTO.setSelfTimeDTOList(selfTimeDTOList);
        DubboResult result = selfDateService.insertSelfDate(esSelfDateDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改自提日期")
    @PutMapping(value = "/updateSelfDate")
    @ResponseBody
    public ApiResponse updateSelfDate(@Valid @RequestBody @ApiParam(name = "自提日期form对象", value = "form") EsSelfDateForm form) {
        EsSelfDateDTO esSelfDateDTO = new EsSelfDateDTO();
        BeanUtil.copyProperties(form, esSelfDateDTO);
        List<EsSelfTimeDTO> selfTimeDTOList = BeanUtil.copyList(form.getSelfTimeFormList(), EsSelfTimeDTO.class);
        esSelfDateDTO.setSelfTimeDTOList(selfTimeDTOList);
        DubboResult result = selfDateService.updateSelfDate(esSelfDateDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除自提日期信息")
    @DeleteMapping(value = "deleteSelfDate/{id}")
    @ApiImplicitParam(name = "id", value = "自提日期id", required = true, dataType = "long", example = "1", paramType = "path")
    @ResponseBody
    public ApiResponse deleteSelfDate(@PathVariable Long id) {
        DubboResult result = selfDateService.deleteSelfDate(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

