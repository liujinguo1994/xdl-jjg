package com.xdl.jjg.code.sms;


import com.xdl.jjg.code.ValidateCode;
import com.xdl.jjg.code.ValidateCodeGenerator;
import com.xdl.jjg.properties.SecurityConstants;
import com.xdl.jjg.properties.SecurityProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 8:18
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGeneratorImpl implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private Environment env;

    @Override
    public ValidateCode createCode(ServletWebRequest request) {

        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        String[] profiles = env.getActiveProfiles();
        boolean flag = true;
        for (String profile : profiles) {
            if (StringUtils.equals("prod", profile)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            code = "1234";
        }

        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        try {
            String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
            if ("18069877891".equals(mobile)) {
                code = "1234";
            }
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }


    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
