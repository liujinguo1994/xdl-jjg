package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 订单完成VO
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@Data
@ApiModel(description = "订单完成")
public class CompleteVO implements Serializable {

    @ApiModelProperty(value = "订单编号" )
    private String orderSn;


    @ApiModelProperty(value = "操作人",hidden=true)
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
        return "CompleteVO{" +
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

        CompleteVO that = (CompleteVO) o;

        return new EqualsBuilder()
                .append(orderSn, that.orderSn)
                .append(operator, that.operator)
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
