package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-07-02
 */
@Data
@Accessors(chain = true)
@TableName("es_delivery_date")
public class EsDeliveryDate extends Model<EsDeliveryDate> {

	private static final long serialVersionUID = 1L;

	@TableField("id")
	private Long id;

	/**
	 * 自提日期ID
	 */
	@TableField("date_id")
	private Long dateId;

	/**
	 * 自提点ID
	 */
	@TableField("delivery_id")
	private Long deliveryId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
