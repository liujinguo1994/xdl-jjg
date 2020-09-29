package com.xdl.jjg.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author: ciyuan
 * @Date: 2019/5/19 19:51
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -2055127860539627042L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
