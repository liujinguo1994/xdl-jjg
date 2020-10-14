package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lns 1220316142@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class QuerySellerCouponDTO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;

    /**
     * 0未使用，1已使用
     */
    private Long state;
    /**
     * 领取时间
     */
    private Long createStartTime;
    /**
     * 领取时间
     */
    private Long createEndTime;
    /**
     * 主键
     */
    private Long couponId;

    private Integer mobile;

}
