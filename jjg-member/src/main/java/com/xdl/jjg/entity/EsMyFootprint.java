package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员足迹
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:43
 */
@Data
@TableName("es_my_footprint")
public class EsMyFootprint extends Model<EsMyFootprint> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 商品ID
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 访问时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 商品价格
	 */
	@TableField("money")
	private Double money;

	/**
	 * 商品图片
	 */
	@TableField("img")
	private String img;

	/**
	 * 会员ID
	 */
	@TableField("member_id")
	private Long memberId;

}
