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
 * 供应商结算单-es_supplierQueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:48
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSupplierBillQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结算单编号
     */
	@ApiModelProperty(value = "结算单编号")
	private String billSn;

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
     * 结算总金额
     */
	@ApiModelProperty(value = "结算总金额")
	private Double price;

    /**
     * 结算金额
     */
	@ApiModelProperty(value = "结算金额")
	private Double billMoney;

    /**
     * 状态 0 已结算 1 未结算
     */
	@ApiModelProperty(value = "状态 0 已结算 1 未结算")
	private Integer state;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺id")
	private Long shopId;

    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;

    /**
     * 结算时间
     */
	@ApiModelProperty(value = "结算时间")
	private Long updateTime;

    /**
     * 创建日期
     */
	@ApiModelProperty(value = "创建日期")
	private Long createTime;

}
