package com.xdl.jjg.manager;


import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsPaymentMethodDO;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsPaymentBillDTO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.jjg.trade.model.dto.SynPaymentParameter;
import com.jjg.trade.model.enums.ClientType;
import com.jjg.trade.model.enums.OrderStatusEnum;
import com.jjg.trade.model.enums.RequestParam;
import com.jjg.trade.model.enums.TradeType;
import com.jjg.trade.model.form.PayParamForm;
import com.jjg.trade.model.vo.FormVO;
import com.jjg.trade.model.vo.PayBillVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.plugin.PaymentPluginManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: OrderPayManager
 * @Description: 订单支付业务层
 * @Author: libw  981087977@qq.com
 * @Date: 6/10/2019 20:25
 * @Version: 1.0
 */
@Component
public class OrderPayManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Http请求默认端口 */
    private static final String HTTP_PORT = "80";

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsOrderService orderService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsTradeService tradeService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsPaymentMethodService paymentMethodService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsPaymentBillService paymentBillService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IPayService payService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private List<PaymentPluginManager> paymentPluginList;

    @Value("${server.address.serverNameBuyerApi}")
    private String serverNameBuyerApi;

    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker(5,5);

    /**
     * 订单支付
     *
     * @param param 订单支付参数
     * @param request
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 15:33:36
     * @return: Form
     */
    public FormVO pay(PayParamForm param, HttpServletRequest request) {
        //对订单状态进行检测，只有已确认的订单才可以进行支付
        boolean isLegitimate;
        if (param.getTradeType().equals(TradeType.order.name())) {
            isLegitimate = this.checkStatus(param.getSn(), TradeType.order, 0);
        } else {
            isLegitimate = this.checkStatus(param.getSn(), TradeType.trade, 0);
        }
        //判断订单状态是否已确认可以订单支付，否抛出异常
        if (!isLegitimate) {
            throw new ArgumentException(TradeErrorCode.INCORRECT_TRADE_STATUS.getErrorCode(),
                    TradeErrorCode.INCORRECT_TRADE_STATUS.getErrorMsg());
        }

        // 查询交易金额或订单金额
        Double orderPrice;
        if (TradeType.trade.name().equals(param.getTradeType())) {
            // 获取交易详情，查询交易单金额
            DubboResult result = tradeService.getEsTradeInfoByOrderSn(param.getSn());
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }

            orderPrice = ((EsTradeDO) result.getData()).getPayMoney();
        } else {
            // 获取订单详情，查询订单金额
            DubboResult orderResult = orderService.getEsBuyerOrderInfo(param.getSn());
            if (!orderResult.isSuccess()) {
                throw new ArgumentException(orderResult.getCode(), orderResult.getMsg());
            }
            orderPrice = ((EsOrderDO) orderResult.getData()).getNeedPayMoney();
        }

        PayBillVO bill = new PayBillVO(orderPrice);
        bill.setPayMode(param.getPayMode());
        bill.setTradeType(TradeType.valueOf(param.getTradeType()));
        bill.setClientType(ClientType.valueOf(param.getClientType()));
        //设置用户唯一标识
        bill.setOpenid(param.getOpenid());

        // 查询支付方式
        DubboResult<EsPaymentMethodDO> payMethodResult = paymentMethodService.getByPluginId(param.getPaymentPluginId());
        if (!payMethodResult.isSuccess()) {
            throw new ArgumentException(payMethodResult.getCode(), payMethodResult.getMsg());
        }

        EsPaymentMethodDO paymentMethod = payMethodResult.getData();

        String outTradeNo = param.getSn();

        // 生成支付流水
        EsPaymentBillDTO paymentBill = new EsPaymentBillDTO(param.getSn(), outTradeNo, bill.getTradeType().name()
                , paymentMethod.getMethodName(), bill.getOrderPrice(), paymentMethod.getPluginId());
        // 保存支付参数
        switch (ClientType.valueOf(param.getClientType())) {
            case PC:
                paymentBill.setPayConfig(paymentMethod.getPcConfig());
                break;
            case WAP:
                paymentBill.setPayConfig(paymentMethod.getWapConfig());
                break;
            case NATIVE:
                paymentBill.setPayConfig(paymentMethod.getAppNativeConfig());
                break;
            case APPLET:
                paymentBill.setPayConfig(paymentMethod.getAppletConfig());
                break;
            default:
                break;
        }
        DubboResult payBillResult = this.paymentBillService.insertPaymentBill(paymentBill);
        if (!payBillResult.isSuccess()) {
            throw new ArgumentException(payBillResult.getCode(), payBillResult.getMsg());
        }

        // 修改订单或交易单的支付方式

        // 修改交易支付方式
        EsTradeDTO trade = new EsTradeDTO();
        DubboResult<EsTradeDO> esTradeInfo = tradeService.getEsTradeInfo(param.getSn());
        if (esTradeInfo.isSuccess()){
            EsTradeDO esTradeDO = esTradeInfo.getData();
            if (esTradeDO.getUseBalance()>0){
                paymentBill.setPaymentName("余额 + "+paymentBill.getPaymentName());
            }
            if (paymentBill.getPaymentPluginId() == null){
                trade.setPluginId("yueDirectPlugin");
                trade.setPaymentMethodName("余额");
            }else {
                trade.setPluginId(paymentBill.getPaymentPluginId());
            }
        }
        trade.setTradeSn(param.getSn());
        trade.setPaymentMethodName(paymentBill.getPaymentName());

        DubboResult result = tradeService.updateTradeMessage(trade);
        if (!result.isSuccess()) {
            throw new ArgumentException(result.getCode(), result.getMsg());
        }

        // 修改订单支付方式
        EsOrderDTO order = new EsOrderDTO();
        order.setTradeSn(param.getSn());
        order.setPaymentMethodName(paymentBill.getPaymentName());
        order.setPluginId(paymentBill.getPaymentPluginId());
        DubboResult orderResult = orderService.updateOrderPayParam(order);
        if (!orderResult.isSuccess()) {
            throw new ArgumentException(orderResult.getCode(), orderResult.getMsg());
        }


        bill.setOutTradeNo(outTradeNo);
        bill.setPaymentPluginId(param.getPaymentPluginId());

        setRequestParamForPay(bill, request);

        DubboResult<FormVO> pay = payService.pay(bill);

        if (!pay.isSuccess()){
            throw new ArgumentException(pay.getCode(), pay.getMsg());
        }

        return  pay.getData();
    }


    public String onQuery(PayBillVO bill,HttpServletRequest request) {
        try {
            getConfig(bill);
            setRequestParamForPay(bill, request);
            return payService.onQuery(bill);
        } catch (ArgumentException e) {
            return payService.onQuery(bill);
        }
    }


    /**
     * 获取支付插件配置参数
     * @author: libw 981087977@qq.com
     * @date: 2019/07/10 16:10:16
     * @param bill  支付参数
     * @return: void
     */
    private void getConfig(PayBillVO bill) {
        EsPaymentMethodDO paymentMethod = paymentMethodService.getByPluginId(bill.getPaymentPluginId()).getData();

        switch (bill.getClientType()) {
            case PC:
                bill.setConfig(paymentMethod.getPcConfig());
                break;
            case WAP:
                bill.setConfig(paymentMethod.getWapConfig());
                break;
            case NATIVE:
                bill.setConfig(paymentMethod.getAppNativeConfig());
                break;
            default:
                break;
        }
    }

    /**
     * 设置支付用的请求参数
     * @author: libw 981087977@qq.com
     * @date: 2019/07/10 16:36:47
     * @param bill
     * @param request
     * @return: void
     */
    private void setRequestParamForPay(PayBillVO bill, HttpServletRequest request) {

        // 获取端口
        String port = "" + request.getServerPort();
        if (HTTP_PORT.equals(port)) {
            port = "";
        } else {
            port = ":" + port;
        }

        // 上下文路径
        String contextPath = request.getContextPath();
        if ("/".equals(contextPath)) {
            contextPath = "";
        }

        // 正式环境
//         String domain = request.getScheme() + "://" + "buyerapi.zofmall.com";
        String domain = request.getScheme() + "://" + serverNameBuyerApi;

        domain += contextPath;

        Map<String, String> map = new HashMap<>(16);
        map.put(RequestParam.DOMAIN.getName(), domain);
        map.put(RequestParam.IP.getName(),getIpAddress(request));
        bill.setRequestInfo(map);
    }
    private String getIpAddress(HttpServletRequest request){
        try{
            String ip = request.getHeader("x-forwarded-for");
            if(StringUtil.isEmpty(ip)){
                return request.getRemoteAddr();
            }
            if(ip.contains(",")){
                ip = ip.split(",")[0];
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Cdn-Src-Ip");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }catch (Throwable e){
            return "";
        }
    }


    public DubboResult payReturn(TradeType tradeType, String paymentPluginId, SynPaymentParameter parameter) {
        DubboResult dubboResult = this.payService.onReturn(tradeType, paymentPluginId, parameter);
        return dubboResult.success(dubboResult);
    }

    public String payCallback(TradeType tradeType, String paymentPluginId, ClientType clientType,SynPaymentParameter parameter) {
        DubboResult dubboResult = this.payService.onCallback(tradeType, paymentPluginId, clientType, parameter);
        String status = dubboResult.getData().toString();
        return status;
    }

    public String wxPayCallback(TradeType tradeType, String paymentPluginId, ClientType clientType,Map<String, String> params) {
        DubboResult dubboResult = this.payService.wxCallback(tradeType, paymentPluginId, clientType,params);
        String status = dubboResult.getData().toString();
        return status;
    }

//    public String queryResult(PayParam param) {
//
//        PaymentPluginManager plugin = this.findPlugin(param.getPaymentPluginId());
//
//        PaymentBillDO paymentBill = this.paymentBillManager.getBillBySnAndTradeType(param.getSn(), param.getTradeType());
//        //已经支付回调，则不需要查询
//        if (paymentBill.getIsPay() == 1) {
//            return "success";
//        }
//
//        PayBill bill = new PayBill();
//        bill.setOutTradeNo(paymentBill.getOutTradeNo());
//        bill.setClientType(ClientType.valueOf(param.getClientType()));
//        bill.setTradeType(TradeType.valueOf(param.getTradeType()));
//
//        return plugin.onQuery(bill);
//    }
//
//    public Map originRefund(String returnTradeNo, String refundSn, Double refundPrice) {
//
//        //查询对应的支付流水，找到对应的支付参数
//        PaymentBillDO payBill = this.paymentBillManager.getBillByReturnTradeNo(returnTradeNo);
//
//        if (payBill == null) {
//            throw new ServiceException(PaymentErrorCode.E504.code(), "支付账单不存在");
//        }
//        PaymentPluginManager plugin = findPlugin(payBill.getPaymentPluginId());
//
//        RefundBill refundBill = new RefundBill();
//        //支付参数
//        Map map = JsonUtil.jsonToObject(payBill.getPayConfig(), Map.class);
//
//        List<Map> list = (List<Map>) map.get("config_list");
//        Map<String, String> result = new HashMap<>(list.size());
//        if (list != null) {
//            for (Map item : list) {
//                result.put(item.get("name").toString(), item.get("value").toString());
//            }
//        }
//        refundBill.setConfigMap(result);
//        refundBill.setRefundPrice(refundPrice);
//        refundBill.setRefundSn(refundSn);
//        refundBill.setReturnTradeNo(returnTradeNo);
//        refundBill.setTradePrice(payBill.getTradePrice());
//        boolean b = plugin.onTradeRefund(refundBill);
//        Map hashMap = new HashMap(2);
//        if (b) {
//            hashMap.put("result", "true");
//        } else {
//            hashMap.put("result", "false");
//            hashMap.put("fail_reason", this.cache.get(AbstractPaymentPlugin.REFUND_ERROR_MESSAGE + "_" + refundBill.getRefundSn()));
//        }
//        return hashMap;
//    }
//
//    public String queryRefundStatus(String returnTradeNo, String refundSn) {
//
//        //查询对应的支付流水，找到对应的支付参数
//        PaymentBillDO payBill = this.paymentBillManager.getBillByReturnTradeNo(returnTradeNo);
//        RefundBill refundBill = new RefundBill();
//        //支付参数
//        Map map = JsonUtil.jsonToObject(payBill.getPayConfig(), Map.class);
//        List<Map> list = (List<Map>) map.get("config_list");
//        Map<String, String> result = new HashMap<>(list.size());
//        if (list != null) {
//            for (Map item : list) {
//                result.put(item.get("name").toString(), item.get("value").toString());
//            }
//        }
//        refundBill.setConfigMap(result);
//        refundBill.setRefundSn(refundSn);
//        refundBill.setReturnTradeNo(returnTradeNo);
//
//        PaymentPluginManager plugin = findPlugin(payBill.getPaymentPluginId());
//
//        return plugin.queryRefundStatus(refundBill);
//    }
//
    /**
     * 查找支付插件
     *
     * @param pluginId
     * @return
     */
    private PaymentPluginManager findPlugin(String pluginId) {
        for (PaymentPluginManager plugin : paymentPluginList) {
            if (plugin.getPluginId().equals(pluginId)) {
                return plugin;
            }
        }
        return null;
    }

    /**
     * 检测交易（订单）是否可以被支付
     *
     * @param sn        订单（交易）号
     * @param tradeType 交易类型
     * @param times     次数
     * @return 是否可以被支付
     */
    private boolean checkStatus(String sn, TradeType tradeType, Integer times) {
        try {
            // 如果超过三次则直接返回false，不能支付
            if (times >= 3) {
                return false;
            }

            // 订单或者交易状态
            String status = null;
            if (tradeType.equals(TradeType.order)) {
                // 获取订单详情，判断订单是否是已确认状态
                DubboResult orderResult = orderService.getEsBuyerOrderInfo(sn);
                if (!orderResult.isSuccess()) {
                    throw new ArgumentException(orderResult.getCode(), orderResult.getMsg());
                }
                EsOrderDO order = (EsOrderDO) orderResult.getData();

                if (order != null) {
                    status = order.getOrderState();
                } else {
                    throw new ArgumentException(TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorCode(),
                            TradeErrorCode.ORDER_DOES_NOT_EXIST.getErrorMsg());
                }
            } else {
                // 获取交易详情，判断交易是否是已确认状态
                DubboResult result = tradeService.getEsTradeInfoByOrderSn(sn);
                if (!result.isSuccess()) {
                    throw new ArgumentException(result.getCode(), result.getMsg());
                }

                EsTradeDO trade = (EsTradeDO) result.getData();
                if (trade != null) {
                    status = trade.getTradeStatus();
                } else {
                    throw new ArgumentException(TradeErrorCode.TRADE_DOES_NOT_EXIST.getErrorCode(),
                            TradeErrorCode.TRADE_DOES_NOT_EXIST.getErrorMsg());
                }
            }

            // 检验交易或者订单状态是否是已确认可被支付
            if (status.equals(OrderStatusEnum.CONFIRM.value())) {
                return true;
            } else {
                Thread.sleep(1000);
                return this.checkStatus(sn, tradeType, ++times);
            }
        } catch (Exception e) {
            logger.error("检测订单是否可被支付,订单不可被支付，重试检测" + times + ",次，消息" + e.getMessage());
            this.checkStatus(sn, tradeType, ++times);
        }
        return false;
    }
}
