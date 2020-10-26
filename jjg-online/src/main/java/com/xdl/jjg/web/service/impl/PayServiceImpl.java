package com.xdl.jjg.web.service.impl;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsPaymentBillDO;
import com.shopx.trade.api.model.domain.EsPaymentMethodDO;
import com.shopx.trade.api.model.domain.dto.SynPaymentParameter;
import com.shopx.trade.api.model.domain.vo.FormVO;
import com.shopx.trade.api.model.domain.vo.PayBillVO;
import com.shopx.trade.api.model.domain.vo.RefundBillVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.RequestParam;
import com.shopx.trade.api.model.enums.TradeType;
import com.shopx.trade.api.plugin.PaymentPluginManager;
import com.shopx.trade.api.service.IEsPaymentBillService;
import com.shopx.trade.api.service.IPayService;
import com.shopx.trade.service.service.AbstractPaymentPlugin;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PayServiceImpl
 * @Description: 支付接口实现类
 * @Author: libw  981087977@qq.com
 * @Date: 7/9/2019 20:04
 * @Version: 1.0
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IPayService.class, timeout = 5000)
public class PayServiceImpl implements IPayService {

    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private List<PaymentPluginManager> paymentPluginList;
    @Autowired
    private EsPaymentMethodServiceImpl paymentMethodService;
    @Autowired
    private IEsPaymentBillService paymentBillService;
    @Autowired
    private JedisCluster cache;


    /** Http请求默认端口 */
    private static final String HTTP_PORT = "80";

    @Value("${server.address.serverNameBuyerApi}")
    private String serverNameBuyerApi;
    /**
     * 支付
     *
     * @param bill 交易信息
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:56:29
     * @return: com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.vo.FormVO>
     */
    @Override
    public DubboResult<FormVO> pay(PayBillVO bill) {
        PaymentPluginManager paymentPlugin = this.findPlugin(bill.getPaymentPluginId());
        try {
            getConfig(bill);
            assert paymentPlugin != null;
            FormVO pay = paymentPlugin.pay(bill);
            return DubboResult.success(pay);
        } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    /**
     * 同步回调
     *
     * @param tradeType 交易类型
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:56:55
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult onReturn(TradeType tradeType, String paymentPluginId,SynPaymentParameter parameter) {
        try {
            PaymentPluginManager plugin = this.findPlugin(paymentPluginId);
            DubboResult dubboResult = null;
            if (plugin != null) {
                 dubboResult = plugin.onReturn(tradeType, parameter);
            }
            return DubboResult.success(dubboResult);
        }catch (ArgumentException e){
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }

    }

    /**
     * 支付宝异步回调
     * @param tradeType  交易类型
     * @param clientType 客户端类型
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:57:13
     * @return: java.lang.String
     */
    @Override
    public DubboResult onCallback(TradeType tradeType,String paymentPluginId, ClientType clientType,SynPaymentParameter parameter) {
        PaymentPluginManager plugin = this.findPlugin(paymentPluginId);
        if (plugin != null) {
            String s = plugin.onCallback(tradeType, clientType,parameter);
            return DubboResult.success(s);
        }
        return DubboResult.success();
    }


    /**
     * @Description: 微信异步回调
     * @Author       LiuJG 344009799@qq.com
     * @Date         2020/4/13 11:13
     * @param        tradeType
     * @param        paymentPluginId
     * @param        params
     * @return       com.shopx.common.model.result.DubboResult
     * @exception
     *
     */
    @Override
    public DubboResult wxCallback(TradeType tradeType, String paymentPluginId, ClientType clientType,Map<String, String> params) {
        PaymentPluginManager plugin = this.findPlugin(paymentPluginId);
        if (plugin != null) {
            String s = plugin.wxCallback(tradeType,clientType,params);
            return DubboResult.success(s);
        }
        return DubboResult.success();
    }

    /**
     * 主动查询支付结果
     *
     * @param bill 交易单信息
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:59:50
     * @return: java.lang.String
     */
    @Override
    public String onQuery(PayBillVO bill) {
        PaymentPluginManager paymentPlugin = this.findPlugin(bill.getPaymentPluginId());
        try {
            assert paymentPlugin != null;
            return paymentPlugin.onQuery(bill);
        } catch (ArgumentException e) {
            return paymentPlugin.onQuery(bill);
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
        /*request.getServerName()*/
        // 正式环境
//        String domain = request.getScheme() + "://" + "buyerapi.zofmall.com";
        String domain = request.getScheme() + "://" + serverNameBuyerApi;


        domain += contextPath;

        Map<String, String> map = new HashMap<>(16);
        map.put(RequestParam.DOMAIN.getName(), domain);
        bill.setRequestInfo(map);
    }

    /**
     * 原路退回
     *
     * @param returnTradeNo 第三方订单号
     * @param refundSn      退款编号
     * @param refundPrice   退款金额
     * @return
     */
    @Override
    public DubboResult<Map> originRefund(String returnTradeNo, String refundSn, Double refundPrice) {
        try {
            //查询对应的支付流水，找到对应的支付参数
            EsPaymentBillDO payBill = paymentBillService.getBillByReturnTradeNo(returnTradeNo).getData();

            if (payBill == null) {
                throw new ArgumentException(TradeErrorCode.PAYMENT_BILLS_DO_NOT_EXIST.getErrorCode(),
                        TradeErrorCode.PAYMENT_BILLS_DO_NOT_EXIST.getErrorMsg());
            }
            PaymentPluginManager plugin = findPlugin(payBill.getPaymentPluginId());

            RefundBillVO refundBill = new RefundBillVO();
            //支付参数
            Map map = JsonUtil.jsonToObject(payBill.getPayConfig(), Map.class);

            List<Map> list = (List<Map>) map.get("configList");
            Map<String, String> result = new HashMap<>(list.size());
            if (list != null) {
                for (Map item : list) {
                    result.put(item.get("name").toString(), item.get("value").toString());
                }
            }
            refundBill.setConfigMap(result);
            refundBill.setRefundPrice(refundPrice);
            refundBill.setRefundSn(refundSn);
            refundBill.setReturnTradeNo(payBill.getReturnTradeNo());
            refundBill.setTradePrice(payBill.getTradeMoney());
            logger.error("返回Map:"+JsonUtil.objectToJson(refundBill));
            boolean b = plugin.onTradeRefund(refundBill);
            Map hashMap = new HashMap(2);
            if (b) {
                hashMap.put("result", "true");
            } else {
                hashMap.put("result", "false");
                hashMap.put("fail_reason", this.cache.get(AbstractPaymentPlugin.REFUND_ERROR_MESSAGE + "_" + refundBill.getRefundSn()));
            }
            logger.error("返回Map:"+JsonUtil.objectToJson(hashMap));
            return DubboResult.success(hashMap);
        } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }
    }

    /**
     * 查找支付插件
     *
     * @param pluginId
     * @return
     */
    private PaymentPluginManager findPlugin(String pluginId) {
        for (PaymentPluginManager plugin : paymentPluginList) {
            String pluginId1 = plugin.getPluginId();
            if (plugin.getPluginId().equals(pluginId)) {
                return plugin;
            }
        }
        return null;
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
            case APPLET:
                bill.setConfig(paymentMethod.getAppletConfig());
                break;
            default:
                break;
        }
    }
}
