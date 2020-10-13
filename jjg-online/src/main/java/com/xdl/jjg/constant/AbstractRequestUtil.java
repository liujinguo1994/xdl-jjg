package com.xdl.jjg.constant;

import com.xdl.jjg.constants.ThreadContextHolder;
import com.xdl.jjg.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * httpRequest常用方法工具
 *
 * @author kingapex
 * 2010-2-12上午11:34:48
 */
public abstract class AbstractRequestUtil {
    private AbstractRequestUtil() {
    }

    /**
     * 将request中的参数转为Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> paramToMap(HttpServletRequest request) {

        Map<String, String> params = new HashMap<String, String>(16);
        Map rMap = request.getParameterMap();
        Iterator rIter = rMap.keySet().iterator();

        while (rIter.hasNext()) {
            Object key = rIter.next();
            String value = request.getParameter(key.toString());
            if (key == null || value == null) {
                continue;
            }
            params.put(key.toString(), value.toString());
        }

        return params;

    }


    public static String getRequestUrl(HttpServletRequest request) {
        String redirect = (String) request.getAttribute("redirect");
        if (redirect != null) {
            return redirect;
        }
        String pathInfo = (request).getPathInfo();
        String queryString = (request).getQueryString();

        String uri = (request).getServletPath();
        String ctx = request.getContextPath();
        ctx = "/".equals(ctx) ? "" : ctx;
        /*		uri = uri.startsWith("/")?uri.substring(1, uri.length()):uri;
		 */
        if (uri == null) {
            uri = (request).getRequestURI();
            uri = uri.substring((request).getContextPath().length());
        }

        return ctx + uri + ((pathInfo == null) ? "" : pathInfo)
                + ((queryString == null) ? "" : ("?" + queryString));
    }

    /**
     * 获取完整的url，包括域名端口等
     *
     * @return
     */
    public static String getWholeUrl(HttpServletRequest request) {
        String servername = request.getServerName();
        String path = request.getServletPath();
        int port = request.getServerPort();

        String portstr = "";
        if (port != 80) {
            portstr = ":" + port;
        }
        String contextPath = request.getContextPath();
        if ("/".equals(contextPath)) {
            contextPath = "";
        }


        String url = "http://" + servername + portstr + contextPath + "/" + path;

        return url;

    }


    /**
     * 根据 request获取完整的域名，如http://www.abc.com:8080
     * 如果端口为80则为:http://www.abc.com
     *
     * @return
     */
    public static String getDomain() {
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        if (request == null) {
            return "";
        }
        String port = "" + request.getServerPort();
        if ("80".equals(port)) {
            port = "";
        } else {
            port = ":" + port;
        }

        String contextPath = request.getContextPath();
        if ("/".equals(contextPath)) {
            contextPath = "";
        }

        String domain = request.getScheme() + "://" + request.getServerName() + port;
        domain += contextPath;
        return domain;
    }

    /**
     * 获取Integer 的值
     *
     * @param request
     * @param name
     * @return 如果没有返回null
     */
    public static Integer getIntegerValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        } else {
            return Integer.valueOf(value);
        }

    }


    public static Double getDoubleValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        } else {
            return Double.valueOf(value);
        }

    }


    /**
     * 获取int的值
     *
     * @param request
     * @param name
     * @return 如果没有返回0
     */
    public static int getIntValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtil.isEmpty(value)) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    public static String getRequestMethod(HttpServletRequest request) {
        String method = request.getParameter("_method");
        method = method == null ? "get" : method;
        method = method.toUpperCase();
        return method;
    }


    /**
     * 检测是不是手机访问
     *
     * @return true为是false为不是手机访问
     */
    public static boolean isMobile() {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        if (request == null) {
            return false;
        }
        String userAgentFrom = request.getHeader("user-agent");
        if (StringUtil.isEmpty(userAgentFrom)) {
            return false;
        }

        String userAgent = userAgentFrom.toLowerCase();

        if (userAgent.contains("android") || userAgent.contains("iphone")) {
            return true;
        }
        return false;

    }

}
