package com.xdl.jjg.response.web;

import com.xdl.jjg.response.exception.UnCheckedWebException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

public class ApiResponse<D> extends BaseResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponse.class);

    /**
     * 获取request response对象
     */
    private static ServletRequestAttributes SERVLET_REQUEST_ATTRIBUTES  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    @Getter
    protected D data;

    public static Builder newBuilder() {
        return new Builder();
    }

    protected ApiResponse(D data, int status, String error) {
        super(status, error);
        this.data = data;
    }

    public static class Builder<D> extends BaseResponse.Builder {

        private D data;

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public ApiResponse build() {
            return new ApiResponse(data, status, error);
        }

        public Builder data(D data) {
            this.data = data;
            return this;
        }

        @Override
        public Builder status(Integer status) {
            this.status = status;
            return this;
        }
        @Override
        public Builder error(String error) {
            this.error = error;
            return this;
        }
    }

    public static ApiResponse success() {
        return newBuilder().data(null).status(BaseApiStatus.SUCCESS).error("").build();
    }

    public static <D> ApiResponse success(D data) {
        return newBuilder().data(data).status(BaseApiStatus.SUCCESS).error("").build();
    }


    public static ApiResponse fail(int code, String msg) {
        UnCheckedWebException unCheckedWebException = new UnCheckedWebException(code,msg);
        return fail(unCheckedWebException);
    }

    public static ApiResponse fail(int code, String msg, HttpServletResponse response) {
        Builder builder = newBuilder();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return builder.data(null).status(code).error(msg).build();
    }


    public static <D> ApiResponse fail(D data, int code, String msg) {
        UnCheckedWebException unCheckedWebException = new UnCheckedWebException(code,msg);
        return fail(data,unCheckedWebException);
    }

    /**
     * web端异常返回结果
     *
     * @param exception
     * @return
     */
    public static ApiResponse fail(UnCheckedWebException exception) {
        if(SERVLET_REQUEST_ATTRIBUTES == null){
            SERVLET_REQUEST_ATTRIBUTES  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        }
        HttpServletResponse response = SERVLET_REQUEST_ATTRIBUTES.getResponse();
        Builder builder = newBuilder();
        if (null != exception) {
            builder.status(exception.getExceptionCode());
            builder.error(exception.getMessage());
            LOGGER.error(exception.getMessage(),exception);
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return builder.data(null).build();
    }
    /**
     * web端异常返回结果
     *
     * @param exception
     * @return
     */
    public static <D> ApiResponse fail(D Data, UnCheckedWebException exception) {
        if(SERVLET_REQUEST_ATTRIBUTES == null){
            SERVLET_REQUEST_ATTRIBUTES  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        }
        HttpServletResponse response = SERVLET_REQUEST_ATTRIBUTES.getResponse();
        Builder builder = newBuilder();
        if (null != exception) {
            builder.status(exception.getExceptionCode());
            builder.error(exception.getMessage());
            LOGGER.error(exception.getMessage(),exception);
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return builder.data(Data).status(exception.getExceptionCode()).error(exception.getMessage()).build();
    }
}
