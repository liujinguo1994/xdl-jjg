package com.xdl.jjg.social.openid;

import com.xdl.jjg.properties.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: ciyuan
 * @Date: 2019/6/25 22:08
 */
public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private String openIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID;

    private String providerIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID;

    private boolean postOnly = true;

    private static final String REQUEST_METHOD = "POST";


    public OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_OPEN_ID_URL, REQUEST_METHOD));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals(REQUEST_METHOD)) {
            throw new AuthenticationServiceException("Authentication  method not supported：" + request.getMethod());
        }
        String openId = obtainOpenId(request);
        String providerId = obtainProviderId(request);
        if (null == openId) {
            openId = "";
        }
        if (null == providerId) {
            providerId = "";
        }

        openId = openId.trim();
        providerId = providerId.trim();

        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId, providerId);

        setDetails(request,token);

        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 将请求的一些信息保存到验证请求里面去
     * @param request
     * @param token
     */
    private void setDetails(HttpServletRequest request, OpenIdAuthenticationToken token) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取providerId
     * @param request
     * @return
     */
    private String obtainProviderId(HttpServletRequest request) {
        return request.getParameter(providerIdParameter);
    }

    /**
     * 获取openId
     * @param request
     * @return
     */
    private String obtainOpenId(HttpServletRequest request) {
        return request.getParameter(openIdParameter);
    }
}
