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
@TableName("es_seckill_apply")
public class EsSeckillApply extends Model<EsSeckillApply> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 活动id
	 */
	@TableField("seckill_id")
	private Long seckillId;
	/**
	 * 时刻
	 */
	@TableField("time_line")
	private Integer timeLine;
	/**
	 * 活动开始日期
	 */
	@TableField(value = "start_day", fill = FieldFill.INSERT)
	private Long startDay;
	/**
	 * 活动结束日期
	 */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Long endTime;
	/**
	 * 商品ID
	 */
	@TableField("goods_id")
	private Long goodsId;
	/**
	 * 商品名称
	 */
	@TableField("goods_name")
	private String goodsName;
	/**
	 * 商家ID
	 */
	@TableField("shop_id")
	private Long shopId;
	/**
	 * 商家名称
	 */
	@TableField("shop_name")
	private String shopName;
	/**
	 * 价格
	 */
	private Double money;
	/**
	 * 售空数量
	 */
	@TableField("sold_quantity")
	private Integer soldQuantity;
	/**
	 * 申请状态
	 */
	private Integer state;
	/**
	 * 驳回原因
	 */
	@TableField("fail_reason")
	private String failReason;
	/**
	 * 商品原始价格
	 */
	@TableField("original_price")
	private Double originalPrice;
	/**
	 * 已售数量
	 */
	@TableField("sales_num")
	private Integer salesNum;
	/**
	 * 图片
	 */
	@TableField("image")
	private String image;

	/**
	 * 商品SKU ID
	 */
	@TableField("sku_id")
	private Long skuId;

	/**
	 * SKU规格
	 */
	@TableField("specs")
	private String specs;

	/**
	 * SKU编号
	 */
	@TableField("sku_sn")
	private String skuSn;

	/**
	 * 定时任务Id
	 */
	@TableField("job_id")
	private Long jobId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
