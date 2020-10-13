package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberDepositDO;
import com.shopx.member.api.model.domain.dto.EsMemberDepositDTO;
import com.shopx.member.api.model.domain.vo.EsMemberDepositVO;
import com.shopx.member.api.service.IEsMemberDepositService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsAppletDepositQueryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 小程序-会员余额明细 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-18 09:28:26
 */
@Api(value = "/applet/member/memberDeposit", tags = "小程序-会员余额明细")
@RestController
@RequestMapping("/applet/member/memberDeposit")
public class EsAppletMemberDepositController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberDepositService iesMemberDepositService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @ApiOperation(value = "余额明细列表", notes = "余额明细列表", response = EsMemberDepositVO.class)
    @GetMapping("/getMemberDepositList")
    @ResponseBody
    public ApiResponse getMemberDepositList(@Valid EsAppletDepositQueryForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDepositDTO dto = new EsMemberDepositDTO();
        dto.setMemberId(dubboResult.getData().getId());
        dto.setType(form.getType());
        DubboPageResult<EsMemberDepositDO> dubboPageResult = iesMemberDepositService.getWapMemberDepositList(dto, form.getPageSize(), form.getPageNum());
        List<EsMemberDepositVO> esMemberDepositVOList = new ArrayList<>();
        if (dubboPageResult.isSuccess()) {
            if (CollectionUtils.isNotEmpty(dubboPageResult.getData().getList())) {
                List<EsMemberDepositDO> list = dubboPageResult.getData().getList();
                esMemberDepositVOList = BeanUtil.copyList(list, EsMemberDepositVO.class);
            }
            return ApiPageResponse.pageSuccess(dubboPageResult.getData().getTotal(), esMemberDepositVOList);
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(dubboPageResult));
        }
    }
}

