package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 第二件半价活动表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsHalfPriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 起始时间
     */
	@ApiModelProperty(value = "起始时间")
	private Long createTime;

    /**
     * 结束时间
     */
	@ApiModelProperty(value = "结束时间")
	private Long updateTime;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

    /**
     * 商品参与方式1全部商品：2，部分商品
     */
	@ApiModelProperty(value = "商品参与方式1全部商品：2，部分商品")
	private Integer rangeType;

    /**
     * 是否停用 0.没有停用 1.停用
     */
	@ApiModelProperty(value = "是否停用 0.没有停用 1.停用")
	private Integer isDel;

    /**
     * 活动说明
     */
	@ApiModelProperty(value = "活动说明")
	private String description;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家id")
	private Long shopId;

	/**
	 * 活动开始时间
	 */
	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

	/**
	 * 商品集合
	 */
	@ApiModelProperty(value = "商品集合")
	private List<ESGoodsSkuSelectVO> goodsList;
	@ApiModelProperty(value = "活动状态")
	private String  statusText;

	@ApiModelProperty(value = "活动状态标识,expired表示已失效")
	private String status;

	protected Serializable pkVal() {
		return this.id;
	}

}
