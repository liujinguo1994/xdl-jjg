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
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsOrderLogQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 操作者id
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "操作时间")
	private Long opTime;

    /**
     * 订单金额
     */
	@ApiModelProperty(value = "订单金额")
	private Double money;

}
