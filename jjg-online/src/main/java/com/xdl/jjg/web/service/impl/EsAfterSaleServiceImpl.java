package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.enums.ConsumeEnumType;
import com.jjg.shop.model.domain.EsCategoryDO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.*;
import com.jjg.trade.model.enums.*;
import com.jjg.trade.model.vo.RefundApplyVO;
import com.jjg.trade.orderCheck.OrderOperateAllowable;
import com.xdl.jjg.constant.RefundOperateChecker;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.*;
import com.xdl.jjg.web.service.feign.shop.CategoryService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 售后维护配置 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service
public class EsAfterSaleServiceImpl extends ServiceImpl<EsAfterSaleMapper, EsAfterSale> implements IEsAfterSaleService {

    private static Logger logger = LoggerFactory.getLogger(EsAfterSaleServiceImpl.class);

    @Autowired
    private EsAfterSaleMapper afterSaleMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IEsOrderService esOrderService;

    @Autowired
    private IEsTradeService esTradeService;

    @Autowired
    private IEsRefundService esRefundService;

    @Autowired
    private IEsOrderItemsService esOrderItemsService;

    @Autowired
    private IEsPaymentMethodService esPaymentMethodService;

    @Autowired
    private IPayService iPayService;

    @Autowired
    private EsRefundGoodsMapper esRefundGoodsMapper;

    @Autowired
    private EsRefundLogMapper esRefundLogMapper;

