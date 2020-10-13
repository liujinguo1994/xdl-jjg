package com.xdl.jjg.model.enums;
/**
 * <p>
 * 各评分类型
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum CommentSortScoreEnums {

    /**
     * 服务评分
     */
    SERVICE_SCORE(2,"服务评分"),
    /**
     * 商品评分
     */
     GOODS_SCORE(0,"商品评分"),
    /**
     * 物流评分
     */
    CARRAY_SCORE(1,"物流评分");

    private Integer key;

    private String name;

    CommentSortScoreEnums(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
