package com.xdl.jjg.model.co;

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
public class EsMinusCO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;
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
     * 商品参与方式（0，全部商品 1，部分商品）
     */
    @ApiModelProperty(value = "商品参与方式（0，全部商品 1，部分商品）")
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

    @ApiModelProperty(value = "活动状态")
    private String  statusText;

    @ApiModelProperty(value = "活动状态标识,expired表示已失效")
    private String status;


	protected Serializable pkVal() {
		return this.id;
	}

}
