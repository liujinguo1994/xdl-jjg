package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员余额明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberDepositDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 会员ID
     */
	private Long memberId;
    /**
     *类型
     */
    private String type;

    /**
     * 金额
     */
	private Double money;

    /**
     * 操作时间
     */
	private Long createTime;
    /**
     * 是否最近3个月内标识 2：大于三个月 1：近三个月
     */
    private String threeMonth;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 交易编号
     */
    private String tradeSn;
    /**
     * 当前账户余额
     */
    private Double memberBalance;


}
