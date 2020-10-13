package com.xdl.jjg.model.form;

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
public class EsTagGoodsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "标签id")
	private Long tagId;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

}
