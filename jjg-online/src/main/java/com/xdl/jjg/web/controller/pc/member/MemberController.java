package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.goods.api.model.domain.vo.EsGoodsVO;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberAddressDTO;
import com.shopx.member.api.model.domain.vo.EsMemberAddressVO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsMemberAddressForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: MemberController
 * @Description: 会员信息Controller
 * @Author: libw  981087977@qq.com
 * @Date: 7/12/2019 16:57
 * @Version: 1.0
 */
@Api(value = "/members",tags = "会员相关接口")
@RequestMapping("/members")
@RestController
public class MemberController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberAddressService memberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService memberService;

    @GetMapping("/addresses")
    @ResponseBody
    @ApiOperation(value = "查询会员地址列表", notes = "查询会员地址列表", response = EsGoodsVO.class)
    public ApiResponse getMemberAddressList() {
        // 权限获取memberId
        Long memberId = ShiroKit.getUser().getId();
        DubboPageResult result = this.memberAddressService.getMemberAddressListByMemberId(memberId);
        if (result.isSuccess()) {
            List<EsMemberAddressVO > memberAddressList  = BeanUtil.copyList(result.getData().getList(),
                    EsMemberAddressVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), memberAddressList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "新增地址", notes = "依据前端传递表单值新增地址")
    @PostMapping("/addMemberAddress")
    @ResponseBody
    public ApiResponse insertMemberAddress(@Valid EsMemberAddressForm esMemberAddressForm) {
        if(StringUtils.isBlank(esMemberAddressForm.getMobile()) || !FormatValidateUtils.isMobile(esMemberAddressForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        Long memberId = ShiroKit.getUser().getId();
        if(memberId == null){
            ApiResponse.fail(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),
                    TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        esMemberAddressDTO.setMemberId(memberId);
        DubboResult<EsMemberAddressDO> result = memberAddressService.insertMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改收货地址", notes = "依据前端传递表单修改收货地址")
    @PutMapping("/updateMemberAddress/{id}")
    @ResponseBody
    public ApiResponse updateMemberAddress(@RequestBody EsMemberAddressForm esMemberAddressForm,
                                           @PathVariable Long id) {
        if(StringUtils.isBlank(esMemberAddressForm.getMobile()) || !FormatValidateUtils.isMobile(esMemberAddressForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        Long memberId = ShiroKit.getUser().getId();
        if(memberId == null){
            ApiResponse.fail(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),
                    TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
        }
        EsMemberAddressDTO esMemberAddressDTO = new EsMemberAddressDTO();
        BeanUtil.copyProperties(esMemberAddressForm, esMemberAddressDTO);
        esMemberAddressDTO.setId(id);
        esMemberAddressDTO.setMemberId(memberId);
        DubboResult<EsMemberAddressDO> result = memberAddressService.updateMemberAddress(esMemberAddressDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @GetMapping("/queryBalance")
    @ApiOperation(value = "查询当前会员余额")
    public ApiResponse getBalance() {

        DubboResult<EsMemberDO> result = this.memberService.getMember(ShiroKit.getUser().getId());

        if (result.isSuccess()) {
            return ApiResponse.success(result.getData().getMemberBalance());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
