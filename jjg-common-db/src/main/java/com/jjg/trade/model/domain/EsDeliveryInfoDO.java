package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 自提信息表
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 14:34:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsDeliveryInfoDO extends Model<EsDeliveryInfoDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

	/**
	 * 订单编号
	 */

	private String orderSn;

	/**
	 * 订单明细编号
	 */

	private Long orderDetailSn;

    /**
     * 自提内容
     */
	private String content;

    /**
     * 是否完成 0：未完成 1：完成
     */
	private Integer isOk;

    /**
     * 商品SKU_ID
     */
	private Long skuId;

	/**
	 * 商品ID
	 */
	private Long goodsId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
