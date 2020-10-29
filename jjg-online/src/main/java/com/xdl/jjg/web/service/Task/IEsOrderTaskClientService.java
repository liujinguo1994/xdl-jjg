package com.xdl.jjg.web.service.Task;

/**
 * 订单任务
 * @author Snow create in 2018/7/13
 * @version v2.0
 * @since v7.0.0
 */
public interface IEsOrderTaskClientService {

    /**
     * 新订单未付款，自动变更：自动取消
     */
    void cancelTask1();

    void cancelTask();

    /**
     * 发货之后，自动变更：确认收货
     */
    void rogTask();

    /**
     * 确认收货后，自动变更：完成
     */
    void completeTask();

    /**
     * 货到付款订单，自动变更：已付款
     */
    void payTask();

    /**
     * 订单完成后，没有申请过售后，自动变更：售后超时
     */
    void serviceTask();

    /**
     * 订单完成后，多少天后，评论自动变更：好评。
     */
    void commentTask();

}
