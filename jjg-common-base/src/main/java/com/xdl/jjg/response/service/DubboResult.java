package com.xdl.jjg.response.service;


import lombok.Getter;


public class DubboResult<D> extends BaseResultSupport {

    @Getter
    protected D data;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static DubboResult success() {
        return newBuilder().success(true).data(null).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }

    public static <D> DubboResult success(D data) {
        return newBuilder().success(true).data(data).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }

    public static DubboResult fail(int code, String msg) {
        return newBuilder().success(false).data(null).code(code).msg(msg).build();
    }

    public static <D> DubboResult fail(D data, int code, String msg) {
        return newBuilder().success(false).data(data).code(code).msg(msg).build();
    }

    public static DubboResult fail(BaseErrorCode errorCode) {
        return newBuilder().success(false).data(null).code(errorCode.getErrorCode()).msg(errorCode.getErrorMsg()).build();
    }

    protected DubboResult(D data, boolean success, int status, String error) {
        super(success, status, error);
        this.data = data;
    }

    public static class Builder<D> extends BaseResultSupport.Builder {

        private D data;

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public DubboResult build() {
            return new DubboResult(data, success, code, msg);
        }

        public Builder data(D data) {
            this.data = data;
            return this;
        }

        @Override
        public Builder code(Integer code) {
            this.code = code;
            return this;
        }
        @Override
        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        @Override
        public Builder success(Boolean success) {
            this.success = success;
            return this;
        }
    }

}
