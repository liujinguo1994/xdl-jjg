package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsArchVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;

	/**
	 * 商品编号
	 */
	@ApiModelProperty(value = "商品编号")
	private String goodsSn;

	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	private Integer quantity;

	/**
	 * 商品SKUID
	 */
	@ApiModelProperty(value = "商品SKUID")
	private Long skuid;

	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double goodsMoney;

	/**
	 * 成本价格
	 */
	@ApiModelProperty(value = "成本价格")
	private Double cost;

	/**
	 * 市场价格
	 */
	@ApiModelProperty(value = "市场价格")
	private Double mktmoney;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	/**
	 * 品牌ID
	 */
	@ApiModelProperty(value = "品牌ID")
	private Long brandId;

	/**
	 * 重量
	 */
	@ApiModelProperty(value = "重量")
	private Double weight;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String intro;

	/**
	 * 供应商ID
	 */
	@ApiModelProperty(value = "供应商ID")
	private Long supplierId;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;

	/**
	 * 负责人姓名
	 */
	@ApiModelProperty(value = "负责人姓名")
	private String chargePerson;

	/**
	 * 负责人联系方式
	 */
	@ApiModelProperty(value = "负责人联系方式")
	private String chargeMobile;

	/**
	 * 负责人邮箱
	 */
	@ApiModelProperty(value = "负责人邮箱")
	private String email;

	/**
	 * 对接采购经理
	 */
	@ApiModelProperty(value = "对接采购经理")
	private String purchaseManager;

	/**
	 * 采购人联系方式
	 */
	@ApiModelProperty(value = "采购人联系方式")
	private String purchaseMobile;

	/**
	 * 采购人邮箱
	 */
	@ApiModelProperty(value = "采购人邮箱")
	private String purchaseEmail;
	/**
	 * 有效状态
	 */
	@ApiModelProperty(value = "有效状态")
	private Long state;

	@ApiModelProperty(value = "是否生鲜（1生鲜，2非生鲜)")
	private Integer isFresh;
	private List<EsGoodsSkuVO> skuList;
	private Long updateTime;

	@ApiModelProperty(value = "是否赠品 1是 2不是")
	private Integer isGifts;
	/**
	 * 税率
	 */
	@ApiModelProperty(value = "税率")
	private Double taxRate;
	/**
	 * 分类编码
	 */
	@ApiModelProperty(value = "分类编码")
	private String cateCode;
	/**
	 * 商品单位
	 */
	@ApiModelProperty(value = "商品单位")
	private String unit;
	protected Serializable pkVal() {
		return this.id;
	}

}
