package com.xdl.jjg.model.enums;
/**
 * <p>
 * 会员活跃度枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum MemberActiveSortEnums {

    /**
     * 新增会员
     */
    ADD_MEMBER(0),
    /**
     * 活跃会员
     */
    ACTIVE_MEMBER(1),
    /**
     * 休眠会员
     */
    SLEEP_MEMBER(2),
    /**
     * 普通会员
     */
    COMMON_MEMBER(3),
    /**
     * 潜在会员
     */
    LATENT_MEMBER(4),
    /**
     * 休眠会员
     */
    WHOLE_MEMBER(5);

    private Integer key;

    MemberActiveSortEnums(Integer key){
        this.key=key;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
