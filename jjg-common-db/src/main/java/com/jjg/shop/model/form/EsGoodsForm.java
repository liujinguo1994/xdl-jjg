package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@Api
public class EsGoodsForm implements Serializable {

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "商品主键ID",example = "1")
	@NotNull(message = "商品ID不能为空")
	private Long id;
	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	@NotNull(message = "商品名称不能为空")
	private String goodsName;
	/**
	 * 商品编号
	 */
	@ApiModelProperty(value = "商品编号")
	@NotNull(message = "商品编号不能为空")
	private String goodsSn;
	/**
	 * 品牌id
	 */
	@ApiModelProperty(value = "品牌ID")
	private Long brandId;
	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类id")
	@NotNull(message = "分类id不能为空")
	@Min(value = 0, message = "分类值不正确")
	private Long categoryId;
	/**
	 * 商品类型normal普通point积分
	 */
	@ApiModelProperty(value = "商品类型normal普通point积分")
	private String goodsType;
	/**
	 * 重量
	 */
	@ApiModelProperty(value = "重量")
	private Double weight;

	/**
	 * 详情
	 */
	@ApiModelProperty(value = "商品详情")
	private String intro;
	/**
	 * 成本价格
	 */
	@ApiModelProperty(value = "成本价格")
	private Double cost;
	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double money;
	/**
	 * 市场价格(参考价)
	 */
	@ApiModelProperty(value = "市场价格(参考价)")
	private Double mktmoney;

	/**
	 * 可用库存=SKU之和
	 */
	@ApiModelProperty(value = "可用库存=SKU之和")
	private Integer quantity;
/*	*//**
	 * 店铺分类id
	 *//*
	@ApiModelProperty(value = "店铺分类id")
	private Long shopCatId;*/
	/**
	 * 运费模板id
	 */
	@ApiModelProperty(value = "运费模板id")
	private Long templateId;
	/**
	 * 谁承担运费1：买家承担，2：卖家承担
	 */
	@ApiModelProperty(value = "谁承担运费1：买家承担，2：卖家承担")
	private Integer goodsTransfeeCharge;

	/**
	 * 是否自营1 是 2 否
	 */
	@ApiModelProperty(value = "是否自营1 是 2 否")
	@NotNull(message = "是否自营不能为空")
	private Integer selfOperated;

	/**
	 * 是否开票
	 */
	@ApiModelProperty(value = "是否支持开票 1 支持 2 不支持")
	@NotNull(message = "否支持开票不能为空")
	private Integer isInvoice;
	/**
	 * 1，专票2普票3专票+普票
	 */
	@ApiModelProperty(value = "1，专票2普票3专票+普票")
	private Integer invoiceType;

	/**
	 * 原图路径
	 */
	@ApiModelProperty(value = "原图路径")
	@NotBlank(message = "原图路径不能为空")
	private String original;

	@ApiModelProperty(value = "商品SKU集合")
	private List<EsGoodsSkuForm> skuList;
	/**
	 * 商品标签集合
	 */
	@ApiModelProperty(value = "商品标签集合")
	private Integer[] tagsIds;

	@ApiModelProperty(value = "卖家承诺ID")
	private Long promiseId;
	@ApiModelProperty(value = "是否有卖家承诺 1 是 2 否")
	private Integer sellerPromise;
	@ApiModelProperty(value = "店铺自定义分类ID")
	private Long customId;

	@ApiModelProperty(value = "是否生鲜 1生鲜  2 非生鲜")
	@NotNull(message = "是否生鲜不能为空")
	private Integer isFresh;
	@ApiModelProperty(value = "售后服务")
	private String afterService;
	@ApiModelProperty(value = "商品参数集合")
	private List< EsGoodsParamsForm> paramsList;

	private Integer isLfc;
	private Integer isVirtual;
}
