package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDepositDO;
import com.shopx.member.api.model.domain.dto.EsMemberDepositDTO;
import com.shopx.member.api.model.domain.vo.EsMemberDepositVO;
import com.shopx.member.api.service.IEsMemberDepositService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsMemberDepositQueryForm;
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
 * @author xl 236154186@qq.com
 * @since 2019-12-13
 */
@Api(value = "/esMemberDeposit", tags = "会员余额明细接口")
@RestController
@RequestMapping("/esMemberDeposit")
public class EsMemberDepositController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberDepositService iesMemberDepositService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iEsMemberService;

    @ApiOperation(value = "余额明细列表", notes = "余额明细列表", response = EsMemberDepositVO.class)
    @GetMapping("/getMemberDepositList")
    @ResponseBody
    public ApiResponse getMemberDepositList(@Valid EsMemberDepositQueryForm form) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDepositDTO es = new EsMemberDepositDTO();
        es.setThreeMonth(form.getThreeMonth());
        es.setMemberId(userId);
        DubboPageResult<EsMemberDepositDO> result = iesMemberDepositService.getMemberDepositList(es, form.getPageSize(), form.getPageNum());
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

