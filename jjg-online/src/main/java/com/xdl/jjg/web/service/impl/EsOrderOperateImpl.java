package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.member.model.enums.ConsumeEnumType;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsOrderItemsDO;
import com.jjg.trade.model.dto.EsDeliveryDTO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.enums.*;
import com.jjg.trade.model.vo.CancelVO;
import com.jjg.trade.model.vo.CompleteVO;
import com.jjg.trade.model.vo.RogVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsOrder;
import com.xdl.jjg.entity.EsOrderItems;
import com.xdl.jjg.entity.EsOrderLog;
import com.xdl.jjg.entity.EsTrade;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.message.OrderStatusChangeMsg;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsOrderItemsService;
import com.xdl.jjg.web.service.IEsOrderOperateService;
import com.xdl.jjg.web.service.IEsSupplierBillDetailService;
import com.xdl.jjg.web.service.IEsTradeService;
import com.xdl.jjg.web.service.feign.member.CompanyService;
import com.xdl.jjg.web.service.feign.member.MemberActiveInfoService;
import com.xdl.jjg.web.service.feign.member.MemberDepositService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import com.xdl.jjg.web.service.feign.system.SettingsService;
import com.xdl.jjg.web.service.support.OrderOperateChecker;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单流程操作
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsOrderOperateService.class,timeout = 5000)
public class EsOrderOperateImpl implements IEsOrderOperateService {
    private static Logger logger = LoggerFactory.getLogger(EsOrderOperateImpl.class);
    @Autowired
    private EsOrderMapper esOrderMapper;

    @Autowired
    private EsTradeMapper esTradeMapper;

    @Autowired
    private EsOrderLogMapper esOrderLogMapper;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private MemberActiveInfoService iEsMemberActiveInfoService;

    @Autowired
    private MemberDepositService iEsMemberDepositService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SettingsService iEsSettingsService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private IEsTradeService iEsTradeService;

    @Autowired
    private EsBillDetailMapper esBillDetailMapper;

    @Autowired
    private IEsSupplierBillDetailService iEsSupplierBillDetailService;

