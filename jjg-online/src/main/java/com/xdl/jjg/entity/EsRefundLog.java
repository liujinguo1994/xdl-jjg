package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("es_refund_log")
public class EsRefundLog extends Model<EsRefundLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 退款sn
	 */
	@TableField("refund_sn")
	private String refundSn;
	/**
	 * 日志记录时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
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
