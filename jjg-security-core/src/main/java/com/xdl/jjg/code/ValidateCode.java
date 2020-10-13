package com.xdl.jjg.code;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 7:57
 */
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = -4743134886469523144L;
    /**
     * 随机数
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 该构造器用于设置过去时间的具体时间
     *
     * @param code
     * @param expireTime 验证码过期的具体时间 该验证码在expireTime过期
     */
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * 该构造器用于设置过期时间的秒数
     *
     * @param code
     * @param expireIn 过期时间的秒数   该验证码在expireIn秒后过期
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
