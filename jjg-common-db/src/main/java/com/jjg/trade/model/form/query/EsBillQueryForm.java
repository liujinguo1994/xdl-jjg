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
 * 签约公司结算单QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:41
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsBillQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结算单编号
     */
	@ApiModelProperty(value = "结算单编号")
	private String billSn;

    /**
     * 状态 0 已结算 1 未结算
     */
	@ApiModelProperty(value = "状态 0 已结算 1 未结算")
	private Integer state;

    /**
     * 结算开始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "结算开始时间")
	private Long startTime;

    /**
     * 结算结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "结算结束时间")
	private Long endTime;

    /**
     * 公司ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "公司ID")
	private Long companyId;
}
