package com.xdl.jjg.response.service;

import lombok.Data;

/**
 * 基础 error code
 */
@Data
public abstract class BaseErrorCode {

    public static int SUCCESS_CODE = 0;

    private int errorCode;

    private String errorMsg;

    public BaseErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
