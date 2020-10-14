package com.jjg.member.model.enums;

public enum CustomCachePrefix {

    /**
     * 店铺自定义分类
     */
    CUSTOM_CAT;



    public String getPrefix() {
        return this.name() + "_";
    }


}
