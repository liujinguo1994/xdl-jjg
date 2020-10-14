package com.jjg.member.model.enums;

/**
 * 发货状态
 * @author Snow
 * @version 1.0
 * @since v7.0.0
 * 2017年3月31日下午2:44:54
 */
public enum ShipStatusEnum {

	/**
	 * 未发货
	 */
	SHIP_NO("未发货"),

	/**
	 * 已发货
	 */
	SHIP_YES("已发货"),

	/**
	 * 已收货
	 */
	SHIP_ROG("已收货");


	private String description;

	ShipStatusEnum(String description){
		  this.description=description;

	}

	public String description(){
		return this.description;
	}
	public String getDescription() {
		return description;
	}
	public String value(){
		return this.name();
	}

	public static String getShipName(String code){
		for(ShipStatusEnum status:ShipStatusEnum.values()){
			if(code.equals(status.value())){
				return status.getDescription();
			}
		}
		return "";
	}


}
