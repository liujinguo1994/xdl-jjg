package com.xdl.jjg.constants;

/**
 * 基础 error code
 */
public abstract class BaseErrorCode {

    public static int SUCCESS_CODE = 0;

    private int errorCode;

    private String errorMsg;

    public BaseErrorCode(int errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg  = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
