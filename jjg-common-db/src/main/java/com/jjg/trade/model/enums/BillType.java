package com.jjg.member.model.enums;

import java.util.Objects;

/**
 * @ClassName: BillType
 * @Description: 结算类型
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum BillType {

    /**
     * 卖家
     */
    SHOP(0, "卖家"),

    /**
     * 签约公司
     */
    COMPANY(1, "签约公司"),

    /**
     * 签约公司
     */
    SUPPLIER(2, "供应商");

    private int value;
    private String desc;

    BillType(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public static String getDesc(Integer value) {
        for (BillType billType : BillType.values()) {
            if (Objects.equals(billType.value, value)) {
                return billType.desc;
            }
        }
        return null;
    }
}
