package com.xdl.jjg.response.exception;


import com.xdl.jjg.response.service.ErrorCodeEnum;

public class ArgumentException extends RuntimeException {

    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public ArgumentException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ArgumentException(ErrorCodeEnum errorCodeEnum){
        super(errorCodeEnum.getErrorMassage());
        this.exceptionCode = errorCodeEnum.getErrorCode();
    }

    public ArgumentException(int exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
