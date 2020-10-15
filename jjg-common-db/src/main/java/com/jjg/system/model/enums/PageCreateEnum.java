package com.jjg.system.model.enums;

/**
 * 场景枚举
 */
public enum PageCreateEnum {

    INDEX("首页"), GOODS("商品页"), HELP("帮助页面");

    String scene;

    PageCreateEnum(String scene) {
        this.scene = scene;
    }

    public String getScene() {
        return scene;
    }

}
