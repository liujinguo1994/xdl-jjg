package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:29
 */
@Data
@Api
@Accessors(chain = true)
public class EsMyFootprintQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

    /**
     * 访问时间
     */
	@ApiModelProperty(value = "访问时间")
	private Long createTime;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

    /**
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格")
	private Double money;

    /**
     * 商品图片
     */
	@ApiModelProperty(value = "商品图片")
	private String img;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员ID")
	private Long memberId;

}
