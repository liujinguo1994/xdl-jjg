package com.xdl.jjg.model.vo;

import com.shopx.trade.api.model.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单流程图对象
 *
 * @author Snow create in 2018/6/25
 * @version v2.0
 * @since v7.0.0
 */
@Data
public class EsOrderFlow implements Serializable {

    @ApiModelProperty(value = "文字")
    private String text;

    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "展示效果",allowableValues = "0,1,2,3",example = "0:灰色,1:普通显示,2:结束显示,3:取消显示")
    private Integer showStatus;

    @Override
    public String toString() {
        return "OrderFlow{" +
                "text='" + text + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", showStatus=" + showStatus +
                '}';
    }


    public EsOrderFlow() {

    }

    public EsOrderFlow(OrderStatusEnum orderStatus) {
        this.text = orderStatus.description();
        this.orderStatus = orderStatus.value();
        this.showStatus = 0;
    }

}
