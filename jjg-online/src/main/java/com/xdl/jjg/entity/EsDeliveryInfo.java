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
 * 自提信息表
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 14:34:33
 */
@Data
@Accessors(chain = true)
@TableName("es_delivery_info")
public class EsDeliveryInfo extends Model<EsDeliveryInfo> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 订单明细编号
	 */
	@TableField("order_detail_sn")
	private Long orderDetailSn;

	/**
	 * 自提内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 自提时间
	 */
	@TableField("delivery_time")
	private String deliveryTime;

	/**
	 * 是否完成
	 */
	@TableField("is_ok")
	private Integer isOk;

	/**
	 * 商品SKU_ID
	 */
	@TableField("sku_id")
	private Long skuId;
	/**
	 * 商品ID
	 */
	@TableField("goods_id")
	private Long goodsId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
