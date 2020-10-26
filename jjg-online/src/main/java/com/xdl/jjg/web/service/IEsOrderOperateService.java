package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.dto.EsDeliveryDTO;
import com.jjg.trade.model.enums.OrderPermission;
import com.jjg.trade.model.enums.ServiceStatusEnum;
import com.jjg.trade.model.vo.CancelVO;
import com.jjg.trade.model.vo.CompleteVO;
import com.jjg.trade.model.vo.RogVO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 订单流程操作
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
public interface IEsOrderOperateService {


//    /**
//     * 确认订单
//     *
//     * @param confirmVO  订单确认vo
//     * @param permission 需要检测的订单权限
//     */
//    void confirm(ConfirmVO confirmVO, OrderPermission permission);
//
    /**
     * 为某订单付款<br/>
     *
     * @param orderSn    订单号
     * @param payPrice   本次付款金额
     * @param orderPermission 需要检测的订单权限
     * @return
     * @throws IllegalArgumentException 下列情形之一抛出此异常:
     *                                  <li>order_sn(订单id)为null</li>
     * @throws IllegalStateException    如果订单支付状态为已支付
     */
    DubboResult<EsOrderDO> payOrder(String orderSn, Double payPrice, OrderPermission orderPermission);
//
//
    /**
     * 发货
     *
     */
    DubboResult<EsOrderDO> ship(EsDeliveryDTO deliveryDTO, OrderPermission orderPermission);


    /**
     * 订单收货
     *
     * @param rogVO      收货VO
     */
    DubboResult<EsOrderDO>  rog(RogVO rogVO, OrderPermission orderPermission);


    /**
     * 订单取消
     */
    DubboResult<EsOrderDO> cancelOrder(CancelVO cancelVO, OrderPermission orderPermission);

    /**
     * 订单完成
     *
     * @param completeVO 订单完成vo
     */
    DubboResult<EsOrderDO> complete(CompleteVO completeVO, OrderPermission orderPermission);

    /**
     * 更新订单的售后状态
     *
     * @param orderSn
     * @param serviceStatus
     */
    DubboResult<EsOrderDO> updateServiceStatus(String orderSn, ServiceStatusEnum serviceStatus);
//
//
//    /**
//     * 修改收货人信息
//     *
//     * @param orderConsignee
//     * @return
//     */
//    OrderConsigneeVO updateOrderConsignee(OrderConsigneeVO orderConsignee);
//
//    /**
//     * 修改订单价格
//     *
//     * @param orderSn
//     * @param orderPrice
//     */
//    void updateOrderPrice(String orderSn, Double orderPrice);
//
//    /**
//     * 更新订单的评论状态
//     *
//     * @param orderSn
//     * @param commentStatus
//     */
//    void updateCommentStatus(String orderSn, CommentStatusEnum commentStatus);
//
//    /**
//     * 更新订单项快照ID
//     *
//     * @param itemsJson
//     * @param orderSn
//     * @return
//     */
//    void updateItemJson(String itemsJson, String orderSn);
//
//    /**
//     * 更新订单的订单状态
//     *
//     * @param orderSn
//     * @param orderStatus
//     */
//    void updateOrderStatus(String orderSn, OrderStatusEnum orderStatus);
//
//    /**
//     * 更新交易状态
//     *
//     * @param sn          交易sn
//     * @param orderStatus 状态
//     */
//    void updateTradeStatus(String sn, OrderStatusEnum orderStatus);
//
//    /**
//     * 执行操作
//     *
//     * @param orderSn      订单编号
//     * @param permission   权限
//     * @param orderOperate 要执行什么操作
//     * @param paramVO      参数对象
//     */
//    void executeOperate(String orderSn, OrderPermission permission, OrderOperateEnum orderOperate, Object paramVO);

}
