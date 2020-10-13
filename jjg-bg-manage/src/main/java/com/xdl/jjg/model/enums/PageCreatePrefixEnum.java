package com.xdl.jjg.model.enums;

/**
 * PageCreatePrefixEnum
 */
public enum PageCreatePrefixEnum {

    /**
     * 首页
     */
    INDEX("/"),
    /**
     * 商品页面
     */
    GOODS("/goods/{goods_id}"),
    /**
     * 帮助页面
     */
    HELP("/help/{article_id}");

    String prefix;

    PageCreatePrefixEnum(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getHandlerGoods(Integer goodsId) {
        return this.prefix.replace("{goods_id}", goodsId.toString());
    }

    public String getHandlerHelp(Integer articleId) {
        return this.prefix.replace("{article_id}", articleId.toString());

    }
}
