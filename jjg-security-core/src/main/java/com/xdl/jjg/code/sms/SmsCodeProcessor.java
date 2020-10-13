package com.xdl.jjg.code.sms;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdl.jjg.code.ValidateCode;
import com.xdl.jjg.code.impl.AbstractValidateCodeProcessor;
import com.xdl.jjg.properties.SecurityConstants;
import com.xdl.jjg.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 22:51
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSender smsCodeSender;


    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
        smsCodeSender.send(mobile, validateCode.getCode());
        request.getResponse().setContentType("application/json;charset=UTF-8");
        request.getResponse().getWriter().write(objectMapper.writeValueAsString(ResponseMessage.ok("短信发送成功！")));
    }
}
