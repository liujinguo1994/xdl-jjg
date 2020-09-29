package com.xdl.jjg.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Author: ciyuan
 * @Date: 2019/5/20 1:11
 * TODO： 图形验证码生成接口
 */
public interface ValidateCodeGenerator {

    /**
     * 创建验证码接口
     * @param request
     * @return
     */
    ValidateCode createCode(ServletWebRequest request);
}
