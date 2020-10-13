package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单主表-es_trade
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapRefundCountVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 可申请数量
     */
    @ApiModelProperty(value = "可申请数量")
	private Integer canApply;

    /**
     * 申请中
     */
    @ApiModelProperty(value = "申请中")
	private Integer apply;


    /**
     * 已完成
     */
    @ApiModelProperty(value = "已完成")
	private Integer complete;

}
