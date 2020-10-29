package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberActiveInfoDTO;
import com.jjg.member.model.dto.EsMemberBalanceDTO;
import com.jjg.member.model.enums.ActiveTypeEnum;
import com.jjg.member.model.enums.ConsumeEnumType;
import com.jjg.operateChecker.OrderOperateAllowable;
import com.jjg.operateChecker.OrderOperateChecker;
import com.jjg.shop.model.domain.EsCategoryDO;
import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.jjg.trade.model.domain.EsBuyerTradeDO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.domain.EsWapBalanceTradeDO;
import com.jjg.trade.model.dto.CartItemsDTO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.jjg.trade.model.dto.TradePromotionGoodsDTO;
import com.jjg.trade.model.enums.*;
import com.jjg.trade.model.vo.EsTradeVO;
import com.jjg.trade.model.vo.PriceDetailVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.TradeCachePrefix;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.manager.event.TradeIntoDbEvent;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.message.OrderStatusChangeMsg;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.*;
import com.xdl.jjg.utils.CurrencyUtil;
import com.xdl.jjg.web.service.IEsFullDiscountGiftService;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.IEsTradeService;
import com.xdl.jjg.web.service.feign.member.MemberActiveInfoService;
import com.xdl.jjg.web.service.feign.member.MemberDepositService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import com.xdl.jjg.web.service.feign.shop.CategoryService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuQuantityService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单主表-es_trade 服务实现类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsTradeService.class/*,timeout = 5000*/)
public class EsTradeServiceImpl extends ServiceImpl<EsTradeMapper, EsTrade> implements IEsTradeService {
    private static Logger logger = LoggerFactory.getLogger(EsTradeServiceImpl.class);

    @Autowired
    private EsTradeMapper esTradeMapper;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private EsOrderMapper esOrderMapper;

