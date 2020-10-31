package com.jjg.trade.orderCheck;


import com.jjg.trade.model.enums.OrderOperateEnum;
import com.jjg.trade.model.enums.OrderStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单流程
 * @author Snow create in 2018/5/16
 * @version v2.0
 * @since v7.0.0
 */
public class OrderStep {


    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;


    /**
     * 允许的操作
     */
    private List<OrderOperateEnum> allowableOperate;

    public OrderStep(OrderStatusEnum orderStatus, OrderOperateEnum... operates ){
        this.orderStatus = orderStatus;
        this.allowableOperate = new ArrayList<OrderOperateEnum>();
        for (OrderOperateEnum orderOperate : operates) {
            allowableOperate.add(orderOperate);
        }
    }

    public boolean checkAllowable(OrderOperateEnum operate){
        for (OrderOperateEnum orderOperate : allowableOperate) {
            if(operate.compareTo(orderOperate) == 0){
                return true;
            }
        }
        return false;
    }

}
