package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 支付记录日志表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsPayLogDO extends Model<EsPayLogDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 支付类型(支付宝、微信、银联)
     */
	private String payType;
    /**
     * 付款时间
     */
	private Long payTime;
    /**
     * 付款金额
     */
	private Double payMoney;
    /**
     * 付款会员名
     */
	private String payMemberName;
    /**
     * 付款状态
     */
	private String payState;
    /**
     * 收款单流水号
     */
	private String payLogSn;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
