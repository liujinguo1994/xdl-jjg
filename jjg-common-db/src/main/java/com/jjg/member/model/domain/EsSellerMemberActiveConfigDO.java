package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员活跃度配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:03
 */
@Data
public class EsSellerMemberActiveConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 天数
     */
	private Integer days;

    /**
     * 会员类型 0新增，1活跃，2休眠，3普通
     */
	private Integer memberType;
    /**
     * 订单数
     */
    private Integer orders;
    /**
     * 未访问天数
     */
	private Integer visitDays;

}
