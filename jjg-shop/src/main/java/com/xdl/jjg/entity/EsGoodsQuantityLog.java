package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-16 09:52:06
 */
@Data
@Accessors(chain = true)
@TableName("es_goods_quantity_log")
public class EsGoodsQuantityLog extends Model<EsGoodsQuantityLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 虚拟库存
	 */
	@TableField("xn_quantity")
	private Integer xnQuantity;

	/**
	 * 实际库存
	 */
	@TableField("quantity")
	private Integer quantity;

	/**
	 * 商品数量
	 */
	@TableField("goods_sum")
	private Integer goodsSum;

	/**
	 * 操作类型0 扣减库存 1增加库存
	 */
	@TableField("type")
	private Integer type;

	@TableField("goods_id")
	private Long goodsId;

	@TableField("sku_id")
	private Long skuId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
