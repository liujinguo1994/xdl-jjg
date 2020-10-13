package com.xdl.jjg.model.enums;

/**
 * 交易状态
 * @author Snow
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午9:20:46
 *
 */
public enum TradeStatusEnum {

	/** 新订单*/
	NEW("新订单"),

	/** 已付款*/
	PAID_OFF("已付款");

	private String description;

	TradeStatusEnum(String description){
		  this.description=description;

	}

	public String description(){
		return this.description;
	}

	public String value(){
		return this.name();
	}

}
