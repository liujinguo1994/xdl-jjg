package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsComplaintReasonConfigDO;
import com.shopx.member.api.model.domain.EsComplaintStatDO;
import com.shopx.member.api.model.domain.EsComplaintTypeConfigDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsComplaintDTO;
import com.shopx.member.api.model.domain.dto.EsComrImglDTO;
import com.shopx.member.api.model.domain.vo.EsComplaintReasonConfigVO;
import com.shopx.member.api.model.domain.vo.EsComplaintStatVO;
import com.shopx.member.api.model.domain.vo.EsComplaintTypeConfigVO;
import com.shopx.member.api.model.domain.vo.wap.EsWapComplaintVO;
import com.shopx.member.api.service.IEsComplaintReasonConfigService;
import com.shopx.member.api.service.IEsComplaintService;
import com.shopx.member.api.service.IEsComplaintTypeConfigService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAppletComplaintForm;
import com.shopx.trade.web.request.query.EsAppletComplaintQueryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 小程序-投诉信息模块 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-28 09:28:26
 */
@Api(value = "/applet/member/complaint",tags = "小程序-投诉信息模块")
@RestController
@RequestMapping("/applet/member/complaint")
public class EsAppletComplaintController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsComplaintService complaintService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsComplaintTypeConfigService typeConfigService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsComplaintReasonConfigService reasonConfigService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @ApiOperation(value = "新增投诉信息")
    @PostMapping("/addComplaint")
    @ResponseBody
    public ApiResponse addComplaint(@RequestBody @Valid EsAppletComplaintForm complaintForm) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(complaintForm.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsComplaintDTO complaintDTO = new EsComplaintDTO();
        BeanUtil.copyProperties(complaintForm, complaintDTO);
        complaintDTO.setMemberId(dubboResult.getData().getId());
        String imgStr = complaintForm.getImgStr();
        if (StringUtils.isNotBlank(imgStr)){
            complaintDTO.setComrImglDTOList(openImg(imgStr));
        }
        DubboResult result = complaintService.insertComplaintBuyer(complaintDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    private List<EsComrImglDTO> openImg(String imgStr){
        String[] array = imgStr.split(",");
        List<EsComrImglDTO> list = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            EsComrImglDTO comrImglDTO = new EsComrImglDTO();
            comrImglDTO.setOriginal(array[i]);
            comrImglDTO.setSort(i);
            comrImglDTO.setType(0);
            list.add(comrImglDTO);
        }
        return list;
    }

    @ApiOperation(value = "分页查询投诉信息列表", response = EsWapComplaintVO.class)
    @GetMapping(value = "/getComplaintList")
    @ResponseBody
    public ApiResponse getComplaintList(EsAppletComplaintQueryForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsComplaintDTO dto = new EsComplaintDTO();
        dto.setMemberId(dubboResult.getData().getId());
        dto.setState(form.getState());
        DubboPageResult<EsWapComplaintVO> result = complaintService.getWapComplaintList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsWapComplaintVO> list = result.getData().getList();
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询投诉类型列表", response =EsComplaintTypeConfigVO.class)
    @GetMapping(value = "/getComplaintTypeList")
    @ResponseBody
    public ApiResponse getComplaintTypeList() {
        DubboPageResult<EsComplaintTypeConfigDO> result = typeConfigService.getComplaintTypeConfigListInfo();
        if (result.isSuccess()) {
            List<EsComplaintTypeConfigDO> data = result.getData().getList();
            List<EsComplaintTypeConfigVO> list = BeanUtil.copyList(data, EsComplaintTypeConfigVO.class);
            return ApiResponse.success(list);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据投诉类型ID查询投诉原因列表", response =EsComplaintReasonConfigVO.class)
    @GetMapping(value = "/getByTypeId/{complainTypeId}")
    @ApiImplicitParam(name = "complainTypeId", value = "投诉类型id", required = true, dataType = "long", paramType = "path")
    @ResponseBody
    public ApiResponse getByTypeId(@PathVariable Long complainTypeId) {
        DubboPageResult<EsComplaintReasonConfigDO> result = reasonConfigService.getByTypeId(complainTypeId);
        if (result.isSuccess()){
            List<EsComplaintReasonConfigDO> data = result.getData().getList();
            List<EsComplaintReasonConfigVO> list = BeanUtil.copyList(data, EsComplaintReasonConfigVO.class);
            return ApiResponse.success(list);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "投诉数量统计", response =EsComplaintStatVO.class)
    @GetMapping(value = "/getComplaintStat/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse getComplaintStat(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsComplaintStatDO> result = complaintService.getComplaintStat(dubboResult.getData().getId());
        if (result.isSuccess()){
            EsComplaintStatVO vo = new EsComplaintStatVO();
            BeanUtil.copyProperties(result.getData(),vo);
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

