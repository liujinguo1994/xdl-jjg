package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDepositDO;
import com.shopx.member.api.model.domain.dto.EsMemberDepositDTO;
import com.shopx.member.api.model.domain.vo.EsMemberDepositVO;
import com.shopx.member.api.service.IEsMemberDepositService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsWapDepositQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
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
 *会员余额明细
 * @author yuanj 595831329@qq.com
 * @since 2020-04-07
 */
@Api(value = "/wap/esMemberDeposit", tags = "移动端-会员余额明细接口")
@RestController
@RequestMapping("/wap/esMemberDeposit")
public class EsWapMemberDepositController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberDepositService iesMemberDepositService;

    @ApiOperation(value = "余额明细列表", notes = "余额明细列表", response = EsMemberDepositVO.class)
    @GetMapping("/getMemberDepositList")
    @ResponseBody
    public ApiResponse getMemberDepositList(@Valid EsWapDepositQueryForm form) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDepositDTO dto = new EsMemberDepositDTO();
        BeanUtil.copyProperties(form,dto);
        dto.setMemberId(userId);
        DubboPageResult<EsMemberDepositDO> result = iesMemberDepositService.getWapMemberDepositList(dto, form.getPageSize(), form.getPageNum());
        List<EsMemberDepositVO> esMemberDepositVOList = new ArrayList<>();
        if (result.isSuccess()) {
            if (CollectionUtils.isNotEmpty(result.getData().getList())) {
                List<EsMemberDepositDO> list = result.getData().getList();
                esMemberDepositVOList = BeanUtil.copyList(list, EsMemberDepositVO.class);
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esMemberDepositVOList);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }
}