    @Autowired
    private IEsOrderItemsService iEsOrderItemsService;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> payOrder(String orderSn, Double payPrice, OrderPermission orderPermission){
        try {
            DubboResult dubboResult = this.executeOperate(orderSn, orderPermission, payPrice, OrderOperateEnum.PAY);
            if (!dubboResult.isSuccess()){
                throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.ORDER_SHIP_ERROR.getErrorCode(),TradeErrorCode.ORDER_SHIP_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> ship(EsDeliveryDTO deliveryDTO, OrderPermission orderPermission) {
        try {
            String orderSn = deliveryDTO.getOrderSn();

            this.executeOperate(orderSn,orderPermission,deliveryDTO, OrderOperateEnum.SHIP);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.ORDER_SHIP_ERROR.getErrorCode(),TradeErrorCode.ORDER_SHIP_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO>  rog(RogVO rogVO, OrderPermission orderPermission) {
        try {
            String orderSn = rogVO.getOrderSn();

            this.executeOperate(orderSn,orderPermission,rogVO, OrderOperateEnum.ROG);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.ORDER_ROG_ERROR.getErrorCode(),TradeErrorCode.ORDER_ROG_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> cancelOrder(CancelVO cancelVO, OrderPermission orderPermission) {
        try {
            String orderSn = cancelVO.getOrderSn();

            this.executeOperate(orderSn,orderPermission,cancelVO, OrderOperateEnum.CANCEL);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.ORDER_CANCEL_ERROR.getErrorCode(),TradeErrorCode.ORDER_CANCEL_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> complete(CompleteVO completeVO, OrderPermission orderPermission) {
        String orderSn = completeVO.getOrderSn();
        try {
            this.executeOperate(orderSn,orderPermission,completeVO,OrderOperateEnum.COMPLETE);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            e.printStackTrace();
        }
        return DubboResult.success();
    }

    /**
     * 更新订单的售后状态
     *
     * @param orderSn
     * @param serviceStatus
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateServiceStatus(String orderSn, ServiceStatusEnum serviceStatus) {
        try {
            QueryWrapper<EsOrder> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
            EsOrder esOrder = this.esOrderMapper.selectOne(wrapper);
            if (esOrder == null){
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            esOrder.setServiceState(serviceStatus.value());
            this.esOrderMapper.updateById(esOrder);
            // 更新 订单明细表 售后状态
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn, orderSn);
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(esOrderItems)){
                esOrderItems.forEach(orderItems -> {
                    orderItems.setState(serviceStatus.value());
                    this.esOrderItemsMapper.updateById(orderItems);
                });
            }
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("更改订单售后状态失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.ORDER_SERVICE_STATUS_CHANGE_ERROR.getErrorCode(), TradeErrorCode.ORDER_SERVICE_STATUS_CHANGE_ERROR.getErrorMsg());
        } catch (Throwable th) {
            logger.error("更改订单售后状态失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 执行操作
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult executeOperate(String orderSn,OrderPermission permission,Object paramVO, OrderOperateEnum orderOperate){

        try {
            //当前时间
            long currentTimeMillis = System.currentTimeMillis();

            // 获取此订单
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();

            queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
            EsOrder esOrder = new EsOrder();
            EsTrade esTrade = null;
            EsOrderDO esOrderDO = new EsOrderDO();
            if ("CANCEL".equals(orderOperate.name())) {
                QueryWrapper<EsTrade> tradeWrapper = new QueryWrapper<>();
                // 获取此订单
                if(permission.name().equals(OrderPermission.seller.name())){
                    esOrder = this.esOrderMapper.selectOne(queryWrapper);
                    orderSn = esOrder.getTradeSn();
                }
                tradeWrapper.lambda().eq(EsTrade::getTradeSn,orderSn);
                esTrade = this.esTradeMapper.selectOne(tradeWrapper);

                esOrder.setOrderState(esTrade.getTradeStatus());
                esOrder.setPaymentType(esTrade.getPaymentType());
            }else {
                esOrder = this.esOrderMapper.selectOne(queryWrapper);
                BeanUtils.copyProperties(esOrder,esOrderDO);
            }
            DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
            List<EsOrderItemsDO> esOrderItemsDOList = result.getData().getList();
            esOrderDO.setEsOrderItemsDO(esOrderItemsDOList);


            EsOrderDTO esOrderDTO = new EsOrderDTO();
            // 订单金额
            BigDecimal price = new BigDecimal("0");
            //2、验证此订单可进行的操作
            this.checkAllowable(esOrder, orderOperate);
            //要变更的订单状态
            OrderStatusEnum newStatus = null;
            //日志信息
            String logMessage = "操作信息";
            String operator = "系统默认";
            boolean flag = true;
            switch (orderOperate) {
                case CANCEL:
                    CancelVO cancelVO = (CancelVO) paramVO;
                    logMessage = "取消订单";
                    newStatus = OrderStatusEnum.CANCELLED;
                    operator = cancelVO.getOperator();
                    // 取消订单
                    if (esTrade.getTradeStatus().equals(OrderStatusEnum.CONFIRM.value())){
                        // 通过交易单号获取  下单是对整个交易单进行下单，所以取消订单需要针对这个交易单进行取消
                        QueryWrapper<EsOrder> cancelWrapper = new QueryWrapper<>();

                        cancelWrapper.lambda().eq(EsOrder::getTradeSn,orderSn);
                        List<EsOrder> orders = this.esOrderMapper.selectList(cancelWrapper);
                        Double balance = 0.0;
                        for (EsOrder esOrder1 : orders) {// 取消订单
                            if (esOrder1.getOrderState().equals(OrderStatusEnum.CONFIRM.value())) {
                                esOrder1.setOrderState(OrderStatusEnum.CANCELLED.value());
                                esOrder1.setCancelReason(cancelVO.getReason());
                                esOrder1.setCancelTime(currentTimeMillis);
                                this.esOrderMapper.updateById(esOrder1);
                                logger.info("前台取消");
                                balance = balance + esOrder1.getPayedMoney();
                            }

                        }
                        // 如果使用余额支付 取消订单的情况是会员可能使用余额支付，但是存在第三方支付（未支付），订单状态还是待付款。取消订单 只退回所用的余额
                        if (balance > 0){
                            iEsTradeService.setMemberBalance(esOrder.getTradeSn(),esOrder.getOrderSn(),esOrder.getMemberId(),
                                    balance, ConsumeEnumType.REFUND.value());
                            logger.info("退款成功");
                        }
                    }
                    // 修改交易单的订单状态
                    esTrade.setTradeStatus(OrderStatusEnum.CANCELLED.value());
                    esTrade.setCancelTime(currentTimeMillis);
                    this.esTradeMapper.updateById(esTrade);
                    break;
                case PAY:
                    logMessage = "支付订单";
                    newStatus = OrderStatusEnum.PAID_OFF;
                    Double payPrice = (Double) paramVO;

                    switch (permission) {
                        case buyer:
                            operator = esOrder.getMemberName();
                            break;

                        case client:
                            break;

                        case seller:
                            operator = esOrder.getShipName();
                            break;

                        case admin:
    //                        operator = AdminUserContext.getAdmin().getUsername();
                            break;
                        default:
                            break;
                    }

                    //款到发货订单 卖家不能确认收款
                    if (permission.equals(OrderPermission.seller) && esOrder.getPaymentType().equals(PaymentTypeEnum.ONLINE.name())) {
                        throw new ArgumentException(TradeErrorCode.UNAUTHORIZED_OPERATION_ORDER.getErrorCode(),
                                TradeErrorCode.UNAUTHORIZED_OPERATION_ORDER.getErrorMsg());
                    }

                    // 更新订单表支付信息
                    esOrder.setOrderState(OrderStatusEnum.PAID_OFF.value());
                    esOrder.setPayState(PayStatusEnum.PAY_YES.value());
                    esOrder.setPaymentTime(currentTimeMillis);
                    esOrder.setUpdateTime(currentTimeMillis);
                    this.esOrderMapper.updateById(esOrder);
                    // 更新交易表支付信息
                    this.esTradeMapper.updateTradeStatus(TradeStatusEnum.PAID_OFF.value(),esOrder.getTradeSn());


                    esOrder.setOrderState(OrderStatusEnum.PAID_OFF.name());
                    esOrder.setPayState(PayStatusEnum.PAY_YES.value());
                    esOrder.setPaymentTime(currentTimeMillis);

                    break;
                case SHIP:
                    //检测订单是否已经申请售后
                    if (ServiceStatusEnum.APPLY.value().equals(esOrder.getServiceState())
                            || ServiceStatusEnum.PASS.name().equals(esOrder.getServiceState())){
                        logger.error("已经申请售后 不能发货");
                        throw new ArgumentException(TradeErrorCode.ORDER_CAN_NOT_SHIP_ERROR.getErrorCode(),TradeErrorCode.ORDER_CAN_NOT_SHIP_ERROR.getErrorMsg());
                    }
                    EsDeliveryDTO deliveryVO = (EsDeliveryDTO) paramVO;
                    List<Integer> goodsIdList = deliveryVO.getGoodsId();
                    for (Integer id : goodsIdList) {
                        // 查看订单中的该商品时候存在申请售后的情况
                        QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
                        wrapper.lambda().eq(EsOrderItems::getGoodsId,id).eq(EsOrderItems::getOrderSn,orderSn);
                        List<EsOrderItems> orderItemsList = this.esOrderItemsMapper.selectList(wrapper);
                        for(EsOrderItems esOrderItems:orderItemsList){
                            if (ServiceStatusEnum.APPLY.value().equals(esOrderItems.getState())
                                    || ServiceStatusEnum.PASS.name().equals(esOrderItems.getState())){
                                throw new ArgumentException(TradeErrorCode.ORDER_CAN_NOT_SHIP_ERROR.getErrorCode(),TradeErrorCode.ORDER_CAN_NOT_SHIP_ERROR.getErrorMsg());
                            }else {
                                if(esOrder.getLfcId() ==null){
                                    String itemsJson = esOrder.getItemsJson();
                                    List<EsOrderItems> esOrderItems1 = JsonUtil.jsonToList(itemsJson, EsOrderItems.class);
                                    for (EsOrderItems ok : esOrderItems1) {
                                        if (ok.getGoodsId().equals(id)) {
                                            ok.setHasShip(1);
                                        }
                                    }
                                    String json2 = JsonUtil.objectToJson(esOrderItems1);
                                    esOrder.setItemsJson(json2);
                                }
                                this.esOrderMapper.updateById(esOrder);
                                //更新物流信息
                                esOrderItems.setLogiId(deliveryVO.getLogiId());
                                esOrderItems.setLogiName(deliveryVO.getLogiName());
                                esOrderItems.setShipNo(deliveryVO.getDeliveryNo());
                                esOrderItems.setHasShip(1);
                                this.esOrderItemsMapper.updateById(esOrderItems);
                            }
                        }
                    }
                    // 判断订单明细中的发货状态，
                    QueryWrapper<EsOrderItems> wrapper2 = new QueryWrapper<>();
                    wrapper2.lambda().eq(EsOrderItems::getOrderSn,orderSn);
                    List<EsOrderItems> esOrderItems1 = this.esOrderItemsMapper.selectList(wrapper2);
                    int i = 0;
                    for (EsOrderItems ot : esOrderItems1) {
                        if (ot.getHasShip() == 1 || ot.getHasShip() ==3) {
                            i++;
                        }
                    }
                    if (i == esOrderItems1.size()){
                        logMessage = "订单发货";
                        newStatus = OrderStatusEnum.SHIPPED;
                        operator = deliveryVO.getOperator();
                        esOrder.setOrderState(OrderStatusEnum.SHIPPED.value());
                        esOrder.setShipState(ShipStatusEnum.SHIP_YES.value());
                        esOrder.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                        esOrder.setShipTime(currentTimeMillis);
                        this.esOrderMapper.updateById(esOrder);
                        break;
                    } else {
                        flag = false;
                        break;
                    }

                case ROG:
                    RogVO rogVO = (RogVO) paramVO;
                    logMessage = "确认收货";
                    newStatus = OrderStatusEnum.ROG;
                    operator = rogVO.getOperator();
                    //订单售后状态修改
                    String orderServiceStatus = ServiceStatusEnum.NOT_APPLY.value();
                    esOrder.setOrderState(OrderStatusEnum.ROG.value());
                    esOrder.setShipState(ShipStatusEnum.SHIP_ROG.value());
                    esOrder.setServiceState(orderServiceStatus);
                    esOrder.setSigningTime(currentTimeMillis);
                    this.esOrderMapper.updateById(esOrder);
                    //修改订单商品项的售后状态
                    esOrderItemsDOList.forEach(esOrderItemsDO -> {
                        EsOrderItems esOrderItems = new EsOrderItems();
                        BeanUtils.copyProperties(esOrderItemsDO,esOrderItems);
                        this.esOrderItemsMapper.updateById(esOrderItems);
                    });
                    break;
                case COMPLETE:

                    CompleteVO completeVO = (CompleteVO) paramVO;
                    logMessage = "订单已完成";
                    newStatus = OrderStatusEnum.COMPLETE;
                    operator = completeVO.getOperator();
                    esOrder.setCompleteTime(currentTimeMillis);
                    esOrder.setOrderState(OrderStatusEnum.COMPLETE.value());
                    this.esOrderMapper.updateById(esOrder);
                    // 生成结算单
                    iEsSupplierBillDetailService.generateBill(esOrderDO);
                    
                    logger.info("订单完成操作成功");
                    break;
                default:
                    break;
            }
            //部分发货不走此方法，订单状态不更改
            if (flag == true) {
                esOrder.setUpdateTime(System.currentTimeMillis());
                BeanUtils.copyProperties(esOrder,esOrderDTO);

                OrderStatusChangeMsg message = new OrderStatusChangeMsg();
                message.setOldStatus(OrderStatusEnum.valueOf(esOrderDO.getOrderState()));
                message.setNewStatus(newStatus);
                message.setEsOrderDTO(esOrderDTO);

                /**
                 * 发送订单状态变化消息
                 */
                iEsTradeService.sendOrderCreateMessage(message);

                price = price.compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(esOrder.getPayedMoney().toString()) : price;

                // 记录日志
                EsOrderLog esOrderLog = new EsOrderLog();
                esOrderLog.setOrderSn(esOrderDTO.getOrderSn());
                esOrderLog.setMessage(logMessage);
                esOrderLog.setOpId(esOrderDTO.getMemberId());
                esOrderLog.setOpName(operator);
                esOrderLog.setOpTime(System.currentTimeMillis());
                esOrderLog.setMoney(price.doubleValue());
                this.esOrderLogMapper.insert(esOrderLog);
                logger.info("添加订单日志信息");
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }
    }

    /**
     * 进行可操作校验
     * 看此状态下是否允许此操作
     * @param order
     * @param orderOperate
     */
    private void checkAllowable(EsOrder order, OrderOperateEnum orderOperate) {
        //如果是client权限，则不验证下一步操作
//        if (OrderPermission.client.equals(permission)) {
//            return;
//        }

        OrderStatusEnum status = OrderStatusEnum.valueOf(order.getOrderState());

        PaymentTypeEnum paymentType = PaymentTypeEnum.valueOf(order.getPaymentType());
        boolean isAllowble = OrderOperateChecker.checkAllowable(paymentType, status, orderOperate);


        if (!isAllowble) {
            throw new ArgumentException(TradeErrorCode.ORDER_CANCEL_ERROR.getErrorCode(),"订单" + status.description() + "状态不能进行" + orderOperate.description() + "操作");
        }

    }

}
