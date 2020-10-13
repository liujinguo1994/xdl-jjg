package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 团购活动表QueryForm
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGroupBuyActiveQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String actName;

    /**
     * 团购开启时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购开启时间")
    private Long startTime;

    /**
     * 团购结束时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购结束时间")
    private Long endTime;

    /**
     * 团购报名截止时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购报名截止时间")
    private Long joinEndTime;

    /**
     * 团购添加时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购添加时间")
    private Long addTime;

    /**
     * 团购活动标签Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购活动标签Id")
    private Long actTagId;

    /**
     * 参与团购商品数量
     */
    @ApiModelProperty(value = "参与团购商品数量")
    private Integer goodsNum;

}
