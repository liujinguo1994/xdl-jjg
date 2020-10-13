package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 返回人寿数据

 * @author yuanj
 * @version v1.0
 * @since v7.0.0
 * 2019年9月19日 上午10:55:08
 */
@ApiModel(value = "返回人寿数据")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LfcResultForm implements Serializable {

	/**
	 * 数据列表
	 */
	@ApiModelProperty(value = "状态码")
	private String code;

	/**
	 * 当前页码
	 */
	@ApiModelProperty(value = "返回数据")
	private Object result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "LfcResult{" +
				"code='" + code + '\'' +
				", result=" + result +
				'}';
	}
}