package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 配置投诉原因
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:54
 */
@Data
@TableName("es_complaint_reason_config")
public class EsComplaintReasonConfig extends Model<EsComplaintReasonConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 投诉原因
	 */
	@TableField("complaint_reason")
	private String complaintReason;

	/**
	 * 投诉类型
	 */
	@TableField("complain_type_id")
	private Long complainTypeId;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

}
