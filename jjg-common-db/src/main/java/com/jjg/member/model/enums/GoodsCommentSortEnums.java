package com.jjg.member.model.enums;

/**
 * <p>
 * 好评，中评，差评
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum GoodsCommentSortEnums {
    /**
     * 好评
     */
    GOOD_COMMENT(0,"好评"),
    /**
     * 中评
     */
    COMMONT_COMMENT(1,"中评"),
    /**
     * 差评
     */
    BAD_COMMENT(2,"差评"),
    /**
     * 带图评论
     */
    IMG_COMMENT(3,"带图评论"),
    /**
     * 全部
     */
    ALL_COMMENT(4,"全部");

    private Integer key;

    private String name;

    GoodsCommentSortEnums(Integer key, String name) {
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
