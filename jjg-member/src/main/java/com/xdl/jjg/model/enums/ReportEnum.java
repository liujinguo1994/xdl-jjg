package com.xdl.jjg.model.enums;

/**
 * <p>
 * 举报枚举类型
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25
 */
public enum ReportEnum {
    /**
     * 待处理
     */
    WAIT("待处理"),
    /**
     * 处理中
     */
    APPLYING("处理中"),
    /**
     * 已处理
     */
    APPLYED("已处理");
    private String description;

    ReportEnum(String description) {
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
