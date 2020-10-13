package com.xdl.jjg.model.enums;/**
 * @author wangaf
 * @date 2019/11/29 16:42
 **/

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/29
 @Version V1.0
 **/
public enum CouponTypeEnum {

    SINGLE_PRODUCT("单品劵"),

    CURRENCY("通用劵"),

    CATEGORY("品类劵"),

    FREIGHT("运费劵");
    private String description;

    CouponTypeEnum(String description) {
        this.description = description;
    }


    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
