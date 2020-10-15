package com.jjg.trade.model.enums;

import java.util.Objects;

/**
 * @ClassName: SeckillGoodsApplyStatusEnum
 * @Description: 限时购状态枚举
 * @Author: libw  981087977@qq.com
 * @Date: 6/18/2019 11:30
 * @Version: 1.0
 */
public enum SeckillGoodsApplyStatusEnum {

    /** 申请中 */
    APPLY(0, "申请中"),

    /** 已通过 */
    PASS(1, "已通过"),

    /** 已驳回 */
    FAIL(2, "已驳回");

    private int value;
    private String desc;

    SeckillGoodsApplyStatusEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public static String getDesc(Integer value) {
        for (SeckillGoodsApplyStatusEnum seckillGoodsApplyStatusEnum : SeckillGoodsApplyStatusEnum.values()) {
            if (Objects.equals(seckillGoodsApplyStatusEnum.value, value)) {
                return seckillGoodsApplyStatusEnum.desc;
            }
        }
        return null;
    }
}
