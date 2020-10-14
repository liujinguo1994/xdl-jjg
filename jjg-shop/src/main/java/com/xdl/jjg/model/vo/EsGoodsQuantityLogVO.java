package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-16 09:52:07
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsQuantityLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 更新时间
     */
	@ApiModelProperty(value = "更新时间")
	private Long updateTime;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 虚拟库存
     */
	@ApiModelProperty(value = "虚拟库存")
	private Integer xnQuantity;

    /**
     * 实际库存
     */
	@ApiModelProperty(value = "实际库存")
	private Integer quantity;

    /**
     * 商品数量
     */
	@ApiModelProperty(value = "商品数量")
	private Integer goodsSum;

    /**
     * 操作类型0 扣减库存 1增加库存
     */
	@ApiModelProperty(value = "操作类型0 扣减库存 1增加库存")
	private Integer type;

	protected Serializable pkVal() {
		return this.id;
	}

}
