package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 店铺评分VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:27
 */
@Data
@Api
@Accessors(chain = true)
public class EsMemberShopScoreVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	private Long id;

    /**
     * 会员id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员id")
	private Long memberId;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 发货速度评分
     */
	@ApiModelProperty(value = "发货速度评分")
	private Double deliveryScore;

    /**
     * 描述相符度评分
     */
	@ApiModelProperty(value = "描述相符度评分")
	private Double descriptionScore;

    /**
     * 服务评分
     */
	@ApiModelProperty(value = "服务评分")
	private Double serviceScore;

    /**
     * 卖家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "卖家id")
	private Long shopId;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

	protected Serializable pkVal() {
		return this.id;
	}

}
