package com.xdl.jjg.response.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * 分页返回结果集
 * @param <D>
 */
public class DubboPageResult<D> extends DubboResult {

    private DubboPageResult(Object data, boolean success , int code, String msg) {
        super(data, success, code, msg);
    }

    @Override
    public ListData<D> getData() {
        return (ListData<D>) super.getData();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static <D> DubboPageResult success(Long total, List<D> list) {
        return newBuilder().success(true).data(new ListData(total,list)).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }
    public static <D> DubboPageResult success(List<D> list) {
        list = null==list? new ArrayList<D>():list;
        return newBuilder().success(true).data(new ListData((long)list.size(),list)).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }

    public static DubboPageResult fail(int code, String msg) {
        return newBuilder().success(false).data(null).code(code).msg(msg).build();
    }

    public static <D> DubboPageResult fail(Long total, List<D> list, int code, String msg) {
        return newBuilder().success(false).data(new ListData(total,list)).code(code).msg(msg).build();
    }

    public static DubboPageResult fail(BaseErrorCode errorCode) {
        return newBuilder().success(false).data(null).code(errorCode.getErrorCode()).msg(errorCode.getErrorMsg()).build();
    }

    public static class Builder<D> extends DubboResult.Builder {

        private ListData<D> data;

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder data(ListData data) {
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

        @Override
        public DubboPageResult build() {
            return new DubboPageResult(data, success ,code, msg);
        }

    }


    public static class ListData<D> extends BaseDO {

        @Getter
        private Long total;

        @Getter
        private List<D> list;

        public ListData(Long total, List<D> list) {
            this.total = total;
            this.list = list;
        }
    }
}