    @Autowired
    private EsRefundMapper esRefundMapper;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private EsOrderMapper esOrderMapper;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsServiceOrderDO> applyRefund(BuyerRefundApplyDTO refundApply) {
        try {
            EsRefundDO esRefundDO = this.innerRefund(refundApply);
            String detail = "";
            if(RefundTypeEnum.RETURN_MONEY.value().equals(refundApply.getRefundType())){
                detail = "申请退款";
            }if(RefundTypeEnum.RETURN_GOODS.value().equals(refundApply.getRefundType())){
                detail = "申请退货退款";
            }else if(RefundTypeEnum.CHANGE_GOODS.value().equals(refundApply.getRefundType())){
                detail = "申请换货";
            }else if(RefundTypeEnum.REPAIR_GOODS.value().equals(refundApply.getRefundType())){
                detail = "申请维修";
            }
            this.log(esRefundDO.getSn(), esRefundDO.getMemberName(), detail);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("申请售后失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Exception e) {
            logger.error("申请售后失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsServiceOrderDO> cancelOrder(BuyerRefundApplyDTO refundApplyDTO) {
        try {
            // 订单编号
            String orderSn = refundApplyDTO.getOrderSn();
            // 会员id
            Long memberId = refundApplyDTO.getMemberId();
            // 获取订单信息对订单进行属主校验
            DubboResult<EsOrderDO> orderInfoAfterSale = this.esOrderService.getEsBuyerOrderInfoAfterSale(orderSn);
            if (!orderInfoAfterSale.isSuccess()){
                throw new ArgumentException(orderInfoAfterSale.getCode(),orderInfoAfterSale.getMsg());
            }
            EsOrderDO esOrderDO = orderInfoAfterSale.getData();
            // 总运费
            Double shippingPrice = esOrderDO.getShippingMoney();

            //不存在的订单或者不属于当前会员的订单进行校验
            if (esOrderDO == null || !memberId.equals(esOrderDO.getMemberId())) {
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            if (!OrderStatusEnum.PAID_OFF.value().equals(esOrderDO.getOrderState()) && !PayStatusEnum.PAY_YES.value().equals(esOrderDO.getPayState())) {
                throw new ArgumentException(TradeErrorCode.NON_CANCELLING.getErrorCode(), TradeErrorCode.NON_CANCELLING.getErrorMsg());
            }
            //订单操作校验
            OrderOperateAllowable orderOperateAllowableVO = esOrderDO.getOrderOperateAllowable();
            if (!orderOperateAllowableVO.getAllowServiceCancel()) {
                throw new ArgumentException(TradeErrorCode.NON_CANCELLING.getErrorCode(), "操作不允许");
            }
            //单品退款
            // 查询退款商品
            List<EsBuyerOrderItemsDO> orderSkuList = getOrderSkuList(esOrderDO.getEsBuyerOrderItemsDO(), refundApplyDTO.getSkuIdList());
            //查询生鲜的分类

            DubboResult<EsCategoryDO> categoryDO = categoryService.getFirstByName("生鲜");
            EsCategoryDO category = categoryDO.getData();
            List<Long> cateIds = new ArrayList<>();
            DubboPageResult<EsCategoryDO> categoryChildren = categoryService.getCategoryChildren(category.getId());
            List<EsCategoryDO> esCategoryDOS = categoryChildren.getData().getList();
            for (EsCategoryDO ca : esCategoryDOS) {
                DubboPageResult<EsCategoryDO> categoryChildren1 =  categoryService.getCategoryChildren(ca.getId());
                List<EsCategoryDO> esCategoryList = categoryChildren1.getData().getList();
                for (EsCategoryDO cate : esCategoryList) {
                    cateIds.add(cate.getId());
                }
            }
            //更改订单items
            // 获取订单项
            DubboPageResult<EsOrderItemsDO> esOrderItems = this.esOrderItemsService.getEsOrderItemsByOrderSnNoZP(orderSn);
            if (!esOrderItems.isSuccess()){
                throw new ArgumentException(esOrderItems.getCode(),esOrderItems.getMsg());
            }
            List<EsOrderItemsDO> esOrderItemsByOrderSn = esOrderItems.getData().getList();
            int sumFresh=0;//生鲜总数
            int agreeFresh=0;//生鲜同意申请
            int agreeComm=0;//普通同意申请

            List<EsOrderItemsDO> listSkT=null;
            //订单里的详情
            if (esOrderDO != null) {
                listSkT = esOrderItemsByOrderSn;
                for (EsOrderItemsDO sku : listSkT) {

                    //根据skuId找到商品，更改状态为待审核
                    for (EsBuyerOrderItemsDO okd : orderSkuList) {
                        if (sku.getSkuId().equals(okd.getSkuId())) {
                            sku.setState(ServiceStatusEnum.APPLY.name());
                        }
                    }
                    if (cateIds.contains(sku.getCategoryId())){
                        //生鲜数量+1
                        sumFresh++;
                        if (sku.getState().equals(ServiceStatusEnum.PASS.value()) || sku.getState().equals(ServiceStatusEnum.COMPLETED.value())){
                            //统计申通通过的生鲜数量
                            agreeFresh++;
                        }
                    }else{
                        if (sku.getState().equals(ServiceStatusEnum.PASS.value()) || sku.getState().equals(ServiceStatusEnum.COMPLETED.value())){
                            //统计申通通过的普通商品数量
                            agreeComm++;
                        }
                    }
                    //不发货
                    sku.setHasShip(3);
                    EsOrderItemsDTO esOrderItemsDTO = new EsOrderItemsDTO();
                    BeanUtils.copyProperties(sku,esOrderItemsDTO);
                    // 修改订单项的售后（订单单品取消）状态
                    this.esOrderItemsService.updateOrderItemsMessage(esOrderItemsDTO);
                }
                String json2 = JsonUtil.objectToJson(listSkT);
                esOrderDO.setItemsJson(json2);
                EsOrderDTO esOrderDTO = new EsOrderDTO();
                BeanUtils.copyProperties(esOrderDO,esOrderDTO);
                this.esOrderService.updateOrderMessage(esOrderDTO);

                //根据当前时间生成退款单号
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyMMddhhmmss");
                String refundSn = df.format(date);

                // 计算总共退款金额
                Double refundPrice = 0.0;
                Double tot = 0.0;
                for (EsBuyerOrderItemsDO orderSkuDO : orderSkuList) {
                    // 判断是否重复提交
//                    if (checkRefund(orderSn, orderSkuDO.getGoodsId())) {
////                        throw new ArgumentException(TradeErrorCode.APPLICATION_SUBMITTED.getErrorCode(), "申请已提交");
////                    }
                    refundPrice = MathUtil.add(refundPrice,MathUtil.multiply(orderSkuDO.getMoney(),orderSkuDO.getNum()));
                    //单品退款退款价格大于小计，则按小计生成退款单
                    Double sub = MathUtil.multiply(orderSkuDO.getMoney() , orderSkuDO.getNum());
                    logger.info("单品退款退款1"+ sub);
                    tot = tot + sub;
                    if (refundPrice.compareTo(tot) > 0) {
                        refundPrice = tot;
                    }
                    EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                    BeanUtils.copyProperties(orderSkuDO,orderItemsDO);
                    this.refundGoods(orderItemsDO, orderItemsDO.getNum(), refundSn);
                }

                //退运费计算
                if (shippingPrice>0){
                    Double commonFreightPrice = esOrderDO.getCommonFreightPrice();
                    logger.info("普通运费"+ commonFreightPrice);
                    Double freshFreightPrice = esOrderDO.getFreshFreightPrice();
                    if (freshFreightPrice>0){
                        int refundFresh=0;//退款生鲜个数
                        for (EsBuyerOrderItemsDO orderSkuDTO : orderSkuList) {
                            if (cateIds.contains(orderSkuDTO.getCategoryId())){
                                refundFresh ++;
                            }
                        }
                        //生鲜退款+待审核和通过的=购买生鲜总数
                        if (refundFresh+agreeFresh == sumFresh && esOrderDO.getHasFresh()==0){
                            refundPrice = MathUtil.add(refundPrice,freshFreightPrice);
                            esOrderDO.setHasFresh(1);
                        }
                    }
                    if (commonFreightPrice>0){
                        int refundComm=0;
                        for (EsBuyerOrderItemsDO orderSkuDTO : orderSkuList) {
                            if (!cateIds.contains(orderSkuDTO.getCategoryId())){
                                refundComm ++;
                            }
                        }
                        //普通商品退款+待审核和通过的=购买普通总数
                        if (refundComm+agreeComm == listSkT.size()-sumFresh && esOrderDO.getHasComm()==0){
                            refundPrice = MathUtil.add(refundPrice,commonFreightPrice);
                            esOrderDO.setHasComm(1);
                        }
                    }
                }
                if (esOrderDO.getHasComm()==1 || esOrderDO.getHasFresh()==1){
                    EsOrderDTO esOrderDTO1 = new EsOrderDTO();
                    BeanUtils.copyProperties(esOrderDO,esOrderDTO1);
                    this.esOrderService.updateOrderMessage(esOrderDTO1);
                }
                //拼装退款单
                EsRefund refund = new EsRefund();

                refund.setSn(refundSn);
                refund.setCustomerRemark(refundApplyDTO.getCustomerRemark());
                refund.setUrl(JsonUtil.objectToJson(refundApplyDTO.getUrlList()));
                refund.setProcessStatus(ProcessStatusEnum.TO_BE_PROCESS.value());
                refund.setMemberId(refundApplyDTO.getMemberId());
                refund.setMemberName(refundApplyDTO.getMemberName());
                refund.setShopId(esOrderDO.getShopId());
                refund.setShopName(esOrderDO.getShopName());
                refund.setOrderSn(esOrderDO.getOrderSn());
                refund.setRefundStatus(RefundStatusEnum.APPLY.name());

                refund.setRefundMoney(refundPrice.doubleValue());

                refund.setCreateTime(System.currentTimeMillis());
                refund.setPayOrderNo(esOrderDO.getPayOrderNo());
                refund.setShipName(esOrderDO.getShipName());
                DubboResult<EsTradeDO> esTradeInfo = this.esTradeService.getEsTradeInfo(esOrderDO.getTradeSn());
                if (!esTradeInfo.isSuccess()){
                    throw new ArgumentException(esTradeInfo.getCode(),esTradeInfo.getMsg());
                }
                EsTradeDO data = esTradeInfo.getData();

                //判断当前支付方式是否支持原路退回,如果不支持则退款方式不能为空
                DubboResult<EsPaymentMethodDO> byPluginId = esPaymentMethodService.getByPluginId(data.getPluginId());
                if (!byPluginId.isSuccess()){
                    throw new ArgumentException(byPluginId.getCode(),byPluginId.getMsg());
                }
                EsPaymentMethodDO paymentMethodDO = byPluginId.getData();

                String refundWay = RefundWayEnum.ORIGINAL.name();
                // TODO 以下可能没用
                if (paymentMethodDO == null || paymentMethodDO.getIsRetrace() == 0) {
                    refundWay = RefundWayEnum.OFFLINE.name();
                    if (refundApplyDTO.getAccountType() == null) {
                        throw new ArgumentException(TradeErrorCode.REFUND_METHOD_REQUIRED.getErrorCode(), TradeErrorCode.REFUND_METHOD_REQUIRED.getErrorMsg());
                    } else {
                        //银行转账
                        if (AccountTypeEnum.BANKTRANSFER.value().equals(refundApplyDTO.getAccountType())) {
                            refund.setBankAccountName(refundApplyDTO.getBankAccountName());
                            refund.setBankAccountNumber(refundApplyDTO.getBankAccountNumber());
                            refund.setBankDepositName(refundApplyDTO.getBankDepositName());
                            refund.setBankName(refundApplyDTO.getBankName());
                        } else {
                            //支付宝或者微信
                            refund.setReturnAccount(refundApplyDTO.getReturnAccount());
                        }
                        refund.setAccountType(refundApplyDTO.getAccountType());
                    }
                }
                refund.setRefundWay(refundWay);
                refund.setRefundReason(refundApplyDTO.getRefundReason());
                refund.setRefundType(RefuseTypeEnum.RETURN_MONEY.name());
                refund.setTradeSn(esOrderDO.getTradeSn());
                refund.setPaymentType(PaymentTypeEnum.ONLINE.name());
                refund.setRefuseType(RefundTypeEnum.CANCEL_ORDER.name());
                refund.setPayOrderNo(esOrderDO.getPayOrderNo());
                this.esRefundMapper.insert(refund);

                // 订单退款商品数量 + 已申请退款的商品数量 == 订单商品总数的时候，更新订单状态
                if (getReturnGoodsCount(orderSn) == esOrderItemsByOrderSn.size() ) {
                    // 修改订单表该订单的售后状态
                    EsOrderDTO esOrderDTO1 = new EsOrderDTO();
                    esOrderDTO1.setOrderSn(esOrderDO.getOrderSn());
                    esOrderDTO1.setServiceState(ServiceStatusEnum.APPLY.name());
                    esOrderDTO1.setOrderState(OrderStatusEnum.CANCELLED.name());
                    esOrderDTO1.setApplyRefundTime(System.currentTimeMillis());
                    this.esOrderService.updateOrderMessage(esOrderDTO1);
                }
                String detail = "申请退款";
                this.log(refundSn, refundApplyDTO.getMemberName(), detail);
            }
            return DubboResult.success();
        } catch (ArgumentException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (BeansException be) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    // 退款商品数量
    private int getReturnGoodsCount(String orderSn) {
        return this.esRefundMapper.selectNum(RefundStatusEnum.REFUSE.value()
                ,  ProcessStatusEnum.TO_BE_PROCESS.value(), orderSn);
    }

    private boolean checkRefund(String orderSn, Long goodsId) {

        String shState = RefundStatusEnum.REFUSE.value();
        String tkState = "";
        Integer integer = this.esRefundMapper.selectRefundNum(orderSn, goodsId, shState, tkState);
        return integer > 0;
    }


    /**
     * 查询需要退款的商品
     *
     * @param orderGoodIds 商品编号
     * @return
     * @auther Bamboo
     * @date 2019/03/14 10:17:52
     */
    private List<EsBuyerOrderItemsDO> getOrderSkuList(List<EsBuyerOrderItemsDO> orderSkuDTOList, List<Integer> orderGoodIds) {
        List<EsBuyerOrderItemsDO> list = new ArrayList<>();
        List<Long> longs = BeanUtil.copyList(orderGoodIds, Long.class);
        for (EsBuyerOrderItemsDO orderSkuDTO : orderSkuDTOList) {
            if (longs.contains(orderSkuDTO.getSkuId())) {
                list.add(orderSkuDTO);
            }
        }
        return list;
    }
    /**
     * 记录操作日志
     *
     * @param sn
     * @param operator
     * @param detail
     */
    private void log(String sn, String operator, String detail) {
        EsRefundLog refundLog = new EsRefundLog();
        refundLog.setOperator(operator);
        refundLog.setRefundSn(sn);
        refundLog.setCreateTime(System.currentTimeMillis());
        refundLog.setLogdetail(detail);

        this.esRefundLogMapper.insert(refundLog);

    }

    private EsRefundDO innerRefund(BuyerRefundApplyDTO refundApply) {

        Double refundPrice = 0.0d;

                //获取订单信息，判断订单有效性
        // TODO 该接口需要封装赠品信息
        DubboResult<EsOrderDO> esBuyerOrderInfo = esOrderService.getEsBuyerOrderInfoAfterSale(refundApply.getOrderSn());
        if (!esBuyerOrderInfo.isSuccess()){
            throw new ArgumentException(esBuyerOrderInfo.getCode(), esBuyerOrderInfo.getMsg());
        }
        EsOrderDO orderDO = esBuyerOrderInfo.getData();
        String orderState = orderDO.getOrderState();
        // 判断 维权类型 如果是申请售后
        if (!OrderStatusEnum.ROG.value().equals(orderDO.getOrderState()) && !OrderStatusEnum.PAID_OFF.value().equals(orderDO.getOrderState())
                && !OrderStatusEnum.COMPLETE.value().equals(orderDO.getOrderState())) {
            throw new ArgumentException(TradeErrorCode.ORDER_NOT_RECEIVED.getErrorCode(), TradeErrorCode.ORDER_NOT_RECEIVED.getErrorMsg());
        }

        //如果退款金额为0
        if (orderDO.getNeedPayMoney() == 0 && orderDO.getPayedMoney() == 0) {
            throw new ArgumentException(TradeErrorCode.REFUND_AMOUNT_IS_ZERO.getErrorCode(), TradeErrorCode.REFUND_AMOUNT_IS_ZERO.getErrorMsg());
        }

        //不存在的订单或者不属于当前会员的订单进行校验
        if (orderDO == null || !refundApply.getMemberId().equals(orderDO.getMemberId())) {
            throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
        }

        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EsOrderItems::getSkuId,refundApply.getSkuIdList()).eq(EsOrderItems::getOrderSn,refundApply.getOrderSn());
        List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);

        List<EsOrderItemsDO> esOrderItemsDOList = BeanUtil.copyList(esOrderItems, EsOrderItemsDO.class);
        EsOrderItemsDO orderSkuDO = null;

        //根据当前时间生成退款单号
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyMMddhhmmss");
        String refundSn = df.format(date);

        if (CollectionUtils.isNotEmpty(refundApply.getSkuIdList())) {
            for (EsOrderItemsDO orderSku : esOrderItemsDOList) {
                if (!orderSku.getState().equals(ServiceStatusEnum.NOT_APPLY.name())) {
                    throw new ArgumentException(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
                }

                orderSkuDO = orderSku;
                //退款时价格为货品的总价格，退货时价格为单个货品的价格乘以数量
                if (refundApply.getRefuseType().equals(RefuseTypeEnum.RETURN_MONEY.value())) {
                    refundApply.setReturnNum(orderSku.getNum());
                    refundPrice = refundPrice + orderSku.getMoney();
                } else {
                    //如果没有传递退货数量，则默认为购买数量
                    if (refundApply.getReturnNum() == null || refundApply.getReturnNum() == 0) {
                        refundApply.setReturnNum(orderSku.getNum());
                    }

                    if (orderSku.getNum() < refundApply.getReturnNum()) {
                        throw new ArgumentException(TradeErrorCode.ABNORMAL_QUANTITY_OF_GOODS.getErrorCode(), TradeErrorCode.ABNORMAL_QUANTITY_OF_GOODS.getErrorMsg());
                    }

                    refundPrice = refundPrice + MathUtil.multiply(orderSku.getMoney(), refundApply.getReturnNum());
                }

                //生成退货商品表
                this.refundGoods(orderSku, orderSku.getNum(), refundSn);
                //修改订单项的售后状态为已申请

                orderSku.setState(ServiceStatusEnum.APPLY.name());
                EsOrderItems esOrderItems1 = new EsOrderItems();
                BeanUtils.copyProperties(orderSku,esOrderItems1);
                QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(EsOrderItems::getOrderSn,orderSku.getOrderSn()).eq(EsOrderItems::getSkuId,orderSku.getSkuId());
                this.esOrderItemsMapper.update(esOrderItems1,queryWrapper1);

            }

            // 修改订单表该订单的售后状态
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            esOrderDTO.setOrderSn(orderDO.getOrderSn());
            esOrderDTO.setServiceState(ServiceStatusEnum.APPLY.name());
            esOrderDTO.setOrderState(orderState);
            this.esOrderService.updateOrderMessage(esOrderDTO);

        }

        //退款单入库
        EsRefund refund = new EsRefund();
        refund.setSn(refundSn);
        refund.setOrderSn(refundApply.getOrderSn());
        refund.setCustomerRemark(refundApply.getCustomerRemark());
        refund.setRefundMoney(refundPrice);
        refund.setRefundReason(refundApply.getRefundReason());
        refund.setRefundType(refundApply.getRefundType());
        refund.setPaymentType(orderDO.getPaymentType());
        refund.setPayOrderNo(orderDO.getPayOrderNo());
        refund.setShipName(orderDO.getShipName());
        DubboResult<EsTradeDO> esTradeInfo = this.esTradeService.getEsTradeInfo(orderDO.getTradeSn());
        if (!esTradeInfo.isSuccess()){
            throw new ArgumentException(esTradeInfo.getCode(),esTradeInfo.getMsg());
        }
        EsTradeDO data = esTradeInfo.getData();
        //判断当前支付方式是否支持原路退回,如果不支持则退款方式不能为空
        DubboResult<EsPaymentMethodDO> byPluginId = esPaymentMethodService.getByPluginId(data.getPluginId());
        if (!byPluginId.isSuccess()){
            throw new ArgumentException(byPluginId.getCode(),byPluginId.getMsg());
        }
        EsPaymentMethodDO paymentMethodDO = byPluginId.getData();

        String refundWay = RefundWayEnum.ORIGINAL.name();
        // TODO 以下可能没用
        if (paymentMethodDO == null || paymentMethodDO.getIsRetrace() == 0) {
            refundWay = RefundWayEnum.OFFLINE.name();
            if (refundApply.getAccountType() == null) {
                throw new ArgumentException(TradeErrorCode.REFUND_METHOD_REQUIRED.getErrorCode(), TradeErrorCode.REFUND_METHOD_REQUIRED.getErrorMsg());
            } else {
                //银行转账
                if (AccountTypeEnum.BANKTRANSFER.value().equals(refundApply.getAccountType())) {
                    refund.setBankAccountName(refundApply.getBankAccountName());
                    refund.setBankAccountNumber(refundApply.getBankAccountNumber());
                    refund.setBankDepositName(refundApply.getBankDepositName());
                    refund.setBankName(refundApply.getBankName());
                } else {
                    //支付宝或者微信
                    refund.setReturnAccount(refundApply.getReturnAccount());
                }
                refund.setAccountType(refundApply.getAccountType());
            }
        }
        refund.setMemberId(refundApply.getMemberId());
        refund.setMemberName(refundApply.getMemberName());
        refund.setShopId(orderDO.getShopId());
        refund.setShopName(orderDO.getShopName());
        refund.setTradeSn(orderDO.getTradeSn());
        refund.setPayOrderNo(orderDO.getPayOrderNo());
        refund.setCreateTime(System.currentTimeMillis());
        refund.setRefundStatus(RefundStatusEnum.APPLY.value());
        refund.setRefuseType(refundApply.getRefuseType());
        refund.setRefundWay(refundWay);
        refund.setProcessStatus(ProcessStatusEnum.TO_BE_PROCESS.value());
        refund.setUrl(JsonUtil.objectToJson(refundApply.getUrlList()));
//        refund.setRefundGift(JsonUtil.objectToJson(orderDO.getGiftList()));

        this.esRefundMapper.insert(refund);
        refundApply.setRefundSn(refundSn);

        EsRefundDO esRefundDO = new EsRefundDO();

        BeanUtils.copyProperties(refund,esRefundDO);

        // 发送申请退款的消息
        return esRefundDO;
    }



    /**
     * 退货商品入库
     *
     * @param orderSkuDTO
     * @param num
     * @param refundSn
     */
    private void refundGoods(EsOrderItemsDO orderSkuDTO, Integer num, String refundSn) {
        // 像退货商品表插入数据
        EsRefundGoods refundGoods = new EsRefundGoods(orderSkuDTO);
        refundGoods.setReturnNum(num);
        // 实际支付的商品单价
        refundGoods.setMoney(orderSkuDTO.getMoney());
        refundGoods.setRefundSn(refundSn);
        refundGoods.setStorageNum(num);
        this.esRefundGoodsMapper.insert(refundGoods);
    }

    @Override
    public RefundApplyVO refundApply(String orderSn, Integer skuId) {
        RefundApplyVO refundApplyVO = new RefundApplyVO();
        // 读取订单详情
        DubboResult<EsOrderDO> esBuyerOrderInfo = esOrderService.getEsBuyerOrderInfoAfterSale(orderSn);
        if (!esBuyerOrderInfo.isSuccess()){
            throw new ArgumentException(esBuyerOrderInfo.getCode(), esBuyerOrderInfo.getMsg());
        }
        EsOrderDO esOrderDO = esBuyerOrderInfo.getData();
        // 验证用户信息
//        if (!esOrderDO.getMemberId().equals(UserContext.getBuyer().getUid())) {
//            throw new ServiceException(AftersaleErrorCode.E604.name(), "订单不存在");
//        }

        DubboResult<EsPaymentMethodDO> byPluginId = esPaymentMethodService.getByPluginId(esOrderDO.getPaymentMethodId().toString());
        if (!byPluginId.isSuccess()){
            throw new ArgumentException(byPluginId.getCode(), byPluginId.getMsg());
        }
        EsPaymentMethodDO paymentMethodDO = byPluginId.getData();
        //判断是否支持原路退回 (余额支付判断 已付金额 是否大于0)
        if (new BigDecimal(esOrderDO.getPayedMoney().toString()).compareTo(BigDecimal.ZERO) > 0
                || (paymentMethodDO != null && paymentMethodDO.getIsRetrace() == 1)) {
            refundApplyVO.setOriginalWay("yes");
        } else {
            refundApplyVO.setOriginalWay("no");
        }

        refundApplyVO.setReturnPoint(0);
//        /** add libw 2019/03/15 添加单品退款功能 start **/
//        List<OrderSkuDTO> orderDetail = getOrderSkuList(order.getOrderSkuList(), orderSn);
//        order.setOrderSkuList(orderDetail);
//        /** add libw 2019/03/15 添加单品退款功能 end **/
        //如果不传skuid则为整单申请 退款金额为订单支付金额
        if (skuId == null) {
            refundApplyVO.setReturnMoney(esOrderDO.getNeedPayMoney());
            refundApplyVO.setOrder(esOrderDO);
            refundApplyVO.setSkuList(esOrderDO.getEsBuyerOrderItemsDO());
        } else {
            //如果skuid不为空则为申请售后，退款金额为货品金额，退货列表只存入当前货品
            List<EsBuyerOrderItemsDO> list = new ArrayList<>();
            List<EsBuyerOrderItemsDO> orderSkuList = esOrderDO.getEsBuyerOrderItemsDO();
            for (EsBuyerOrderItemsDO orderSkuDTO : orderSkuList) {
                if (orderSkuDTO.getSkuId().equals(skuId)) {
                    refundApplyVO.setReturnMoney(orderSkuDTO.getMoney());
                    list.add(orderSkuDTO);
                }
            }
            refundApplyVO.setOrder(esOrderDO);
            refundApplyVO.setSkuList(list);
        }
        return refundApplyVO;
    }

    /**
     * 插入售后维护配置数据
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAfterSale(EsAfterSaleDTO afterSaleDTO) {
        try {
            // 验证分类存在不存在
            DubboResult dubboResult = this.categoryService.getCategory(afterSaleDTO.getCategoryId());
            if (dubboResult.getData() == null) {
                throw new ArgumentException(dubboResult.getCode(), dubboResult.getMsg());
            }

            // 判断数据是否存在
            if (isExist(afterSaleDTO.getCategoryId())) {
                throw new ArgumentException(TradeErrorCode.AFTER_SALE_IS_EXIST.getErrorCode(), TradeErrorCode.AFTER_SALE_IS_EXIST.getErrorMsg());
            }

            EsAfterSale afterSale = new EsAfterSale();
            BeanUtil.copyProperties(afterSaleDTO, afterSale);
            this.afterSaleMapper.insert(afterSale);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后维护配置新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("售后维护配置新增失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新售后维护配置数据
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @param id           主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAfterSale(EsAfterSaleDTO afterSaleDTO, Long id) {
        try {
            EsAfterSale afterSale = this.afterSaleMapper.selectById(id);
            if (afterSale == null) {
                throw new ArgumentException(TradeErrorCode.AFTER_SALE_NOT_EXIST.getErrorCode(), TradeErrorCode.AFTER_SALE_NOT_EXIST.getErrorMsg());
            }

            // 验证分类存在不存在
            DubboResult dubboResult = this.categoryService.getCategory(afterSaleDTO.getCategoryId());
            if (dubboResult.getData() == null) {
                throw new ArgumentException(dubboResult.getCode(), dubboResult.getMsg());
            }

            // 判断数据是否存在
            if (isExist(afterSaleDTO.getCategoryId())) {
                throw new ArgumentException(TradeErrorCode.AFTER_SALE_IS_EXIST.getErrorCode(), TradeErrorCode.AFTER_SALE_IS_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(afterSaleDTO, afterSale);
            QueryWrapper<EsAfterSale> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAfterSale::getId, id);
            this.afterSaleMapper.update(afterSale, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后维护配置更新失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后维护配置更新失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取售后维护配置详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    @Override
    public DubboResult<EsAfterSaleDO> getAfterSale(Long id) {
        try {
            EsAfterSale afterSale = this.afterSaleMapper.selectById(id);
            if (afterSale == null) {
                throw new ArgumentException(TradeErrorCode.AFTER_SALE_NOT_EXIST.getErrorCode(), TradeErrorCode.AFTER_SALE_NOT_EXIST.getErrorMsg());
            }
            EsAfterSaleDO afterSaleDO = new EsAfterSaleDO();
            BeanUtil.copyProperties(afterSale, afterSaleDO);
            return DubboResult.success(afterSaleDO);
        } catch (ArgumentException ae) {
            logger.error("售后维护配置查询失败" , ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后维护配置查询失败" , th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询售后维护配置列表
     *
     * @param afterSaleDTO 售后维护配置DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAfterSaleDO>
     */
    @Override
    public DubboPageResult<EsAfterSaleDO> getAfterSaleList(EsAfterSaleDTO afterSaleDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAfterSale> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(afterSaleDTO.getCategoryId() == null, EsAfterSale::getCategoryId, afterSaleDTO.getCategoryId());
            Page<EsAfterSale> page = new Page<>(pageNum, pageSize);
            IPage<EsAfterSale> iPage = this.page(page, queryWrapper);
            List<EsAfterSaleDO> afterSaleDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                afterSaleDOList = iPage.getRecords().stream().map(afterSale -> {
                    EsAfterSaleDO afterSaleDO = new EsAfterSaleDO();
                    BeanUtil.copyProperties(afterSale, afterSaleDO);
                    return afterSaleDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(afterSaleDOList);
        } catch (ArgumentException ae) {
            logger.error("售后维护配置分页查询失败" , ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后维护配置分页查询失败" , th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除售后维护配置数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAfterSaleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAfterSale(Long id) {
        try {
            this.afterSaleMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后维护配置删除失败" , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后维护配置删除失败" , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult approval(SellerRefundApprovalDTO refundApproval) {
        // 通过退款单SN 查询
        try {
            QueryWrapper<EsRefund> refundWrapper = new QueryWrapper<>();
            refundWrapper.lambda().eq(EsRefund::getSn,refundApproval.getSn());
            EsRefund esRefund = this.esRefundMapper.selectOne(refundWrapper);
            if (esRefund == null || !esRefund.getShopId().equals(refundApproval.getShopId())){
                throw new ArgumentException(TradeErrorCode.REFUND_FORM_DOES_NOT_EXIST.getErrorCode(),TradeErrorCode.REFUND_FORM_DOES_NOT_EXIST.getErrorMsg());
            }
            // 通过订单sn获取订单信息
            QueryWrapper<EsOrder> orderWrapper = new QueryWrapper<>();
            orderWrapper.lambda().eq(EsOrder::getOrderSn,esRefund.getOrderSn());
            EsOrder esOrder = this.esOrderMapper.selectOne(orderWrapper);
            // 支付宝可退回金额
            BigDecimal canRefundPrice = getRefundPrice(esOrder);
            // 余额退款金额
            BigDecimal balanceRefundPrice = BigDecimal.ZERO;
            BigDecimal refundPrice = BigDecimal.ZERO;
            // 退款金额大于可退款余额的情况
            if (new BigDecimal(esRefund.getRefundMoney().toString()).compareTo(canRefundPrice) > 0) {
                refundPrice = canRefundPrice;
                balanceRefundPrice = new BigDecimal(esRefund.getRefundMoney().toString()).subtract(canRefundPrice);
            } else {
                refundPrice = new BigDecimal(esRefund.getRefundMoney().toString());
            }
            //操作权限校验
            this.checkAllowable(esRefund, RefundOperateEnum.SELLER_APPROVAL);

            // 售后单详情
            DubboResult<EsRefundDO> refundDetailBySn = this.esRefundService.getRefundDetailBySn(refundApproval.getSn());
            if (!refundDetailBySn.isSuccess()){
                throw new ArgumentException(refundDetailBySn.getCode(),refundDetailBySn.getMsg());
            }
            EsRefundDO esRefundDO = refundDetailBySn.getData();
            List<EsRefundGoodsDO> esRefundGoods = esRefundDO.getEsRefundGoods();
            List<Long> goodsIds = esRefundGoods.stream().map(EsRefundGoodsDO::getGoodsId).collect(Collectors.toList());

            //退款金额校验
            this.checkMoney(esRefund, refundPrice.doubleValue());
            // 审核状态
            String refundStatus = RefundStatusEnum.REFUSE.value();
            // 进度状态
            String processStatus = ProcessStatusEnum.TO_BE_PROCESS.value();

            //已付款状态取消单品
            DubboResult<EsOrderDO> esBuyerOrderInfo = this.esOrderService.getEsBuyerOrderInfo(esRefund.getOrderSn());
            if (!esBuyerOrderInfo.isSuccess()){
                throw new ArgumentException(esBuyerOrderInfo.getCode(),esBuyerOrderInfo.getMsg());
            }
            EsOrderDO esOrderDO = esBuyerOrderInfo.getData();
            if (esOrderDO.getOrderState().equals(OrderStatusEnum.PAID_OFF.value())){
                // 商品项集合
                List<EsOrderItemsDO> esOrderItemsDO = esOrderDO.getEsOrderItemsDO();

                for (EsOrderItemsDO orderItemsDO : esOrderItemsDO) {
                    for (EsRefundGoodsDO esRefundGoodsDO : esRefundGoods) {
                        if (orderItemsDO.getSkuId().equals(esRefundGoodsDO.getSkuId()) && orderItemsDO.getState().equals(ServiceStatusEnum.APPLY.value())){
                            if (refundApproval.getAgree().equals(1)){
                                orderItemsDO.setState(ServiceStatusEnum.PASS.value());
                                DubboResult<EsOrderItemsDO> dubboResult = this.esOrderItemsService.queryBySnapshotId(orderItemsDO.getSnapshotId(), orderItemsDO.getGoodsId());
                                if (!dubboResult.isSuccess()){
                                    throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
                                }
                                orderItemsDO.setHasShip(2);
                                EsOrderItemsDTO esOrderItemsDTO = new EsOrderItemsDTO();
                                BeanUtils.copyProperties(orderItemsDO,esOrderItemsDTO);
                                this.esOrderItemsService.updateOrderItemsMessage(esOrderItemsDTO);
                            }else {
                                // TODO 0329
                                orderItemsDO.setState(ServiceStatusEnum.REFUSE.value());
                            }
                        }
                    }

                }
                String json2=JsonUtil.objectToJson(esOrderItemsDO);
                esOrderDO.setItemsJson(json2);
                EsOrderDTO esOrderDTO = new EsOrderDTO();
                BeanUtils.copyProperties(esOrderDO,esOrderDTO);
                this.esOrderService.updateOrderMessage(esOrderDTO);

            }
            String refundFailReason = "";
            // 判断是否同意退款
            if (refundApproval.getAgree().equals(1)) {
                if (esRefund.getRefundType().equals(RefuseTypeEnum.RETURN_MONEY.value())) {
                    refundStatus = RefundStatusEnum.PASS.value();
                }
                if (esRefund.getRefundType().equals(RefuseTypeEnum.RETURN_GOODS.value())) {
                    refundStatus = RefundStatusEnum.PASS.value();
                }

                // 如果为申请退款且退款方式支持原路退回则审核通过后直接原路退款
                if ((esRefund.getRefuseType().equals(RefuseTypeEnum.RETURN_MONEY.value()) && RefundWayEnum.ORIGINAL.name().equals(esRefund.getRefundWay()))
                        || new BigDecimal(esOrder.getPayedMoney().toString()).compareTo(BigDecimal.ZERO) > 0) {

                    // 当订单总金额大于已付金额时
                    if (esOrder.getOrderMoney() - esOrder.getPayedMoney() > 0) {
                        // 订单号 退款金额
                        logger.error("退款PayOrderNo"+esRefund.getPayOrderNo());
                        logger.error("退款SN"+esRefund.getSn());
                        logger.error("退款"+refundPrice.doubleValue());
                        DubboResult<Map> mapDubboResult = iPayService.originRefund(esRefund.getPayOrderNo(), esRefund.getSn(), refundPrice.doubleValue());
                        Map data = mapDubboResult.getData();
                        if ("true".equals(StringUtil.toString(data.get("result")))) {
                            processStatus = ProcessStatusEnum.COMPLETED.value();
                        } else {
                            processStatus = ProcessStatusEnum.REFUND_FAIL.value();
                            refundFailReason = StringUtil.toString(data.get("fail_reason"));
                        }
                    }

                    esRefund.setRefundStatus(refundStatus);
                    esRefund.setRefundMoney(refundApproval.getRefundPrice());
                    // 余额原路退回
                    if (balanceRefundPrice.compareTo(BigDecimal.ZERO) > 0) {

                        DubboResult dubboResult = this.esTradeService.setMemberBalance(esOrder.getTradeSn(), esOrder.getOrderSn(), esOrder.getMemberId(), balanceRefundPrice.doubleValue(), ConsumeEnumType.REFUND.value());
                        if(!dubboResult.isSuccess()){
                            throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
                        }
                        processStatus = ProcessStatusEnum.COMPLETED.value();
                    }
                }
            } else {

            }
            EsRefundDTO esRefundDTO = new EsRefundDTO();
            BeanUtils.copyProperties(esRefund,esRefundDTO);
            esRefundDTO.setRefundStatus(refundStatus);
            esRefundDTO.setSellerRemark(refundApproval.getRemark());
            esRefundDTO.setRefundMoney(refundApproval.getRefundPrice());
            esRefundDTO.setRefundPoint(refundApproval.getRefundPoint().doubleValue());
            esRefundDTO.setRefundFailReason(refundFailReason);
            esRefundDTO.setProcessStatus(processStatus);
            esRefundDTO.setRefundPayPrice(refundPrice.doubleValue());
            this.esRefundService.updateRefund(esRefundDTO);

            // 记录日志
            this.log(refundApproval.getSn(), refundApproval.getShopName(), "审核退货（款），结果为：" + (refundApproval.getAgree() == 1 ? "同意" : "拒绝"));

            //未发货单品审核通过后更改订单状态
            int i = 0;
            boolean flags=false;
            DubboPageResult<EsOrderItemsDO> esOrderItemsByOrderSn = this.esOrderItemsService.getEsOrderItemsByOrderSn(esRefund.getOrderSn());
            if (!esOrderItemsByOrderSn.isSuccess()){
                throw new ArgumentException(esOrderItemsByOrderSn.getCode(),esOrderItemsByOrderSn.getMsg());
            }
            List<EsOrderItemsDO> list = esOrderItemsByOrderSn.getData().getList();
            for (EsOrderItemsDO ots : list
            ) {
                if (ots.getHasShip() == 1 || ots.getHasShip() == 2) {
                    i++;
                }
                if (ots.getHasShip()==1){
                    flags=true;
                }
            }
            if (list.size() == i && esOrder.getOrderState().equals(OrderStatusEnum.PAID_OFF.value()) && flags ) {
                //订单改成已发货状态
                EsOrderDTO esOrderDTO = new EsOrderDTO();
                BeanUtils.copyProperties(esOrder,esOrderDTO);
                esOrderDTO.setOrderState(OrderStatusEnum.SHIPPED.value());
                esOrderDTO.setShipState(ShipStatusEnum.SHIP_YES.value());
                esOrderDTO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                this.esOrderService.updateOrderMessage(esOrderDTO);
            }
            //部分取消后商品全部取消订单状态改成已取消
            if (list.size() == i && esOrder.getOrderState().equals(OrderStatusEnum.PAID_OFF.value()) && !flags){
                //订单改成已发货状态
                EsOrderDTO esOrderDTO = new EsOrderDTO();
                BeanUtils.copyProperties(esOrder,esOrderDTO);
                esOrderDTO.setOrderState(OrderStatusEnum.CANCELLED.value());
                this.esOrderService.updateOrderMessage(esOrderDTO);

            }

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("审核售后失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable be) {
            logger.error("审核售后失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    private void checkMoney(EsRefund esRefund, double price) {
        //获取退款单信息
        DubboResult<EsRefundDO> refundDetailBySn = this.esRefundService.getRefundDetailBySn(esRefund.getSn());
        if (!refundDetailBySn.isSuccess()){
            throw new ArgumentException(refundDetailBySn.getCode(),refundDetailBySn.getMsg());
        }
        Double refundPrice = 0.0;
        List<EsRefundGoodsDO> esRefundGoods = refundDetailBySn.getData().getEsRefundGoods();

        if (esRefund.getRefundType().equals(RefundTypeEnum.CANCEL_ORDER.value())) {
            // 取消订单
            DubboResult<EsOrderDO> esBuyerOrderInfo = this.esOrderService.getEsOrderInfo(esRefund.getOrderSn());
            if (!esBuyerOrderInfo.isSuccess()){
                throw new ArgumentException(esBuyerOrderInfo.getCode(),esBuyerOrderInfo.getMsg());
            }
            EsOrderDO esOrderDO = esBuyerOrderInfo.getData();
            Double needPayMoney = esOrderDO.getNeedPayMoney();
            refundPrice = needPayMoney;
        } else {
            // 累加退款金额
            refundPrice = esRefundGoods.stream().mapToDouble(n->
                MathUtil.multiply(n.getMoney(),n.getReturnNum())).sum();
        }
        if (price > refundPrice) {
            throw new ArgumentException(TradeErrorCode.REFUND_NOT_LG_PAY.getErrorCode(), "退款金额不能大于支付金额");
        }
    }

    /**
     * 进行操作校验 看此状态下是否允许此操作
     *
     * @param esRefund        退款VO
     * @param refundOperate 进行的操作
     */
    private void checkAllowable(EsRefund esRefund, RefundOperateEnum refundOperate) {

        // 退款当前流程状态
        String status = esRefund.getProcessStatus();
        ProcessStatusEnum refundStatus = ProcessStatusEnum.valueOf(status);

        // 退货/退款
        String refundType = esRefund.getRefundType();
        RefundTypeEnum type = RefundTypeEnum.valueOf(refundType);

        boolean allowble = RefundOperateChecker.checkAllowable(type, refundStatus, refundOperate);
        if (!allowble) {
            throw new ArgumentException(TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorCode(),TradeErrorCode.UNAUTHORIZED_OPERATION.getErrorMsg());
        }
    }

    /**
     * 获取支付宝可退回金额
     *
     */
    private BigDecimal getRefundPrice(EsOrder esOrder) {
        // 支付宝已退款金额
        QueryWrapper<EsRefund> refundWrapper = new QueryWrapper<>();
        refundWrapper.lambda().eq(EsRefund::getOrderSn,esOrder.getOrderSn());
        String[] status = {"PASS","COMPLETED"};
        refundWrapper.lambda().in(EsRefund::getRefundStatus,status);
        List<EsRefund> esRefunds = this.esRefundMapper.selectList(refundWrapper);
        double refundedPayPrice = esRefunds.stream().mapToDouble(EsRefund::getRefundPayPrice).sum();
        // 支付宝等第三方支付的金额
        BigDecimal payMoney = new BigDecimal(esOrder.getPayMoney());
        // 退款金额 不得大于 第三方支付的金额

        return payMoney.subtract(new BigDecimal(refundedPayPrice));
    }

    /**
     * 重复性检查check
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/06 09:59:22
     * @param categoryId 分类id
     * @return: boolean
     */
    public boolean isExist(Long categoryId) {
        QueryWrapper<EsAfterSale> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(EsAfterSale::getCategoryId, categoryId);

        int count = this.afterSaleMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
