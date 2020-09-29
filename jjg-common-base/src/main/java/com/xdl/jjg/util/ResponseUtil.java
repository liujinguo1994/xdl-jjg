package com.xdl.jjg.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应操作结果
 * <pre>
 *  {
 *      code： 错误码，
 *      msg：错误消息，
 *      data：  响应数据
 *  }
 * </pre>
 *
 * <p>
 * 错误码：
 * <ul>
 * <li> 0，成功；
 * <li> 4xx，前端错误，说明前端开发者需要重新了解后端接口使用规范：
 * <ul>
 * <li> 401，参数错误，即前端没有传递后端需要的参数；
 * <li> 402，参数值错误，即前端传递的参数值不符合后端接收范围。
 * </ul>
 * <li> 5xx，后端错误，除501外，说明后端开发者应该继续优化代码，尽量避免返回后端错误码：
 * <ul>
 * <li> 501，验证失败，即后端要求用户登录；
 * <li> 502，系统内部错误，即没有合适命名的后端内部错误；
 * <li> 503，业务不支持，即后端虽然定义了接口，但是还没有实现功能；
 * <li> 504，更新数据失效，即后端采用了乐观锁更新，而并发更新时存在数据更新失效；
 * <li> 505，更新数据失败，即后端数据库更新失败（正常情况应该更新成功）。
 * </ul>
 * </ul>
 */
public class ResponseUtil implements Serializable {


    public static Object ok() {
        return ok(null) ;
    }

    public static Object ok(Object data) {
        return ok("成功", data) ;
    }

    public static Object ok(String msg, Object data) {
        return set(0,msg,data);
    }

    public static Object fail() {
        return fail("错误");
    }

    public static Object fail(String msg){
        return fail(-1,msg);
    }

    public static Object fail(Integer code, String msg) {
        return set(code,msg,null);
    }

    public static Object set(Integer code, String msg,Object data) {
        Map<String, Object> obj = new HashMap();
        obj.put("code", code);
        obj.put("msg", msg);
        obj.put("data", data);
        return obj;
    }

    public static Object badArgument() {
        return fail(401, "参数不对");
    }

    public static Object badArgumentValue() {
        return fail(402, "参数值不对");
    }

    public static Object unlogin() {
        return fail(501, "请登录");
    }

    public static Object serious() {
        return fail(502, "系统内部错误");
    }

    public static Object unsupport() {
        return fail(503, "业务不支持");
    }

    public static Object updatedDateExpired() {
        return fail(504, "更新数据已经失效");
    }

    public static Object updatedDataFailed() {
        return fail(505, "更新数据失败");
    }
}

