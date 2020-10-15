package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsOrderItemsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
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
     * 已发货数量
     */
	private Integer shipNum;
    /**
     * 订单编号
     */
	private String tradeSn;
    /**
     * 子订单编号
     */
	private String orderSn;
    /**
     * 图片
     */
	private String image;
    /**
     * 商品名称
     */
	private String name;
    /**
     * 优惠后价格
     */
	private Double money;
    /**
     * 分类ID
     */
	private Long categoryId;
    /**
     * 售后状态
     */
	private String state;
    /**
     * 支付快照id
     */
	private Long snapshotId;
    /**
     * 规格json
     */
	private String specJson;
    /**
     * 促销类型
     */
	private String promotionType;
    /**
     * 活动ID
     */
	private Long promotionId;
    /**
     * 发货单号
     */
	private String shipNo;
    /**
     * 签收人
     */
	private String theSign;
    /**
     * 物流公司ID
     */
	private Long logiId;
    /**
     * 物流公司名称
     */
	private String logiName;
	/**
	 * 单品评价是否完成
	 */
	private String singleCommentStatus;
	/**
	 * 发货状态 0 未发货，1 已发货
	 */
	private Integer hasShip;
	/**
	 * 发票流水号
	 */
	private String invoiceNumber;
	/**
	 * 会员ID
	 * 此字段 仅供会员查询评价列表是使用
	 */
	private Long memberId;


	/**
	 * "配送方式 notInScope 不在配送范围，express 快递，selfMention 自提")tion
	 */
	private String deliveryMethod;


	protected Serializable pkVal() {
		return this.id;
	}

}
