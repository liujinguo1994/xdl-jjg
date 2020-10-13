package com.xdl.jjg.response.exception;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/6/11 14:26
 */
public class RedissonLockException extends RuntimeException {

    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public RedissonLockException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public RedissonLockException(int exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

}
