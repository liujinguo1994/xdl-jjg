package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsOrderMetaQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 扩展-键
     */
	@ApiModelProperty(value = "扩展-键")
	private String metaKey;

    /**
     * 扩展-值
     */
	@ApiModelProperty(value = "扩展-值")
	private String metaValue;

    /**
     * 售后状态
     */
	@ApiModelProperty(value = "售后状态")
	private String state;

}
