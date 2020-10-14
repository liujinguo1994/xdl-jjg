package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * RFM表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:57
 */
@Data
@ApiModel
public class EsMemberRfmConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = false,value = "主键id",example = "1")
    private Long id;

    /**
     * 消费间隔
     */
    @ApiModelProperty(required = false,value = "消费间隔",example = "1")
	private Integer recency;

    /**
     * 消费间隔成长值
     */
    @ApiModelProperty(required = false,value = "消费间隔成长值",example = "1")
    private Integer recencyGrowthValue;

    /**
     * 消费频率
     */
    @ApiModelProperty(required = false,value = "消费频率",example = "1")
	private Integer frequency;

    /**
     * 消费频率成长值
     */
    @ApiModelProperty(required = false,value = "消费频率成长值",example = "1")
    private Integer frequencyGrowthValue;

    /**
     * 消费金额
     */
    @ApiModelProperty(required = false,value = "消费金额",example = "50.22")
    private Double monetary;

    /**
     * 消费金额成长值
     */
    @ApiModelProperty(required = false,value = "消费金额成长值",example = "22")
    private Integer monetaryGrowthValue;

    /**
     * rfm信息json字符串
     */
    @ApiModelProperty(required = false,value = "rfm信息json字符串")
    private String rfmInfo;

}
