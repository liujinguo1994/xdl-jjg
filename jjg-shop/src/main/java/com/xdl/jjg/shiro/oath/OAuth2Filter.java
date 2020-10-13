package com.xdl.jjg.shiro.oath;


import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * oauth2过滤器
 */
public class OAuth2Filter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestParam((HttpServletRequest) request, "token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //获取请求uid
        String uid = getRequestParam((HttpServletRequest) request, "uid");
        if (StringUtils.isBlank(uid)) {
            return null;
        }
        return new OAuth2Token(token, uid);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestParam((HttpServletRequest) request, "token");
        //获取请求uid
        String uid = getRequestParam((HttpServletRequest) request, "uid");
        if (StringUtils.isBlank(token) || StringUtils.isBlank(uid)) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
            String json = JsonUtil.objectToJson(ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR, "invalid token", httpResponse));
            httpResponse.getWriter().print(json);
            return false;
        }
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            String json = JsonUtil.objectToJson(ApiResponse.fail(888888, throwable.getMessage(), httpResponse));
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {

        }

        return false;
    }

    /**
     * 获取请求的param
     */
    private String getRequestParam(HttpServletRequest httpRequest, String param) {
        //从header中获取param
        String value = httpRequest.getHeader(param);
        //如果header中不存在param，则从参数中获取param
        if (StringUtils.isBlank(value)) {
            value = httpRequest.getParameter(value);
        }
        return value;
    }
}
