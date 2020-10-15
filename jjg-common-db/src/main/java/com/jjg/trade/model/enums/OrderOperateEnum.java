package com.jjg.trade.model.enums;

/**
 * 订单的操作方式
 * @author kingapex
 * @version 1.0
 * @since v7.0.0
 * 2017年5月19日下午10:43:06
 */
public enum OrderOperateEnum {


	/** 确认 */
	CONFIRM("确认"),

	/** 支付 */
	PAY("支付"),

	/** 取消 */
	CANCEL("取消"),

	/** 发货 */
	SHIP("发货"),

	/** 确认收货 */
	ROG("确认收货"),

	/** 评论 */
	COMMENT("评论"),

	/** 完成 */
	COMPLETE("完成");

	private String description;

	OrderOperateEnum(String description){
		  this.description = description;
	}

	public String description(){
		return this.description;
	}

}
