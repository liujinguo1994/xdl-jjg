package com.jjg.member.model.domain;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员余额明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberDepositDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 操作类型(充值，消费，退款)
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
