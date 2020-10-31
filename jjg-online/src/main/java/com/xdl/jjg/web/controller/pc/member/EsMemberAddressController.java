package com.xdl.jjg.web.controller.pc.member;


import com.jjg.member.model.domain.EsMemberAddressDO;
import com.jjg.member.model.dto.EsMemberAddressDTO;
import com.jjg.member.model.vo.EsMemberAddressVO;
import com.jjg.trade.model.form.EsMemberAddressForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.manager.CheckoutParamManager;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.FormatValidateUtils;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.MemberAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esMemberAddress", tags = "会员收货地址接口")
@RestController
@RequestMapping("/esMemberAddress")
public class EsMemberAddressController extends BaseController {

    @Autowired
    private MemberAddressService iesMemberAddressService;
    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @ApiOperation(value = "查询会员地址列表", notes = "查询会员地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1"),
    })
    @GetMapping("/getAddressesList")
    @ResponseBody
    public ApiResponse getAddressList(@NotEmpty int pageNum, @NotEmpty int pageSize) {
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        Long userId = ShiroKit.getUser().getId();
        esMemberAddressDTO.setMemberId(userId);
        DubboPageResult<EsMemberAddressDO> result = iesMemberAddressService.getMemberAddressList(esMemberAddressDTO, pageSize, pageNum);
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
            return ApiResponse.success(DubboPageResult.success(result.getData().getTotal(),esMemberAddressVOList));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询会员地址列表没有分页", notes = "查询会员地址列表无分页")
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
            return ApiResponse.success(esMemberAddressVOList);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "新增地址", notes = "依据前端传递表单值新增地址")
    @PostMapping("/insertMemberAddress")
    @ResponseBody
    public ApiResponse insertMemberAddress(@Valid EsMemberAddressForm esMemberAddressForm) {
        //验证用户手机号
        Boolean judgeMobile = FormatValidateUtils.isMobile(esMemberAddressForm.getMobile());
        if (!judgeMobile) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        Long userId = ShiroKit.getUser().getId();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        esMemberAddressDTO.setMemberId(userId);
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.insertMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改收货地址", notes = "依据前端传递表单修改收货地址")
    @PutMapping("/updateMemberAddress")
    @ResponseBody
    public ApiResponse updateMemberAddress(@RequestBody @Valid EsMemberAddressForm esMemberAddressForm) {
        //验证用户手机号
        Boolean judgeMobile = FormatValidateUtils.isMobile(esMemberAddressForm.getMobile());
        if (!judgeMobile) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), "手机号格式有误");
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        Long userId = ShiroKit.getUser().getId();
        esMemberAddressDTO.setMemberId(userId);
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.updateMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
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
            // 删除缓存数据
            this.checkoutParamManager.deleteAddressId();
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
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.getDefaultMemberAddress(userId);
        if (result.isSuccess()) {
            EsMemberAddressVO esMemberAddressVO = new EsMemberAddressVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberAddressVO);
            }
            return ApiResponse.success(esMemberAddressVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "设置默认收货地址", notes = "设置收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "要设置的地址id", required = true, dataType = "Long", paramType = "path", example = "1")
    })
    @GetMapping("/setAddress/{addressId}")
    @ResponseBody
    public ApiResponse setAddress(@PathVariable Long addressId) {
        //获取当前用户id
        ShiroUser user = ShiroKit.getUser();
        if(user == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        //获取当前用户id
        DubboResult<EsMemberAddressDO> result = iesMemberAddressService.setDefaultMemberAddress(addressId, userId);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}

