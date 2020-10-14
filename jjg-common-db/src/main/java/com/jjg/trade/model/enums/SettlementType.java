package com.jjg.member.model.enums;

import java.util.Objects;

/**
 * @ClassName: SettlementType
 * @Description: 结算类型枚举类
 * @Author: bamboo  asp.bamboo@gmail.com
 * @Date: 2019/8/20 13:46
 * @Version: 1.0
 */
public enum SettlementType {

    /**
     * 店铺
     */
    SHOP(1, "shopSettlementImpl"),

    /**
     * 供应商
     */
    VENDOR(2, "vendorSettlementImpl"),

    /**
     * 签约公司
     */
    COMPANY(3, "companySettlementImpl");

    /**
     * 结算类型
     */
    private int value;

    /**
     * 结算工具类Bean名称
     */
    private String name;

    SettlementType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public static String getName(Integer value) {
        for (SettlementType settlementType : SettlementType.values()) {
            if (Objects.equals(settlementType.value, value)) {
                return settlementType.name;
            }
        }
        return null;
    }
}
