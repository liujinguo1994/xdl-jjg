package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberAddressDTO;
import com.shopx.member.api.model.domain.vo.EsMemberAddressVO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAppletAddressForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序-用户地址
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 09:28:26
 */

@Api(value = "/applet/member/address", tags = "小程序-用户地址")
@RestController
@RequestMapping("/applet/member/address")
public class EsAppletAddressController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;


    @ApiOperation(value = "查询会员地址列表", response = EsMemberAddressVO.class)
    @GetMapping("/getAddressesLists/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse getAddressLists(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        esMemberAddressDTO.setMemberId(dubboResult.getData().getId());
        DubboPageResult<EsMemberAddressDO> result = iesMemberAddressService.getMemberAddressLists(esMemberAddressDTO);
        if (result.isSuccess()) {
            List<EsMemberAddressDO> esMemberAddressDOList = result.getData().getList();
            List<EsMemberAddressVO> esMemberAddressVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberAddressDOList)) {
                esMemberAddressVOList = esMemberAddressDOList.stream().map(esMemberAddress -> {
                    EsMemberAddressVO esMemberAddressVO = new EsMemberAddressVO();
                    BeanUtil.copyProperties(esMemberAddress, esMemberAddressVO);
                    return esMemberAddressVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(DubboPageResult.success(esMemberAddressVOList).getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "新增地址", response = EsMemberAddressVO.class)
    @PostMapping("/insertMemberAddress")
    @ResponseBody
    public ApiResponse insertMemberAddress(@RequestBody @Valid EsAppletAddressForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        //验证用户手机号
        Boolean b = FormatValidateUtils.isMobile(form.getMobile());
        if (!b) {
            return ApiResponse.fail(MemberErrorCode.FORM_IS_ERROR.getErrorCode(), MemberErrorCode.FORM_IS_ERROR.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(form, esMemberAddressDTO);
        esMemberAddressDTO.setMemberId(dubboResult.getData().getId());
        esMemberAddressDTO.setCountry("中国");
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.insertMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改收货地址",response = EsMemberAddressVO.class)
    @PutMapping("/updateMemberAddress")
    @ResponseBody
    public ApiResponse updateMemberAddress(@RequestBody @Valid EsAppletAddressForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        //验证用户手机号
        Boolean b = FormatValidateUtils.isMobile(form.getMobile());
        if (!b) {
            return ApiResponse.fail(MemberErrorCode.FORM_IS_ERROR.getErrorCode(), MemberErrorCode.FORM_IS_ERROR.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(form, esMemberAddressDTO);
        esMemberAddressDTO.setMemberId(dubboResult.getData().getId());
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.updateMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的会员地址id", required = true, dataType = "long", paramType = "path")
    })
    @DeleteMapping("/deleteMemberAddress/{id}")
    @ResponseBody
    public ApiResponse deleteMemberAddress(@PathVariable Long id) {
        DubboResult result = iesMemberAddressService.deleteMemberAddress(id);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}