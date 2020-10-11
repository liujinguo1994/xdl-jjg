package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 售后申请原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
@Data
@TableName("es_return_reason")
public class EsReturnReason extends Model<EsReturnReason> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 原因
	 */
	@TableField("reason")
	private String reason;

	/**
	 * 售后类型
	 */
	@TableField("refund_type")
	private String refundType;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
