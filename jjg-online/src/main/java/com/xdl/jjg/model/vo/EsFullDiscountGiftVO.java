package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 满减赠品表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFullDiscountGiftVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 赠品名称
     */
	@ApiModelProperty(value = "赠品名称")
	private String giftName;

    /**
     * 赠品金额
     */
	@ApiModelProperty(value = "赠品金额")
	private Double giftMoney;

    /**
     * 赠品图片
     */
	@ApiModelProperty(value = "赠品图片")
	private String giftImg;

//    /**
//     * 赠品类型
//     */
//	@ApiModelProperty(value = "赠品类型")
//	private Integer giftType;

    /**
     * 可用库存
     */
	@ApiModelProperty(value = "可用库存")
	private Integer enableStore;

    /**
     * 档案SKUid
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "档案SKUid")
	private Long skuiId;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺id")
	private Long shopId;

	/**
	 * 商品id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

//	/**
//	 * 实际库存
//	 */
//	@ApiModelProperty(value = "实际库存")
//	private Integer quantity;
//
//	/**
//	 * 可用库存=真实库存+虚拟库存-冻结库存
//	 */
//	@ApiModelProperty(value = "可用库存")
//	private Integer enableQuantity;
//
//	/**
//	 * 虚拟库存
//	 */
//	@ApiModelProperty(value = "虚拟库存")
//	private Integer xnQuantity;
//
//	@ApiModelProperty(value = "创建时间")
//	private Long createTime;
//	@ApiModelProperty(value = "更新时间")
//	private Long updateTime;
//	private List<EsSellerGoodsSkuDO> skuList;
	protected Serializable pkVal() {
		return this.id;
	}

}
