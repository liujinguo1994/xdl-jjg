package com.jjg.member.model.enums;
/**
 * <p>
 * 评价类型枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum CommentSortEnums {

    /**
     * 服务
     */
    SERVICE("2"),
    /**
     * 商品描述
     */
     DESCRIPTION("0"),
    /**
     * 物流
     */
    DELIVERY("1");

    private String key;
    CommentSortEnums(String key){
    this.key = key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
