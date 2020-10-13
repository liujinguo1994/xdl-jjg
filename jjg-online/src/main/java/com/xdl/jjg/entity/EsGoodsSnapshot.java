package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商品快照
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_goods_snapshot")
public class EsGoodsSnapshot extends Model<EsGoodsSnapshot> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 商品id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 商品名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 商品编号
	 */
	@TableField("goods_sn")
	private String goodsSn;

	/**
	 * 品牌名称
	 */
	@TableField("brand_name")
	private String brandName;

	/**
	 * 分类名称
	 */
	@TableField("category_name")
	private String categoryName;

	/**
	 * 商品类型
	 */
	@TableField("goods_type")
	private String goodsType;

	/**
	 * 重量
	 */
	@TableField("weight")
	private Double weight;

	/**
	 * 商品详情
	 */
	@TableField("intro")
	private String intro;

	/**
	 * 商品价格
	 */
	@TableField("money")
	private Double money;

	/**
	 * 商品成本价
	 */
	@TableField("cost")
	private Double cost;

	/**
	 * 商品市场价
	 */
	@TableField("mktmoney")
	private Double mktmoney;

	/**
	 * 参数json
	 */
	@TableField("params_json")
	private String paramsJson;

	/**
	 * 图片json
	 */
	@TableField("img_json")
	private String imgJson;

	/**
	 * 快照时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 所属卖家
	 */
	@TableField("shop_id")
	private Long shopId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
