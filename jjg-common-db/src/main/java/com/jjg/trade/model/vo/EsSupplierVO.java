package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 供应商结算单-es_supplier
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSupplierVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 结算单编号
     */
	@ApiModelProperty(value = "结算单编号")
	private String billSn;
    /**
     * 结算开始时间
     */
	@ApiModelProperty(value = "结算开始时间")
	private Long startTime;
    /**
     * 结算结束时间
     */
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
	private Long payTime;
    /**
     * 创建日期
     */
	@ApiModelProperty(value = "创建日期")
	private Long createTime;


	protected Serializable pkVal() {
		return this.id;
	}

}
