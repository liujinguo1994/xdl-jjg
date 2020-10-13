package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberLevelConfigDO;
import com.shopx.member.api.model.domain.vo.wap.EsWapMemberInfoVO;
import com.shopx.member.api.service.IEsMemberLevelConfigService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 小程序-会员管理 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-18 09:28:26
 */
@Api(value = "/applet/member/memberManage", tags = "小程序-会员管理")
@RestController
@RequestMapping("/applet/member/memberManage")
public class EsAppletMemberManageController {

    private static Logger logger = LoggerFactory.getLogger(EsAppletMemberManageController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberLevelConfigService esMemberLevelConfigService;


    @ApiOperation(value = "查询会员信息",response = EsWapMemberInfoVO.class)
    @GetMapping("/getMemberInfo/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse getMemberInfo(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        if (dubboResult.isSuccess()){
            EsMemberDO esMemberDO = dubboResult.getData();
            EsWapMemberInfoVO memberInfoVO = new EsWapMemberInfoVO();
            BeanUtil.copyProperties(esMemberDO, memberInfoVO);
            Integer grade = memberInfoVO.getGrade() == null ? 0 : memberInfoVO.getGrade();
            DubboResult<EsMemberLevelConfigDO> resultGrade = esMemberLevelConfigService.getMemberLevelByGrade(grade);
            if(resultGrade.isSuccess() &&  null != resultGrade.getData().getLevel()){
                memberInfoVO.setGradeLevel(resultGrade.getData().getLevel());
            }
            return ApiResponse.success(memberInfoVO);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
        }
    }
}

