package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 收货操作VO
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
public class RogVO implements Serializable {

    @ApiModelProperty(value = "订单编号" )
    private String orderSn;

    @ApiModelProperty(value = "操作者" )
    private String operator;



    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "RogVO{" +
                "orderSn='" + orderSn + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        RogVO rogVO = (RogVO) o;

        return new EqualsBuilder()
                .append(orderSn, rogVO.orderSn)
                .append(operator, rogVO.operator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orderSn)
                .append(operator)
                .toHashCode();
    }
}
