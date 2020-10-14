package com.jjg.member.model.enums;

/**
 * <p>
 * 会员活跃度枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum MemberTypeEnums {

    /**
     * 新增会员
     */
    ADD_MEMBER("新增会员", 0),
    /**
     * 活跃会员
     */
    ACTIVE_MEMBER("活跃会员", 1),
    /**
     * 休眠会员
     */
    SLEEP_MEMBER("休眠会员", 2),
    /**
     * 普通会员
     */
    COMMON_MEMBER("普通会员", 3),
    /**
     * 潜在会员
     */
    LATENT_MEMBER("潜在会员", 4),
    /**
     * 全部会员
     */
    WHOLE_MEMBER("全部会员", 5);

    private String name;
    private int index;

    private MemberTypeEnums(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (MemberTypeEnums c : MemberTypeEnums.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
