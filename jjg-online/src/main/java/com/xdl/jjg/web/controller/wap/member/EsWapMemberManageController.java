package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberLevelConfigDO;
import com.shopx.member.api.model.domain.vo.wap.EsWapMemberInfoVO;
import com.shopx.member.api.service.*;
import com.shopx.trade.api.model.domain.vo.EsMemberBalanceVO;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 移动端-会员管理 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/member/memberManage", tags = "移动端-会员管理")
@RestController
@RequestMapping("/wap/member/memberManage")
public class EsWapMemberManageController {

    private static Logger logger = LoggerFactory.getLogger(EsWapMemberManageController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberLevelConfigService esMemberLevelConfigService;


    @ApiOperation(value = "查询会员信息",response = EsWapMemberInfoVO.class)
    @GetMapping("/getMemberInfo")
    @ResponseBody
    public ApiResponse getMemberInfo() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        DubboResult result = memberService.getMember(memberId);
        if (result.isSuccess()){
            EsMemberDO esMemberDO = (EsMemberDO) result.getData();
            EsWapMemberInfoVO memberInfoVO = new EsWapMemberInfoVO();
            if(esMemberDO.getPassword() != null){
                memberInfoVO.setPassWordIsExist("true");
            }else {
                memberInfoVO.setPassWordIsExist("false");
            }
            BeanUtil.copyProperties(esMemberDO, memberInfoVO);
            int grade = memberInfoVO.getGrade() == null? 1 : memberInfoVO.getGrade();
            if(grade == 0){
                grade = 1;
            }
            DubboResult<EsMemberLevelConfigDO> resultGrade = esMemberLevelConfigService.getMemberLevelByGrade(grade);
            if(resultGrade.isSuccess() &&  null != resultGrade.getData().getLevel()){
                memberInfoVO.setGradeLevel(resultGrade.getData().getLevel());
            }
            return ApiResponse.success(memberInfoVO);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping("/queryBalance")
    @ApiOperation(value = "查询当前会员余额、积分",response = EsMemberBalanceVO.class)
    public ApiResponse getBalance() {

        DubboResult<EsMemberDO> result = this.memberService.getMember(ShiroKit.getUser().getId());

        if (result.isSuccess()) {
            EsMemberDO esMemberDO = result.getData();
            EsMemberBalanceVO balanceVO=new EsMemberBalanceVO();
            BeanUtil.copyProperties(esMemberDO,balanceVO);
            return ApiResponse.success(balanceVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

