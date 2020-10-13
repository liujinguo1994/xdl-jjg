package com.xdl.jjg.model.enums;

/**
 * 订单状态
 *
 * @author AJIN
 * @version 1.0
 * @since v7.0.0
 * 2019年08月10日下午2:44:54
 */
public enum GoodOperationEnum {

    /**
     * 商品投诉
     */
    COMPLAINT("商品投诉");

    private String description;

    GoodOperationEnum(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

    public static String getOrderName(String code){
        for(GoodOperationEnum order: GoodOperationEnum.values()){
            if(code.equals(order.value())){
                return order.getDescription();
            }
        }
        return "";
    }

}
