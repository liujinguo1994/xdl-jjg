package com.jjg.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.domain.EsSpecValuesDO;
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
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsSellerGoodsSkuVO implements Serializable {
	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "商品SKU 主键ID")
	private Long id;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;
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
	 * 条形码
	 */
	@ApiModelProperty(value = "条形码")
	private String barCode;
	/**
	 * 真实库存(仓储系统可用库存)
	 */
	@ApiModelProperty(value = "实际库存")
	private Integer quantity;
	/**
	 * 可用库存=真实库存+虚拟库存-冻结库存
	 */
	@ApiModelProperty(value = "可用库存")
	private Integer enableQuantity;
	/**
	 * 冻结库存
	 */
	@ApiModelProperty(value = "冻结库存")
	private Integer djQuantity;
	/**
	 * 虚拟库存
	 */
	@ApiModelProperty(value = "虚拟库存")
	private Integer xnQuantity;
	/**
	 * 规格信息json
	 */
	@ApiModelProperty(value = "规格信息json")
	private String specs;
	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double money;
	/**
	 * 成本价格
	 */
	@ApiModelProperty(value = "成本价格")
	private Double cost;
	/**
	 * 重量
	 */
	@ApiModelProperty(value = "重量")
	private Double weight;
	/**
	 * 卖家id
	 */
	@ApiModelProperty(value = "卖家id")
	private Long shopId;
	/**
	 * 卖家名称
	 */
	@ApiModelProperty(value = "卖家名称")
	private String shopName;
	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类id")
	private Long categoryId;
	/**
	 * 缩略图
	 */
	@ApiModelProperty(value = "缩略图")
	private String thumbnail;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	/**
	 * 是否支持自体
	 */
	@ApiModelProperty(value = "是否支持自体")
	private Boolean isSelf;
	/**
	 * 保修时间
	 */
	@ApiModelProperty(value = "保修时间")
	private Integer guaranteeTime;

	/**
	 * 质检状态
	 */
	@ApiModelProperty(value = "质检状态")
	private Integer qualityState;

	/**
	 * 质检报告
	 */
	@ApiModelProperty(value = "质检报告")
	private String qualityReport;

	/**
	 * 长
	 */
	@ApiModelProperty(value = "长")
	private String skuLong;

	/**
	 * 宽
	 */
	@ApiModelProperty(value = "宽")
	private String wide;

	/**
	 * 高
	 */
	@ApiModelProperty(value = "高")
	private String high;

	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = " 是否启用  1 启用 2不启用")
	private Boolean isEnable;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	private String categoryName;
	/**
	 * 预警状态
	 */
	@ApiModelProperty(value = "预警状态")
	private String stateName;
	/**
	 * sku编号
	 */
	@ApiModelProperty(value = " sku编号")
	private String skuSn;

	private String specText;
	@ApiModelProperty(value = "商品规格集合")
	private List<EsSpecValuesDO> specList;
	@ApiModelProperty(value = " 预警值")
	private Integer warningValue;
	@ApiModelProperty(value = "相册")
	private List<EsGoodsGalleryVO> goodsGallery;
	@ApiModelProperty(value = "相册组编号")
	private String albumNo;
}
