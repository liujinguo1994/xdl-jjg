package com.xdl.jjg.web.controller.pc.member;

import com.jjg.member.model.domain.EsMemberDepositDO;
import com.jjg.member.model.dto.EsMemberDepositDTO;
import com.jjg.member.model.vo.EsMemberDepositVO;
import com.jjg.trade.model.form.query.EsMemberDepositQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.MemberDepositService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberDepositService iesMemberDepositService;

    @Autowired
    private MemberService iEsMemberService;

    @ApiOperation(value = "余额明细列表", notes = "余额明细列表", response = EsMemberDepositVO.class)
    @GetMapping("/getMemberDepositList")
    @ResponseBody
    public ApiResponse getMemberDepositList(@Valid EsMemberDepositQueryForm form) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiPageResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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

