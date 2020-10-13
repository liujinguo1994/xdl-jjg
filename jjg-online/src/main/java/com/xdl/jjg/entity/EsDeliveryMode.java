package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 配送方式
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_delivery_mode")
public class EsDeliveryMode extends Model<EsDeliveryMode> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 配送方式ID
	 */
	@TableField("delivery_mode_id")
	private Long deliveryModeId;

	/**
	 * 配送方式名称(自提，配送)
	 */
	@TableField("delivery_mode_name")
	private String deliveryModeName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
