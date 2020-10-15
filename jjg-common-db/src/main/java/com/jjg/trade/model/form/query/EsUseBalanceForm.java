package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-10
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsUseBalanceForm implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 父订单编号
	 */
	@ApiModelProperty(value = "父订单编号")
	@NotBlank(message = "订单编号必填")
	private String tradeSn;

//	/**
//	 * 子订单订单编号
//	 */
//	@ApiModelProperty(value = "子订单订单编号")
//	private String orderSn;

	/**
	 * 余额
	 */
	@ApiModelProperty(value = "余额")
	private Double balance;


}
