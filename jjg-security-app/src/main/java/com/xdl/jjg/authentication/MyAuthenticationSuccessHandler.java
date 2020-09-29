package com.xdl.jjg.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdl.jjg.properties.SecurityProperties;
import com.xdl.jjg.util.ResponseMessage;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: ciyuan
 * @Date: 2019/5/19 1:15
 * TODO: 自定义登录成功的处理
 * 继承默认的成功处理器
 */
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OAuth2AccessTokenUtil oAuth2AccessTokenUtil;

    /**
     * 这里有多种AuthorizationServerTokenServices的实现，因此必须指定bean的名称为defaultAuthorizationServerTokenServices
     */
    @Autowired
    private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;

    /**
     * 登录成功时调用的方法
     * @param request
     * @param response
     * @param authentication 封装认证信息，包括发起的认证请求信息，如：请求的ip session ；以及认证通过以后
     *                       cy.security.browser.MyUserDetailsServiceImpl#loadUserByUsername(java.lang.String)返回的UserDetails
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");

        //生成OAuth2AccessToken
        OAuth2AccessToken token = oAuth2AccessTokenUtil.get(request, authentication);
        //返回token给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResponseMessage.ok(token)));

    }
}
