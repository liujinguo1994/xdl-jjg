package com.jjg.member.model.enums;
/**
 * <p>
 * 评价类型枚举类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-19
 */
public enum MemberCacheEnums {

    /**
     * 会员
     */
    memeber;


    public String getPrefix() {
        return this.name() + "_";
    }


}
