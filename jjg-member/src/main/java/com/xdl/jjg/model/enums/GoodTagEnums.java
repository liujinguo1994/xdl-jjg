package com.xdl.jjg.model.enums;

import java.util.Objects;

/**
 * @ClassName: GoodTagEnums
 * @Description: 店铺中商品分类(热销、上新)
 * @Author: xl  981087977@qq.com
 * @Version: 1.0
 */
public enum GoodTagEnums {

    /**
     * 热销
     */
    HOT(1L, "热销"),

    /**
     * 新品上架
     */
    NEW(2L, "新品上架");

    private Long value;
    private String desc;

    GoodTagEnums(final Long value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Long getValue() {
        return value;
    }

    public static String getDesc(Integer value) {
        for (GoodTagEnums goodTagEnums : GoodTagEnums.values()) {
            if (Objects.equals(goodTagEnums.value, value)) {
                return goodTagEnums.desc;
            }
        }
        return null;
    }
}
