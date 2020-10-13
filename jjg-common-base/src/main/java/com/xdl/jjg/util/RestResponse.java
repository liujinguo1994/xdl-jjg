/**
 * Copyright (c) 2015, China Construction Bank Co., Ltd. All rights reserved.
 * 南天软件版权所有.
 * <p>
 * 审核人：
 */
package com.xdl.jjg.util;


import java.io.Serializable;

/**
 * 公用Rest响应结果封装类
 *
 * <p>
 *
 * @author nantian.co
 * @version 1.0 2015年6月24日 
 * @see
 */
public class RestResponse implements Serializable {

    /**
     * 成功
     */
    public static final String SUCCESS = "200";

    /**
     * 参数异常
     */
    public static final String ARGUMENT_ILLEGAL = "301";

    /**
     * 内部异常
     */
    public static final String FAIL = "500";


    /**
     * 执行结果状态
     */
    private String status = SUCCESS;
    /**
     * 成功或者失败的提示信息
     */
    private String msg = "";

    /**
     * 返回结果对象
     */
    private Object data = "";


    public static RestResponse getInstance() {
        return new RestResponse();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMsg() {
        return msg == null ? "" : msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data == null ? "" : data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "RestResponse [status=" + status + ", msg=" + msg + ", data="
                + data + "]";
    }


}
