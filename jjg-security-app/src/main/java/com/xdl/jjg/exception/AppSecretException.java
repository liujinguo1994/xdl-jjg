package com.xdl.jjg.exception;

/**
 * @Author: ciyuan
 * @Date: 2019/6/29 11:18
 * app登录验证异常
 */
public class AppSecretException extends RuntimeException {

    public AppSecretException(String msg) {
        super(msg);

    }
}
