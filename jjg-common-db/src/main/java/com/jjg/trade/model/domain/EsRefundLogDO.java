package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
public class EsRefundLogDO extends Model<EsRefundLogDO> {

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


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
