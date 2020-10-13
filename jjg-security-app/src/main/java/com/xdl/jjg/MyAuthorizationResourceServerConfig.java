package com.xdl.jjg;

import com.xdl.jjg.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.xdl.jjg.code.ValidateCodeSecurityConfig;
import com.xdl.jjg.properties.SecurityConstants;
import com.xdl.jjg.properties.SecurityProperties;
import com.xdl.jjg.social.openid.OpenIdAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.web.cors.CorsUtils;

/**
 * @Author: ciyuan
 * @Date: 2019/6/12 21:34
 * spring security 资源服务器
 */
@Configuration
@EnableResourceServer
public class MyAuthorizationResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer easySocialConfigurer;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler);
        // TODO: 2019/9/2  并发策略处理
        //验证码的安全配置
        http.apply(validateCodeSecurityConfig)
                .and()
                //短信验证码安全配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                //社交登录安全配置
                .apply(easySocialConfigurer)
                .and()
                //第三方登录安全配置
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
                //声明不需要登录验证的接口
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        "/social/signUp",
                        "/social/registered",
                        "/phone/registered",
                        "/code/sms",
                        "/user/myTeam",
                        "/swagger**/**",
                        "/v2/**",
                        "/auction/class/list",
                        "/auction/time/list",
                        "/auction/list",
                        "/auction/getAuctionVOByAuctionId",
                        "/banner/list",
                        "/banner/getVersionByType",
                        "/trade/order/list/confirm/receipt",
                        "/authentication/mobile",
                        "/user/social/registered",
                        "/user/phone/registered",
                        "/user/social/mpRegistered",
                        "/weixin/mpLogin",
                        "/images/**",
                        "/test/**",
                        "/webjars/**",
                        "/wechat/getSign/JS-SDK",
                        "/pay/weiXinNotify",
                        "/pay/weiXinRefundNotify",
                        "/configuration/ui",
                        "/configuration/security",
                        "/auction/video/list",
                        "/auction/video/listComment",
                        securityProperties.getBrowser().getSession().getSessionInvalidUrl()
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();

        //http.authorizeRequests().anyRequest() .permitAll().and().logout().permitAll();
    }
}
