package com.xdl.jjg.model.vo;

import com.shopx.trade.api.model.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单可进行的操作
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@Data
public class OrderOperateAllowable implements Serializable {

    @ApiModelProperty(value = "是否允许被取消" )
    private Boolean allowCancel;

    @ApiModelProperty(value = "是否允许被确认" )
    private Boolean allowConfirm;

    @ApiModelProperty(value = "是否允许被支付" )
    private Boolean allowPay;

    @ApiModelProperty(value = "是否允许被发货" )
    private Boolean allowShip;

    @ApiModelProperty(value = "是否允许被收货" )
    private Boolean allowRog;

    @ApiModelProperty(value = "是否允许被评论" )
    private Boolean allowComment;

    @ApiModelProperty(value = "是否允许被完成" )
    private Boolean allowComplete;

    @ApiModelProperty(value = "是否允许申请售后" )
    private Boolean allowApplyService;

    @ApiModelProperty(value = "是否允许取消(售后)" )
    private Boolean allowServiceCancel;

    /**
     * 空构造器
     */
    public OrderOperateAllowable() {
    }


    /**
     * 根据各种状态构建对象
     * @param orderStatus
     * @param serviceStatus
     */
    public OrderOperateAllowable(OrderStatusEnum orderStatus, ServiceStatusEnum serviceStatus) {

        // 是否允许被取消
        this.allowCancel = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.CANCEL);
        // 是否允许被确认
        this.allowConfirm = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.CONFIRM);
        // 是否允许被支付
        this.allowPay = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.PAY);
        // 是否允许被发货
        this.allowShip = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.SHIP)
                && !ServiceStatusEnum.APPLY.name().equals(serviceStatus.name())
                && !ServiceStatusEnum.PASS.name().equals(serviceStatus.name());
        // 是否允许被收货
        this.allowRog = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.ROG);
        // 是否允许被完成
        this.allowComplete = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.COMPLETE);

        boolean defaultServiceStatus = ServiceStatusEnum.NOT_APPLY.value().equals(serviceStatus.value());
    }


    public OrderOperateAllowable(
            OrderStatusEnum orderStatus,
            CommentStatusEnum commentStatus,
            ShipStatusEnum shipStatus,
            ServiceStatusEnum serviceStatus,
            PayStatusEnum payStatus) {

        // 是否允许被取消
        this.allowCancel = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.CANCEL);
        // 是否允许被确认
        this.allowConfirm = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.CONFIRM);
        // 是否允许被支付
        this.allowPay = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.PAY);
        // 是否允许被发货
        this.allowShip = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.SHIP)
                && !ServiceStatusEnum.APPLY.name().equals(serviceStatus.name())
                && !ServiceStatusEnum.PASS.name().equals(serviceStatus.name());
        // 是否允许被收货
        this.allowRog = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.ROG);
        //是否允许被评论
        this.allowComment = CommentStatusEnum.UNFINISHED.value().equals(commentStatus.value())&& ShipStatusEnum.SHIP_ROG.value().equals(shipStatus.value());
        // 是否允许被完成
        this.allowComplete = OrderOperateChecker.checkAllowable(orderStatus, OrderOperateEnum.COMPLETE);

        boolean defaultServiceStatus = ServiceStatusEnum.NOT_APPLY.value().equals(serviceStatus.value());

        //是否允许被申请售后 = 已付款 && 未申请过售后 && 订单是已收货状态
        allowApplyService = PayStatusEnum.PAY_YES.value().equals(payStatus.value())
                && defaultServiceStatus
                && ShipStatusEnum.SHIP_ROG.value().equals(shipStatus.value());

        //订单是否允许取消(售后) = 支付状态已付款  &&  订单已付款状态
        this.allowServiceCancel = PayStatusEnum.PAY_YES.value().equals(payStatus.value())
                && OrderStatusEnum.PAID_OFF.value().equals(orderStatus.value())
                && (ServiceStatusEnum.NOT_APPLY.name().equals(serviceStatus.name())
                ||ServiceStatusEnum.EXPIRED.name().equals(serviceStatus.name()));
    }

}
