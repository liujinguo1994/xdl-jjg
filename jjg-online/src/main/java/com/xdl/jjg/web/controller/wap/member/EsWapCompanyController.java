package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.DubboResult;
import com.shopx.member.api.model.domain.EsCompanyDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.service.IEsCompanyService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsCompanyForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(value = "/wap/member/company", tags = "移动端-公司模块")
@RestController
@RequestMapping("/wap/member/company")
public class EsWapCompanyController {

    private static Logger logger = LoggerFactory.getLogger(EsWapCompanyController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCompanyService companyService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService memberService;


    @ApiOperation(value = "绑定签约公司")
    @PostMapping("/bandCompany")
    @ResponseBody
    public ApiResponse getByMemberId(@RequestBody @Valid EsCompanyForm esCompanyForm) {
        Long memberId = ShiroKit.getUser().getId();
        DubboResult<EsCompanyDO> result = companyService.getCompanyByCode(esCompanyForm.getCompanyCode());
        if (result.isSuccess()){
            EsMemberDTO memberDTO=new EsMemberDTO();
            memberDTO.setId(memberId);
            memberDTO.setCompanyCode(esCompanyForm.getCompanyCode());
            memberService.updateMember(memberDTO);
            return ApiResponse.success(memberDTO);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}