package com.xdl.jjg.model.enums;

/**
 * 申请售后的状态
 * @author Snow
 * @version v2.0
 * @since v6.4
 * 2017年9月6日 下午4:16:53
 */
public enum ServiceStatusEnum {

	/** 未申请 */
	NOT_APPLY("未申请"),

	/** 申请中 */
	APPLY("申请中"),

	/** 审核通过 */
	PASS("审核通过"),

	/** 审核驳回 */
	REFUSE("审核驳回"),

    /** 待退款 */
    REFUND_MONEY("待退款"),

    /** 待发货 */
    TO_BE_SHIP("待发货"),

    /** 已完成 */
    COMPLETED("售后已完成"),

	/** 已失效或不允许申请售后 */
	EXPIRED("已失效不允许申请售后");


	private String description;

	ServiceStatusEnum(String description){
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
	public static String getServiceName(String code){
		for(ServiceStatusEnum status:ServiceStatusEnum.values()){
			if(code.equals(status.value())){
				return status.getDescription();
			}
		}
		return "";
	}

}
