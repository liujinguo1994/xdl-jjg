package com.xdl.jjg.model.enums;

/**
 * @author yuanj
 * @version v2.0
 * @Description: 海康支付使用配置参数
 * @date 2020/05/06 15:55
 * @since v7.0.0
 */
public enum HikpayConfigItem {

	/**
	 * 商户代码
	 */
	mer_id("商户代码"),

	/**
	 * 商家名称
	 */
	mer_name("商家名称"),

	/**
	 * 商家电话
	 */
	mer_phone("商家电话"),

	/**
	 * 商家图片
	 */
	mer_images("商家图片"),

	/**
	 * 约定Key
	 */
	key("key"),

	/**
	 * ip地址
	 */
	ip("ip白名单");


	private String text;

	HikpayConfigItem(String text) {
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
