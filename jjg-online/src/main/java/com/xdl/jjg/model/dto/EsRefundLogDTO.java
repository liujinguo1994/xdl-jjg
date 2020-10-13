package com.xdl.jjg.model.dto;

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
public class EsRefundLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 退款sn
     */
	private String refundSn;
    /**
     * 日志记录时间
     */
	private Long createTime;
    /**
     * 日志详细
     */
	private String logdetail;
    /**
     * 操作者
     */
	private String operator;


	protected Serializable pkVal() {
		return this.id;
	}

}
