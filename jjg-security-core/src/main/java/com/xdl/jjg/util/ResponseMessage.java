package com.xdl.jjg.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ciyuan
 * @date 2019/1/8
 * TODO: 2019/1/8  响应类
 */
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = -3277204634847412967L;

    public static Object ok() {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", 0);
        obj.put("msg", "成功");
        return obj;
    }

    public static Object ok(Object data) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", 0);
        obj.put("msg", "成功");
        obj.put("data", data);
        return obj;
    }

    public static Object ok(String msg, Object data) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", 0);
        obj.put("msg", msg);
        obj.put("data", data);
        return obj;
    }

    public static Object fail(String msg, Object data) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", -1);
        obj.put("msg", msg);
        obj.put("data", data);
        return obj;
    }

    public static Object fail(String msg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", -1);
        obj.put("msg", msg);
        return obj;
    }

    public static Object fail() {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", -1);
        obj.put("msg", "错误");
        return obj;
    }

    public static Object fail(int code, Object msg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", code);
        obj.put("msg", msg);
        return obj;
    }



    public static Object badArgument(){
        return fail(401, "参数不对");
    }


    public static Object badArgumentValue(){
        return fail(402, "参数值不对");
    }

    public static Object unlogin(){
        return fail(501, "请登录");
    }

    public static Object serious(){
        return fail(502, "系统内部错误");
    }

    public static Object unsupport(){
        return fail(503, "业务不支持");
    }
}

