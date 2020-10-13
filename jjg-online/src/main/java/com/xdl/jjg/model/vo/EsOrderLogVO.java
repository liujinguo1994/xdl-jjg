package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class EsOrderLogVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
	private String orderSn;
    /**
     * 操作者id
     */
    @ApiModelProperty(value = "操作者id")
	private Long opId;
    /**
     * 操作者名称
     */
    @ApiModelProperty(value = "操作者名称")
	private String opName;
    /**
     * 日志信息
     */
    @ApiModelProperty(value = "日志信息")
	private String message;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
	private Long opTime;
    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
	private BigDecimal money;


	protected Serializable pkVal() {
		return this.id;
	}

}
