package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 卖家查询会员信息实体类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSellerQueryMemberDO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 最近浏览时间
     */
    private Long recentlyViewTime;
    /**
     * 最近下单时间
     */
    private Long recentlyOrderTime;
    /**
     * 店铺会员类别
     */
    private Integer memberType;

}
