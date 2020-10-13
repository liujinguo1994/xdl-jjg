package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 自提日期
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_self_date")
public class EsSelfDate extends Model<EsSelfDate> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 自提日期
	 */
	@TableField("self_date")
	private Long selfDate;
//	/**
//	 * 自提点ID
//	 */
//	@TableField("delivery_id")
//	private Long deliveryId;
	/**
	 * 有效状态
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 是否选中
	 */
	@TableField(exist = false)
	private Boolean selected;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
