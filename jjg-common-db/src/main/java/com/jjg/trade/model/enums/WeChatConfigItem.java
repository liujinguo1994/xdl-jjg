package com.jjg.trade.model.enums;

/**
 * @ClassName: WeChatConfigItem
 * @Description: 微信客户端使用配置参数
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum WeChatConfigItem {

	/**
	 * 商户号MCH_ID
	 */
	MCH_ID("商户号MCH_ID"),

	/**
	 * APP_ID
	 */
	APP_ID("APP_ID"),

	/**
	 * API密钥(key)
	 */
	KEY("API密钥(key)"),

	/**
	 * 应用密钥(AppSecret)
	 */
	APP_SECRET("应用密钥(AppSecret)"),

	/**
	 * 证书路径
	 */
	P12_PATH("证书路径");

	private String text;

	WeChatConfigItem(String text) {
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
