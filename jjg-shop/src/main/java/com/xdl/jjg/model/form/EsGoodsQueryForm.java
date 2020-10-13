package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsGoodsQueryForm extends QueryPageForm {

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	/**
	 * 商品编号
	 */
	@ApiModelProperty(value = "商品编号")
	private String goodsSn;


	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类id")
	private Long categoryId;

	/**
	 * 上架状态 1上架 0下架
	 */
	@ApiModelProperty(value = "上架状态 1上架 2下架")
	private Integer marketEnable;

	/**
	 * 运费模板id
	 */
	@ApiModelProperty(value = "运费模板id")
	private Long templateId;

	/**
	 * 谁承担运费0：买家承担，1：卖家承担
	 */
	@ApiModelProperty(value = " 谁承担运费1：买家承担，2：卖家承担")
	private Integer goodsTransfeeCharge;
	/**
	 * 0 待审核，1 审核通过 2 未通过
	 */
	@ApiModelProperty(value = "0 待审核，1 审核通过 2 未通过")
	private Integer isAuth;

	/**
	 * 是否自营0 是 1 否
	 */
	@ApiModelProperty(value = "是否自营1 是 2 否")
	private Integer selfOperated;
	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Long startTime;
	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Long endTime;
	/**
	 * 分类路径
	 */
	@ApiModelProperty(value = "分类路径")
	private String categoryPath;
	/**
	 * 起始价格
	 */
	@ApiModelProperty(value = "起始价格")
	private Double startMoney;
	/**
	 * 截止价格
	 */
	@ApiModelProperty(value = "截止价格")
	private Double endMoney;
	@ApiModelProperty(value = "是否人寿 1 是 2 否")
	private Long isLfc;

	private Integer isVirtual;

}
