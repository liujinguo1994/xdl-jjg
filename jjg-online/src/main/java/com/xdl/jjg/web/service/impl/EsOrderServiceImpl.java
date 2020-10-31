package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsMemberRfmConfigDO;
import com.jjg.member.model.domain.EsReceiptHistoryDO;
import com.jjg.member.model.dto.EsMemberActiveInfoDTO;
import com.jjg.member.model.enums.ActiveTypeEnum;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import com.jjg.system.model.domain.EsLogiCompanyDO;
import com.jjg.system.model.dto.EsSmsSendDTO;
import com.jjg.system.model.enums.SmsTemplateCodeEnum;
import com.jjg.system.model.vo.ExpressDetailVO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.*;
import com.jjg.trade.model.enums.*;
import com.jjg.trade.model.vo.*;
import com.jjg.trade.orderCheck.OrderOperateAllowable;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.TradeCachePrefix;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.message.OrderStatusChangeMsg;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.utils.OrderStatusUtils;
import com.xdl.jjg.web.service.*;
import com.xdl.jjg.web.service.feign.member.ExpressPlatformService;
import com.xdl.jjg.web.service.feign.member.MemberRfmConfigService;
import com.xdl.jjg.web.service.feign.member.ReceiptHistoryService;
import com.xdl.jjg.web.service.feign.shop.CategoryService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import com.xdl.jjg.web.service.feign.system.LogiCompanyService;
import com.xdl.jjg.web.service.feign.system.SmsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细表-es_order 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-05-29
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsOrderService.class, timeout = 5000)
public class EsOrderServiceImpl extends ServiceImpl<EsOrderMapper, EsOrder> implements IEsOrderService {
    private static Logger logger = LoggerFactory.getLogger(EsOrderServiceImpl.class);

    @Autowired
    private EsOrderMapper esOrderMapper;

    @Autowired
    private EsTradeMapper esTradeMapper;