    @Autowired
    private EsOrderLogMapper esOrderLogMapper;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private EsDeliveryInfoMapper esDeliveryInfoMapper;

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private MemberActiveInfoService iEsMemberActiveInfoService;

    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);

    @Autowired
    private GoodsService iEsGoodsService;

    @Autowired
    private MemberDepositService iEsMemberDepositService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GoodsSkuQuantityService iEsGoodsSkuQuantityService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IEsOrderService orderService;

    @Autowired
    private IEsFullDiscountGiftService iEsFullDiscountGiftService;

    @Autowired
    private TradeIntoDbEvent events;

    @Value("${rocketmq.order.topic}")
    private String order_topic;

    @Value("${rocketmq.member.active.topic}")
    private String member_active_topic;

    /**
     * 根据id获取订单主表-es_trade详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsTradeDO>
     */
    @Override
    public DubboResult<EsTradeDO> getEsTradeInfo(Long id) {
        try {
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getId,id);
            EsTrade esTrade = this.getOne(queryWrapper);
            //出仓对象转DO
            EsTradeDO esTradeDO = new EsTradeDO();
            if(esTrade != null){
                BeanUtils.copyProperties(esTrade,esTradeDO);
            }
            return DubboResult.success(esTradeDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult<EsTradeDO> getEsTradeInfo(String tradeSn) {
        try {
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getTradeSn,tradeSn);
            EsTrade esTrade = this.getOne(queryWrapper);
            //出仓对象转DO
            EsTradeDO esTradeDO = new EsTradeDO();
            if(esTrade != null){
                BeanUtils.copyProperties(esTrade,esTradeDO);
            }
            return DubboResult.success(esTradeDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    /**
     * 查询订单主表集合分页
     * @param tradeDTO
     * @return
     */
    @Override
    public DubboPageResult<EsTradeDO> getEsTradeInfoList(EsTradeDTO tradeDTO, int pageSize, int pageNum) {

        Page page = new Page(pageNum, pageSize);
        try {
            IPage<EsTrade> tradeList = this.esTradeMapper.getTradeList(page, tradeDTO);
            List<EsTradeDO> tradeDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(tradeList.getRecords())) {
                tradeDOList = tradeList.getRecords().stream().map(esTrade -> {
                    EsTradeDO tradeDo = new EsTradeDO();
                    BeanUtil.copyProperties(esTrade, tradeDo);
                    return tradeDo;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(tradeList.getTotal(),tradeDOList);
        } catch (
                ArgumentException ae) {
            logger.error("查询交易订单列表失败", ae);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查询交易订单列表失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *保存订单信息
     * @param esTradeDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsTradeDO> insertTrade(EsTradeDTO esTradeDTO) {
        try {
            if (esTradeDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(),"参数错误");
            }
            EsTrade esTrade = new EsTrade();
            BeanUtils.copyProperties(esTradeDTO,esTrade);
            esTrade.setTotalMoney(esTradeDTO.getTotalMoney());
            esTrade.setDiscountMoney(esTradeDTO.getDiscountMoney());
            esTrade.setFreightMoney(esTradeDTO.getFreightMoney());
            esTrade.setGoodsMoney(esTradeDTO.getGoodsMoney());
            this.save(esTrade);
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("保存订单失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SAVE_ORDER_ERROR.getErrorCode(), TradeErrorCode.SAVE_ORDER_ERROR.getErrorMsg());
        } catch (Throwable th) {
            logger.error("保存订单失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 修改订单详情
     * @param esTradeDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsTradeDO> updateTradeMessage(EsTradeDTO esTradeDTO) {
        try {
            if (esTradeDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsTrade trade = new EsTrade();
            //DTO 转换成 entity
            BeanUtils.copyProperties(esTradeDTO,trade);

            QueryWrapper<EsTrade> queryTradeWrapper = new QueryWrapper<>();
            if (esTradeDTO.getId() != null){
                queryTradeWrapper.lambda().eq(EsTrade::getId,esTradeDTO.getId());
            }
            if (esTradeDTO.getTradeSn() != null){
                //主订单编号
                queryTradeWrapper.lambda().eq(EsTrade::getTradeSn,esTradeDTO.getTradeSn());

            }
            //判断该交易数据是否存在
            EsTrade esTrade = this.baseMapper.selectOne(queryTradeWrapper);
            if (esTrade == null){
                throw new ArgumentException(TradeErrorCode.GET_TRADE_ERROR.getErrorCode(),TradeErrorCode.GET_TRADE_ERROR.getErrorMsg());
            }
            // 修改交易信息
            this.esTradeMapper.update(trade,queryTradeWrapper);

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单详情失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单详情失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 删除订单(逻辑删除)
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsTradeDO> deleteTradeMessage(Long id) {
        try {
            if (id == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsTrade esTrade = new EsTrade();
            esTrade.setIsDel(DelStatus.IS_DEL.getValue());
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getId,id);
            this.esTradeMapper.update(esTrade,queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除交易信息失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("删除交易信息失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改订单状态
     * @param esTradeDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsTradeDO> updateTradeStatus(EsTradeDTO esTradeDTO) {
        try {
            if (esTradeDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsTrade trade = new EsTrade();
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getId,esTradeDTO.getId());

            //判断该数据是否存在
            Integer integer = this.baseMapper.selectCount(queryWrapper);
            if (integer == 0){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            // 修改交易的付款参数
            this.esTradeMapper.update(trade,queryWrapper);

            //TODO 修改订单的付款参数？
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单状态失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable be) {
            logger.error("修改订单状态失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 将交易信息存入数据库，缓存
     * @param esTradeDTO
     * @param
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public DubboResult insertEsTradeIntoDB(EsTradeDTO esTradeDTO, PriceDetailVO tradePrice) {
        try {
            EsTradeVO esTradeVO = new EsTradeVO();

            BeanUtils.copyProperties(esTradeDTO,esTradeVO);
            List<EsOrderDTO>  orderDTOList = (List<EsOrderDTO> ) BeanUtil.copyList(esTradeDTO.getOrderList(),new EsOrderDTO().getClass());
            esTradeVO.setOrderList(orderDTOList);

            //压入缓存
            String cacheKey = TradeCachePrefix.TRADE_SESSION_ID_KEY.getPrefix() + esTradeVO.getTradeSn();
            this.jedisCluster.set(cacheKey, JsonUtil.objectToJson(esTradeVO));
            // 入库操作
            long start = System.currentTimeMillis();
            logger.info("入库操作开始时间[{}]",start);
            Double needPayMoney = this.innerIntoDB(esTradeDTO, tradePrice);
            long end = System.currentTimeMillis();
            logger.info("入库操作结束时间[{}]，入库操作耗时[{}]",start,end-start);
            return DubboResult.success(needPayMoney);

        }catch (ArgumentException ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SAVE_ORDER_ERROR.getErrorCode(),TradeErrorCode.SAVE_ORDER_ERROR.getErrorMsg());
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    private Double innerIntoDB(EsTradeDTO esTradeDTO,PriceDetailVO tradePrice) {
        //订单mq消息
        OrderStatusChangeMsg orderStatusChangeMsg = new OrderStatusChangeMsg();
        // 需要第三方支付的钱
        Double needPayMoney = 0.0;
        EsTrade trade = new EsTrade();
        BeanUtils.copyProperties(esTradeDTO,trade);
        //总价格
        trade.setTotalMoney(esTradeDTO.getTotalMoney());
        //商品价格
        trade.setGoodsMoney(esTradeDTO.getGoodsMoney());
        //运费
        trade.setFreightMoney(esTradeDTO.getFreightMoney());
        //优惠的金额
        trade.setDiscountMoney(esTradeDTO.getDiscountMoney());
        // 总金额
        Double totalPrice = tradePrice.getTotalPrice();
        // 前台输入的使用余额
        Double balance = tradePrice.getBalance();

        needPayMoney = totalPrice - balance;

        trade.setUseBalance(balance);

        //订单入库
        List<EsOrderDTO> orderList = esTradeDTO.getOrderList();
        List<EsOrderDTO> collect = orderList.stream().sorted(Comparator.comparing(EsOrderDTO::getOrderMoney).reversed()).collect(Collectors.toList());
        Boolean flag = false;
        for (EsOrderDTO esOrderDTO : collect) {


            long yhbistart = System.currentTimeMillis();
            logger.info("优惠比例计算开始时间：[{}]；",yhbistart);
            //商品金额
            Double goodsTotalPrice = esOrderDTO.getGoodsMoney();
            //此订单总优惠金额(包含所有活动优惠，优惠券的优惠。)
            Double discountTotalPrice = esOrderDTO.getDiscountMoney();

            List<CartItemsDTO> list = esOrderDTO.getCartItemsList();
            int num = 0;
            // 商品数量
            for (CartItemsDTO cartItemsDTO: list) {
                num = num + cartItemsDTO.getNum();
            }
            /*  //折扣金额=活动折扣金额+公司折扣金额
            discountTotalPrice=MathUtil.add(discountTotalPrice,esOrderDTO.getPrice().getCompanyDiscountPrice());
            */
            //List<CartItemsDTO> cartItemsList = esOrderDTO.getCartItemsList();
            for(CartItemsDTO cartItemsDTO:list){
                List<TradePromotionGoodsDTO> promotionList = cartItemsDTO.getPromotionList();
                String promotionType = "";
                for(TradePromotionGoodsDTO promotionGoodsDTO:promotionList){
                    if(promotionGoodsDTO.getIsCheck()!=null && promotionGoodsDTO.getIsCheck()==1){
                        promotionType = promotionGoodsDTO.getPromotionType();
                    }
                }
                if(promotionType.equals(PromotionTypeEnum.MINUS.name())
                        ||promotionType.equals(PromotionTypeEnum.SECKILL.name())
                        ||promotionType.equals(PromotionTypeEnum.HALF_PRICE.name())){
                    //原价小计
                    Double originalSubTotal = CurrencyUtil.mul(cartItemsDTO.getGoodsPrice(), cartItemsDTO.getNum());
                    logger.info("原价小计[{}]",originalSubTotal);
                    // 优惠金额
                    double youhuiPrice = CurrencyUtil.sub(originalSubTotal, cartItemsDTO.getSubtotal());
                    logger.info("优惠金额[{}]",youhuiPrice);
                    //总优惠 - 商品立减的优惠
                    discountTotalPrice = CurrencyUtil.sub(discountTotalPrice,youhuiPrice );
                    logger.info("优惠小计[{}]",discountTotalPrice);
                }
            }
            int a=0;
            Double sub=0.0;
            Double dicPrice=discountTotalPrice;
            //折扣金额=活动折扣金额+公司折扣金额
            discountTotalPrice=CurrencyUtil.add(discountTotalPrice,esOrderDTO.getPrice().getCompanyDiscountPrice());

            for(CartItemsDTO skuVO:list){
                if(goodsTotalPrice.intValue()<=0){
                    break;
                }
                if(list.size()!=1){
                    //此商品占参与此活动所有商品总价的比例。（注：此处最后的4，代表小数精度，解决10除以3类似的问题）
                    Double ratio = CurrencyUtil.div(skuVO.getSubtotal(),goodsTotalPrice,4);
                    /**
                     * 活动优惠的金额按照上面计算出来的百分比，分配到每个商品，并计算得出单价
                     */
                    //当前活动优惠的金额 分配到此商品时，应优惠的金额 = 总优惠金额 x 所占比例
                    Double disPrice = CurrencyUtil.mul(discountTotalPrice, ratio);

                    //成交价=商品小计-应优惠的金额
                    Double refundPrice = CurrencyUtil.sub(skuVO.getSubtotal(), disPrice);
                    //此商品成交的单价
                    skuVO.setCartPrice(CurrencyUtil.div(refundPrice, skuVO.getNum()));
                    //此商品成交价总价
                    skuVO.setSubtotal(refundPrice);
                    if (list.size()-1==a){
                        double cc=CurrencyUtil.sub(CurrencyUtil.sub(tradePrice.getAfterCompanyPrice(),dicPrice),sub);
                        if (cc<0){
                            cc=CurrencyUtil.sub(0,cc);
                            cc=CurrencyUtil.mul(2,cc);
                            for (CartItemsDTO skuVO1 : list) {
                                if(skuVO1.getSubtotal()>cc){
                                    skuVO1.setSubtotal(CurrencyUtil.sub(skuVO1.getSubtotal(),cc));
                                    break;
                                }
                            }
                            skuVO.setSubtotal(CurrencyUtil.div(cc,2));
                        }else{
                            skuVO.setSubtotal(cc);
                        }
                    }
                    sub=CurrencyUtil.add(sub,skuVO.getSubtotal());
                    a++;
                    logger.info("分摊商品优惠金额："+skuVO.getSubtotal()+"--"+skuVO.getCartPrice());
                }else{
                    //成交价=商品小计-应优惠的金额
                    Double refundPrice = CurrencyUtil.sub(skuVO.getSubtotal(), discountTotalPrice);
                    //此商品成交的单价
                    skuVO.setCartPrice(CurrencyUtil.div(refundPrice, skuVO.getNum()));
                    //此商品成交价总价
                    skuVO.setSubtotal(refundPrice);
                }
            }

            long yhbiend = System.currentTimeMillis();
            logger.info("优惠比例计算结束时间：[{}]；优惠比例计算总耗时[{}]",yhbiend,yhbiend-yhbistart);
            logger.info("订单总金额："+esOrderDTO.getOrderMoney());
            //如果余额支付金额大于交易总额，
            Double totalOrderMoney = MathUtil.add(esOrderDTO.getOrderMoney(), esOrderDTO.getShippingMoney());

            long ddrkstart = System.currentTimeMillis();
            logger.info("订单入库开始时间：[{}]",ddrkstart);
            if (balance >= totalOrderMoney){
                EsOrder esOrder = new EsOrder();
                BeanUtils.copyProperties(esOrderDTO,esOrder);
                orderStatusChangeMsg.setEsOrderDTO(esOrderDTO);
                orderStatusChangeMsg.setOldStatus(OrderStatusEnum.NEW);
                orderStatusChangeMsg.setNewStatus(OrderStatusEnum.PAID_OFF);
                //商品总额(折扣后价格)
                esOrder.setGoodsMoney(esOrderDTO.getGoodsMoney());
                //余额支付 余额足够的情况
                esOrder.setPayedMoney(MathUtil.add( esOrderDTO.getOrderMoney(),esOrderDTO.getPrice().getFreightPrice()));
                esOrderDTO.setPayedMoney(MathUtil.add( esOrderDTO.getOrderMoney(),esOrderDTO.getPrice().getFreightPrice()));
                //订单总额
                esOrder.setOrderMoney(esOrderDTO.getOrderMoney());
                //支付方式
                esOrder.setPluginId("yueDirectPlugin");
                esOrder.setPaymentMethodName("余额");
                //优惠金额
                esOrder.setDiscountMoney(esOrderDTO.getDiscountMoney());
                //余额支付，直接修改付款状态
                esOrder.setPayState(PayStatusEnum.PAY_YES.value());
                esOrder.setOrderState(OrderStatusEnum.PAID_OFF.value());
                //付款时间
                esOrder.setPaymentTime(System.currentTimeMillis());
                //配送费用
                if(esOrderDTO.getPrice().getIsFreeFreight() == 1){
                    esOrder.setShippingMoney(esOrderDTO.getPrice().getFreightPrice());
                }
                esOrder.setItemsJson(JsonUtil.objectToJson(esOrderDTO.getCartItemsList()));
                esOrder.setIsDel(0);
                esOrder.setGoodsNum(num);
                esOrder.setCreateTime(esOrderDTO.getCreateTime());
                esOrder.setRemark(esOrderDTO.getRemark());
                esOrder.setShippingMoney(esOrderDTO.getShippingMoney());

                // 需要第三方支付的金额为 0
                esOrder.setNeedPayMoney(0.0);
                // 设置 需第三方支付的金额
                esOrder.setPayMoney(0.0);

                this.esOrderMapper.insert(esOrder);
//                list.forEach(cartItemsDTO -> {
//                    // 保存订单明细表
//                    this.saveOrderItemMessage(esOrder,cartItemsDTO);
//                });
////                 保存订单创建日志表
//                this.saveOrderLogMessage(esOrderDTO);
                // 设置下个订单可以使用的余额
                balance = MathUtil.subtract(balance,totalOrderMoney);
                logger.info("下一个订单可使用的余额"+ balance);
                logger.info("使用余额支付");

            }else{
                flag = true;
                //余额不足 需要支付的金额先存入交易单表中 订单总额大于余额支付的金额 则订单为已确认状态
                EsOrder esOrder = new EsOrder();
                BeanUtils.copyProperties(esOrderDTO,esOrder);
                orderStatusChangeMsg.setEsOrderDTO(esOrderDTO);

                if(balance>0){
                    //支付方式
                    esOrder.setPluginId("yueDirectPlugin");
                    esOrder.setPaymentMethodName("余额");
                }
                orderStatusChangeMsg.setOldStatus(OrderStatusEnum.NEW);
                orderStatusChangeMsg.setNewStatus(OrderStatusEnum.CONFIRM);

                //商品总额(折扣后价格)
                esOrder.setGoodsMoney(esOrderDTO.getGoodsMoney());
                //订单总额
                esOrder.setOrderMoney(esOrderDTO.getOrderMoney());
                //订单备注
                esOrder.setRemark(esOrderDTO.getRemark());
                //优惠金额
                esOrder.setDiscountMoney(esOrderDTO.getDiscountMoney());
                // 如果 使用的金额大于一个订单的 但是小于两个订单的情况

                esOrder.setPayedMoney(balance);
                esOrderDTO.setPayedMoney(balance);
                Double subtract = MathUtil.subtract(totalOrderMoney, balance);
                logger.info("需要第三方支付的金额"+ subtract);
                // 防止为负数
                subtract = subtract > 0.0 ? subtract : 0.0;
                // 需要第三方支付的金额 = 该订单总额 - 剩余的余额
                esOrder.setNeedPayMoney(subtract);
                // 设置 需第三方支付的金额
                esOrder.setPayMoney(subtract);
                // 设置下个订单可以使用的余额
                balance = MathUtil.subtract(balance,totalOrderMoney);
                logger.info("余额不足的情况 "+ balance);
                balance = balance > 0.0 ? balance : 0.0;

                //配送费用
                if(esOrderDTO.getPrice().getIsFreeFreight() == 1){
                    esOrder.setShippingMoney(esOrderDTO.getPrice().getFreightPrice());
                }
                esOrder.setItemsJson(JsonUtil.objectToJson(esOrderDTO.getCartItemsList()));
                esOrder.setIsDel(0);
                esOrder.setGoodsNum(num);
                this.esOrderMapper.insert(esOrder);
//                list.forEach(cartItemsDTO -> {
//                    // 保存订单明细表
//                    this.saveOrderItemMessage(esOrder,cartItemsDTO);
//                });
////                 保存订单创建日志表
//                this.saveOrderLogMessage(esOrderDTO);
                logger.info("使用第三方支付");
            }


            long ddrkend = System.currentTimeMillis();
            logger.info("订单入库结束时间：[{}]；订单入库总耗时[{}]",ddrkend,ddrkend-ddrkstart);

            // 发送订单创建消息

            long fsddztstart = System.currentTimeMillis();
            this.sendOrderCreateMessage(orderStatusChangeMsg);
            long fsddztend = System.currentTimeMillis();
            logger.info("发送订单状态消息结束时间：[{}]；发送订单状态消息总耗时[{}]",fsddztend,fsddztend-fsddztstart);
        }
        // 判断余额支付是否可以把所有订单支付完成，如果可以支付完成 则trade 表的交易状态为 已付款
        if (!flag){
            trade.setTradeStatus(OrderStatusEnum.PAID_OFF.value());
            trade.setPluginId("yueDirectPlugin");
            trade.setPaymentMethodName("余额");
            trade.setPayMoney(0.0);
            this.esTradeMapper.insert(trade);
        }else {
            trade.setPayMoney(needPayMoney);
            this.esTradeMapper.insert(trade);
        }
        // 余额最后扣减 ：防止生成订单出错，但余额已经扣了一部分

        long yestart = System.currentTimeMillis();
        logger.info("余额扣减开始时间：[{}]",yestart);
        collect.forEach(esOrderDTO -> {
            //从用户账户扣除每个订单 需要的余额
            if (BigDecimal.ZERO.compareTo(new BigDecimal(esOrderDTO.getPayedMoney())) <= 0) {
                this.setMemberBalance(esOrderDTO.getTradeSn(),esOrderDTO.getOrderSn(),esTradeDTO.getMemberId(),esOrderDTO.getPayedMoney() , MemberBalanceEnum.REDUCE_BALANCE.value());
            }
            logger.info("订单号："+esOrderDTO.getOrderSn()+"订单余额支付金额"+esOrderDTO.getPayedMoney());
        });
        long yeend = System.currentTimeMillis();
        logger.info("余额扣减结束时间：[{}]；余额扣减总耗时[{}]",yeend,yeend-yestart);
        return needPayMoney;
    }

    /**
     * 会员余额 操作
     * @author:LIUJG
     * @param tradeSn 交易单号
     * @param orderSn 订单编号
     * @param memberId 会员ID
     * @param balance 要操作的金额
     * @param value 增加或减少
     */
    @Override
    public DubboResult setMemberBalance(String tradeSn,String orderSn,Long memberId, Double balance, String value) {

        try {
            // 减余额
            if (value.equals(MemberBalanceEnum.REDUCE_BALANCE.value())){

                EsMemberBalanceDTO esMemberBalanceDTO = new EsMemberBalanceDTO();
                esMemberBalanceDTO.setType(ConsumeEnumType.CONSUME);
                esMemberBalanceDTO.setMoney(balance);
                esMemberBalanceDTO.setMemberId(memberId);
                esMemberBalanceDTO.setOrderSn(orderSn);
                esMemberBalanceDTO.setTradeSn(tradeSn);
                DubboResult dubboResult = this.memberService.updateMemberBalance(esMemberBalanceDTO);
                if (!dubboResult.isSuccess()){
                    throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
                }
            }else if(value.equals(MemberBalanceEnum.ADD_BALANCE.value())){
                // 加余额
                EsMemberBalanceDTO esMemberBalanceDTO = new EsMemberBalanceDTO();

                esMemberBalanceDTO.setType(ConsumeEnumType.RECHARGE);
                esMemberBalanceDTO.setMoney(balance);
                esMemberBalanceDTO.setMemberId(memberId);
                esMemberBalanceDTO.setOrderSn(orderSn);
                esMemberBalanceDTO.setTradeSn(tradeSn);
                DubboResult dubboResult = this.memberService.updateMemberBalance(esMemberBalanceDTO);
                if (!dubboResult.isSuccess()){
                    throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
                }
            }else if(value.equals(ConsumeEnumType.REFUND.value())){
                // 退款
                EsMemberBalanceDTO esMemberBalanceDTO = new EsMemberBalanceDTO();
                esMemberBalanceDTO.setType(ConsumeEnumType.REFUND);
                esMemberBalanceDTO.setMoney(balance);
                esMemberBalanceDTO.setMemberId(memberId);
                esMemberBalanceDTO.setOrderSn(orderSn);
                esMemberBalanceDTO.setTradeSn(tradeSn);
                DubboResult dubboResult = this.memberService.updateMemberBalance(esMemberBalanceDTO);
                if (!dubboResult.isSuccess()){
                    throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     *  发送订单创建消息
     * @param orderStatusChangeMsg
     */
    @Override
    public void sendOrderCreateMessage(OrderStatusChangeMsg orderStatusChangeMsg) {
        //  订单编号取模
        String orderSn = orderStatusChangeMsg.getEsOrderDTO().getOrderSn();
        String s = orderSn.substring(orderSn.length() - 1);
        Long orderId = Long.valueOf(s) % 8;
        try {

//            // 修改订单的状态
//            orderService.updateOrderStatusMq(orderStatusChangeMsg);
//
//            String tradeSn = orderStatusChangeMsg.getEsOrderDTO().getTradeSn();
//            String tradeVOCache = jedisCluster.get(TradeCachePrefix.TRADE_SESSION_ID_KEY.getPrefix()+tradeSn);
//            EsTradeVO tradeVO = null;
//            if(!StringUtils.isBlank(tradeVOCache)){
//                tradeVO = JsonUtil.jsonToObject(tradeVOCache,EsTradeVO.class);
//            }
//            if (events != null) {
//                try {
//                    events.onTradeIntoDb(tradeVO,orderStatusChangeMsg.getNewStatus().value());
//                } catch (Exception e) {
//                    logger.error("交易入库消息出错", e);
//                }
//            }

            //由于消息系统不稳定 暂时先不通过消息进行处理订单业务 发送订单创建消息
            mqProducer.send(order_topic,JsonUtil.objectToJson(orderStatusChangeMsg),new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;
                    long index = id % mqs.size();
                    return mqs.get((int)index);
                }
            },orderId);
            logger.info("订单状态消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveOrderItemMessage(EsOrder esOrder, CartItemsDTO cartItemsDTO) {
        EsOrderItems esOrderItems = new EsOrderItems();
        esOrderItems.setId(snowflakeIdWorker.nextId());
        esOrderItems.setGoodsSn(cartItemsDTO.getGoodsSn());
        esOrderItems.setTradeSn(esOrder.getTradeSn());
        esOrderItems.setOrderSn(esOrder.getOrderSn());
        esOrderItems.setGoodsId(cartItemsDTO.getGoodsId());
        esOrderItems.setSkuId(cartItemsDTO.getSkuId());
        esOrderItems.setIsFresh(cartItemsDTO.getIsFresh());
        esOrderItems.setNum(cartItemsDTO.getNum());
        esOrderItems.setImage(cartItemsDTO.getGoodsImage());
        esOrderItems.setName(cartItemsDTO.getName());
        esOrderItems.setCategoryId(cartItemsDTO.getCategoryId());
        esOrderItems.setMoney(cartItemsDTO.getCartPrice());
        esOrderItems.setDeliveryMethod(cartItemsDTO.getDeliveryMethod());
        // 设置分类名称
        DubboResult<EsCategoryDO> category = categoryService.getCategory(cartItemsDTO.getCategoryId());
        if(category.isSuccess()){
            esOrderItems.setCategoryName(category.getData().getName());
        }
        // 判断组合活动的选中状态，存活动信息
        List<TradePromotionGoodsDTO> promotionList = cartItemsDTO.getPromotionList();
        promotionList.forEach(tradePromotionGoodsVO -> {
            Integer isCheck = tradePromotionGoodsVO.getIsCheck();
            if (isCheck == 1){
                esOrderItems.setPromotionType(tradePromotionGoodsVO.getPromotionType());
                esOrderItems.setPromotionId(tradePromotionGoodsVO.getActivityId());
            }

        });

        //初始售后状态
        esOrderItems.setState(ServiceStatusEnum.NOT_APPLY.value());
        esOrderItems.setSpecJson(JsonUtil.objectToJson(cartItemsDTO.getSpecList()));
        esOrderItems.setSingleCommentStatus(CommentStatusEnum.UNFINISHED.value());
        this.esOrderItemsMapper.insert(esOrderItems);

        // 如果存在自提商品信息的情况 保存自提商品信息
        Integer isDelivery = cartItemsDTO.getIsDelivery();
        if (isDelivery == 1){
            EsDeliveryInfo esDeliveryInfo = new EsDeliveryInfo();
            esDeliveryInfo.setOrderSn(esOrder.getOrderSn());
            esDeliveryInfo.setOrderDetailSn(esOrderItems.getId());
            esDeliveryInfo.setContent(esOrder.getDeliveryText());
            esDeliveryInfo.setIsOk(2);
            esDeliveryInfo.setDeliveryTime(esOrder.getDeliveryTime());
            esDeliveryInfo.setSkuId(esOrderItems.getSkuId());
            esDeliveryInfo.setGoodsId(esOrderItems.getGoodsId());

            esDeliveryInfoMapper.insert(esDeliveryInfo);
        }
    }

    /**
     * 保存订单创建日志
     * @param esOrderDTO
     */
    private void saveOrderLogMessage(EsOrderDTO esOrderDTO) {
        EsOrderLog esOrderLog = new EsOrderLog();
        esOrderLog.setOrderSn(esOrderDTO.getOrderSn());
        esOrderLog.setMessage("创建订单");
        esOrderLog.setOpId(esOrderDTO.getMemberId());
        esOrderLog.setOpName(esOrderDTO.getMemberName());
        esOrderLog.setOpTime(System.currentTimeMillis());
        esOrderLog.setMoney(esOrderDTO.getOrderMoney());
        this.esOrderLogMapper.insert(esOrderLog);
    }

    /**
     * 发送会员活跃度消息
     * @param esOrderDTO
     */
    private void sendActiveMessage(EsOrderDTO esOrderDTO) {
        Map<String,List<EsMemberActiveInfoDTO>> memberActiveMap = new HashMap<String, List<EsMemberActiveInfoDTO>>();
        // 活跃度DTO
        List<EsMemberActiveInfoDTO> memberActiveList = new ArrayList<>();
        EsMemberActiveInfoDTO esMemberActiveInfoDTO = new EsMemberActiveInfoDTO();
        esMemberActiveInfoDTO.setMemberId(esOrderDTO.getMemberId());
        esMemberActiveInfoDTO.setOrderSn(esOrderDTO.getOrderSn());
        esMemberActiveInfoDTO.setShopId(esOrderDTO.getShopId());
        esMemberActiveInfoDTO.setShopName(esOrderDTO.getShopName());
        esMemberActiveInfoDTO.setCreateTime(System.currentTimeMillis());
        esMemberActiveInfoDTO.setPaymentTime(System.currentTimeMillis());
        memberActiveList.add(esMemberActiveInfoDTO);
        memberActiveMap.put(ActiveTypeEnum.ADD_ACTIVE.value(),memberActiveList);
        try {
            // 增加活跃度 发送消息
            mqProducer.send(member_active_topic,JsonUtil.objectToJson(memberActiveMap));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public DubboResult<EsTradeDO> getEsTradeInfoByOrderSn(String orderSn) {
        try {
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getTradeSn,orderSn);
            EsTrade esTrade = this.getOne(queryWrapper);
            //出仓对象转DO
            EsTradeDO esTradeDO = new EsTradeDO();
            if(esTrade == null){
                throw new ArgumentException(TradeErrorCode.TRADE_DATE_NOT_EXIST.getErrorCode(),TradeErrorCode.TRADE_DATE_NOT_EXIST.getErrorMsg());
            }
            BeanUtils.copyProperties(esTrade,esTradeDO);
            return DubboResult.success(esTradeDO);
        } catch (ArgumentException ae) {
            logger.error("交易信息为空", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsTradeDO> cancelTrade(Long memberId, String tradeSn) {
        try {
            QueryWrapper<EsTrade> queryTradeWrapper = new QueryWrapper<>();
            queryTradeWrapper.lambda().eq(!StringUtil.isEmpty(tradeSn),EsTrade::getTradeSn,tradeSn)
                    .eq(EsTrade::getMemberId,memberId);

            QueryWrapper<EsOrder> queryOrderWrapper = new QueryWrapper<>();
            queryOrderWrapper.lambda().eq(EsOrder::getTradeSn,tradeSn)
                    .eq(EsOrder::getMemberId,memberId);

            EsTrade esTrade = this.getOne(queryTradeWrapper);
            if (esTrade == null){
                throw new ArgumentException(TradeErrorCode.GET_TRADE_ERROR.getErrorCode(),TradeErrorCode.GET_TRADE_ERROR.getErrorMsg());
            }
            long timeMillis = System.currentTimeMillis();
            // 修改交易表订单状态
            EsTrade esTrade1 = new EsTrade();
            esTrade1.setTradeStatus(OrderStatusEnum.CANCELLED.value());
            esTrade1.setCancelTime(timeMillis);
            EsOrder esOrder = new EsOrder();
            esOrder.setOrderState(OrderStatusEnum.CANCELLED.value());

            // 修改trade表订单状态，
            this.esTradeMapper.update(esTrade1,queryTradeWrapper);
            // 如果存在余额支付的情况 取消交易单则需要退回余额
            if (esTrade.getUseBalance() > 0 ){
                // 退款
                EsMemberBalanceDTO esMemberBalanceDTO = new EsMemberBalanceDTO();

                esMemberBalanceDTO.setType(ConsumeEnumType.REFUND);
                esMemberBalanceDTO.setMoney(esTrade.getUseBalance());
                esMemberBalanceDTO.setMemberId(memberId);

                esMemberBalanceDTO.setTradeSn(tradeSn);
                this.memberService.updateMemberBalance(esMemberBalanceDTO);
            }

            List<EsOrder> orders = this.esOrderMapper.selectList(queryOrderWrapper);
            if (CollectionUtils.isEmpty(orders)){
                throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
            }
            List<EsGoodsSkuQuantityDTO> goodsQuantityDTO = new ArrayList<>();

            // 订单库存信息回滚
            orders.forEach(esOrder1 -> {

                //2、验证此订单可进行的操作
                this.checkAllowable(esOrder1, OrderOperateEnum.CANCEL);

                // 取消子订单状态
                esOrder1.setOrderState(OrderStatusEnum.CANCELLED.value());
                esOrder1.setCancelTime(timeMillis);
                this.esOrderMapper.updateById(esOrder1);
                String orderSn = esOrder1.getOrderSn();
                QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
                List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);


                esOrderItems.stream().forEach(esOrderItems1 -> {
                    EsGoodsSkuQuantityDTO goodsQuantity = new EsGoodsSkuQuantityDTO();
                    goodsQuantity.setGoodsId(esOrderItems1.getGoodsId());
                    goodsQuantity.setSkuId(esOrderItems1.getSkuId());
                    goodsQuantity.setGoodsNumber(esOrderItems1.getNum());
                    goodsQuantity.setOrderSn(esOrder1.getOrderSn());
                    goodsQuantityDTO.add(goodsQuantity);
                });

            });
            //  增加商品库存
            iEsGoodsSkuQuantityService.insertGoodsSkuQuantity(goodsQuantityDTO);

            // 订单取消，将会影响会员的活跃度，通过ordersn 改变活跃度
            Map<String,List<String>> orderSnMap = new HashMap<String, List<String>>();

            List<String> orderSns = orders.stream().filter(
                    esOrder1 -> StringUtils.equals(esOrder1.getOrderSn(), esOrder.getOrderSn())
            ).map(EsOrder::getOrderSn).collect(Collectors.toList());

            orderSnMap.put(ActiveTypeEnum.DELET_ACTIVE.value(),orderSns);
            // 发送消息
            mqProducer.send(member_active_topic,JsonUtil.objectToJson(orderSnMap));
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("取消订单失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable be) {
            logger.error("取消订单失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
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

    @Override
    public DubboPageResult<EsBuyerTradeDO> getEsBuyerOrderList2(EsTradeDTO tradeDTO, int pageSize, int pageNum) {
        try {
            EsBuyerTrade buyerTrade = new EsBuyerTrade();
            buyerTrade.setOrderStatus(tradeDTO.getTradeStatus());
            buyerTrade.setKeyword(tradeDTO.getKeyword());
            buyerTrade.setMemberId(tradeDTO.getMemberId());
            Page<EsTrade> page = new Page<>(pageNum, pageSize);
            IPage<EsBuyerTrade> iPage = esTradeMapper.getOrderList(page, buyerTrade);


            List<EsBuyerTradeDO> tradeListDO = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                tradeListDO = iPage.getRecords().stream().map(trade -> {
                    EsBuyerTradeDO buyerTradeDO = new EsBuyerTradeDO();
                    BeanUtil.copyProperties(trade, buyerTradeDO);

                    // 设置订单操作权限
                    OrderOperateAllowable orderOperateAllowable =
                            new OrderOperateAllowable(OrderStatusEnum.valueOf(buyerTradeDO.getOrderStatus()),
                            ServiceStatusEnum.valueOf(buyerTradeDO.getServiceStatus()));
                    buyerTradeDO.setOrderOperateAllowable(orderOperateAllowable);
                    return buyerTradeDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), tradeListDO);
        } catch (ArgumentException ae) {
            logger.error("订单不存在", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据订单编号和退款金额
     * 获取应退的支付金额和余额
     *
     * @param orderSn     订单编号
     * @param refundMoney 退款金额
     * @author: libw 981087977@qq.com
     * @date: 2019/07/30 15:38:12
     * @return: com.shopx.common.model.result.DubboResult<java.util.Map>
     */
    @Override
    public DubboResult<Map> getRefundPrice(String orderSn, Double refundMoney) {
       try{
           // 获取tradeSn
           DubboResult orderResult = orderService.getEsBuyerOrderInfo(orderSn);
           if(!orderResult.isSuccess()) {
               throw new ArgumentException(orderResult.getCode(), orderResult.getMsg());
           }
           EsOrderDO orderDO = (EsOrderDO) orderResult.getData();

           // 获取余额第三方退款金额
           logger.info("当前TradeSn："+orderDO.getTradeSn()+"入参退款金额："+refundMoney);
           Map<String, String> map = updateTradeMoney(orderDO.getTradeSn(), refundMoney);

           return DubboResult.success(map);
       } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
       }
    }

    /**
     * 更新交易金额
     * @param payMoney       第三方支付金额
     * @param balance        余额支付金额
     * @param refundPayMoney 第三方退款金额
     * @param refundBalance  余额退款金额
     * @param type           类型(0：退款； 1：回滚)
     * @param tradeSn        交易单号
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 09:13:57
     * @return: com.shopx.common.model.result.DubboResult<java.lang.Boolean>
     */
    @Override
    public DubboResult<Boolean> updateTradeMoney(Double payMoney, Double balance, Double refundPayMoney,
                                                 Double refundBalance, Integer type, String tradeSn) {
        try {
            // 根据交易sn
            QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getTradeSn, tradeSn);
            EsTrade trade = this.getOne(queryWrapper);

            if (trade == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            if (type == 1) {
                refundPayMoney = refundPayMoney * -1;
                refundBalance = refundBalance * -1;
            }

            int i = esTradeMapper.updateTradeMoney(trade.getPayMoney(), trade.getUseBalance(), refundPayMoney, refundBalance, tradeSn);
            return DubboResult.success(i > 0);
        } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    @Override
    public DubboResult<EsTradeDO> getEsTradeByTradeSn(String tradeSn) {

        try {
            EsTradeDO esTradeByTradeSn = this.esTradeMapper.getEsTradeByTradeSn(tradeSn);

            return DubboResult.success(esTradeByTradeSn);
        } catch (Exception e) {
            return DubboResult.fail(e.hashCode(),e.getMessage());
        }
    }

    /**
     * 计算退款金额并更新交易单
     * @author: libw 981087977@qq.com
     * @date: 2019/07/30 15:58:49
     * @param tradeSn       交易编号
     * @param refundMoney   退款金额
     * @return: void
     */
    private Map<String, String> updateTradeMoney(String tradeSn, Double refundMoney) {
        Map<String, String> map = new HashMap<>(16);

        // 根据交易sn
        QueryWrapper<EsTrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsTrade::getTradeSn, tradeSn);
        EsTrade trade = this.getOne(queryWrapper);

        // 第三方支付的金额
        Double payMoney = trade.getPayMoney() == null ? 0.0 : trade.getPayMoney();
        // 余额支付的金额
        Double balance = trade.getUseBalance();

        // 第三方退款金额
        Double refundPayMoney = 0.0;
        // 余额支付的金额
        Double refundBalanceMoney = 0.0;

        // 退款金额小于第三方支付金额
        if(refundMoney - payMoney <= 0) {
            logger.info("退款金额："+refundMoney);
            logger.info("第三方支付金额："+payMoney);
            refundPayMoney = refundMoney;
            logger.info("第三方退款金额："+refundPayMoney);
        } else {
            refundPayMoney = payMoney;
            logger.info("首先退第三方金额"+refundPayMoney);
            refundBalanceMoney = refundMoney - refundPayMoney;
            logger.info("退完第三方金额后需退的余额"+refundBalanceMoney);
        }

        // 更新数据更新成功返回map，更新不成功递归
        if (esTradeMapper.updateTradeMoney(payMoney, balance, refundPayMoney, refundBalanceMoney, tradeSn) <= 0){
            logger.info("第三方支付金额1："+refundPayMoney);
            logger.info("余额支付金额1："+refundPayMoney);
            logger.info("首先退第三方金额1："+refundPayMoney);
            logger.info("退完第三方金额后需退的余额1："+refundBalanceMoney);
            logger.info("交易编号："+tradeSn);

            updateTradeMoney(tradeSn, refundMoney);
        }

        map.put("balance", refundBalanceMoney.toString());
        map.put("payMoney", refundPayMoney.toString());
        map.put("tradeSn", tradeSn);

        return map;
    }


    public DubboPageResult<EsWapBalanceTradeDO> updateOrder(Double balance, String tradeSn) {
        try{
            // 需要第三方支付的钱
            Double needPayMoney;
            QueryWrapper<EsTrade> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTrade::getTradeSn,tradeSn);
            EsTrade esTrade = this.esTradeMapper.selectOne(queryWrapper);
            Double totalPrice = esTrade.getTotalMoney();
            needPayMoney = totalPrice - balance;
            esTrade.setUseBalance(balance);


            List<EsWapBalanceTradeDO> list=new ArrayList<>();
            //订单从表
            QueryWrapper<EsOrder> wrapper=new QueryWrapper<>();
            wrapper.lambda().in(EsOrder::getTradeSn,tradeSn);
            List<EsOrder> esOrderList = this.esOrderMapper.selectList(wrapper);
            List<EsOrder> collect = esOrderList.stream().sorted(Comparator.comparing(EsOrder::getOrderMoney).reversed()).collect(Collectors.toList());
            Boolean flag = false;
            for (EsOrder order : collect) {
                EsWapBalanceTradeDO balanceTradeDO=new EsWapBalanceTradeDO();

                //如果余额支付金额大于交易总额，
                if (balance >= totalPrice){
                    //支付方式
                    order.setPluginId("yueDirectPlugin");
                    order.setPaymentMethodName("余额");
                    //余额支付，直接修改付款状态
                    order.setPayState(PayStatusEnum.PAY_YES.value());
                    order.setOrderState(OrderStatusEnum.PAID_OFF.value());
                    this.esOrderMapper.updateById(order);
                    //从用户账户扣除使用的余额
                    if (BigDecimal.ZERO.compareTo(new BigDecimal(balance)) < 0) {
                        this.setMemberBalance(tradeSn,order.getOrderSn(),order.getMemberId(),
                                order.getOrderMoney(), MemberBalanceEnum.REDUCE_BALANCE.value());
                    }
                    logger.info("使用余额支付");

                }else{
                    flag = true;
                    //余额不足 需要支付的金额先存入交易单表中 订单总额大于余额支付的金额 则订单为已确认状态
                    if(balance>0){
                        //支付方式
                        order.setPluginId("yueDirectPlugin");
                        order.setPaymentMethodName("余额");
                    }

                    // 如果 使用的金额大于一个订单的 但是小于两个订单的情况
                    if(balance >= order.getOrderMoney()){
                        //该订单使用的余额 = 该订单的总额
                        order.setPayedMoney(order.getOrderMoney());
                        //从用户账户扣除每个订单 需要扣除的余额
                        if (BigDecimal.ZERO.compareTo(new BigDecimal(balance)) <= 0) {
                            this.setMemberBalance(order.getTradeSn(),order.getOrderSn(),order.getMemberId(),balance , MemberBalanceEnum.REDUCE_BALANCE.value());
                        }
                        // 设置下个订单可以使用的余额
                        balance = MathUtil.subtract(balance,order.getOrderMoney());

                        // 需要第三方支付的金额为 0
                        order.setNeedPayMoney(0.0);
                        // 设置 需第三方支付的金额
                        order.setPayMoney(0.0);
                    }else {
                        // 如果 剩下的余额小于一个订单的
                        //该订单使用的余额
                        order.setPayedMoney(balance);

                        Double subtract = MathUtil.subtract(order.getOrderMoney(), balance);
                        // 防止为负数
                        subtract = subtract > 0.0 ? subtract : 0.0;
                        // 需要第三方支付的金额 = 该订单总额 - 剩余的余额
                        order.setNeedPayMoney(subtract);
                        // 设置 需第三方支付的金额
                        order.setPayMoney(subtract);

                        //从用户账户扣除每个订单 需要的余额
                        if (BigDecimal.ZERO.compareTo(new BigDecimal(balance)) <= 0) {
                            this.setMemberBalance(order.getTradeSn(),order.getOrderSn(),order.getMemberId(),balance , MemberBalanceEnum.REDUCE_BALANCE.value());
                        }
                        // 设置下个订单可以使用的余额
                        balance = MathUtil.subtract(balance,order.getOrderMoney());

                        balance = balance > 0.0 ? balance : 0.0;
                    }
                    //更新订单表


                    this.esOrderMapper.updateById(order);

                    logger.info("使用第三方支付");
                }
                balanceTradeDO.setTradeSn(order.getTradeSn());
                balanceTradeDO.setOrderSn(order.getOrderSn());
                balanceTradeDO.setTotalMoney(esTrade.getTotalMoney());
                list.add(balanceTradeDO);

            }
            // 判断余额支付是否可以把所有订单支付完成，如果可以支付完成 则trade 表的交易状态为 已付款
            if (!flag){
                list.stream().map(balanceTradeDO -> {
                    balanceTradeDO.setPaymentMethodName("余额");
                    balanceTradeDO.setPluginId("yueDirectPlugin");
                    balanceTradeDO.setTradeStatus(OrderStatusEnum.PAID_OFF.value());
                    balanceTradeDO.setNeedMoney(0.0);
                    return balanceTradeDO;
                }).collect(Collectors.toList());
                esTrade.setTradeStatus(OrderStatusEnum.PAID_OFF.value());
                esTrade.setPluginId("yueDirectPlugin");
                esTrade.setPaymentMethodName("余额");
                esTrade.setPayMoney(0.0);
                this.esTradeMapper.updateById(esTrade);
            }else {
                list.stream().map(balanceTradeDO -> {
                    balanceTradeDO.setNeedMoney(needPayMoney);
                    return balanceTradeDO;
                }).collect(Collectors.toList());
                esTrade.setPayMoney(needPayMoney);
                this.esTradeMapper.updateById(esTrade);
            }

            return DubboPageResult.success(list);
        } catch (ArgumentException e) {
            return DubboPageResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }
}
