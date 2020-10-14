package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSeckillApplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 活动id
     */
	@ApiModelProperty(value = "活动id")
	private Long seckillId;
    /**
     * 时刻
     */
	@ApiModelProperty(value = "时刻")
	private Integer timeLine;
	/**
	 * 活动开始日期
	 */
	@ApiModelProperty(value = "活动开始日期")
	private Long startDay;
	/**
	 * 活动结束日期
	 */
	@ApiModelProperty(value = "活动结束日期")
	private Long endTime;
    /**
     * 商品ID
     */
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;
    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
    /**
     * 商家ID
     */
	@ApiModelProperty(value = "商家ID")
	private Long shopId;
    /**
     * 商家名称
     */
	@ApiModelProperty(value = "商家名称")
	private String shopName;
    /**
     * 价格
     */
	@ApiModelProperty(value = "价格")
	private Double money;
    /**
     * 售空数量
     */
	@ApiModelProperty(value = "售空数量")
	private Integer soldQuantity;
    /**
     * 申请状态
     */
	@ApiModelProperty(value = "申请状态")
	private Integer state;
	@ApiModelProperty(value = "申请状态文本")
	private String stateText;
    /**
     * 驳回原因
     */
	@ApiModelProperty(value = "驳回原因")
	private String failReason;
    /**
     * 商品原始价格
     */
	@ApiModelProperty(value = "商品原始价格")
	private Double originalPrice;
    /**
     * 已售数量
     */
	@ApiModelProperty(value = "已售数量")
	private Integer salesNum;

	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片")
	private String image;


	/**
	 * 商品SKU ID
	 */
	@ApiModelProperty(value = "商品SKU ID")
	private Long skuId;

	/**
	 * SKU规格
	 */
	@ApiModelProperty(value = "SKU规格")
	private String specs;

	/**
	 * SKU编号
	 */
	@ApiModelProperty(value = "SKU编号")
	private String skuSn;


	protected Serializable pkVal() {
		return this.id;
	}

}
