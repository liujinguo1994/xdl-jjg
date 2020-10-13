package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCompanyDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.service.IEsCompanyService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAppletBindCompanyForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 小程序-签约公司 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-19 09:28:26
 */
@Api(value = "/applet/member/company", tags = "小程序-签约公司")
@RestController
@RequestMapping("/applet/member/company")
public class EsAppletCompanyController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCompanyService companyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;


    @ApiOperation(value = "绑定签约公司")
    @PostMapping("/bindCompany")
    @ResponseBody
    public ApiResponse getByMemberId(@RequestBody @Valid EsAppletBindCompanyForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsCompanyDO> result = companyService.getCompanyByCode(form.getCompanyCode());
        if (result.isSuccess()){
            EsMemberDTO memberDTO=new EsMemberDTO();
            memberDTO.setId(dubboResult.getData().getId());
            memberDTO.setCompanyCode(form.getCompanyCode());
            memberService.updateMember(memberDTO);
            return ApiResponse.success(memberDTO);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}