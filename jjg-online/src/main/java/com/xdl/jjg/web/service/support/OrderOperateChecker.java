package com.xdl.jjg.web.service.support;


import com.jjg.trade.model.enums.OrderOperateEnum;
import com.jjg.trade.model.enums.OrderStatusEnum;
import com.jjg.trade.model.enums.PaymentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单操作检验
 *
 * @author Snow create in 2018/5/16
 * @version v2.0
 * @since v7.0.0
 */
public class OrderOperateChecker {

    /** 货到付款流程 */
    private static final Map<OrderStatusEnum,OrderStep> COD_FLOW = new HashMap<OrderStatusEnum,OrderStep>();

    /** 款到发化流程 */
    private static final Map<OrderStatusEnum,OrderStep> PAY_FIRST_FLOW = new HashMap<OrderStatusEnum,OrderStep>();

    //定义流程
    static{

        //定义货到付款流程
        initCodFlow();

        //定义款到发货流程
        initPayFirstFlow();
    }


    /**
     * 校验操作是否被允许
     * @param paymentType
     * @param status
     * @param operate
     * @return
     */
    public static boolean checkAllowable(PaymentTypeEnum paymentType, OrderStatusEnum status, OrderOperateEnum operate ){

        Map<OrderStatusEnum,OrderStep> flow = null;
        if(PaymentTypeEnum.COD.compareTo( paymentType)==0 ){
            flow = COD_FLOW;
        }else{
            flow = PAY_FIRST_FLOW;
        }

        if(flow ==null){
            return false;
        }

        OrderStep step =  flow.get(status);
        return step.checkAllowable(operate);

    }





    /**
     * 定义款到发货流程
     */
    private static void initPayFirstFlow(){

        //新订单，可以确认，可以取消
        OrderStep newStep = new OrderStep( OrderStatusEnum.NEW,  OrderOperateEnum.CONFIRM, OrderOperateEnum.CANCEL);
        PAY_FIRST_FLOW.put( OrderStatusEnum.NEW, newStep);

        //确认的订单，可以支付，可以取消
        OrderStep confirmStep = new OrderStep( OrderStatusEnum.CONFIRM,  OrderOperateEnum.PAY,  OrderOperateEnum.CANCEL);
        PAY_FIRST_FLOW.put( OrderStatusEnum.CONFIRM, confirmStep);

        //已经支付，可以发货
        OrderStep payStep = new OrderStep( OrderStatusEnum.PAID_OFF,  OrderOperateEnum.SHIP);
        PAY_FIRST_FLOW.put( OrderStatusEnum.PAID_OFF, payStep);

        //发货的订单，可以确认收货
        OrderStep shipStep = new OrderStep( OrderStatusEnum.SHIPPED,  OrderOperateEnum.ROG);
        PAY_FIRST_FLOW.put( OrderStatusEnum.SHIPPED, shipStep);

        //收货的订单，可以完成
        OrderStep rogStep = new OrderStep( OrderStatusEnum.ROG,  OrderOperateEnum.COMPLETE);
        PAY_FIRST_FLOW.put( OrderStatusEnum.ROG, rogStep);

        //售后的订单可以完成
        OrderStep refundStep = new OrderStep( OrderStatusEnum.AFTER_SERVICE,  OrderOperateEnum.COMPLETE);
        PAY_FIRST_FLOW.put( OrderStatusEnum.AFTER_SERVICE, refundStep);


        //取消的的订单不能有任何操作
        OrderStep cancelStep = new OrderStep( OrderStatusEnum.CANCELLED);
        PAY_FIRST_FLOW.put( OrderStatusEnum.CANCELLED, cancelStep);

//        //异常的订单不能有任何操作
//        OrderStep errorStep = new OrderStep( OrderStatusEnum.INTODB_ERROR);
//        PAY_FIRST_FLOW.put( OrderStatusEnum.INTODB_ERROR, errorStep);

        //完成的订单不能有任何操作
        OrderStep completeStep = new OrderStep( OrderStatusEnum.COMPLETE);
        PAY_FIRST_FLOW.put( OrderStatusEnum.COMPLETE, completeStep);
    }

    /**
     * 定义货到付款流程 新系统中 不涉及货到付款
     */
    private static void initCodFlow(){
        //新订单，可以确认，可以取消
        OrderStep newStep = new OrderStep( OrderStatusEnum.NEW,  OrderOperateEnum.CONFIRM, OrderOperateEnum.CANCEL);
        COD_FLOW.put( OrderStatusEnum.NEW, newStep);

        //确认的订单，可以发货,可取消
        OrderStep confirmStep = new OrderStep( OrderStatusEnum.CONFIRM, OrderOperateEnum.SHIP, OrderOperateEnum.CANCEL);
        COD_FLOW.put( OrderStatusEnum.CONFIRM, confirmStep);

        //发货的订单，可以确认收货、支付
        OrderStep shipStep = new OrderStep( OrderStatusEnum.SHIPPED,  OrderOperateEnum.ROG, OrderOperateEnum.PAY);
        COD_FLOW.put( OrderStatusEnum.SHIPPED, shipStep);

        //收货的订单，可以付款
        OrderStep rogStep = new OrderStep( OrderStatusEnum.ROG,  OrderOperateEnum.PAY);
        COD_FLOW.put( OrderStatusEnum.ROG, rogStep);

        //已经支付，可以完成
        OrderStep payStep = new OrderStep( OrderStatusEnum.PAID_OFF, OrderOperateEnum.COMPLETE);
        COD_FLOW.put( OrderStatusEnum.PAID_OFF, payStep);

        //售后的订单可以完成
        OrderStep refundStep = new OrderStep( OrderStatusEnum.AFTER_SERVICE,  OrderOperateEnum.COMPLETE);
        COD_FLOW.put( OrderStatusEnum.AFTER_SERVICE, refundStep);

        //取消的的订单不能有任何操作
        OrderStep cancelStep = new OrderStep( OrderStatusEnum.CANCELLED);
        COD_FLOW.put( OrderStatusEnum.CANCELLED, cancelStep);

        //完成的订单不能有任何操作
        OrderStep completeStep = new OrderStep( OrderStatusEnum.COMPLETE);
        COD_FLOW.put( OrderStatusEnum.COMPLETE, completeStep);
    }


}
