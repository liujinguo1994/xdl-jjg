package com.jjg.trade.model.enums;

/**
 * 订单状态
 * @author Snow
 * @version 1.0
 * @since v7.0.0
 * 2017年3月31日下午2:44:54
 */
public enum PayStatusEnum {

	/** 新订单 */
	PAY_NO("新订单"),

	/** 部分支付 */
	PAY_PARTIAL("部分支付"),

	/** 已付款 */
	PAY_YES("已付款");

	private String description;

	PayStatusEnum(String description){
		  this.description=description;

	}

	public String description(){
		return this.description;
	}



	public String value(){
		return this.name();
	}
	public String getDescription() {
		return description;
	}
	public static String getPayName(String code){
		for(PayStatusEnum status:PayStatusEnum.values()){
			if(code.equals(status.value())){
				return status.getDescription();
			}
		}
		return "";
	}


}
