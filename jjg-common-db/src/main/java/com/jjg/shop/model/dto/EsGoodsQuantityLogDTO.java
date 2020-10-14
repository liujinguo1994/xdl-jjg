package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-16 09:52:07
 */
@Data
@Accessors(chain = true)
public class EsGoodsQuantityLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 更新时间
     */
	private Long updateTime;

    /**
     * 订单编号
     */
	private String orderSn;

    /**
     * 虚拟库存
     */
	private Integer xnQuantity;

    /**
     * 实际库存
     */
	private Integer quantity;

    /**
     * 商品数量
     */
	private Integer goodsSum;

    /**
     * 操作类型0 扣减库存 1增加库存
     */
	private Integer type;


	private Long goodsId;

	private Long skuId;

	protected Serializable pkVal() {
		return this.id;
	}

}
