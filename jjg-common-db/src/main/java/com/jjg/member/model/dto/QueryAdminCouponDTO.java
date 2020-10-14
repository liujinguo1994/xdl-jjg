package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lns 1220316142@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class QueryAdminCouponDTO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;

    /**
     * 关键词
     */
    private String keyword;
    /**
     * 当前时间
     */
    private Long currentTime;

}
