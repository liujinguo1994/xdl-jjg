package com.xdl.jjg.model.form;

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
public class EsOrderItemsForm implements Serializable {

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


	protected Serializable pkVal() {
		return this.id;
	}

}
