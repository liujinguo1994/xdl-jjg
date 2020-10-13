package com.xdl.jjg.model.enums;

/**
 * 进度枚举
 */
public enum ProgressEnum {


    DOING("进行中"), SUCCESS("成功"), EXCEPTION("异常");

    String status;

    ProgressEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}