package com.xdl.jjg.model.enums;

public enum ActiveTypeEnum {
    ADD_ACTIVE("新增活跃信息"),
    DELET_ACTIVE("删除活跃信息");

    private String description;

    private ActiveTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
