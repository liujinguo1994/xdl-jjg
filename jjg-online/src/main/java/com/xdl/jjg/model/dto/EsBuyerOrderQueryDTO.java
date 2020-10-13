package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单明细表-es_order
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsBuyerOrderQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
	private Long id;
    /**
     * 父订单编号
     */
	private String tradeSn;
    /**
     * 子订单订单编号
     */
	private String orderSn;
    /**
     * 店铺ID
     */
	private Long shopId;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 买家账号
     */
	private String memberName;
    /**
     * 订单状态
     */
	private String orderState;
    /**
     * 付款状态
     */
	private String payState;
    /**
     * 货运状态
     */
	private String shipState;
    /**
     * 售后状态
     */
	private String serviceState;
    /**
     * 结算状态
     */
	private String billState;
    /**
     * 收货人姓名
     */
	private String shipName;
    /**
     * 收货地址
     */
	private String shipAddr;
    /**
     * 收货人邮编
     */
	private String shipZip;
    /**
     * 收货人手机
     */
	private String shipMobile;
    /**
     * 收货人电话
     */
	private String shipTel;
    /**
     * 是否被删除 0 未删除 1删除
     */
	private Integer isDel;
    /**
     * 商品数量
     */
	private Integer goodsNum;
    /**
     * 订单备注
     */
	private String remark;
    /**
     * 订单取消原因
     */
	private String cancelReason;
    /**
     * 货物列表json
     */
	private String itemsJson;
    /**
     * 会员收货地址ID
     */
	private Long addressId;

    /**
     * 完成时间
     */
	private Long completeTime;
    /**
     * 确认收货签收时间
     */
	private Long signingTime;
    /**
     * 发货时间
     */
	private Long shipTime;
    /**
     * 支付返回的交易号
     */
	private String payOrderNo;
    /**
     * 订单来源 (pc、wap、app)
     */
	private String clientType;
    /**
     * 是否需要发票,0：否，1：是 buyao
     */
	private Integer needReceipt;
    /**
	 * 下单时间起
     * 订单创建时间
     */
	private Long createTime;
    /**
     * 订单更新时间
     */
	private Long updateTime;

    /**
     * 商品名称
     */
    private String goodsName;

	/**
	 * 下单时间止
	 * @return
	 */
	private Long createTimeEnd;

	/**
	 * 订单发票查询是 只查询 确认收货 和 订单完成的
	 */
	private List<String> orderStatus;

	/**
	 * 关键词
	 */
	private String keyword;

	protected Serializable pkVal() {
		return this.id;
	}

}
