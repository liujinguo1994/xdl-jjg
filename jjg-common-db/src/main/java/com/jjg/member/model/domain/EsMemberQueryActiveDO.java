package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 查询会员列表详细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberQueryActiveDO implements Serializable {

    /**
     * 新增会员天数
     */
    private Integer addDays;
    /**
     * 活跃会员天数
     */
    private Integer activeDays;
    /**
     * 会员下单数
     */
    private Integer orders;
    /**
     * 未下单天数
     */
    private Integer noOrders;
    /**
     * 为登陆天数
     */
    private Integer noVistor;
}
