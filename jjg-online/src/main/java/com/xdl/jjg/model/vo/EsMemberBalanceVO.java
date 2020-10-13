package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class EsMemberBalanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 可用积分
	 */
	@ApiModelProperty(value = "可用积分")
	private Long consumPoint;


	/**
	 * 余额
	 */
	@ApiModelProperty(value = "余额")
	private Double memberBalance;


}
