package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSettlementDetailDO extends Model<EsSettlementDetailDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 退款编号
     */
    private String refundSn;

    /**
     * 商品ID
     */
	private Long goodsId;
    /**
     * skuID
     */
	private Long skuId;
    /**
     * 数量
     */
	private Integer num;

    /**
     * 子订单编号
     */
	private String orderSn;

    /**
     * 商品名称
     */
	private String goodName;

    /**
     * 优惠后价格
     */
	private Double saleMoney;

	/**
	 * 金额
	 */
	private Double money;

	/**
	 * 下单时间
	 */
	private Long creatTime;

	/**
	 * 支付方式
	 */
	private Integer paymentType;
}
