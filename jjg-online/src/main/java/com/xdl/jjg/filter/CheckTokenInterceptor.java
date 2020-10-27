package com.xdl.jjg.filter;


import com.jjg.member.model.domain.EsMemberTokenDO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaoLin
 * @ClassName CheckTokenInterceptor
 * @Description 校验token
 * @create 2020/1/3 13:46
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CheckTokenInterceptor.class);

    @Autowired
    private IEsMemberTokenService memberTokenService;

    /**
     * 在执行controller方法之前进行请求参数处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        try{
            String token = getRequestParam(request,"token");
            String uid = getRequestParam(request,"uid");
            if(StringUtils.isBlank(token) && StringUtils.isBlank(uid)){
                return true;
            }
            long memberId = Long.parseLong(uid);
            DubboResult<EsMemberTokenDO> memberTokenResult = memberTokenService.getMemberToken(memberId);
            if(memberTokenResult.isSuccess() && memberTokenResult.getData() != null){
                EsMemberTokenDO memberTokenDO =  memberTokenResult.getData();
                if(StringUtils.equals(memberTokenDO.getToken(), token)){
                    return true;
                }
                response.setHeader("Mandatory-Offline", "true");
                response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                String json = JsonUtil.objectToJson(ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR, "invalid token",response));
                response.getWriter().print(json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //清除缓存
        ShiroKit.getSubject().logout();
        return false;
    }


    /**
     * 获取请求的param
     */
    private String getRequestParam(HttpServletRequest httpRequest, String param){
        //从header中获取param
        String value = httpRequest.getHeader(param);
        //如果header中不存在param，则从参数中获取param
        if(StringUtils.isBlank(value)){
            value = httpRequest.getParameter(value);
        }
        logger.info("获取请求参数："+value);
        return value;
    }
}
