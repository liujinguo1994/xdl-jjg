package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品折扣活动表VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-28 14:26:05
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsDiscountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 活动开始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

    /**
     * 活动结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

    /**
     * 折扣
     */
	@ApiModelProperty(value = "折扣")
	private Double discount;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

    /**
     * 商品参与方式（0，全部商品 1，部分商品）
     */
	@ApiModelProperty(value = "商品参与方式（1，全部商品 2，部分商品）")
	private Integer rangeType;

    /**
     * 是否停用
     */
	@ApiModelProperty(value = "是否停用")
	private Integer isDel;

    /**
     * 描述
     */
	@ApiModelProperty(value = "描述")
	private String remark;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家id")
	private Long shopId;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(value = "修改时间")
	private Long updateTime;
	@ApiModelProperty(value = "活动状态")
	private String  statusText;

	@ApiModelProperty(value = "活动状态标识,expired表示已失效")
	private String status;
	@ApiModelProperty(value = "商品集合")
	private List<ESGoodsSkuSelectVO> goodsList;
	protected Serializable pkVal() {
		return this.id;
	}

}
