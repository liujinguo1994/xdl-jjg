package com.xdl.jjg.social;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * @Author: ciyuan
 * @Date: 2019/6/27 20:59
 */
public interface SocialAuthenticationFilterPostProcessor {
    /**
     * 在browser和app环境下对返回结果的不同处理
     * browser:返回登录之后的页面
     * app:返回签名
     *
     * @param socialAuthenticationFilter 过滤器
     */
    void process(SocialAuthenticationFilter socialAuthenticationFilter);


}
