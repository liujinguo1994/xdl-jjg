package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsMinusQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单品立减金额
     */
	@ApiModelProperty(value = "单品立减金额")
	private Double singleReductionValue;

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
     * 单品立减活动标题
     */
	@ApiModelProperty(value = "单品立减活动标题")
	private String title;

    /**
     * 商品参与方式（1，全部商品 2，部分商品）
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

}
