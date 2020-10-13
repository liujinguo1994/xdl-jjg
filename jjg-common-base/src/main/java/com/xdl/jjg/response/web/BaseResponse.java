package com.xdl.jjg.response.web;

import com.xdl.jjg.response.service.BaseDO;
import lombok.Getter;

/**
 * Package: com.app.api.base
 * User: min_xu
 * Date: 16/8/19 下午3:01
 * 说明：
 */
public abstract class BaseResponse extends BaseDO {

    @Getter
    protected int res_code = BaseApiStatus.SUCCESS;

    @Getter
    protected String res_msg = "";

    protected BaseResponse(int res_code, String error) {
        this.res_code = res_code;
        this.res_msg = error;
    }

    public abstract static class Builder<R extends BaseResponse, B extends Builder<R, B>> {

        private B theBuilder;

        protected Integer res_code;
        protected String error;

        public Builder() {
            theBuilder = getThis();
        }

        protected abstract B getThis();

        public B res_code(Integer res_code) {
            this.res_code = res_code;
            return theBuilder;
        }

        public B error(String error) {
            this.error = error;
            return theBuilder;
        }

        public abstract R build();

    }


}
