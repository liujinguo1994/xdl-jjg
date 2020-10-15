package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回人寿数据

 * @author yuanj
 * @version v1.0
 * @since v7.0.0
 * 2020年3月25日 上午10:55:08
 */
@ApiModel(value = "返回人寿数据")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class LfcResult implements Serializable {

	/**
	 * 状态码
	 */
	@ApiModelProperty(value = "状态码")
	private String code;

	/**
	 * 返回数据
	 */
	@ApiModelProperty(value = "返回数据")
	private Object result;

}