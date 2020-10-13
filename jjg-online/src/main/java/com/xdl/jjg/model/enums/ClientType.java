package com.xdl.jjg.model.enums;

/**
 * 支付客户端类型
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum ClientType {

	/**
	 * pc客户端
	 */
	PC("pc_config", "PC"),
	/**
	 * wap
	 */
	WAP("wap_config", "WAP"),
	/**
	 * 原生
	 */
	NATIVE("app_native_config", "APP"),
	/**
	 * 小程序
	 */
	APPLET("applet_config", "APPLET");

	private String dbColumn;
	private String client;

	ClientType(String dbColumn, String client) {
		this.dbColumn = dbColumn;
		this.client = client;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public void setDbColumn(String dbColumn) {
		this.dbColumn = dbColumn;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String value() {
		return this.name();
	}
	

}
