package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsOrderLogDTO implements Serializable{

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
     * 操作者id
     */
	private Long opId;
    /**
     * 操作者名称
     */
	private String opName;
    /**
     * 日志信息
     */
	private String message;
    /**
     * 操作时间
     */
	private Long opTime;
    /**
     * 订单金额
     */
	private Double money;


	protected Serializable pkVal() {
		return this.id;
	}

}
