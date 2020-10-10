package com.xdl.jjg.response.web;

/**
 * 说明：该类原则上只能由web端调用（返回时需要结合properties文件加上相应的message信息）
 *
 * Web API的返回值定义，使用静态属性定义，具体描述信息使用properties文件映射
 *      code长度为标准6位长度，新老版本兼容使用逐步替换策略
 *      每个模块的code由该模块开发人员维护
 *
 */
public abstract class BaseApiStatus {
    public static final int SUCCESS = 0;
    public static final String FAIL_MESSAGE = "系统异常...";
    public static final int SYST_SYSTEM_ERROR = 10000;
    public static final int SYST_SERVICE_UNAVAILABLE = 10001;
    public static final int SYST_SQL_ERROR = 10002;
}