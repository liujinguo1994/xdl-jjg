package com.xdl.jjg.manager.consumer;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.roketmq.MQProducer;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuQuantityDTO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsGoodsSkuQuantityService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.dto.CartItemsDTO;
import com.shopx.trade.api.model.domain.dto.EsGiftSkuQuantityDTO;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import com.shopx.trade.api.model.domain.dto.EsTradeDTO;
import com.shopx.trade.api.model.domain.vo.EsFullDiscountGiftVO;
import com.shopx.trade.api.model.domain.vo.EsFullDiscountVO;
import com.shopx.trade.api.model.domain.vo.EsTradeSnMoneyVO;
import com.shopx.trade.api.model.domain.vo.TradePromotionGoodsVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.IEsFullDiscountGiftService;
import com.shopx.trade.api.service.IEsOrderChangeService;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.api.service.IEsSeckillService;
import com.shopx.trade.web.manager.TradeManager;
import com.shopx.trade.web.manager.event.TradeIntoDbEvent;
import com.shopx.trade.web.transaction.TransactionProducer;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 商品库存增加/扣减
 *
 * @author LIUJG
 */

@Component("goodsQuantityChangeConsumer" )
public class GoodsQuantityChangeConsumer implements TradeIntoDbEvent {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsSkuQuantityService iEsGoodsSkuQuantityService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderService iEsOrderService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderChangeService iEsOrderChangeService;
    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.stock.reduce.topic}")
    private String stock_reduce_topic;

    @Value("${rocketmq.trade.topic}")
    private String trade_topic;

    @Autowired
    private TradeManager tradeManager;

    @Autowired
    private TransactionProducer transactionProducer;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountGiftService iEsFullDiscountGiftService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillService iEsSeckillService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService iEsGoodsService;

    /**
     * 交易入库
     *
     * @param esTradeDTO
     */

    @Override
    public DubboResult<EsTradeSnMoneyVO> onTradeIntoDb(EsTradeDTO esTradeDTO)/*throws MQClientException*/ {
        EsTradeSnMoneyVO esTradeSnMoneyVO = new EsTradeSnMoneyVO();
        if (esTradeDTO == null) {
            throw new ArgumentException(TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorCode(), TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorMsg());
        }
        try {
            //获取交易中的订单集合
            List<EsOrderDTO> orderList = esTradeDTO.getOrderList();

            for (EsOrderDTO esOrderDTO : orderList) {
                List<EsGoodsSkuQuantityDTO> goodsQuantityDTO = new ArrayList<>();
                // SKU表库存减操作
                List<CartItemsDTO> cartItemsList = esOrderDTO.getCartItemsList();

                // 单品活动是否是秒杀活动
                AtomicReference<Boolean> seckillFlag = new AtomicReference<>(false);

                cartItemsList.stream().forEach(cartItemsDTO -> {
                    EsGoodsSkuQuantityDTO goodsQuantity = new EsGoodsSkuQuantityDTO();
                    //单品活动中秒杀商品需要单独进行 秒杀商品表库存扣减
                    List<TradePromotionGoodsVO> singleList = cartItemsDTO.getSingleList();
                    if (singleList != null && singleList.size() > 0) {
                        for (TradePromotionGoodsVO promotionGoodsVO : singleList) {
                            //判断是否参加的限时抢购的活动
                            if (promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name())) {
                                goodsQuantity.setActivityId(promotionGoodsVO.getActivityId());
                                seckillFlag.set(true);
                            }
                        }
                    }
                    goodsQuantity.setGoodsId(cartItemsDTO.getGoodsId());
                    goodsQuantity.setSkuId(cartItemsDTO.getSkuId());
                    goodsQuantity.setGoodsNumber(cartItemsDTO.getNum());
                    goodsQuantity.setOrderSn(esOrderDTO.getOrderSn());
                    goodsQuantityDTO.add(goodsQuantity);
                });
                List<EsGoodsSkuQuantityDTO> quantityDTOList = new ArrayList<>();
                List<EsOrderDTO> orderList1 = esTradeDTO.getOrderList();

                quantityDTOList = orderList1.stream().map(order -> {
                    EsGoodsSkuQuantityDTO goodsQuantity = new EsGoodsSkuQuantityDTO();
                    List<CartItemsDTO> cartItemsList1 = order.getCartItemsList();
                    goodsQuantity.setOrderSn(order.getOrderSn());
                    goodsQuantity.setGoodsNumber(1);
                    cartItemsList1.forEach(cartItemsDTO -> {
                        List<TradePromotionGoodsVO> groupList = cartItemsDTO.getGroupList();
                        if (CollectionUtils.isNotEmpty(groupList)) {
                            groupList.forEach(tradePromotionGoodsVO -> {
                                EsFullDiscountVO fullDiscountVO = tradePromotionGoodsVO.getFullDiscount();
                                // 满减满赠，两种情况可以同时存在，是否增赠品 1 是，2 否
                                if (fullDiscountVO.getIsSendGift() == 1) {
                                    EsFullDiscountGiftVO fullDiscountGift = fullDiscountVO.getFullDiscountGift();
                                    goodsQuantity.setGoodsId(fullDiscountGift.getGoodsId());
                                    goodsQuantity.setSkuId(fullDiscountGift.getSkuiId());
                                }
                            });
                        }
                    });
                    return goodsQuantity;
                }).collect(Collectors.toList());
                List<EsGoodsSkuQuantityDTO> finalQuantityDTOList = quantityDTOList;
                // 防止赠品信息为null 报错
                quantityDTOList.forEach(esGoodsSkuQuantityDTO -> {
                    if (esGoodsSkuQuantityDTO.getSkuId() != null) {
                        goodsQuantityDTO.addAll(finalQuantityDTOList);
                    }
                });

                long kczzstart = System.currentTimeMillis();
                // 库存数据库异步处理
                mqProducer.send(stock_reduce_topic,JsonUtil.objectToJson(goodsQuantityDTO));
                // 基于缓存库存扣减
                iEsGoodsSkuQuantityService.reduceGoodsSkuQuantityRedis(goodsQuantityDTO);
                long kczzEnd = System.currentTimeMillis();
                logger.info("库存遍历结束时间---》[{}]}；库存遍历总耗时---》[{}]", kczzEnd, kczzEnd - kczzstart);
                //扣减赠品表库存
                for (EsOrderDTO esOrderDTO1 : orderList1) {
                    EsGiftSkuQuantityDTO giftQuantity = new EsGiftSkuQuantityDTO();
                    giftQuantity.setOrderSn(esOrderDTO1.getOrderSn());
                    List<CartItemsDTO> cartItemsList1 = esOrderDTO1.getCartItemsList();
                    cartItemsList1.forEach(cartItemsDTO -> {
                        List<TradePromotionGoodsVO> groupList = cartItemsDTO.getGroupList();
                        if (CollectionUtils.isNotEmpty(groupList)) {
                            for (TradePromotionGoodsVO tradePromotionGoodsVO : groupList) {
                                EsFullDiscountVO fullDiscountVO = tradePromotionGoodsVO.getFullDiscount();
                                // 满减满赠，两种情况可以同时存在，是否增赠品 1 是，2 否
                                if (fullDiscountVO.getIsSendGift() == 1) {
                                    EsFullDiscountGiftVO fullDiscountGift = fullDiscountVO.getFullDiscountGift();
                                    giftQuantity.setId(fullDiscountGift.getId());
                                    giftQuantity.setSkuiId(fullDiscountGift.getSkuiId());
                                    giftQuantity.setShopId(fullDiscountGift.getShopId());
                                    giftQuantity.setEnableStore(fullDiscountGift.getEnableStore());
                                    giftQuantity.setGoodsId(fullDiscountGift.getGoodsId());
                                    DubboResult dubboResult1 = iEsFullDiscountGiftService.reduceFullDiscountGiftNum(giftQuantity);
                                    logger.info("赠品库存扣减：" + dubboResult1.isSuccess());
                                    logger.info("赠品库存扣减：" + dubboResult1.getCode() + "====" + dubboResult1.getMsg());
                                    // 如果库存不足 则作为非活动商品下单
                                    if (!dubboResult1.isSuccess()) {
                                        tradePromotionGoodsVO.getFullDiscount().setFullDiscountGift(null);
                                        continue;
                                    }
                                }
                            }
                        }
                    });
                }

                // 处理秒杀商品缓存库存数据
                if (seckillFlag.get()) {
                    iEsSeckillService.addSoldNum(goodsQuantityDTO);
                }
            }
            long sendStart = System.currentTimeMillis();
            logger.info("发送创建订单消息开始时间---》[{}]}", sendStart);
            //通过uuid 当key
            String uuid = UUID.randomUUID().toString().replace("_", "");
            //封装消息实体
            Message message = new Message(trade_topic, null, uuid, JsonUtil.objectToJson(esTradeDTO).getBytes());
            //发送消息 用 sendMessageInTransaction  第一个参数可以理解成消费方需要的参数 第二个参数可以理解成消费方不需要 本地事务需要的参数
            SendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, null);
            System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());
            long sendEnd = System.currentTimeMillis();
            logger.info("发送创建订单消息结束时间---》[{}]}；发送消息总耗时---》[{}]", sendEnd, sendEnd - sendStart);
            if (SendStatus.SEND_OK == sendResult.getSendStatus()) {

                String address = tradeManager.addressMessage(esTradeDTO);
                // 返回订单编号和总价 address
                esTradeSnMoneyVO.setTradeSn(esTradeDTO.getTradeSn());
                // 需第三方支付金额
                esTradeSnMoneyVO.setTotalMoney(MathUtil.subtract(esTradeDTO.getTotalMoney(), esTradeDTO.getUseBalance()));
                esTradeSnMoneyVO.setAddress(address);
            }
            return DubboResult.success(esTradeSnMoneyVO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(e.hashCode(), e.getMessage());
        }
    }


    @Override
    public DubboResult<EsTradeSnMoneyVO> onTradeIntoDbYC(EsTradeDTO esTradeDTO)/*throws MQClientException*/ {
        EsTradeSnMoneyVO esTradeSnMoneyVO = new EsTradeSnMoneyVO();
        if (esTradeDTO == null) {
            throw new ArgumentException(TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorCode(), TradeErrorCode.TRADE_DATE_ERROR_CODE.getErrorMsg());
        }
        try {
            //获取交易中的订单集合
            List<EsOrderDTO> orderList = esTradeDTO.getOrderList();

            for (EsOrderDTO esOrderDTO : orderList) {
                List<EsGoodsSkuQuantityDTO> goodsQuantityDTO = new ArrayList<>();
                // SKU表库存减操作
                List<CartItemsDTO> cartItemsList = esOrderDTO.getCartItemsList();

                // 单品活动是否是秒杀活动
                AtomicReference<Boolean> seckillFlag = new AtomicReference<>(false);

                cartItemsList.stream().forEach(cartItemsDTO -> {
                    EsGoodsSkuQuantityDTO goodsQuantity = new EsGoodsSkuQuantityDTO();
                    //单品活动中秒杀商品需要单独进行 秒杀商品表库存扣减
                    List<TradePromotionGoodsVO> singleList = cartItemsDTO.getSingleList();
                    if (singleList != null && singleList.size() > 0) {
                        for (TradePromotionGoodsVO promotionGoodsVO : singleList) {
                            //判断是否参加的限时抢购的活动
                            if (promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name())) {
                                goodsQuantity.setActivityId(promotionGoodsVO.getActivityId());
                                seckillFlag.set(true);
                            }
                        }
                    }
                    goodsQuantity.setGoodsId(cartItemsDTO.getGoodsId());
                    goodsQuantity.setSkuId(cartItemsDTO.getSkuId());
                    goodsQuantity.setGoodsNumber(cartItemsDTO.getNum());
                    goodsQuantity.setOrderSn(esOrderDTO.getOrderSn());
                    goodsQuantityDTO.add(goodsQuantity);
                });
                List<EsGoodsSkuQuantityDTO> quantityDTOList = new ArrayList<>();
                List<EsOrderDTO> orderList1 = esTradeDTO.getOrderList();

                quantityDTOList = orderList1.stream().map(order -> {
                    EsGoodsSkuQuantityDTO goodsQuantity = new EsGoodsSkuQuantityDTO();
                    List<CartItemsDTO> cartItemsList1 = order.getCartItemsList();
                    goodsQuantity.setOrderSn(order.getOrderSn());
                    goodsQuantity.setGoodsNumber(1);
                    cartItemsList1.forEach(cartItemsDTO -> {
                        List<TradePromotionGoodsVO> groupList = cartItemsDTO.getGroupList();
                        if (CollectionUtils.isNotEmpty(groupList)) {
                            groupList.forEach(tradePromotionGoodsVO -> {
                                EsFullDiscountVO fullDiscountVO = tradePromotionGoodsVO.getFullDiscount();
                                // 满减满赠，两种情况可以同时存在，是否增赠品 1 是，2 否
                                if (fullDiscountVO.getIsSendGift() == 1) {
                                    EsFullDiscountGiftVO fullDiscountGift = fullDiscountVO.getFullDiscountGift();
                                    goodsQuantity.setGoodsId(fullDiscountGift.getGoodsId());
                                    goodsQuantity.setSkuId(fullDiscountGift.getSkuiId());
                                }
                            });
                        }
                    });
                    return goodsQuantity;
                }).collect(Collectors.toList());
                List<EsGoodsSkuQuantityDTO> finalQuantityDTOList = quantityDTOList;
                // 防止赠品信息为null 报错
                quantityDTOList.forEach(esGoodsSkuQuantityDTO -> {
                    if (esGoodsSkuQuantityDTO.getSkuId() != null) {
                        goodsQuantityDTO.addAll(finalQuantityDTOList);
                    }
                });

                long kczzstart = System.currentTimeMillis();
                // 库存数据库异步处理
                mqProducer.send(stock_reduce_topic,JsonUtil.objectToJson(goodsQuantityDTO));
                // 基于缓存库存扣减
                iEsGoodsSkuQuantityService.reduceGoodsSkuQuantityRedis(goodsQuantityDTO);
                long kczzEnd = System.currentTimeMillis();
                logger.info("库存遍历结束时间---》[{}]}；库存遍历总耗时---》[{}]", kczzEnd, kczzEnd - kczzstart);
                //扣减赠品表库存
                for (EsOrderDTO esOrderDTO1 : orderList1) {
                    EsGiftSkuQuantityDTO giftQuantity = new EsGiftSkuQuantityDTO();
                    giftQuantity.setOrderSn(esOrderDTO1.getOrderSn());
                    List<CartItemsDTO> cartItemsList1 = esOrderDTO1.getCartItemsList();
                    cartItemsList1.forEach(cartItemsDTO -> {
                        List<TradePromotionGoodsVO> groupList = cartItemsDTO.getGroupList();
                        if (CollectionUtils.isNotEmpty(groupList)) {
                            for (TradePromotionGoodsVO tradePromotionGoodsVO : groupList) {
                                EsFullDiscountVO fullDiscountVO = tradePromotionGoodsVO.getFullDiscount();
                                // 满减满赠，两种情况可以同时存在，是否增赠品 1 是，2 否
                                if (fullDiscountVO.getIsSendGift() == 1) {
                                    EsFullDiscountGiftVO fullDiscountGift = fullDiscountVO.getFullDiscountGift();
                                    giftQuantity.setId(fullDiscountGift.getId());
                                    giftQuantity.setSkuiId(fullDiscountGift.getSkuiId());
                                    giftQuantity.setShopId(fullDiscountGift.getShopId());
                                    giftQuantity.setEnableStore(fullDiscountGift.getEnableStore());
                                    giftQuantity.setGoodsId(fullDiscountGift.getGoodsId());
                                    DubboResult dubboResult1 = iEsFullDiscountGiftService.reduceFullDiscountGiftNum(giftQuantity);
                                    logger.info("赠品库存扣减：" + dubboResult1.isSuccess());
                                    logger.info("赠品库存扣减：" + dubboResult1.getCode() + "====" + dubboResult1.getMsg());
                                    // 如果库存不足 则作为非活动商品下单
                                    if (!dubboResult1.isSuccess()) {
                                        tradePromotionGoodsVO.getFullDiscount().setFullDiscountGift(null);
                                        continue;
                                    }
                                }
                            }
                        }
                    });
                }

                // 处理秒杀商品缓存库存数据
                if (seckillFlag.get()) {
                    iEsSeckillService.addSoldNum(goodsQuantityDTO);
                }
            }
            long sendStart = System.currentTimeMillis();
            logger.info("发送创建订单消息开始时间---》[{}]}", sendStart);
            //通过uuid 当key
            String uuid = UUID.randomUUID().toString().replace("_", "");
            //封装消息实体
            Message message = new Message(trade_topic, null, uuid, JsonUtil.objectToJson(esTradeDTO).getBytes());
            //发送消息 用 sendMessageInTransaction  第一个参数可以理解成消费方需要的参数 第二个参数可以理解成消费方不需要 本地事务需要的参数
            SendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, null);
            System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());
            long sendEnd = System.currentTimeMillis();
            logger.info("发送创建订单消息结束时间---》[{}]}；发送消息总耗时---》[{}]", sendEnd, sendEnd - sendStart);
            if (SendStatus.SEND_OK == sendResult.getSendStatus()) {

                String address = tradeManager.addressMessage(esTradeDTO);
                // 返回订单编号和总价 address
                esTradeSnMoneyVO.setTradeSn(esTradeDTO.getTradeSn());
                // 需第三方支付金额
                esTradeSnMoneyVO.setTotalMoney(MathUtil.subtract(esTradeDTO.getTotalMoney(), esTradeDTO.getUseBalance()));
                esTradeSnMoneyVO.setAddress(address);
            }
            return DubboResult.success(esTradeSnMoneyVO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(e.hashCode(), e.getMessage());
        }
    }
}

