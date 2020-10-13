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
 * 结算单QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:48
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsBillDetailQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 结算状态 0 已结算 1 未结算
     */
	@ApiModelProperty(value = "结算状态 0 已结算 1 未结算")
	private Integer state;

    /**
     * 类型(0 供应商 1 签约公司)
     */
	@ApiModelProperty(value = "类型(0 供应商 1 签约公司)")
	private Integer type;

    /**
     * 签约公司结算单ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "签约公司结算单ID")
	private Long signCompanyId;

    /**
     * 供应商结算ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "供应商结算ID")
	private Long supplierBillId;

}
