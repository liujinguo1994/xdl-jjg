package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.dto.EsMemberAddressDTO;
import com.shopx.member.api.model.domain.vo.EsMemberAddressVO;
import com.shopx.member.api.model.domain.vo.wap.EsWapMemberAddressForm;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(value = "/wap/member/address", tags = "移动端-用户地址")
@RestController
@RequestMapping("/wap/member/address")
public class EsWapAddressController {

    private static Logger logger = LoggerFactory.getLogger(EsWapAddressController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;


    @ApiOperation(value = "查询会员地址列表", notes = "查询会员地址列表",response = EsMemberAddressVO.class)
    @GetMapping("/getAddressesLists")
    @ResponseBody
    public ApiResponse getAddressLists() {
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        Long userId = ShiroKit.getUser().getId();
        esMemberAddressDTO.setMemberId(userId);
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

    @ApiOperation(value = "新增地址", notes = "依据前端传递表单值新增地址",response = EsMemberAddressVO.class)
    @PostMapping("/insertMemberAddress")
    @ResponseBody
    public ApiResponse insertMemberAddress(@RequestBody @Valid EsWapMemberAddressForm esMemberAddressForm) {
        //验证用户手机号
        Boolean judgeMobile = FormatValidateUtils.isMobile(esMemberAddressForm.getMobile());
        if (!judgeMobile) {
            return ApiResponse.fail(MemberErrorCode.FORM_IS_ERROR.getErrorCode(), MemberErrorCode.FORM_IS_ERROR.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        Long userId = ShiroKit.getUser().getId();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        esMemberAddressDTO.setMemberId(userId);
        esMemberAddressDTO.setCountry("中国");
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.insertMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改收货地址", notes = "手机端修改收货地址",response = EsMemberAddressVO.class)
    @PutMapping("/updateMemberAddress")
    @ResponseBody
    public ApiResponse updateMemberAddress(@RequestBody @Valid EsWapMemberAddressForm esMemberAddressForm) {
        //验证用户手机号
        Boolean judgeMobile = FormatValidateUtils.isMobile(esMemberAddressForm.getMobile());
        if (!judgeMobile) {
            return ApiResponse.fail(MemberErrorCode.FORM_IS_ERROR.getErrorCode(), "手机号格式有误");
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        Long userId = ShiroKit.getUser().getId();
        esMemberAddressDTO.setMemberId(userId);
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.updateMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }

    @ApiOperation(value = "设置默认收货地址", notes = "设置收货地址",response = EsMemberAddressVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "要设置的地址id", required = true, dataType = "Long", paramType = "query", example = "1")
    })
    @PostMapping("/setDefaultAddress")
    @ResponseBody
    public ApiResponse setAddress( Long addressId) {
        Long userId = ShiroKit.getUser().getId();
        //获取当前用户id
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.setDefaultMemberAddress(addressId, userId);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的会员地址id", required = true, dataType = "Long", paramType = "path", example = "1")
    })
    @DeleteMapping("/deleteMemberAddress/{id}")
    @ResponseBody
    public ApiResponse deleteMemberAddress(@PathVariable Long id) {
        DubboResult result = iesMemberAddressService.deleteMemberAddress(id);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询默认收货地址", notes = "查询默认收货地址")
    @GetMapping("/getDefaultAddress")
    @ResponseBody
    public ApiResponse getDefaultAddress() {
        //获取当前用户id
        Long userId = ShiroKit.getUser().getId();
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getWapDefaultMemberAddress(userId);
        if (result.isSuccess()) {
            EsMemberAddressVO esMemberAddressVO = new EsMemberAddressVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberAddressVO);
            }
            return ApiResponse.success(esMemberAddressVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


}