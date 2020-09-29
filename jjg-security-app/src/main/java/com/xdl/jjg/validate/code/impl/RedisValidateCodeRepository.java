package com.xdl.jjg.validate.code.impl;

import com.xdl.jjg.code.ValidateCode;
import com.xdl.jjg.code.ValidateCodeException;
import com.xdl.jjg.code.ValidateCodeRepository;
import com.xdl.jjg.code.ValidateCodeType;
import javafx.animation.SequentialTransition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ciyuan
 * @Date: 2019/6/23 19:22
 * APP端基于Redis操作验证码的仓储实现类
 * 这里要兼容browser模式下的获取session的方法
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * redis操作工具
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        redisTemplate.opsForValue().set(buildKey(request,validateCodeType),code,30, TimeUnit.MINUTES);
    }

    /**
     * 生成存储验证码的key值
     * @param request
     * @param validateCodeType
     * @return
     */
    private Object buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        //从请求中获取设备编号
        String deviceId = request.getHeader("deviceId");
        logger.info("获取的deviceId为{}", deviceId);
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        //根据所获取到的信息生成存储验证码的key值
        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        if (null == value) {
            return null;
        }
        return (ValidateCode) value;
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request, validateCodeType));
    }
}
