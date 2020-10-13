package com.xdl.jjg.model.enums;

/**
 * 支付客户端类型
 */
public enum ClientType {

    // pc端，移动wap端，移动app端
    PC("pc_enable", "PC"), WAP("wap_enable", "WAP"), APP("app_enable", "APP");

    private String description;
    private String client;

    ClientType(String description, String client) {
        this.description = description;
        this.client = client;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }


}
