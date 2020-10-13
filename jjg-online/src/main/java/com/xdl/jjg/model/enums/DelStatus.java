package com.xdl.jjg.model.enums;

import java.util.Objects;

/**
 * @ClassName: DelStatus
 * @Description: 删除状态
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum DelStatus {

    /**
     * 未删除
     */
    NOT_DEL(0, "未删除"),

    /**
     * 已删除
     */
    IS_DEL(1, "已删除");

    private int value;
    private String desc;

    DelStatus(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public static String getDesc(Integer value) {
        for (DelStatus delStatus : DelStatus.values()) {
            if (Objects.equals(delStatus.value, value)) {
                return delStatus.desc;
            }
        }
        return null;
    }
}
