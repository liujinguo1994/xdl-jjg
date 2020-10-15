package com.jjg.trade.model.enums;

/**
 * @author zh
 * @version v1.0
 * @Description: 发票类型枚举
 * @date 2018/5/3 11:12
 * @since v7.0.0
 */
public enum ReceiptTypeEnum {
    /**
     * 电子普通发票
     */
    ELECTRONIC_INVOICE("电子普通发票"),

    /**
     * 增值税普通发票
     */
    VALUE_ADDED_TAX_INVOICE("增值税普通发票"),

    /**
     * 增值税专用发票
     */
    VAT_SPECIAL_INVOICE("增值税专用发票");

    private String text;

    ReceiptTypeEnum(String text) {
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
