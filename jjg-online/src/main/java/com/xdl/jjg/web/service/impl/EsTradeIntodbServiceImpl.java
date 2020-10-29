package com.xdl.jjg.web.service.impl;


import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.trade.model.domain.EsGoodsSkuAndLfcDO;
import com.jjg.trade.model.domain.EsLfcOrderSkuDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsLfcOrderDTO;
import com.jjg.trade.model.enums.*;
import com.xdl.jjg.entity.EsOrder;
import com.xdl.jjg.entity.EsOrderItems;
import com.xdl.jjg.entity.EsOrderLog;
import com.xdl.jjg.entity.EsTrade;
import com.xdl.jjg.mapper.EsOrderItemsMapper;
import com.xdl.jjg.mapper.EsOrderLogMapper;
import com.xdl.jjg.mapper.EsOrderMapper;
import com.xdl.jjg.mapper.EsTradeMapper;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.utils.CurrencyUtil;
import com.xdl.jjg.web.service.IEsTradeIntodbService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易入库业务实现类
 *
 * @author Snow create in 2018/5/9
 * @version v2.0
 * @since v7.0.0
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsTradeIntodbService.class,timeout = 5000)
public class EsTradeIntodbServiceImpl implements IEsTradeIntodbService {

    public static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private GoodsSkuService esGoodsSkuService;
    @Autowired
    private EsTradeMapper esTradeMapper;
    @Autowired
    private EsOrderMapper orderMapper;
    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;
    @Autowired
    private EsOrderLogMapper esOrderLogMapper;
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Value("${lfc.order.consignee}")
    private Long consigneeId;
    @Value("${lfc.order.memberid}")
    private Long memberId;
    @Value("${lfc.order.shopid}")
    private Long shopId;
    @Value("${lfc.order.shopname}")
    private String shopName;
    @Value("${lfc.order.membername}")
    private String memberName;

    @Override
    public void intoDB(EsTradeDO tradeVO, Double balance) {

    }

    @Override
    public void intoLfcOrderDB(EsLfcOrderDTO lfcOrder) {
        List<EsLfcOrderSkuDO> orderSkuList = lfcOrder.getOrderSkus();
        List<EsGoodsSkuAndLfcDO> goodsSkuDOList=new ArrayList<>();
        Double orderPrice=0.0;
        for (EsLfcOrderSkuDO orderSku:orderSkuList) {
            String skuId = orderSku.getSkuId();
            DubboResult<EsGoodsSkuCO> result = esGoodsSkuService.getGoodsSku(Long.valueOf(skuId));
            if(result.isSuccess()){
                EsGoodsSkuCO esGoodsSkuCO = result.getData();
                orderPrice= CurrencyUtil.add(orderPrice, CurrencyUtil.mul(esGoodsSkuCO.getMoney(),orderSku.getSaleCount()));
                EsGoodsSkuAndLfcDO goodsSkuAndLfcDO=new EsGoodsSkuAndLfcDO();
                BeanUtil.copyProperties(esGoodsSkuCO,goodsSkuAndLfcDO);
                goodsSkuAndLfcDO.setSaleCount(orderSku.getSaleCount());
                goodsSkuAndLfcDO.setSkuId(esGoodsSkuCO.getId());
                goodsSkuAndLfcDO.setMorey(esGoodsSkuCO.getMoney());
                goodsSkuAndLfcDO.setShopId(esGoodsSkuCO.getShopId());
                goodsSkuDOList.add(goodsSkuAndLfcDO);
            }
        }
        EsTrade esTrade=new EsTrade(orderSkuList);
        esTrade.setTradeSn(snowflakeIdWorker.nextId()+"");
        esTrade.setTotalMoney(orderPrice);
        esTrade.setGoodsMoney(orderPrice);
        esTrade.setIsDeposit(0);
        esTrade.setHasBalance(0);
        long now =System.currentTimeMillis() / 1000;
        esTrade.setCreateTime(now);
        esTrade.setConsigneeName(lfcOrder.getReceiverName());
        esTrade.setConsigneeAddress(lfcOrder.getReceiverAddressDetail());
        //收货人ID
        esTrade.setConsigneeId(consigneeId);
        esTrade.setConsigneeMobile(lfcOrder.getReceiverPhone());
        esTrade.setConsigneeProvince(lfcOrder.getReceiverProvinceName());
        esTrade.setConsigneeCity(lfcOrder.getReceiverCityName());
        esTrade.setConsigneeCounty(lfcOrder.getReceiverDistrictName());
        esTrade.setLfcId(lfcOrder.getOrderId());
        this.esTradeMapper.insert(esTrade);

        EsOrder esOrder = new EsOrder(lfcOrder,goodsSkuDOList);
        esOrder.setOrderSn(snowflakeIdWorker.nextId()+"");
        esOrder.setCreateTime(now);
        esOrder.setTradeSn(esTrade.getTradeSn());
        esOrder.setLfcId(lfcOrder.getOrderId());
        esOrder.setOrderMoney(orderPrice);
        esOrder.setGoodsMoney(orderPrice);
        esOrder.setDiscountMoney(orderPrice);
        esOrder.setOrderState(OrderStatusEnum.CONFIRM.value());
        esOrder.setUpdateTime(now);
        esOrder.setNeedPayMoney(0.0);
        esOrder.setMemberId(memberId);
        esOrder.setShopId(shopId);
        esOrder.setShopName(shopName);
        esOrder.setMemberName(memberName);
        esOrder.setItemsJson(JsonUtil.objectToJson(goodsSkuDOList.get(0)));
        //订单来源
        esOrder.setClientType(OrderResourceEnum.LFC.value());
        if(esOrder.getNeedPayMoney()==0){
            long nowTime =System.currentTimeMillis() / 1000;
            esOrder.setOrderState(OrderStatusEnum.PAID_OFF.value());
            esOrder.setPayState(PayStatusEnum.PAY_YES.value());
            esOrder.setPluginId("yueDirectPlugin");
            esOrder.setPaymentMethodName("LFC支付");
            esOrder.setShipState(ShipStatusEnum.SHIP_NO.value());
            esOrder.setPaymentTime(nowTime);
            esOrder.setUpdateTime(nowTime);
        }
        esOrder.setPayedMoney(orderPrice);
        this.orderMapper.insert(esOrder);
        //插入订单详情
        for (EsLfcOrderSkuDO orderSku:orderSkuList) {
            String skuId = orderSku.getSkuId();
            DubboResult<EsGoodsSkuCO> result = esGoodsSkuService.getGoodsSkuEnable(Long.valueOf(skuId));
            if(result.isSuccess()){
                EsGoodsSkuCO esGoodsSkuCO = result.getData();
                EsOrderItems item = new EsOrderItems(esGoodsSkuCO,orderSku.getSaleCount());
                item.setOrderSn(esOrder.getOrderSn());
                item.setTradeSn(esOrder.getTradeSn());
                item.setState(ServiceStatusEnum.NOT_APPLY.name());
                esOrderItemsMapper.insert(item);
            }
        }
        EsOrderLog logDO = new EsOrderLog();
        logDO.setOrderSn(esOrder.getOrderSn());
        logDO.setMessage("创建订单");
        logDO.setOpName("lfcOrder");
        logDO.setOpTime(System.currentTimeMillis() / 1000);
        esOrderLogMapper.insert(logDO);
    }

    @Override
    public void intoXxxOrderDB(EsLfcOrderDTO lfcOrder) {

    }
}