    @Autowired
    private EsOrderLogMapper esOrderLogMapper;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private EsDeliveryInfoMapper esDeliveryInfoMapper;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private IEsOrderMetaService iEsOrderMetaService;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.member.active.topic}")
    private String member_active_topic;

    @Autowired
    private ReceiptHistoryService iEsReceiptHistoryService;

    @Autowired
    private IEsOrderItemsService iEsOrderItemsService;

    @Autowired
    private CategoryService iEsCategoryService;

    @Autowired
    private GoodsSkuService iEsGoodsSkuService;

    @Autowired
    private MemberRfmConfigService iEsMemberRfmConfigService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ExpressPlatformService expressPlatformService;

    @Autowired
    private IEsRefundService esRefundService;

    @Autowired
    private IEsCouponService iEsCouponService;
    @Autowired
    private IEsOrderOperateService esOrderOperateService;


    @Autowired
    private LogiCompanyService logiCompanyService;

    @Autowired
    private IEsCakeCardService esCakeCardService;

    @Autowired
    private SmsService esSmsService;

    /**
     * TODO 卖家端 查看订单详情信息
     * 查询订单明细表
     *
     * @param orderSn
     * @param shopId
     * @return
     */
    @Override
    public DubboResult<EsSellerOrderDO> getEsSellerOrderInfo(String orderSn, Long shopId) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn).eq(EsOrder::getShopId, shopId);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            DubboPageResult<EsSellerOrderItemsDO> result= iEsOrderItemsService.getSellerEsOrderItemsByOrderSn(esOrder.getOrderSn());
            //需要返回的订单明细对象
            EsSellerOrderDO esSellerOrderDO = new EsSellerOrderDO();

            if(result.isSuccess()){
                List<EsSellerOrderItemsDO> sellerOrderItemsDOList =  result.getData().getList();
                sellerOrderItemsDOList.forEach(esSellerOrderItemsDO -> {
                    // 需要会员模块提供根据GoodsId 获取发票信息的接口
                    DubboResult<EsReceiptHistoryDO> receiptHistory = iEsReceiptHistoryService.getReceiptHistoryByGoodsIdAndOrdersn(esSellerOrderItemsDO.getGoodsId(), orderSn);
                    //  给每个已经开票的商品进行赋值发票信息
                    if (receiptHistory.getData() != null) {
                        EsMemberReceiptHistoryDO esMemberReceiptHistoryDO = new EsMemberReceiptHistoryDO();
                        BeanUtils.copyProperties(receiptHistory.getData(), esMemberReceiptHistoryDO);
                        esSellerOrderItemsDO.setEsMemberReceiptHistoryDO(esMemberReceiptHistoryDO);
                    }
                });
                esSellerOrderDO.setEsOrderItemsDO(sellerOrderItemsDOList);
            }
            BeanUtils.copyProperties(esOrder, esSellerOrderDO);
            esSellerOrderDO.setOrderMoney(MathUtil.add(esOrder.getOrderMoney(),esOrder.getShippingMoney()));
            OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable(OrderStatusEnum.valueOf( esSellerOrderDO.getOrderState()),
                    ServiceStatusEnum.valueOf(esSellerOrderDO.getServiceState()) );
            esSellerOrderDO.setOrderOperateAllowable(orderOperateAllowable);
            esSellerOrderDO.setOrderStateText(OrderStatusEnum.getOrderName(esSellerOrderDO.getOrderState()));
            esSellerOrderDO.setShipStateText(ShipStatusEnum.getShipName(esSellerOrderDO.getShipState()));
            esSellerOrderDO.setPayStateText(PayStatusEnum.getPayName(esSellerOrderDO.getPayState()));
            esSellerOrderDO.setServiceStateText(ServiceStatusEnum.getServiceName(esSellerOrderDO.getServiceState()));
            return DubboResult.success(esSellerOrderDO);
        } catch (ArgumentException ae) {
            logger.error("SKU查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsOrderDO> getEsOrderInfoByTradeSn(String tradeSn) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getTradeSn, tradeSn).orderByDesc(EsOrder::getCreateTime);
            List<EsOrder> orders = this.esOrderMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(orders)) {
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            List<EsOrderDO> orderDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(orders)) {
                orderDOList = orders.stream().map(esOrder -> {
                    EsOrderDO orderDO = new EsOrderDO();
                    BeanUtil.copyProperties(esOrder, orderDO);
                    return orderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(orderDOList);
        } catch (ArgumentException ae) {
            logger.error("SKU查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsOrderDO> getEsOrderInfoByTradeSnAndState(String tradeSn,String orderState) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getTradeSn, tradeSn).orderByDesc(EsOrder::getCreateTime);
            if (!StringUtils.isBlank(orderState)){
                queryWrapper.lambda().eq(EsOrder::getOrderState,orderState);
            }
            List<EsOrder> orders = this.esOrderMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(orders)) {
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            List<EsOrderDO> orderDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(orders)) {
                orderDOList = orders.stream().map(esOrder -> {
                    EsOrderDO orderDO = new EsOrderDO();
                    BeanUtil.copyProperties(esOrder, orderDO);
                    return orderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(orderDOList);
        } catch (ArgumentException ae) {
            logger.error("SKU查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsOrderDO> getEsOrderInfoList(EsOrderDTO esOrderDTO, int pageSize, int pageNum) {
        QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.orderByDesc("id");

            if (esOrderDTO.getOrderState() != null) {
                queryWrapper.lambda().eq(EsOrder::getOrderState, esOrderDTO.getOrderState());
            }
            if (esOrderDTO.getMemberId() != null) {
                queryWrapper.lambda().eq(EsOrder::getMemberId, esOrderDTO.getMemberId());
            }
            if (esOrderDTO.getItemsJson() != null) {
                queryWrapper.lambda().like(EsOrder::getItemsJson, esOrderDTO.getItemsJson());
            }

            Page<EsOrder> page = new Page<>(pageNum, pageSize);
            // 获取订单列表
            IPage<EsOrder> orderList = this.page(page, queryWrapper);

            List<EsOrderDO> orderDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(orderList.getRecords())) {
                orderDOList = orderList.getRecords().stream().map(esOrder -> {
                    EsOrderDO orderDO = new EsOrderDO();
                    BeanUtil.copyProperties(esOrder, orderDO);
                    return orderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(orderDOList);
        } catch (ArgumentException ae) {
            logger.error("查询订单商品详情列表失败", ae);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询订单商品详情列表失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> insertOrder(EsOrderDTO esOrderDTO) {
        try {
            if (esOrderDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "参数错误");
            }
            //对象转换
            EsOrder esOrder = new EsOrder();
            BeanUtils.copyProperties(esOrderDTO, esOrder);
            esOrder.setGoodsMoney(esOrderDTO.getGoodsMoney());
            esOrder.setDiscountMoney(esOrderDTO.getDiscountMoney());
            esOrder.setOrderMoney(esOrderDTO.getOrderMoney());
            esOrder.setPayedMoney(esOrderDTO.getPayedMoney());
            esOrder.setPayMoney(esOrderDTO.getPayMoney());
            esOrder.setShippingMoney(esOrderDTO.getPrice().getFreightPrice());
            this.save(esOrder);
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("保存订单明细失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SAVE_ORDER_ERROR.getErrorCode(), TradeErrorCode.SAVE_ORDER_ERROR.getErrorMsg());
        } catch (Throwable th) {
            logger.error("保存订单明细失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateOrderMessage(EsOrderDTO esOrderDTO) {
        try {
            if (esOrderDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            if (esOrderDTO.getId() != null) {
                queryWrapper.lambda().eq(EsOrder::getId, esOrderDTO.getId());
            }
            if (esOrderDTO.getTradeSn() != null) {
                queryWrapper.lambda().eq(EsOrder::getTradeSn, esOrderDTO.getTradeSn());
            }
            if (esOrderDTO.getOrderSn() != null) {
                queryWrapper.lambda().eq(EsOrder::getOrderSn, esOrderDTO.getOrderSn());
            }
            EsOrder esOrder = new EsOrder();
            //DTO 转换成 entity
            BeanUtils.copyProperties(esOrderDTO, esOrder);
            //判断该数据是否存在
            Integer integer = this.baseMapper.selectCount(queryWrapper);
            if (integer == 0) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            this.esOrderMapper.update(esOrder, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单明细失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单明细失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateOrderPayParam(EsOrderDTO esOrderDTO) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();

            queryWrapper.lambda().eq(EsOrder::getTradeSn, esOrderDTO.getTradeSn());
            EsOrder esOrder = new EsOrder();
            BeanUtils.copyProperties(esOrderDTO, esOrder);
            this.esOrderMapper.update(esOrder, queryWrapper);

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单状态失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单状态失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * TODO 订单模块
     * 订单减库存成功， 修改订单状态
     *
     * @param esOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateOrderStatus(EsOrderDTO esOrderDTO) {
        try {
            if (esOrderDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, esOrderDTO.getOrderSn());
            EsOrder esOrder = new EsOrder();
            //DTO 转换成 entity
            BeanUtils.copyProperties(esOrderDTO, esOrder);
            //判断该数据是否存在
            EsOrder order = this.baseMapper.selectOne(queryWrapper);
            if (order == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            this.esOrderMapper.update(esOrder, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单状态失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单状态失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateOrderState(EsOrderDTO esOrderDTO) {
        try {
            if (esOrderDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, esOrderDTO.getOrderSn());
            //判断该数据是否存在
            EsOrder order = this.baseMapper.selectOne(queryWrapper);
            if (order == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            order.setOrderState(esOrderDTO.getOrderState());
            order.setServiceState(esOrderDTO.getServiceState());
            logger.error("订单售后状态："+esOrderDTO.getOrderState());
            this.esOrderMapper.update(order, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单状态失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单状态失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> deleteOrderMessage(Integer id) {
        try {
            if (id == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOrder esOrder = new EsOrder();
            esOrder.setIsDel(DelStatus.IS_DEL.getValue());
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getId, id);
            this.esOrderMapper.update(esOrder, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除订单明细失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("删除订单明细失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * TODO 卖家端
     * 卖家端 交易订单信息变化  0701已测试
     *
     * @param esOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateSellerOrderMessage(EsSellerOrderDTO esOrderDTO) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, esOrderDTO.getOrderSn()).eq(EsOrder::getShopId, esOrderDTO.getShopId());
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "订单信息不存在，或无权限操作");
            }
            // TODO 修改该订单状态以及取消原因
            EsOrder order = new EsOrder();
            BeanUtil.copyProperties(esOrderDTO, order);

            // TODO 根据orderSn 查询这个交易属于单商家还是多商家，（是否存在多个orderSn）
            String tradeSn = esOrder.getTradeSn();

            QueryWrapper<EsOrder> esOrderQueryWrapper = new QueryWrapper<>();
            esOrderQueryWrapper.lambda().eq(EsOrder::getTradeSn, tradeSn);
            List<EsOrder> orderList = this.esOrderMapper.selectList(esOrderQueryWrapper);
            List<EsOrderDTO> orderDTO = new ArrayList<EsOrderDTO>();
            orderDTO = orderList.stream().map(esOrder1 -> {
                EsOrderDTO esOrderDTO1 = new EsOrderDTO();
                BeanUtils.copyProperties(esOrder1, esOrderDTO1);
                return esOrderDTO1;
            }).collect(Collectors.toList());
            //订单状态筛选遍历 通用
            Boolean aBoolean = OrderStatusUtils.selectOrderStatus(orderDTO, OrderStatusEnum.COMPLETE.value());
            // 如果已完成的订单为空 则修改交易状态
            if (aBoolean) {
                QueryWrapper<EsTrade> esTradeQueryWrapper = new QueryWrapper<>();
                esTradeQueryWrapper.lambda().eq(EsTrade::getTradeSn, tradeSn);
                EsTrade esTrade = this.esTradeMapper.selectOne(esTradeQueryWrapper);
                if (esTrade == null) {
                    throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "交易信息不存在，或无权限操作");
                }
                //设置取消
                esTrade.setTradeStatus(esOrderDTO.getOrderState());
                this.esTradeMapper.update(esTrade, esTradeQueryWrapper);
            }
            //考虑order 实体中的属性值
            this.esOrderMapper.update(order, queryWrapper);

            // TODO 增加订单操作日志信息
            EsOrderLog esOrderLog = new EsOrderLog();
            esOrderLog.setOrderSn(esOrderDTO.getOrderSn());
            esOrderLog.setOpId(esOrderDTO.getShopId());
            esOrderLog.setOpName(esOrderDTO.getShopName());
            esOrderLog.setMessage(OrderStatusEnum.getOrderName(esOrderDTO.getOrderState()));
            esOrderLog.setOpTime(System.currentTimeMillis());
            esOrderLog.setMoney(esOrderDTO.getOrderMoney() == null ? 0.0: esOrderDTO.getOrderMoney());
            this.esOrderLogMapper.insert(esOrderLog);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单明细失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单明细失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * TODO 卖家端
     * 查询订单列表分页 0701已测试
     *
     * @param esSellerOrderQueryDTO
     * @return
     */
    @Override
    public DubboPageResult<EsOrderDO> getEsSellerOrderList(EsSellerOrderQueryDTO esSellerOrderQueryDTO, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize);
            IPage<EsOrderDO> esOrderList = this.esOrderMapper.getEsSellerOrderList(page, esSellerOrderQueryDTO);
            esOrderList.getRecords().forEach(esOrderDO -> {
                esOrderDO.setOrderStateText(OrderStatusEnum.getOrderName(esOrderDO.getOrderState()));
                esOrderDO.setShipStateText(ShipStatusEnum.getShipName(esOrderDO.getShipState()));
                esOrderDO.setPayStateText(PayStatusEnum.getPayName(esOrderDO.getPayState()));
                esOrderDO.setServiceStateText(ServiceStatusEnum.getServiceName(esOrderDO.getServiceState()));
                DubboPageResult<EsOrderItemsDO> result = this.iEsOrderItemsService.getEsOrderItemsByOrderSn(esOrderDO.getOrderSn());
                if(result.isSuccess()){
                    esOrderDO.setEsOrderItemsDO(result.getData().getList());
                }
                OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable(OrderStatusEnum.valueOf( esOrderDO.getOrderState()),
                        ServiceStatusEnum.valueOf(esOrderDO.getServiceState()) );
                esOrderDO.setOrderOperateAllowable(orderOperateAllowable);
            });
            return DubboPageResult.success(esOrderList.getTotal(), esOrderList.getRecords());
        } catch (ArgumentException ae) {
            logger.error("订单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("订单分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsOrderDO> getEsAdminOrderList(EsAdminOrderQueryDTO esAdminOrderQueryDTO, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize);
            IPage<EsOrderDO> esOrderList = this.esOrderMapper.getEsAdminOrderList(page, esAdminOrderQueryDTO);

            return DubboPageResult.success(esOrderList.getTotal(), esOrderList.getRecords());
        } catch (ArgumentException ae) {
            logger.error("订单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("订单分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 系统
     * 查询订单详情
     *
     * @param
     * @return EsOrderDO
     */
    @Override
    public DubboResult<EsSellerOrderDO> getEsAdminOrderInfo(String orderSn) {
        try {
            // TODO 判断用户权限
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            // 通过订单编号查询订单项商品
            DubboPageResult<EsOrderItemsDO> esOrderItemsByOrderSn = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
            List<EsOrderItemsDO> esOrderItemsDOList1 = esOrderItemsByOrderSn.getData().getList();
            List<EsSellerOrderItemsDO> esOrderItemsDOList = BeanUtil.copyList(esOrderItemsDOList1, EsSellerOrderItemsDO.class);

            //需要返回的订单明细对象
            EsSellerOrderDO esSellerOrderDO = new EsSellerOrderDO();
            List<EsSellerOrderItemsDO> esSellerOrderItemsDOList = new ArrayList<>();

            esSellerOrderItemsDOList = esOrderItemsDOList.stream().map(esSellerOrderItemsDO -> {
                EsSellerOrderItemsDO sellerOrderItemsDO = new EsSellerOrderItemsDO();

                List<EsSpecValuesDO> specList = esSellerOrderItemsDO.getSpecList();

                if (CollectionUtils.isNotEmpty(specList)) {
                    sellerOrderItemsDO.setSpecList(specList);
                }
                // 需要会员模块提供根据GoodsId 获取发票信息的接口
                DubboResult<EsReceiptHistoryDO> receiptHistory = iEsReceiptHistoryService.getReceiptHistoryByGoodsIdAndOrdersn(esSellerOrderItemsDO.getGoodsId(), orderSn);
                // 给每个已经开票的商品进行赋值发票信息
                EsReceiptHistoryDO data = receiptHistory.getData();

                if (data == null) {
                    // 未开票的情况
                    esSellerOrderItemsDO.setInvoiceInformation("未开票");
                } else {
                    // 开票的情况
                    esSellerOrderItemsDO.setInvoiceInformation("已开票");
                    // 赋值发票信息
                    EsMemberReceiptHistoryDO esMemberReceiptHistoryDO = new EsMemberReceiptHistoryDO();
                    BeanUtils.copyProperties(data, esMemberReceiptHistoryDO);
                    esSellerOrderItemsDO.setEsMemberReceiptHistoryDO(esMemberReceiptHistoryDO);
                }

                BeanUtils.copyProperties(esSellerOrderItemsDO, sellerOrderItemsDO);

                return sellerOrderItemsDO;
            }).collect(Collectors.toList());
            // 查询交易单下的订单数 判断订单数量
            int count = this.esOrderMapper.getEsAdminOrderCountByTradeSn(esOrder.getTradeSn());
            BeanUtils.copyProperties(esOrder, esSellerOrderDO);
            // 订单是否拆分
            if (count > 1){
                esSellerOrderDO.setIsItSplit("true");
            }else {
                esSellerOrderDO.setIsItSplit("false");
            }
            esSellerOrderDO.setEsOrderItemsDO(esSellerOrderItemsDOList);

            BeanUtils.copyProperties(esOrder, esSellerOrderDO);
            esSellerOrderDO.setOrderMoney(MathUtil.add(esOrder.getOrderMoney(),esOrder.getShippingMoney()));
            esSellerOrderDO.setEsOrderItemsDO(esSellerOrderItemsDOList);

            return DubboResult.success(esSellerOrderDO);
        } catch (ArgumentException ae) {
            logger.error("SKU查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public EsOrder getOne(Wrapper<EsOrder> queryWrapper, boolean throwEx) {
        return super.getOne(queryWrapper, throwEx);
    }

    /**
     * 卖家端 待删除
     * 查询订单明细
     *
     * @param orderSn
     * @return EsOrderDO
     */
    @Override
    public DubboResult<EsOrderDO> getEsSellerOrder(String orderSn, Long shopId) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn).eq(EsOrder::getShopId, shopId);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            EsOrderDO esOrderDO = new EsOrderDO();
            BeanUtil.copyProperties(esOrder, esOrderDO);
            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * TODO 买家端
     * 查询订单明细表
     *
     * @param orderSn
     * @return EsOrderDO
     */
    @Override
    public DubboResult<EsOrderDO> getEsBuyerOrderInfo(String orderSn) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            // 查询封装订单商品明细
            QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsOrderItems::getOrderSn, orderSn);
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);
            List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
            AtomicReference<String> deliveryMessage = new AtomicReference<>("");
            orderItemsList = esOrderItems.stream().map(orderItems -> {
                QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn,orderItems.getOrderSn())
                        .eq(EsDeliveryInfo::getOrderDetailSn,orderItems.getId())
                        .eq(EsDeliveryInfo::getSkuId,orderItems.getSkuId())
                        .eq(EsDeliveryInfo::getGoodsId,orderItems.getGoodsId());
                List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                BeanUtils.copyProperties(orderItems, orderItemsDO);
                orderItemsDO.setMoney(orderItems.getMoney());
                // 规格
                if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                    List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                    orderItemsDO.setSpecList(specValuesDOList);
                }
                if (infoList.size() > 0){
                    EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                    String content = esDeliveryInfo.getContent();
                    if (StringUtils.isNotEmpty(content)){
                        deliveryMessage.set(content);
                    }
                    orderItemsDO.setIsDelivery(1);
                }else {
                    orderItemsDO.setIsDelivery(2);
                }
                return orderItemsDO;
            }).collect(Collectors.toList());
            EsOrderDO esOrderDO = new EsOrderDO();
            BeanUtil.copyProperties(esOrder, esOrderDO);
            esOrderDO.setEsOrderItemsDO(orderItemsList);
            esOrderDO.setDeliveryMessage(deliveryMessage.get());
            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsBuyerOrderDO> getEsBuyerOrderList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum) {
        Page page = new Page(pageNum, pageSize);
        IPage<EsBuyerOrderDO> esBuyerOrderList = this.esOrderMapper.getEsBuyerOrderList(page, esBuyerOrderQueryDTO);

        List<EsBuyerOrderDO> buyerOrderList = esBuyerOrderList.getRecords();

        buyerOrderList.stream().map(EsBuyerOrderDO -> {
            // 赋值订单商品明细
            String orderSn = EsBuyerOrderDO.getOrderSn();
            DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
            List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOList = new ArrayList<>();
            List<EsOrderItemsDO> esOrderItemsDOList = result.getData().getList();
            esBuyerOrderItemsDOList = esOrderItemsDOList.stream().map(EsOrderItemsDO -> {
                EsBuyerOrderItemsDO esBuyerOrderItemsDO = new EsBuyerOrderItemsDO();
                BeanUtils.copyProperties(EsOrderItemsDO, esBuyerOrderItemsDO);
                // 赋值规格json
                String specJson = EsOrderItemsDO.getSpecJson();
                if (StringUtils.isNotEmpty(specJson)) {
                    List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(specJson, EsSpecValuesDO.class);
                    esBuyerOrderItemsDO.setSpecList(specValuesDOList);
                }
                return esBuyerOrderItemsDO;
            }).collect(Collectors.toList());
            EsBuyerOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOList);
            return buyerOrderList;
        }).collect(Collectors.toList());
        return DubboPageResult.success(esBuyerOrderList.getTotal(), buyerOrderList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderDO> updateOrderStatusMq(OrderStatusChangeMsg orderStatusChangeMsg) {
        try {
            if (orderStatusChangeMsg == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            String tradeSn = orderStatusChangeMsg.getEsOrderDTO().getTradeSn();
            logger.error("订单编号："+tradeSn);
            String tradeVOCache = jedisCluster.get(TradeCachePrefix.TRADE_SESSION_ID_KEY.getPrefix() + tradeSn);
            EsTradeVO tradeVO = null;
            if (!StringUtils.isBlank(tradeVOCache)) {
                tradeVO = JsonUtil.jsonToObject(tradeVOCache, EsTradeVO.class);
            }
            if(tradeVO == null){
              EsTradeDO  tradeDO = esTradeMapper.getEsTradeByTradeSn(tradeSn);
              tradeVO = new EsTradeVO();
              BeanUtil.copyProperties(tradeDO,tradeVO);
              DubboPageResult<EsOrderDO> result = this.getEsOrderInfoByTradeSn(tradeSn);
              if(result.isSuccess()){
                  List<EsOrderDTO> orderDTOList = BeanUtil.copyList(result.getData().getList(),EsOrderDTO.class);
                  tradeVO.setOrderList(orderDTOList);
              }
            }
            List<EsOrderDTO> orderList = tradeVO.getOrderList();
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderStatusChangeMsg.getEsOrderDTO().getOrderSn());
            EsOrder order = new EsOrder();
            order.setOrderSn(orderStatusChangeMsg.getEsOrderDTO().getOrderSn());
            order.setOrderState(orderStatusChangeMsg.getNewStatus().value());
            this.esOrderMapper.update(order, queryWrapper);
            logger.info("MQ修改订单状态成功！");

            // 若新订单状态为 已付款 则发送会员活跃度消息
            if (orderStatusChangeMsg.getNewStatus().name().equals(OrderStatusEnum.PAID_OFF.name())) {

                Map<String, List<EsMemberActiveInfoDTO>> memberActiveMap = new HashMap<String, List<EsMemberActiveInfoDTO>>();
                // 活跃度DTO
                List<EsMemberActiveInfoDTO> memberActiveList = new ArrayList<>();
                EsMemberActiveInfoDTO esMemberActiveInfoDTO = new EsMemberActiveInfoDTO();
                esMemberActiveInfoDTO.setMemberId(orderStatusChangeMsg.getEsOrderDTO().getMemberId());
                esMemberActiveInfoDTO.setOrderSn(orderStatusChangeMsg.getEsOrderDTO().getOrderSn());
                esMemberActiveInfoDTO.setShopId(orderStatusChangeMsg.getEsOrderDTO().getShopId());
                esMemberActiveInfoDTO.setShopName(orderStatusChangeMsg.getEsOrderDTO().getShopName());
                esMemberActiveInfoDTO.setCreateTime(System.currentTimeMillis());
                esMemberActiveInfoDTO.setPaymentTime(System.currentTimeMillis());
                memberActiveList.add(esMemberActiveInfoDTO);
                memberActiveMap.put(ActiveTypeEnum.ADD_ACTIVE.value(), memberActiveList);
                try {
                    // 增加活跃度 发送消息
                    mqProducer.send(member_active_topic, JsonUtil.objectToJson(memberActiveMap));
                    logger.info("会员增加活跃度消息发送成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 订单取消 以及 TODO 全部退货退款
            } else if (orderStatusChangeMsg.getNewStatus().name().equals(OrderStatusEnum.CANCELLED.name())) {

//                orderList.forEach(esOrderDTO -> {
//                    //  订单取消，优惠券数量变更，，变更成功 删除会员优惠券
//                    //  获取优惠券信息
//                    List<EsCouponVO> giftCouponList1 = esOrderDTO.getGiftCouponList();
//                    giftCouponList1.forEach(esCouponVO -> {
//                        Long id = esCouponVO.getId();
//                        DubboResult<EsCouponDO> coupon = iEsCouponService.getCoupon(id);
//                        if (coupon.isSuccess()){
//                            EsCouponDO couponDO = coupon.getData();
//                            // 判断优惠券上架状态 以及 是否可领取
//                            if (couponDO.getIsDel() == 1 /*&& couponDO.getIsReceive() == 1*/ ){
//                                iEsCouponService.redMemberCoupon(esCouponVO,esOrderDTO);
//                            }
//                        }
//                    });
//                });
                // 订单取消，将会影响会员的活跃度，通过ordersn 改变活跃度
                String orderSn = orderStatusChangeMsg.getEsOrderDTO().getOrderSn();
                Map<String, List<String>> orderSnMap = new HashMap<String, List<String>>();
                List<String> orderSns = new ArrayList<>();
                orderSns.add(orderSn);
                orderSnMap.put(ActiveTypeEnum.DELET_ACTIVE.value(), orderSns);
                // 发送消息
                try {
                    mqProducer.send(member_active_topic, JsonUtil.objectToJson(orderSnMap));
                    logger.info("会员降低活跃度消息发送成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("MQ修改订单状态失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("MQ修改订单状态失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsAdminOrderDO> getEsAdminReceiptInfo(String orderSn, Long goodsId) {

        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            EsAdminOrderDO esAdminOrderDO = new EsAdminOrderDO();
            BeanUtils.copyProperties(esOrder, esAdminOrderDO);

            DubboResult<EsReceiptHistoryDO> receiptHistory = iEsReceiptHistoryService.getReceiptHistoryByGoodsIdAndOrdersn(goodsId, orderSn);
            if (receiptHistory != null) {
                EsMemberReceiptHistoryDO esMemberReceiptHistoryDO = new EsMemberReceiptHistoryDO();
                BeanUtils.copyProperties(receiptHistory.getData(), esMemberReceiptHistoryDO);
                esAdminOrderDO.setEsMemberReceiptHistoryDO(esMemberReceiptHistoryDO);
            }

            QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsOrderItems::getOrderSn, orderSn)
                    .eq(EsOrderItems::getGoodsId, goodsId);
            EsOrderItems esOrderItems = this.esOrderItemsMapper.selectOne(wrapper);
            if (esOrderItems != null) {
                EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                BeanUtils.copyProperties(esOrderItems, orderItemsDO);
                esAdminOrderDO.setEsOrderItemsDO(orderItemsDO);
            }
            return DubboResult.success(esAdminOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

   // @Override
   /* public DubboPageResult<EsRFMTradeDO> getEsBuyerMemberLevel() {
       try {
            // 获取会员配置 天数间隔
            DubboPageResult result = iEsMemberRfmConfigService.getMemberRfmConfigListInfo();
            List<EsMemberRfmConfigDO> list = result.getData().getList();
            List<EsOrderDO> orders = null;
            Integer day = 0;
            // 遍历天数间隔 7,15,30天内是否有订单，如果7天内有订单 则直接跳出循环
            for (EsMemberRfmConfigDO esMemberRfmConfigDO : list) {
                day = esMemberRfmConfigDO.getRecency();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS ");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
                Date updateDate4 = calendar.getTime();
                String s = sdf.format(updateDate4) + " ";
                long time = 0;
                time = sdf.parse(s).getTime();
                long currentTime = System.currentTimeMillis();
                orders = this.esOrderMapper.getEsBuyerMemberLevel(currentTime, time);
                if (orders.size() != 0) {
                    break;
                }
            }
            // 超过多少天 不存在订单的情况
            if (orders.size() == 0){
               throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            // 存在订单的情况
            List<EsRFMTradeDO> esRFMTradeDOList = new ArrayList<>();
            Integer finalDay = day;
            esRFMTradeDOList = orders.stream().map(esOrderDO -> {
                EsRFMTradeDO esRFMTradeDO = new EsRFMTradeDO();
                BeanUtils.copyProperties(esOrderDO, esRFMTradeDO);
                esRFMTradeDO.setRecency(finalDay);
                return esRFMTradeDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esRFMTradeDOList);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }

    }*/

    /**
     * 李南山修改订单会员等级
     *
     * @return
     */
    @Override
    public DubboPageResult getEsBuyerMemberLevelLnS() {
        try {
            // 获取会员配置 天数间隔
            DubboPageResult result = iEsMemberRfmConfigService.getMemberRfmConfigListInfo();
            List<EsMemberRfmConfigDO> list = result.getData().getList();
            List<EsRFMTradeDO> orders ;
            List<EsRFMTradeDO> orderDOList = new ArrayList<>();
            //统计多少天以内
            Integer day = 1;
            //前一天标识
            String beforeDay = null;
            for(int num =0;num<list.size();num++){
                day = list.get(num).getRecency();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - (day-1));
                Date updateDate4 = calendar.getTime();
                //计算具体时间段
                String time = sdf.format(updateDate4);
                if( null == beforeDay){
                    Integer judge =1;
                    orders = this.esOrderMapper.getEsBuyerMemberLevelLnsSpace(null,time,judge);
                    List<EsRFMTradeDO> orderResult;
                    if(CollectionUtils.isNotEmpty(orders)){
                        orderResult = this.copyValue(orders,day,null);
                        orderDOList.addAll(orderResult);
                    }
                    beforeDay = time;
                    continue;
                }
                Integer judge =2;
                orders = this.esOrderMapper.getEsBuyerMemberLevelLnsSpace(beforeDay,time,judge);
                List<EsRFMTradeDO> orderResult;
                if(CollectionUtils.isNotEmpty(orders)) {
                    orderResult = this.copyValue(orders, day, null);
                    orderDOList.addAll(orderResult);
                }
                beforeDay = time;
            }
            //超过多少天计算成长值
            if(CollectionUtils.isNotEmpty(list)){
                Integer judge =3;
                orders = this.esOrderMapper.getEsBuyerMemberLevelLnsSpace(beforeDay,beforeDay,judge);
                List<EsRFMTradeDO> orderResult;
                if(CollectionUtils.isNotEmpty(orders)) {
                    orderResult = this.copyValue(orders, day, 1);
                    orderDOList.addAll(orderResult);
                }
            }
            // 超过多少天 不存在订单的情况
            if (orderDOList.size() == 0) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
           return DubboPageResult.success(orderDOList);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量赋值
     * @return
     */
    public List<EsRFMTradeDO> copyValue(List<EsRFMTradeDO> orders,Integer day,Integer sign){
        List<EsRFMTradeDO> esRFMTradeDOList ;
        Integer finalDay = day;
        esRFMTradeDOList = orders.stream().map(esOrderDO -> {
            EsRFMTradeDO esRFMTradeDO = new EsRFMTradeDO();
            BeanUtils.copyProperties(esOrderDO, esRFMTradeDO);
            esRFMTradeDO.setRecency(finalDay);
            if(null != sign){
            esRFMTradeDO.setSign(sign);
            }
            return esRFMTradeDO;
        }).collect(Collectors.toList());
        return esRFMTradeDOList;
    }

    @Override
    public DubboPageResult<EsBuyerOrderDO> getEsMemberOrderList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum) {
        Page page = new Page(pageNum, pageSize);
        IPage<EsBuyerOrderDO> esBuyerOrderList = this.esOrderMapper.getEsMemberOrderList(page, esBuyerOrderQueryDTO);

        List<EsBuyerOrderDO> buyerOrderList = esBuyerOrderList.getRecords();

        buyerOrderList.stream().map(EsBuyerOrderDO -> {
            //订单商品明细
            String orderSn = EsBuyerOrderDO.getOrderSn();
            DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
            List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOList = new ArrayList<>();
            List<EsOrderItemsDO> esOrderItemsDOList = result.getData().getList();
            esBuyerOrderItemsDOList = esOrderItemsDOList.stream().map(EsOrderItemsDO -> {
                EsBuyerOrderItemsDO esBuyerOrderItemsDO = new EsBuyerOrderItemsDO();
                BeanUtils.copyProperties(EsOrderItemsDO, esBuyerOrderItemsDO);
                // 规格json
                String specJson = EsOrderItemsDO.getSpecJson();
                if (StringUtils.isNotEmpty(specJson)) {
                    List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(specJson, EsSpecValuesDO.class);
                    esBuyerOrderItemsDO.setSpecList(specValuesDOList);
                }
                return esBuyerOrderItemsDO;
            }).collect(Collectors.toList());
            EsBuyerOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOList);
            return buyerOrderList;
        }).collect(Collectors.toList());
        return DubboPageResult.success(esBuyerOrderList.getTotal(), buyerOrderList);
    }

    @Override
    public DubboPageResult<EsBuyerOrderDO> getEsMemberReceiptList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize);
            List<String> statusList = new ArrayList<>();
            statusList.add("ROG");
            statusList.add("COMPLETE");
            esBuyerOrderQueryDTO.setOrderStatus(statusList);
            IPage<EsBuyerOrderDO> esBuyerOrderList = this.esOrderMapper.getEsMemberReceiptList(page, esBuyerOrderQueryDTO);

            List<EsBuyerOrderDO> buyerOrderList = esBuyerOrderList.getRecords();

            buyerOrderList.stream().map(EsBuyerOrderDO -> {
                //订单商品明细
                String orderSn = EsBuyerOrderDO.getOrderSn();
                DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
                List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOList = new ArrayList<>();
                List<EsOrderItemsDO> esOrderItemsDOList = result.getData().getList();
                esBuyerOrderItemsDOList = esOrderItemsDOList.stream().map(EsOrderItemsDO -> {
                    // 查询该商品是否支持开发票
                    DubboResult<EsGoodsCO> esGoods = goodsService.getEsGoods(EsOrderItemsDO.getGoodsId());
                    EsGoodsCO goodsCO = esGoods.getData();
                    if (!esGoods.isSuccess()) {
                        throw new ArgumentException(TradeErrorCode.GOODS_DATE_QUERY_ERROR.getErrorCode(), TradeErrorCode.GOODS_DATE_QUERY_ERROR.getErrorMsg());
                    }
                    EsBuyerOrderItemsDO esBuyerOrderItemsDO = new EsBuyerOrderItemsDO();
                    Integer invoice =  goodsCO.getIsInvoice() == null ? 2 :  goodsCO.getIsInvoice();
                    if (invoice == 2) {
                        // 该商品无法开具发票
                        esBuyerOrderItemsDO.setReceiptState(3);

                    } else {
                        // 获取发票历史信息
                        DubboResult<EsReceiptHistoryDO> receiptHistory =
                                iEsReceiptHistoryService.getReceiptHistoryByGoodsIdAndOrdersn(EsOrderItemsDO.getGoodsId(), EsOrderItemsDO.getOrderSn());
                        EsReceiptHistoryDO receiptHistoryData = receiptHistory.getData();
                        if (receiptHistoryData == null) {
                            esBuyerOrderItemsDO.setReceiptType(goodsCO.getInvoiceType());
                            esBuyerOrderItemsDO.setReceiptState(2); // 未开票
                            esBuyerOrderItemsDO.setReceiptOperation("申请开票");
                            esBuyerOrderItemsDO.setReceiptType(goodsCO.getInvoiceType());
                        } else {
                            esBuyerOrderItemsDO.setReceiptType(goodsCO.getInvoiceType());
                            esBuyerOrderItemsDO.setReceiptState(1);
                            esBuyerOrderItemsDO.setReceiptOperation("查看发票");
                            esBuyerOrderItemsDO.setReceiptType(goodsCO.getInvoiceType());
                        }
                    }

                    BeanUtils.copyProperties(EsOrderItemsDO, esBuyerOrderItemsDO);
                    // 规格json
                    String specJson = EsOrderItemsDO.getSpecJson();
                    if (StringUtils.isNotEmpty(specJson)) {
                        List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(specJson, EsSpecValuesDO.class);
                        esBuyerOrderItemsDO.setSpecList(specValuesDOList);
                    }
                    return esBuyerOrderItemsDO;
                }).collect(Collectors.toList());
                EsBuyerOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOList);
                return buyerOrderList;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esBuyerOrderList.getTotal(), buyerOrderList);
        } catch (ArgumentException ae) {
            logger.error("发票信息查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票信息查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据订单编号和商品id查询订单及订单物品明细
     *
     * @param orderSn 订单编号
     * @param goodsId 商品id
     * @author: libw 981087977@qq.com
     * @date: 2019/09/19 13:21:01
     * @return: com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsOrderDO>
     */
    @Override
    public DubboResult<EsOrderDO> getOrderItem(String orderSn, Long goodsId) {
        return null;
    }

    @Override
    public DubboResult<EsOrderDO> getEsBuyerOrder(String orderSn, Long memberId) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn).eq(EsOrder::getMemberId,memberId);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            EsOrderDO esOrderDO = new EsOrderDO();
            BeanUtils.copyProperties(esOrder, esOrderDO);

            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsOrderDO> getEsBuyerOrderInfoAfterSale(String orderSn) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
            EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }

            EsOrderDO esOrderDO = new EsOrderDO();
            BeanUtils.copyProperties(esOrder, esOrderDO);
            // 获取商品项信息
            QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);
            List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOS = BeanUtil.copyList(esOrderItems, EsBuyerOrderItemsDO.class);
            esOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOS);

            // 初始化订单允许状态
            OrderOperateAllowable operateAllowableVO = new OrderOperateAllowable(
                    OrderStatusEnum.valueOf(esOrder.getOrderState()),
                    CommentStatusEnum.valueOf(esOrder.getCommentStatus()),
                    ShipStatusEnum.valueOf(esOrder.getShipState()),
                    ServiceStatusEnum.valueOf(esOrder.getServiceState()),
                    PayStatusEnum.valueOf(esOrder.getPayState()));
            esOrderDO.setOrderOperateAllowable(operateAllowableVO);
            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    @Override
    public DubboResult<EsServiceOrderDO> getEsCancelOrderInfo(EsOrderDTO esOrderDTO) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn,esOrderDTO.getOrderSn())
                    .eq(EsOrder::getMemberId,esOrderDTO.getMemberId())
                    .eq(EsOrder::getIsDel,esOrderDTO.getIsDel());
            EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);
            // 封装DO
            EsServiceOrderDO esServiceOrderDO = new EsServiceOrderDO();
            BeanUtils.copyProperties(esOrder,esServiceOrderDO);
            // 查询 商品项
            DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(esOrderDTO.getOrderSn());
            List<EsOrderItemsDO> esOrderItemsByOrderSn = result.getData().getList();
            // 判断该订单付款 是通过交易单支付还是单个订单支付
            if (StringUtils.isBlank(esOrder.getPaymentMethodName())){
                if (CollectionUtils.isNotEmpty(esOrderItemsByOrderSn)){
                    QueryWrapper<EsTrade> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsTrade::getTradeSn,esOrder.getTradeSn());
                    // TODO
                    EsTrade esTrade = this.esTradeMapper.selectOne(wrapper);
                    String paymentMethod = "";
                    // 如果交易单中 使用余额不为0,则该交易使用了余额
                    if (esTrade.getUseBalance() > 0){
                        if (esTrade.getPayMoney() > 0){
                            paymentMethod = "余额+" + esTrade.getPaymentMethodName();
                        }else {
                            paymentMethod = "余额";
                        }
                    }else {
                        paymentMethod = esTrade.getPaymentMethodName();
                    }
                    esServiceOrderDO.setPaymentMethod(paymentMethod);
                    esServiceOrderDO.setEsOrderItemsListDO(esOrderItemsByOrderSn);
                }
            }
            return DubboResult.success(esServiceOrderDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsOrderFlow> getOrderFlow(String orderSn, Long shopId) {
        try{
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn).eq(EsOrder::getShopId, shopId);
            EsOrder esOrder = this.getOne(queryWrapper);
            if(esOrder == null){
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(), TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            String orderStatus = esOrder.getOrderState();
            String paymentType = esOrder.getPaymentType();
            //新订单
            EsOrderFlow orderCreateFlow = new EsOrderFlow(OrderStatusEnum.NEW);
            //订单确认
            EsOrderFlow orderConfirmFlow = new EsOrderFlow(OrderStatusEnum.CONFIRM);
            //订单已付款
            EsOrderFlow orderPaidOffFlow = new EsOrderFlow(OrderStatusEnum.PAID_OFF);
            //已发货
            EsOrderFlow orderShippedFlow = new EsOrderFlow(OrderStatusEnum.SHIPPED);
            //已收货
            EsOrderFlow orderRogFlow = new EsOrderFlow(OrderStatusEnum.ROG);
            //已完成
            EsOrderFlow orderCompleteFlow = new EsOrderFlow(OrderStatusEnum.COMPLETE);
            //售后中
            EsOrderFlow orderAfteserviceFlow = new EsOrderFlow(OrderStatusEnum.AFTER_SERVICE);
            //已取消
            EsOrderFlow orderCancelledFlow = new EsOrderFlow(OrderStatusEnum.CANCELLED);
            List<EsOrderFlow> resultFlow = new ArrayList<>();
            resultFlow.add(orderCreateFlow);
            resultFlow.add(orderConfirmFlow);
            //订单取消
            if (OrderStatusEnum.CANCELLED.value().equals(orderStatus)) {
                resultFlow.add(orderCancelledFlow);

                //货到付款
            } else if (PaymentTypeEnum.COD.value().equals(paymentType)) {
                resultFlow.add(orderShippedFlow);
                resultFlow.add(orderRogFlow);
                resultFlow.add(orderPaidOffFlow);
                resultFlow.add(orderCompleteFlow);
            } else {//款到发货
                resultFlow.add(orderPaidOffFlow);
                resultFlow.add(orderShippedFlow);
                resultFlow.add(orderRogFlow);
                resultFlow.add(orderCompleteFlow);
            }
            boolean flowFinish = true;
            int i = 1;

            //0:灰色,1:普通显示,2:结束显示,3:取消显示
            for (EsOrderFlow flow : resultFlow) {
                if (flowFinish) {
                    if (flow.getOrderStatus().equals(OrderStatusEnum.CANCELLED.value())) {
                        flow.setShowStatus(3);
                    } else {
                        if (i == resultFlow.size()) {
                            flow.setShowStatus(2);
                        } else {
                            flow.setShowStatus(1);
                        }
                    }
                }
                i++;
                if (flow.getOrderStatus().equals(orderStatus)) {
                    flowFinish = false;
                    break;
                }
            }
            return DubboPageResult.success(resultFlow);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult ship(EsDeliveryDTO deliveryDTO, OrderPermission permission) {
        try{
             DubboResult result= esOrderOperateService.ship(deliveryDTO,permission);
            return DubboResult.success(result.getData());
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsServiceOrderDO> getEsServiceOrderInfo(String orderSn, Long skuId, Long memberId) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn)
                    .eq(EsOrder::getMemberId,memberId)
                    .eq(EsOrder::getIsDel,0);
            EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);
            if (esOrder == null){
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            // 封装DO
            EsServiceOrderDO esServiceOrderDO = new EsServiceOrderDO();
            BeanUtils.copyProperties(esOrder,esServiceOrderDO);
            // 查询 商品项
            DubboResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsAfterSaleOrderItemsByOrderSnAndSkuId(orderSn, skuId);
            EsOrderItemsDO orderItemsDO = result.getData();
            String paymentMethod = "";
            // 如果交易单中 使用余额不为0,则该交易使用了余额 TODO 是否要根据订单 es_order
//            if (esOrder.getNeedPayMoney() >= 0){
//                if (esOrder.getPayedMoney() > 0 && esOrder.getNeedPayMoney() > 0){
//                    paymentMethod = "余额+" + esOrder.getPaymentMethodName();
//                }else if (esOrder.getPayedMoney() > 0){
//                    paymentMethod = "余额";
//                }
//            }else {
//                paymentMethod = esOrder.getPaymentMethodName();
//            }
            esServiceOrderDO.setPaymentMethod(esOrder.getPaymentMethodName());
            esServiceOrderDO.setEsOrderItemsDO(orderItemsDO);

            return DubboResult.success(esServiceOrderDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsServiceOrderDO> getEsRefundOrderList(String orderSn, Long memberId) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn)
                    .eq(EsOrder::getMemberId,memberId)
                    .eq(EsOrder::getIsDel,0);
            EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);
            if (esOrder == null){
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            // 封装DO
            EsServiceOrderDO esServiceOrderDO = new EsServiceOrderDO();
            BeanUtils.copyProperties(esOrder,esServiceOrderDO);
            // 查询 商品项集合
            DubboPageResult<EsOrderItemsDO> items = iEsOrderItemsService.getEsRefundOrderItemsByOrderSnAnd(orderSn);
            List<EsOrderItemsDO> list = items.getData().getList();
            String paymentMethod = "";
            // 如果交易单中 使用余额不为0,则该交易使用了余额 TODO 是否要根据订单 es_order
//            if (esOrder.getNeedPayMoney() >= 0){
//                if (esOrder.getPayedMoney() > 0 && esOrder.getNeedPayMoney() > 0){
//                    paymentMethod = "余额+" + esOrder.getPaymentMethodName();
//                }else if (esOrder.getPayedMoney() > 0){
//                    paymentMethod = "余额";
//                }
//            }else {
//                paymentMethod = esOrder.getPaymentMethodName();
//            }
            esServiceOrderDO.setPaymentMethod(esOrder.getPaymentMethodName());
            esServiceOrderDO.setEsOrderItemsListDO(list);

            return DubboResult.success(esServiceOrderDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsOrderDO> getEsOrderInfo(String orderSn) {
        try {
            QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrder::getOrderSn, orderSn);
            EsOrder esOrder = this.getOne(queryWrapper);
            if (esOrder == null) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
            }
            EsOrderDO esOrderDO = new EsOrderDO();
            BeanUtil.copyProperties(esOrder, esOrderDO);
            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<OrderStatusNumVO> getOrderStatusNum(Long id) {
        QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrder::getMemberId,id);
        // 查出该会员的所有订单
        List<EsOrderDO> orderStatusNum = this.esOrderMapper.getOrderStatusNum(id);
        OrderStatusNumVO numVO = new OrderStatusNumVO();

        for (EsOrderDO esOrderDO:orderStatusNum) {
            // 支付状态未支付，订单状态已确认，为待付款订单
            if (true) {
                // 待付款需要根据交易单数量
                QueryWrapper<EsTrade> tradeWrapper = new QueryWrapper<>();
                tradeWrapper.lambda().eq(EsTrade::getMemberId,id).eq(EsTrade::getTradeStatus,OrderStatusEnum.CONFIRM.value());
                Integer integer = this.esTradeMapper.selectCount(tradeWrapper);
                numVO.setWaitPayNum(integer);
            }
            // 物流状态为未发货，订单状态为已收款，为待发货订单
            if (OrderStatusEnum.PAID_OFF.value().equals(esOrderDO.getOrderState())) {
                numVO.setWaitShipNum(esOrderDO.getCount());
            }
            // 订单状态为已发货，为待收货订单
            if (OrderStatusEnum.SHIPPED.value().equals(esOrderDO.getOrderState())) {
                numVO.setWaitRogNum(esOrderDO.getCount());
            }
            // 订单状态为已收货，为待评价订单
            if (OrderStatusEnum.ROG.value().equals(esOrderDO.getOrderState())) {
                QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(EsOrderItems::getOrderSn,esOrderDO.getOrderSn());
                wrapper.lambda().eq(EsOrderItems::getSingleCommentStatus,CommentStatusEnum.UNFINISHED.value());
                Integer count = esOrderItemsMapper.selectCount(wrapper);
                numVO.setWaitCommentNum(count);
            }
            // 订单状态为申请售后，为待售后
            if (OrderStatusEnum.AFTER_SERVICE.value().equals(esOrderDO.getOrderState())) {
                numVO.setWaitCommentNum(esOrderDO.getCount());
            }
        }

        return DubboResult.success(numVO);
    }

    @Override
    public DubboResult<EsOrderDO> getEsBuyerOrderDetails(String orderSn, String orderState) {
        try {
            EsOrderDO esOrderDO = new EsOrderDO();
            if ("CONFIRM".equals(orderState)){
                // 封装交易单的订单详情

                QueryWrapper<EsOrder> orderQueryWrapper = new QueryWrapper<>();
                orderQueryWrapper.lambda().eq(EsOrder::getTradeSn,orderSn);
                List<EsOrder> orders = this.esOrderMapper.selectList(orderQueryWrapper);
                String remark = "";
                for (EsOrder order : orders) {
                    if (order.getRemark() != null) {
                        remark = remark + order.getRemark() + "。";
                    }
                }
                QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsTrade::getTradeSn, orderSn);
                EsTrade esTrade = this.esTradeMapper.selectOne(queryWrapper);
                if (esTrade == null) {
                    throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
                }
                // 查询封装订单商品明细
                QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(EsOrderItems::getTradeSn, orderSn);
                List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);

                // 如果存在赠品则获取赠品信息
                DubboResult<EsOrderMetaDO> orderMeta = iEsOrderMetaService.getOrderMetaByOrderSnAndMetaKey(orderSn, OrderMetaKeyEnum.GIFT.name());
                // 赠品list
                List<EsOrderItemsDO> zpItemsDOList = new ArrayList<>();
                if (orderMeta.isSuccess()){

                    String metaValue = orderMeta.getData().getMetaValue();
                    List<EsFullDiscountGiftDO> esFullDiscountGiftDOS = JSONArray.parseArray(metaValue, EsFullDiscountGiftDO.class);
                    esFullDiscountGiftDOS.forEach(esFullDiscountGiftDO -> {
                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        orderItemsDO.setId(esFullDiscountGiftDO.getId());
                        orderItemsDO.setName(esFullDiscountGiftDO.getGiftName());
                        orderItemsDO.setImage(esFullDiscountGiftDO.getGiftImg());
                        orderItemsDO.setMoney(esFullDiscountGiftDO.getGiftMoney());
                        orderItemsDO.setGoodsId(esFullDiscountGiftDO.getGoodsId());
                        orderItemsDO.setNum(1);
                        orderItemsDO.setIsGift(1);
                        zpItemsDOList.add(orderItemsDO);
                    });
                }

                List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                AtomicReference<String> deliveryTime = new AtomicReference<>("");
                orderItemsList = esOrderItems.stream().map(orderItems -> {
                    QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn,orderItems.getOrderSn())
                            .eq(EsDeliveryInfo::getOrderDetailSn,orderItems.getId())
                            .eq(EsDeliveryInfo::getSkuId,orderItems.getSkuId())
                            .eq(EsDeliveryInfo::getGoodsId,orderItems.getGoodsId());
                    List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                    EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                    BeanUtils.copyProperties(orderItems, orderItemsDO);
                    orderItemsDO.setStateText(ServiceStatusEnum.valueOf(orderItems.getState()).description());
                    orderItemsDO.setMoney(orderItems.getMoney());
                    // 规格
                    if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                        List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                        orderItemsDO.setSpecList(specValuesDOList);
                    }
                    if (infoList.size() > 0){
                        EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                        String content = esDeliveryInfo.getContent();
                        if (StringUtils.isNotEmpty(content)){
                            deliveryMessage.set(content);
                        }
                        if (StringUtils.isNotEmpty(esDeliveryInfo.getDeliveryTime())){
                            deliveryTime.set(esDeliveryInfo.getDeliveryTime());
                        }
                        orderItemsDO.setIsDelivery(1);
                    }else {
                        orderItemsDO.setIsDelivery(2);
                    }
                    // 获取商品信息
                    DubboResult goodsResult = goodsService.getEsBuyerGoods(orderItems.getGoodsId());
                    if (goodsResult.isSuccess()){
                        EsGoodsCO goods = (EsGoodsCO) goodsResult.getData();
                        Integer marketEnable = goods.getMarketEnable();
                        if (marketEnable == 2){
                            orderItemsDO.setMarketEnable(2);
                        }else {
                            orderItemsDO.setMarketEnable(1);
                        }
                    }
                    return orderItemsDO;
                }).collect(Collectors.toList());
                orderItemsList.addAll(zpItemsDOList);
                BeanUtil.copyProperties(esTrade, esOrderDO);
                esOrderDO.setOrderState(esTrade.getTradeStatus());
                esOrderDO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                esOrderDO.setShipAddr(esTrade.getConsigneeAddress());
                esOrderDO.setShipCity(esTrade.getConsigneeCity());
                esOrderDO.setShipProvince(esTrade.getConsigneeProvince());
                esOrderDO.setShipCounty(esTrade.getConsigneeCounty());
                esOrderDO.setShipMobile(esTrade.getConsigneeMobile());
                esOrderDO.setShipName(esTrade.getConsigneeName());
                esOrderDO.setOrderMoney(esTrade.getTotalMoney());
                esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                esOrderDO.setShipTown(esTrade.getConsigneeTown());
                esOrderDO.setEsOrderItemsDO(orderItemsList);
                esOrderDO.setDeliveryMessage(deliveryMessage.get());
                esOrderDO.setDeliveryTime(deliveryTime.get());
                esOrderDO.setCancelTime(esOrderDO.getCancelTime());
                esOrderDO.setRemark(remark);

                esOrderDO.setOrderSn(esTrade.getTradeSn());

            }else {
                // 封装订单的订单详情
                QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
                EsOrder esOrder = this.getOne(queryWrapper);
                // 规避订单在订单详情页面取消订单时 查询商品详情失败问题 订单取消后订单状态是 CANCELLED  传的订单编号仍是交易单号 所以当查询为空时 查询交易表中的订单详情
                if (esOrder == null) {
                    // 封装交易单的订单详情
                    QueryWrapper<EsTrade> tradeWrapper = new QueryWrapper<>();
                    tradeWrapper.lambda().eq(EsTrade::getTradeSn, orderSn);

//                    QueryWrapper<EsOrder> orderQueryWrapper = new QueryWrapper<>();
//                    orderQueryWrapper.lambda().eq(EsOrder::getTradeSn,orderSn);
//                    List<EsOrder> orders = this.esOrderMapper.selectList(orderQueryWrapper);
//                    String remark = "";
//                    for (EsOrder order : orders) {
//                        if (order.getRemark() != null) {
//                            remark = remark + order.getRemark() + ";";
//                        }
//                    }

                    EsTrade esTrade = this.esTradeMapper.selectOne(tradeWrapper);
                    if (esTrade == null) {
                        throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
                    }
                    // 查询封装订单商品明细
                    QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(EsOrderItems::getTradeSn, orderSn);
                    List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);

                    // 如果存在赠品则获取赠品信息
                    DubboResult<EsOrderMetaDO> orderMeta = iEsOrderMetaService.getOrderMetaByOrderSnAndMetaKey(orderSn, OrderMetaKeyEnum.GIFT.name());
                    // 赠品list
                    List<EsOrderItemsDO> zpItemsDOList = new ArrayList<>();
                    if (orderMeta.isSuccess()){

                        String metaValue = orderMeta.getData().getMetaValue();
                        List<EsFullDiscountGiftDO> esFullDiscountGiftDOS = JSONArray.parseArray(metaValue, EsFullDiscountGiftDO.class);
                        esFullDiscountGiftDOS.forEach(esFullDiscountGiftDO -> {
                            EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                            orderItemsDO.setId(esFullDiscountGiftDO.getId());
                            orderItemsDO.setName(esFullDiscountGiftDO.getGiftName());
                            orderItemsDO.setImage(esFullDiscountGiftDO.getGiftImg());
                            orderItemsDO.setMoney(esFullDiscountGiftDO.getGiftMoney());
                            orderItemsDO.setGoodsId(esFullDiscountGiftDO.getGoodsId());
                            orderItemsDO.setNum(1);
                            orderItemsDO.setIsGift(1);
                            zpItemsDOList.add(orderItemsDO);
                        });
                    }

                    List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                    AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                    AtomicReference<String> deliveryTime = new AtomicReference<>("");

                    orderItemsList = esOrderItems.stream().map(orderItems -> {
                        QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn,orderItems.getOrderSn())
                                .eq(EsDeliveryInfo::getOrderDetailSn,orderItems.getId())
                                .eq(EsDeliveryInfo::getSkuId,orderItems.getSkuId())
                                .eq(EsDeliveryInfo::getGoodsId,orderItems.getGoodsId());
                        List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        BeanUtils.copyProperties(orderItems, orderItemsDO);
                        orderItemsDO.setMoney(orderItems.getMoney());
                        orderItemsDO.setStateText(ServiceStatusEnum.valueOf(orderItems.getState()).description());
                        //如果订单已申请售后
                        if (!"NOT_APPLY".equals(orderItems.getState()) && !"EXPIRED".equals(orderItems.getState())){
                            DubboResult<EsRefundDO> refundSn = esRefundService.getRefundSn(orderItemsDO.getOrderSn(), orderItemsDO.getSkuId());
                            if (refundSn.isSuccess() && refundSn.getData() != null){
                                EsRefundDO esRefundDO = refundSn.getData();
                                orderItemsDO.setServiceHandleStatus(RefundTypeEnum.valueOf(esRefundDO.getRefundType()).description()+":"+
                                        ProcessStatusEnum.valueOf(esRefundDO.getProcessStatus()).description());
                                orderItemsDO.setReFundSn(Long.valueOf(esRefundDO.getSn()));
                            }
                        }
                        // 规格
                        if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                            List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                            orderItemsDO.setSpecList(specValuesDOList);
                        }
                        if (infoList.size() > 0){
                            EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                            String content = esDeliveryInfo.getContent();
                            if (StringUtils.isNotEmpty(content)){
                                deliveryMessage.set(content);
                            }
                            if (StringUtils.isNotEmpty(esDeliveryInfo.getDeliveryTime())){
                                deliveryTime.set(esDeliveryInfo.getDeliveryTime());
                            }
                            orderItemsDO.setIsDelivery(1);
                        }else {
                            orderItemsDO.setIsDelivery(2);
                        }
                        return orderItemsDO;
                    }).collect(Collectors.toList());
                    orderItemsList.addAll(zpItemsDOList);
                    BeanUtil.copyProperties(esTrade, esOrderDO);
                    esOrderDO.setOrderState(esTrade.getTradeStatus());
                    esOrderDO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                    esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                    esOrderDO.setShipAddr(esTrade.getConsigneeAddress());
                    esOrderDO.setShipCity(esTrade.getConsigneeCity());
                    esOrderDO.setShipProvince(esTrade.getConsigneeProvince());
                    esOrderDO.setShipCounty(esTrade.getConsigneeCounty());
                    esOrderDO.setShipMobile(esTrade.getConsigneeMobile());
                    esOrderDO.setShipName(esTrade.getConsigneeName());
                    esOrderDO.setOrderMoney(esTrade.getTotalMoney());
                    esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                    esOrderDO.setShipTown(esTrade.getConsigneeTown());
                    esOrderDO.setEsOrderItemsDO(orderItemsList);
                    esOrderDO.setDeliveryMessage(deliveryMessage.get());
                    esOrderDO.setDeliveryTime(deliveryTime.get());
                    esOrderDO.setCancelTime(esTrade.getCancelTime());
                    esOrderDO.setRemark(esOrder.getRemark());
                    esOrderDO.setOrderSn(orderSn);
                }else {
                    // 查询封装订单商品明细
                    QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(EsOrderItems::getOrderSn, orderSn);
                    List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);

                    // 如果存在赠品则获取赠品信息
                    DubboResult<EsOrderMetaDO> orderMeta = iEsOrderMetaService.getOrderMetaByOrderSnAndMetaKey(orderSn, OrderMetaKeyEnum.GIFT.name());
                    // 赠品list
                    List<EsOrderItemsDO> zpItemsDOList = new ArrayList<>();
                    if (orderMeta.isSuccess()){

                        String metaValue = orderMeta.getData().getMetaValue();
                        List<EsFullDiscountGiftDO> esFullDiscountGiftDOS = JSONArray.parseArray(metaValue, EsFullDiscountGiftDO.class);
                        esFullDiscountGiftDOS.forEach(esFullDiscountGiftDO -> {
                            EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                            orderItemsDO.setId(esFullDiscountGiftDO.getId());
                            orderItemsDO.setName(esFullDiscountGiftDO.getGiftName());
                            orderItemsDO.setImage(esFullDiscountGiftDO.getGiftImg());
                            orderItemsDO.setMoney(esFullDiscountGiftDO.getGiftMoney());
                            orderItemsDO.setGoodsId(esFullDiscountGiftDO.getGoodsId());
                            orderItemsDO.setNum(1);
                            orderItemsDO.setIsGift(1);
                            zpItemsDOList.add(orderItemsDO);
                        });
                    }

                    List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                    AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                    AtomicReference<String> deliveryTime = new AtomicReference<>("");
                    orderItemsList = esOrderItems.stream().map(orderItems -> {
                        QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn, orderItems.getOrderSn())
                                .eq(EsDeliveryInfo::getOrderDetailSn, orderItems.getId())
                                .eq(EsDeliveryInfo::getSkuId, orderItems.getSkuId())
                                .eq(EsDeliveryInfo::getGoodsId, orderItems.getGoodsId());
                        List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        BeanUtils.copyProperties(orderItems, orderItemsDO);
                        orderItemsDO.setStateText(ServiceStatusEnum.valueOf(orderItems.getState()).description());
                        orderItemsDO.setMoney(orderItems.getMoney());
                        //如果订单已申请售后
                        if (!"NOT_APPLY".equals(orderItems.getState()) && !"EXPIRED".equals(orderItems.getState())){
                            DubboResult<EsRefundDO> refundSn = esRefundService.getRefundSn(orderItemsDO.getOrderSn(), orderItemsDO.getSkuId());
                            if (refundSn.isSuccess() && refundSn.getData() != null){
                                EsRefundDO esRefundDO = refundSn.getData();
                                orderItemsDO.setServiceHandleStatus(RefundTypeEnum.valueOf(esRefundDO.getRefundType()).description()+":"+
                                        ProcessStatusEnum.valueOf(esRefundDO.getProcessStatus()).description());
                                orderItemsDO.setReFundSn(Long.valueOf(esRefundDO.getSn()));
                            }
                        }
                        // 规格
                        if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                            List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                            orderItemsDO.setSpecList(specValuesDOList);
                        }
                        if (infoList.size() > 0) {
                            EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                            String content = esDeliveryInfo.getContent();
                            if (StringUtils.isNotEmpty(content)) {
                                deliveryMessage.set(content);
                            }
                            if (StringUtils.isNotEmpty(esDeliveryInfo.getDeliveryTime())){
                                deliveryTime.set(esDeliveryInfo.getDeliveryTime());
                            }
                            orderItemsDO.setIsDelivery(1);
                        } else {
                            orderItemsDO.setIsDelivery(2);
                        }
                        return orderItemsDO;
                    }).collect(Collectors.toList());
                    orderItemsList.addAll(zpItemsDOList);
                    List<EsOrderItems> collect1 = esOrderItems.stream().filter(esOrderItemsDO -> esOrderItemsDO.getShipNo() != null).collect(Collectors.toList());

                    //过滤出来已经发货的商品
                    if (!"SHIP_NO".equals(esOrder.getShipState())|| collect1.size() > 0){
                         //去重
                        List<EsOrderItems> collect = collect1.stream().filter(distinctByKey(o -> o.getShipNo())).collect(Collectors.toList());

                        logger.info("去重后前集合大小"+collect1.size()+"集合"+collect1);
                        logger.info("去重后集合大小"+collect.size()+"集合"+collect);
                        List<ExpressDetailsDO> expressList = new ArrayList<>();
                        collect.forEach(esOrderItemsDO -> {
                            ExpressDetailsDO expressDetailsDO = new ExpressDetailsDO();
                            DubboResult<ExpressDetailVO> expressFormDetail = this.expressPlatformService.getExpressFormDetail(esOrderItemsDO.getLogiId(), esOrderItemsDO.getShipNo());
                            BeanUtil.copyProperties(expressFormDetail.getData(),expressDetailsDO);
                            // 获取每一个订单号对应的图片信息
                            DubboPageResult<EsOrderItemsDO> orderItemsByOrderSnAndShipNo = iEsOrderItemsService.getEsOrderItemsByOrderSnAndShipNo(esOrderItemsDO.getOrderSn(), esOrderItemsDO.getShipNo());
                            List<EsOrderItemsDO> list1 = orderItemsByOrderSnAndShipNo.getData().getList();
                            List<String> imageList = list1.stream().map(EsOrderItemsDO::getImage).collect(Collectors.toList());
                            expressDetailsDO.setImage(imageList);
                            expressList.add(expressDetailsDO);
                        });
                        esOrderDO.setExpressDetailsList(expressList);
                    }
                    BeanUtil.copyProperties(esOrder, esOrderDO);
                    esOrderDO.setOrderMoney(MathUtil.add(esOrder.getOrderMoney(),esOrder.getShippingMoney()));
                    esOrderDO.setEsOrderItemsDO(orderItemsList);
                    esOrderDO.setDeliveryMessage(deliveryMessage.get());
                    esOrderDO.setItemsJson(null);
                    esOrderDO.setDeliveryTime(deliveryTime.get());
                    esOrderDO.setOrderSn(esOrder.getOrderSn());
                }
            }
            return DubboResult.success(esOrderDO);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }



    @Override
    public DubboPageResult<EsOrderDO> getEsWapOrderDetails(String orderSn, String orderState) {
        try {
            List<EsOrderDO> orderDOS=new ArrayList<>();

            if ("CONFIRM".equals(orderState)){
                // 封装交易单的订单详情

                QueryWrapper<EsOrder> orderQueryWrapper = new QueryWrapper<>();
                orderQueryWrapper.lambda().eq(EsOrder::getTradeSn,orderSn);
                List<EsOrder> orders = this.esOrderMapper.selectList(orderQueryWrapper);

                QueryWrapper<EsTrade> tradeQuery = new QueryWrapper<>();
                tradeQuery.lambda().eq(EsTrade::getTradeSn,orderSn);
                EsTrade esTrade = this.esTradeMapper.selectOne(tradeQuery);

                for (EsOrder order: orders) {
                    // 查询封装订单商品明细
                    QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(EsOrderItems::getOrderSn, order.getOrderSn());
                    List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);
                    List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                    EsOrderDO esOrderDO = new EsOrderDO();
                    AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                    orderItemsList = esOrderItems.stream().map(orderItems -> {
                        QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn, orderItems.getOrderSn())
                                .eq(EsDeliveryInfo::getOrderDetailSn, orderItems.getId())
                                .eq(EsDeliveryInfo::getSkuId, orderItems.getSkuId())
                                .eq(EsDeliveryInfo::getGoodsId, orderItems.getGoodsId());
                        List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        BeanUtils.copyProperties(orderItems, orderItemsDO);
                        orderItemsDO.setMoney(orderItems.getMoney());
                        // 规格
                        if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                            List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                            orderItemsDO.setSpecList(specValuesDOList);
                        }
                        if (infoList.size() > 0) {
                            EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                            String content = esDeliveryInfo.getContent();
                            if (StringUtils.isNotEmpty(content)) {
                                deliveryMessage.set(content);
                            }
                            orderItemsDO.setIsDelivery(1);
                        } else {
                            orderItemsDO.setIsDelivery(2);
                        }
                        return orderItemsDO;
                    }).collect(Collectors.toList());
                    BeanUtil.copyProperties(order, esOrderDO);
                    if (esTrade!=null){
                        esOrderDO.setOrderMoney(esTrade.getTotalMoney());
                    }
                    esOrderDO.setEsOrderItemsDO(orderItemsList);
                    esOrderDO.setDeliveryMessage(deliveryMessage.get());
                    orderDOS.add(esOrderDO);
                }
            }else {
                // 封装订单的订单详情
                EsOrderDO esOrderDO = new EsOrderDO();
                QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
                EsOrder esOrder = this.getOne(queryWrapper);
                // 规避订单在订单详情页面取消订单时 查询商品详情失败问题 订单取消后订单状态是 CANCELLED  传的订单编号仍是交易单号 所以当查询为空时 查询交易表中的订单详情
                if (esOrder == null) {
                    // 封装交易单的订单详情
                    QueryWrapper<EsTrade> tradeWrapper = new QueryWrapper<>();
                    tradeWrapper.lambda().eq(EsTrade::getTradeSn, orderSn);

                    QueryWrapper<EsOrder> orderQueryWrapper = new QueryWrapper<>();
                    orderQueryWrapper.lambda().eq(EsOrder::getTradeSn,orderSn);
                    List<EsOrder> orders = this.esOrderMapper.selectList(orderQueryWrapper);
                    String remark = "";
                    for (EsOrder order : orders) {
                        if (order.getRemark() != null) {
                            remark = remark + order.getRemark() + ";";
                        }
                    }

                    EsTrade esTrade = this.esTradeMapper.selectOne(tradeWrapper);
                    if (esTrade == null) {
                        throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
                    }
                    // 查询封装订单商品明细
                    QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(EsOrderItems::getTradeSn, orderSn);
                    List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);
                    List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                    AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                    AtomicReference<String> deliveryTime = new AtomicReference<>("");

                    orderItemsList = esOrderItems.stream().map(orderItems -> {
                        QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn,orderItems.getOrderSn())
                                .eq(EsDeliveryInfo::getOrderDetailSn,orderItems.getId())
                                .eq(EsDeliveryInfo::getSkuId,orderItems.getSkuId())
                                .eq(EsDeliveryInfo::getGoodsId,orderItems.getGoodsId());
                        List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        BeanUtils.copyProperties(orderItems, orderItemsDO);
                        orderItemsDO.setMoney(orderItems.getMoney());
                        // 规格
                        if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                            List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                            orderItemsDO.setSpecList(specValuesDOList);
                        }
                        if (infoList.size() > 0){
                            EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                            String content = esDeliveryInfo.getContent();
                            if (StringUtils.isNotEmpty(content)){
                                deliveryMessage.set(content);
                            }
                            if (StringUtils.isNotEmpty(esDeliveryInfo.getDeliveryTime())){
                                deliveryTime.set(esDeliveryInfo.getDeliveryTime());
                            }
                            orderItemsDO.setIsDelivery(1);
                        }else {
                            orderItemsDO.setIsDelivery(2);
                        }
                        return orderItemsDO;
                    }).collect(Collectors.toList());
                    BeanUtil.copyProperties(esTrade, esOrderDO);
                    esOrderDO.setOrderState(esTrade.getTradeStatus());
                    esOrderDO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                    esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                    esOrderDO.setShipAddr(esTrade.getConsigneeAddress());
                    esOrderDO.setShipCity(esTrade.getConsigneeCity());
                    esOrderDO.setShipProvince(esTrade.getConsigneeProvince());
                    esOrderDO.setShipCounty(esTrade.getConsigneeCounty());
                    esOrderDO.setShipMobile(esTrade.getConsigneeMobile());
                    esOrderDO.setShipName(esTrade.getConsigneeName());
                    esOrderDO.setOrderMoney(esTrade.getTotalMoney());
                    esOrderDO.setShippingMoney(esTrade.getFreightMoney());
                    esOrderDO.setShipTown(esTrade.getConsigneeTown());
                    esOrderDO.setEsOrderItemsDO(orderItemsList);
                    esOrderDO.setDeliveryMessage(deliveryMessage.get());
                    esOrderDO.setDeliveryTime(deliveryTime.get());
                    esOrderDO.setCancelTime(esTrade.getCancelTime());
                    esOrderDO.setRemark(remark);
//                    throw new ArgumentException(TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorCode(), TradeErrorCode.GET_ORDER_MESSAGE_ERROR.getErrorMsg());
                }else {
                    // 查询封装订单商品明细
                    QueryWrapper<EsOrderItems> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(EsOrderItems::getOrderSn, orderSn);
                    List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper1);
                    List<EsOrderItemsDO> orderItemsList = new ArrayList<>();
                    AtomicReference<String> deliveryMessage = new AtomicReference<>("");
                    orderItemsList = esOrderItems.stream().map(orderItems -> {
                        QueryWrapper<EsDeliveryInfo> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.lambda().eq(EsDeliveryInfo::getOrderSn, orderItems.getOrderSn())
                                .eq(EsDeliveryInfo::getOrderDetailSn, orderItems.getId())
                                .eq(EsDeliveryInfo::getSkuId, orderItems.getSkuId())
                                .eq(EsDeliveryInfo::getGoodsId, orderItems.getGoodsId());
                        List<EsDeliveryInfo> infoList = this.esDeliveryInfoMapper.selectList(queryWrapper2);

                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        BeanUtils.copyProperties(orderItems, orderItemsDO);
                        orderItemsDO.setMoney(orderItems.getMoney());
                        // 规格
                        if (StringUtils.isNotEmpty(orderItems.getSpecJson())) {
                            List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(orderItems.getSpecJson(), EsSpecValuesDO.class);
                            orderItemsDO.setSpecList(specValuesDOList);
                        }
                        if (infoList.size() > 0) {
                            EsDeliveryInfo esDeliveryInfo = infoList.get(0);
                            String content = esDeliveryInfo.getContent();
                            if (StringUtils.isNotEmpty(content)) {
                                deliveryMessage.set(content);
                            }
                            orderItemsDO.setIsDelivery(1);
                        } else {
                            orderItemsDO.setIsDelivery(2);
                        }
                        return orderItemsDO;
                    }).collect(Collectors.toList());
                    BeanUtil.copyProperties(esOrder, esOrderDO);
                    esOrderDO.setEsOrderItemsDO(orderItemsList);
                    esOrderDO.setDeliveryMessage(deliveryMessage.get());
                    orderDOS.add(esOrderDO);
                }
            }
            return DubboPageResult.success(orderDOS);
        } catch (ArgumentException ae) {
            logger.error("数据不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("系统错误！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsOrderDO> queryLfcOrder(String lfcId) {
        try {
            QueryWrapper<EsOrder> wrapper=new QueryWrapper();
            wrapper.lambda().eq(EsOrder::getLfcId,lfcId);
            EsOrder esOrder = this.esOrderMapper.selectOne(wrapper);
            EsOrderDO orderDO=new EsOrderDO();
            if (esOrder !=null){
                BeanUtil.copyProperties(esOrder,orderDO);
            }
            return DubboResult.success(orderDO);
        } catch (ArgumentException ae) {
            logger.error("订单不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("订单不存在！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsExportOrdersDO> exportOrder(EsExportOrderDTO dto) {
        try {
            List<EsExportOrdersDO> exportOrderList = esOrderMapper.getExportOrderList(dto);
            exportOrderList.stream().forEach(esExportOrdersDO -> {
                esExportOrdersDO.setOrderStateText(OrderStatusEnum.getOrderName(esExportOrdersDO.getOrderState()));
            });
            return DubboPageResult.success(exportOrderList);
        }catch (ArgumentException e) {
            logger.error("查询导出订单数据失败",e);
            return DubboPageResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (Throwable th) {
            logger.error("查询导出订单数据失败",th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsOrderDO> cancelLfc(CancelVO cancelVO, OrderPermission permission, Long shopId) {
        try{
            // 获取此订单
            String orderSn = cancelVO.getOrderSn();
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);

            QueryWrapper<EsOrder> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
            List<EsOrder> orderList = this.list(orderQueryWrapper);
            orderList.forEach(order->{
                if(order.getShopId().longValue() != shopId.longValue()){
                    throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(),"无权操作此订单");
                }
                order.setOrderState(OrderStatusEnum.CANCELLED.value());
                order.setUpdateTime(new Date().getTime());
                order.setCancelReason(cancelVO.getReason());
                if(CollectionUtils.isNotEmpty(esOrderItems)){
                    order.setItemsJson(JsonUtil.objectToJson(esOrderItems));
                }
            });
            this.updateBatchById(orderList);
            EsOrderLog orderLogDO = new EsOrderLog();
            orderLogDO.setMessage("取消订单");
            orderLogDO.setOrderSn(orderSn);
            orderLogDO.setOpName(cancelVO.getOperator());
            orderLogDO.setOpTime(new Date().getTime());
            this.esOrderLogMapper.insert(orderLogDO);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("订单不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("订单不存在！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult send(String orderSn) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
        EsOrderItems orderItemsDO = this.esOrderItemsMapper.selectOne(queryWrapper);

        DubboResult<EsOrderDO> result = this.getEsBuyerOrderInfo(orderSn);
        EsOrderDO  order = result.getData();
        if (! order.getShipState().equals(ShipStatusEnum.SHIP_NO.value())){
            return  DubboResult.fail(TradeErrorCode.SYSTEM_ERROR.getErrorCode(), "已发送短信通知，请勿重复操作");
        }
        if (orderItemsDO.getGoodsId()==12414){
            DubboResult<EsCakeCardDO>  cardResult= esCakeCardService.getLowCode();
            if (!cardResult.isSuccess() || cardResult.getData() ==null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "蛋糕库存不足");
            }
            EsCakeCardDO lowCode = cardResult.getData();
            EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();

            esSmsSendDTO.setMobile(order.getShipMobile());
            esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_177253177.value());
           // DubboResult<EsCakeCardDO> cakeCardResult = this.esCakeCardService.getCakeCardByOrderSn(orderSn);
           // if(cakeCardResult.isSuccess() && cakeCardResult.getData() != null){
                //esSmsSendDTO.setCode(cakeCardResult.getData().getCode());
           // }else{
                esSmsSendDTO.setCode(lowCode.getCode());
            //}
            esSmsSendDTO.setPassword(lowCode.getPassword());
            esSmsService.sendLfc(esSmsSendDTO);
            lowCode.setUpdateTime(new Date().getTime());
            lowCode.setMobile(order.getShipMobile());
            lowCode.setOrderSn(orderSn);
            lowCode.setIsUsed(1);
            EsCakeCardDTO esCakeCardDTO = new EsCakeCardDTO();
            BeanUtil.copyProperties(lowCode,esCakeCardDTO);
            this.esCakeCardService.updateCakeCard(esCakeCardDTO);
            EsOrderDTO esOrderDTO = new EsOrderDTO();
            esOrderDTO.setOrderSn(orderSn);
            esOrderDTO.setOrderState( OrderStatusEnum.SHIPPED.value());
            esOrderDTO.setShipState( ShipStatusEnum.SHIP_YES.value());
            esOrderDTO.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
            esOrderDTO.setUpdateTime(new Date().getTime());
            this.updateOrderStatus(esOrderDTO);
            orderItemsDO.setHasShip(1);
            this.esOrderItemsMapper.updateById(orderItemsDO);
           return  DubboResult.success();
        }else{
            return  DubboResult.fail(TradeErrorCode.SYSTEM_ERROR.getErrorCode(), "已发送短信通知，请勿重复操作");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsImportShipVO> importShip(byte[] base64) {
        try {
        Workbook workbook = null;
        Integer totalNum=0;//总个数
        Integer successNum=0;//成功数
        Integer failNum=0;//失败数
        //兼容excel 03之前和excel 07版本
        try {
            workbook = new XSSFWorkbook(new ByteArrayInputStream(base64));
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(base64));
        }
        List<EsFailDataShipVO>  failDataShips = new ArrayList<>();

        // 获取每一个工作薄
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet xssfSheet = workbook.getSheetAt(numSheet);
            DataFormatter dataFormatter = new DataFormatter();
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);
                List<Integer> goodsIds=new ArrayList<>();
                EsDeliveryDTO delivery = new EsDeliveryDTO();
                EsFailDataShipVO failDataShip = new EsFailDataShipVO();
                String orderSn;
                totalNum ++;

                if (xssfRow != null) {
                    if (xssfRow.getCell(0) != null) {
                        xssfRow.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        orderSn=xssfRow.getCell(0).getStringCellValue();
                        delivery.setOrderSn(orderSn);
                    }else{
                        throw new ArgumentException(TradeErrorCode.ORDER_SN_IS_NULL.getErrorCode(), "订单号不能为空");
                    }
                    if (xssfRow.getCell(1) != null){
                        xssfRow.getCell(1).setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double goodsId = xssfRow.getCell(1).getNumericCellValue();
                        goodsIds.add(goodsId.intValue());
                        delivery.setGoodsId(goodsIds);
                    }else{
                        throw new ArgumentException(TradeErrorCode.GOODS_ID_IS_NULL.getErrorCode(), "商品id不能为空");
                    }
                    if (xssfRow.getCell(2) != null){
                        xssfRow.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        String logiName = xssfRow.getCell(2).getStringCellValue();
                        DubboResult<EsLogiCompanyDO> logiCompanyDODubboResult = logiCompanyService.getByName(logiName);
                        EsLogiCompanyDO logiCompanyDO = logiCompanyDODubboResult.getData();
                        if (logiCompanyDO != null){
                            delivery.setLogiId(logiCompanyDO.getId());
                            delivery.setLogiName(logiName);
                        }else{
                            failNum++;
                            xssfRow.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                            xssfRow.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                            xssfRow.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                            failDataShip.setOrderSn(xssfRow.getCell(0).getStringCellValue());
                            failDataShip.setGoodsId(Integer.valueOf(xssfRow.getCell(1).getStringCellValue()));
                            failDataShip.setShipName(logiName);
                            failDataShip.setShipNo(xssfRow.getCell(3).getStringCellValue());
                            failDataShips.add(failDataShip);
                            continue;
                        }
                    }else{
                        throw new ArgumentException(TradeErrorCode.LOGI_COMPANY_IS_NULL.getErrorCode(), "物流公司不能为空");
                    }
                    if (xssfRow.getCell(3) != null){
                        xssfRow.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        String shipNo = xssfRow.getCell(3).getStringCellValue();
                        delivery.setDeliveryNo(shipNo);

                    }else{
                        throw new ArgumentException(TradeErrorCode.SHIP_NO_IS_NULL.getErrorCode(), "物流单号不能为空");
                    }
                    //发货
                    delivery.setOperator("店铺:卓付商城");
                    QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();

                    queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
                    EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);

                    QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
                    wrapper.lambda().in(EsOrderItems::getGoodsId,delivery.getGoodsId()).eq(EsOrderItems::getOrderSn,orderSn);
                    List<EsOrderItems> orderItemsList = this.esOrderItemsMapper.selectList(wrapper);
                    if (CollectionUtils.isEmpty(orderItemsList)){
                        throw new ArgumentException(TradeErrorCode.GOODS_ITEMS_IS_NULL.getErrorCode(), "不存在的订单详情");
                    }
                    for(EsOrderItems esOrderItems:orderItemsList){
                        esOrderItems.setLogiId(delivery.getLogiId());
                        esOrderItems.setLogiName(delivery.getLogiName());
                        esOrderItems.setShipNo(delivery.getDeliveryNo());
                        esOrderItems.setHasShip(1);
                        this.esOrderItemsMapper.updateById(esOrderItems);
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    esOrder.setOrderState(OrderStatusEnum.SHIPPED.value());
                    esOrder.setShipState(ShipStatusEnum.SHIP_YES.value());
                    esOrder.setServiceState(ServiceStatusEnum.NOT_APPLY.value());
                    esOrder.setShipTime(currentTimeMillis);
                    this.esOrderMapper.updateById(esOrder);
                   // ship(delivery,OrderPermission.seller);
                    successNum++;
                }
            }
        }
        EsImportShipVO vo = new EsImportShipVO();
        vo.setTotalNum(totalNum);
        vo.setSuccessNum(successNum);
        vo.setFailNum(failNum);
        vo.setFailDataShip(failDataShips);
        return DubboResult.success(vo);
        } catch (ArgumentException ae) {
            logger.error("批量导入失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("批量导入失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsOrderDO> getLfcOrderList(EsSellerOrderQueryDTO esSellerOrderQueryDTO, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize);
            IPage<EsOrderDO> esOrderList = this.esOrderMapper.getEsLfcOrderList(page, esSellerOrderQueryDTO);
            esOrderList.getRecords().forEach(esOrderDO -> {
                esOrderDO.setOrderStateText(OrderStatusEnum.getOrderName(esOrderDO.getOrderState()));
                esOrderDO.setShipStateText(ShipStatusEnum.getShipName(esOrderDO.getShipState()));
                esOrderDO.setPayStateText(PayStatusEnum.getPayName(esOrderDO.getPayState()));
                esOrderDO.setServiceStateText(ServiceStatusEnum.getServiceName(esOrderDO.getServiceState()));
                DubboPageResult<EsOrderItemsDO> result = this.iEsOrderItemsService.getEsOrderItemsByOrderSn(esOrderDO.getOrderSn());
                if(result.isSuccess()){
                    esOrderDO.setEsOrderItemsDO(result.getData().getList());
                }
                OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable(OrderStatusEnum.valueOf( esOrderDO.getOrderState()),
                        ServiceStatusEnum.valueOf(esOrderDO.getServiceState()) );
                esOrderDO.setOrderOperateAllowable(orderOperateAllowable);
            });
            return DubboPageResult.success(esOrderList.getTotal(), esOrderList.getRecords());
        } catch (ArgumentException ae) {
            logger.error("订单分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("订单分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsWapReceptVO> getWapMemberReceiptList(EsBuyerOrderQueryDTO esBuyerOrderQueryDTO, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize);
            List<String> statusList = new ArrayList<>();
            statusList.add("ROG");
            statusList.add("COMPLETE");
            esBuyerOrderQueryDTO.setOrderStatus(statusList);
            IPage<EsBuyerOrderDO> esBuyerOrderList = this.esOrderMapper.getEsMemberReceiptList(page, esBuyerOrderQueryDTO);

            List<EsBuyerOrderDO> buyerOrderList = esBuyerOrderList.getRecords();

            List<EsWapReceptVO> wapReceptVOS = new ArrayList<>();
            for (EsBuyerOrderDO buyerOrderDO: buyerOrderList){
                //订单商品明细
                String orderSn = buyerOrderDO.getOrderSn();
                DubboPageResult<EsOrderItemsDO> result = iEsOrderItemsService.getEsOrderItemsByOrderSn(orderSn);
                EsWapReceptVO receptVO=new EsWapReceptVO();
                receptVO.setOrderSn(orderSn);
                receptVO.setCreateTime(buyerOrderDO.getCreateTime());
                receptVO.setShopId(buyerOrderDO.getShopId());
                receptVO.setShopName(buyerOrderDO.getShopName());
                List<EsOrderItemsDO> esOrderItemsDOList = result.getData().getList();
                List<EsBuyerOrderItemsVO> itemsVOList=new ArrayList<>();
                for (EsOrderItemsDO orderItem: esOrderItemsDOList) {
                    receptVO.setOrderSn(orderSn);
                    DubboResult<EsGoodsCO> esGoods = goodsService.getEsGoods(orderItem.getGoodsId());
                    EsGoodsCO goodsCO = esGoods.getData();
                    EsBuyerOrderItemsVO esBuyerOrderItemsVO = new EsBuyerOrderItemsVO();
                    if (!esGoods.isSuccess()) {
                        throw new ArgumentException(TradeErrorCode.GOODS_DATE_QUERY_ERROR.getErrorCode(), TradeErrorCode.GOODS_DATE_QUERY_ERROR.getErrorMsg());
                    }
                    Integer invoice = goodsCO.getIsInvoice() == null ? 2 : goodsCO.getIsInvoice();
                    if (invoice == 2) {
                        // 该商品无法开具发票
                        esBuyerOrderItemsVO.setReceiptState(3);

                    } else {
                        // 获取发票历史信息
                        DubboResult<EsReceiptHistoryDO> receiptHistory =
                                iEsReceiptHistoryService.getReceiptHistoryByGoodsIdAndOrdersn(orderItem.getGoodsId(), orderItem.getOrderSn());
                        EsReceiptHistoryDO receiptHistoryData = receiptHistory.getData();
                        if (receiptHistoryData == null) {
                            esBuyerOrderItemsVO.setReceiptType(goodsCO.getInvoiceType());
                            esBuyerOrderItemsVO.setReceiptState(2); // 未开票
                            esBuyerOrderItemsVO.setReceiptOperation("申请开票");
                            esBuyerOrderItemsVO.setReceiptType(goodsCO.getInvoiceType());
                        } else {
                            esBuyerOrderItemsVO.setReceiptType(goodsCO.getInvoiceType());
                            esBuyerOrderItemsVO.setReceiptState(1);
                            esBuyerOrderItemsVO.setReceiptOperation("查看发票");
                            esBuyerOrderItemsVO.setReceiptType(goodsCO.getInvoiceType());
                            EsMemberReceiptHistoryVO esMemberReceiptHistoryVO = new EsMemberReceiptHistoryVO();
                            BeanUtil.copyProperties(receiptHistoryData, esMemberReceiptHistoryVO);
                            esBuyerOrderItemsVO.setReceiptHistoryVO(esMemberReceiptHistoryVO);
                        }
                    }
                    itemsVOList.add(esBuyerOrderItemsVO);
                }
                receptVO.setEsBuyerOrderItemsVO(itemsVOList);
                wapReceptVOS.add(receptVO);
            }
            return DubboPageResult.success(esBuyerOrderList.getTotal(), wapReceptVOS);

        } catch (ArgumentException ae) {
            logger.error("发票信息查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发票信息查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
