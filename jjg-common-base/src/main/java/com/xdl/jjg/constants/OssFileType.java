package com.xdl.jjg.constants;

public enum OssFileType {

    /**
     * 二维码文件
     */
    QRCODE("qrcode/", "二维码"),
    /**
     * EXCEL文件
     */
    EXCEL("excel/", "EXCEL文件"),
    /**
     * 商品文件
     */
    GOODS("goods/", "商品文件"),
    /**
     * 其他文件
     */
    OTHER("other/", "其他文件"),
    /**
     * 店铺文件
     */
    SHOP("shop/", "店铺文件"),
    /**
     * 编辑器文件
     */
    UEDITOR("ueditor/", "店铺文件");


    private String value;
    private String desc;

    OssFileType(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
