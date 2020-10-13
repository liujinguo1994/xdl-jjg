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
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_group_buy_quantity_log")
public class EsGroupBuyQuantityLog extends Model<EsGroupBuyQuantityLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 商品ID
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 团购售空数量
	 */
	@TableField("quantity")
	private Integer quantity;

	/**
	 * 操作时间
	 */
	@TableField("op_time")
	private Long opTime;

	/**
	 * 日志类型
	 */
	@TableField("log_type")
	private String logType;

	/**
	 * 操作原因
	 */
	@TableField("reason")
	private String reason;

	/**
	 * 团购id
	 */
	@TableField("gb_id")
	private Long gbId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
