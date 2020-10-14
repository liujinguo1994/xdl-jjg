package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 生成订单返回的VO
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsTradeSnMoneyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(required = false,value = "订单编号")
	private String tradeSn;
	/**
     * 需第三方支付总价格
     */
	@ApiModelProperty(required = false,value = "需第三方支付总价格")
	private Double totalMoney;
	/**
	 * 送货至
	 */
	private String address;


}
