package com.jjg.shop.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDraftGoodsParamsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 草稿ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "草稿ID")
	private Long draftGoodsId;

    /**
     * 参数ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "参数ID")
	private Long paramId;

    /**
     * 参数名
     */
	@ApiModelProperty(value = "参数名")
	private String paramName;

    /**
     * 参数值
     */
	@ApiModelProperty(value = "参数值")
	private String paramValue;

}
