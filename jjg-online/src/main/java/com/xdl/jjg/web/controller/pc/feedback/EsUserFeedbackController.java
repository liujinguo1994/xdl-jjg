package com.xdl.jjg.web.controller.pc.feedback;


import com.jjg.system.model.dto.EsUserFeedbackDTO;
import com.jjg.system.model.vo.EsUserFeedbackVO;
import com.jjg.trade.model.form.EsUserFeedbackForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.web.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/esUserFeedback")
public class EsUserFeedbackController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsUserFeedbackService iesUserFeedbackService;


    /**
     * @Description: 用户添加商城反馈信息
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/6/3 15:59
     */
    @PostMapping(value = "/addUserFeedback")
    @ApiOperation(value = "用户添加商城反馈信息",notes = "根据Form表单进行提交",response = EsUserFeedbackVO.class)
    public ApiResponse insertUserFeedback(@Valid EsUserFeedbackForm esUserFeedbackForm){
        this.isAuth();
        ShiroUser user = ShiroKit.getUser();
        if (user ==  null){
            return com.shopx.common.model.result.ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsUserFeedbackDTO esUserFeedbackDTO = new EsUserFeedbackDTO();
        BeanUtil.copyProperties(esUserFeedbackForm,esUserFeedbackDTO);
        esUserFeedbackDTO.setMemberId(user.getId());
        DubboResult dubboResult = iesUserFeedbackService.insertUserFeedback(esUserFeedbackDTO);
        if (dubboResult.isSuccess()){
            return ApiResponse.success();
        }else {
           return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
        }

    }
    /**
     * 校验权限
     */
    private void isAuth(){
        if(ShiroKit.getUser() == null){
            throw new ArgumentException(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),
                    TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
        }
    }

}

