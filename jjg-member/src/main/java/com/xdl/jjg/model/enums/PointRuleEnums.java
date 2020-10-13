package com.xdl.jjg.model.enums;

/**
 * <p>
 * 积分类型枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum PointRuleEnums {
    /**
     * 评论获得积分
     */
    COMMENT_POINT("评论送积分"),

    /**
     * 订单完成获得积分
     */
    ORDER_POINT("购物送积分"),

    /**
     * 参与活动获得积分
     */
    MOVE_POINT("活动送积分"),

    /**
     * 登陆获得积分
     */
    LAND_POINT("登陆送积分");
    private String description;

    PointRuleEnums(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
