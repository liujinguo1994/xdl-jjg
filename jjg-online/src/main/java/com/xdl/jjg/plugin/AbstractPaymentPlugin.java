package com.xdl.jjg.plugin;


import com.jjg.trade.model.domain.EsPaymentMethodDO;
import com.jjg.trade.model.enums.ClientType;
import com.jjg.trade.model.enums.OpenStatus;
import com.jjg.trade.model.enums.PayConfigParam;
import com.jjg.trade.model.enums.RequestParam;
import com.jjg.trade.model.vo.FormItemVO;
import com.jjg.trade.model.vo.FormVO;
import com.jjg.trade.model.vo.PayBillVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsPaymentBillService;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.*;

/**
 * 支付插件父类<br>
 * 具有读取配置的能力
 *
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午11:38:38
 */
public abstract class AbstractPaymentPlugin {

    protected final Log logger = LogFactory.getLog(getClass());

    protected static final String SUCCESS = "SUCCESS";

    protected static final String FAIL = "FAIL";

    public static final String REFUND_ERROR_MESSAGE = "REFUND_ERROR_MESSAGE";

    @Autowired
    private IEsPaymentMethodService iEsPaymentMethodService;
    @Autowired
    private IEsPaymentBillService iEsPaymentBillService;

    /**
     * 获取插件的配置方式
     *
     * @return
     */
    protected Map<String, String> getConfig(ClientType clientType) {
        //获取当前支付插件的id
        String paymentMethodId = this.getPluginId();
        DubboResult<EsPaymentMethodDO> byPluginId = iEsPaymentMethodService.getByPluginId(paymentMethodId);
        if (!byPluginId.isSuccess()){
            throw new ArgumentException(TradeErrorCode.PAYMENT_METHOD_NOT_OPENED.getErrorCode(),
                    TradeErrorCode.PAYMENT_METHOD_NOT_OPENED.getErrorMsg());
        }
        EsPaymentMethodDO paymentMethod = byPluginId.getData();
        String config = "";
        // 判断客户端支付类型
        if ("PC".equals(clientType.value())){
            config = paymentMethod.getPcConfig();
        }else if ("WAP".equals(clientType)){
            config = paymentMethod.getWapConfig();
        }else if ("NATIVE".equals(clientType)){
            config = paymentMethod.getWapConfig();
        }

        if (StringUtil.isEmpty(config)) {
            return new HashMap<>(16);
        }

        Map map = JsonUtil.jsonToObject(config, Map.class);
        List<Map> list = (List<Map>) map.get(PayConfigParam.CONFIG_LIST.getName());
        String code = OpenStatus.IS_OPEN.getCode();
        String s = map.get(PayConfigParam.IS_OPEN.getName()).toString();
        if (!code.equals(s)) {
            throw new ArgumentException(TradeErrorCode.PAYMENT_METHOD_NOT_OPENED.getErrorCode(),
                    TradeErrorCode.PAYMENT_METHOD_NOT_OPENED.getErrorMsg());
        }

        Map<String, String> result = new HashMap<>(list.size());
        if (!list.isEmpty()) {
            for (Map item : list) {
                result.put(item.get("name").toString(), item.get("value").toString());
            }
        }
        return result;
    }

    /**
     * 返回价格字符串
     *
     * @param price
     * @return
     */
    protected String formatPrice(Double price) {
        NumberFormat nFormat = NumberFormat.getNumberInstance();
        nFormat.setMaximumFractionDigits(0);
        nFormat.setGroupingUsed(false);
        return nFormat.format(price);
    }


    /**
     * 获取插件id
     *
     * @return
     */
    protected abstract String getPluginId();


    /**
     * 获取同步通知url
     *
     * @param bill 交易
     * @return
     */
    protected String getReturnUrl(PayBillVO bill) {
        Map<String, String> requestParam = bill.getRequestInfo();
        return requestParam.get(RequestParam.DOMAIN.getName()) + "/order/pay/return/" + bill.getTradeType() + "/" + bill.getPayMode() + "/" + this.getPluginId();
    }

    /**
     * 获取异步通知url
     *
     * @param bill  支付参数
     * @return
     */
    protected String getCallBackUrl(PayBillVO bill) {
        Map<String, String> requestParam = bill.getRequestInfo();
        return requestParam.get(RequestParam.DOMAIN.getName()) + "/order/pay/callback/" + bill.getTradeType() + "/" + this.getPluginId() + "/" + bill.getClientType();
    }

//    /**
//     * 支付回调后执行方法
//     *
//     * @param outTradeNo
//     * @param returnTradeNo
//     * @param tradeType
//     * @param payPrice
//     */
//    public void paySuccess(String outTradeNo, String returnTradeNo, TradeType tradeType, double payPrice) {
//
//        this.iEsPaymentBillService.pay(outTradeNo, returnTradeNo, tradeType, payPrice);
//    }

    /**
     * 组织数据
     *
     * @param actionUrl 表单提交地址
     * @param paramMap  参数Map
     * @return
     */
    protected FormVO getFormData(String actionUrl, Map<String, String> paramMap) {
        if (paramMap != null && !paramMap.isEmpty()) {
            FormVO form = new FormVO();
            List<FormItemVO> formItems = new ArrayList<>();
            Set<String> keys = paramMap.keySet();
            Iterator var3 = keys.iterator();
            while (var3.hasNext()) {
                FormItemVO item = new FormItemVO();
                String key = (String) var3.next();
                String value = paramMap.get(key);
                item.setItemName(key);
                item.setItemValue(value);
                formItems.add(item);
            }
            form.setFormItems(formItems);
            form.setGatewayUrl(actionUrl);
            return form;
        }
        return null;
    }
}
