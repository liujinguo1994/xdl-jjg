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
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_full_discount_gift")
public class EsFullDiscountGift extends Model<EsFullDiscountGift> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 赠品名称
	 */
	@TableField("gift_name")
	private String giftName;

	/**
	 * 赠品金额
	 */
	@TableField("gift_money")
	private Double giftMoney;

	/**
	 * 赠品图片
	 */
	@TableField("gift_img")
	private String giftImg;

	/**
	 * 赠品类型
	 */
	@TableField("gift_type")
	private Integer giftType;

	/**
	 * 可用库存
	 */
	@TableField("enable_store")
	private Integer enableStore;

	/**
	 * 档案SKUid
	 */
	@TableField("skui_id")
	private Long skuiId;

	/**
	 * 商品id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 店铺id
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 实际库存
	 */
	@TableField("quantity")
	private Integer quantity;
	/**
	 * 可用库存=真实库存+虚拟库存-冻结库存
	 */
	@TableField("enable_quantity")
	private Integer enableQuantity;
	/**
	 * 虚拟库存
	 */
	@TableField("xn_quantity")
	private Integer xnQuantity;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
