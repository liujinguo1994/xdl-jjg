package com.jjg.member.model.enums;

/**
 * @ClassName: AlipayConfigItem
 * @Description: 支付宝客户端使用配置参数
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum AlipayConfigItem {

	/**
	 * 支付宝公钥
	 */
	ALIPAY_PUBLIC_KEY("支付宝公钥"),

	/**
	 * 应用ID
	 */
	APP_ID("应用ID"),

	/**
	 * 商户私钥
	 */
	MERCHANT_PRIVATE_KEY("商户私钥");

	private String text;

	AlipayConfigItem(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String value() {
		return this.name();
	}
	

}
