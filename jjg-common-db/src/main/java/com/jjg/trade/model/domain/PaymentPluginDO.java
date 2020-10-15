package com.jjg.trade.model.domain;


import com.jjg.trade.model.vo.ClientConfigVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @ClassName: PaymentPluginVO
 * @Description: 支付插件vo
 * @Author: libw  981087977@qq.com
 * @Date: 7/16/2019 20:01
 * @Version: 1.0
 */
@Data
public class PaymentPluginDO implements Serializable {

    private static final long serialVersionUID = 8418953348138006672L;

    /**
     * 支付方式名称
     */
    private String methodName;

    /**
     * 支付插件id
     */
    private String pluginId;

    /**
     * 支付方式图片
     */
    private String image;

    /**
     * 是否支持原路退回，0不支持  1支持
     */
    private Integer isRetrace;

    /**
     * 配置项
     */
    private List<ClientConfigVO> enableClient;


    public PaymentPluginDO(EsPaymentMethodDO payment) {
        this.methodName = payment.getMethodName();
        this.pluginId = payment.getPluginId();
        this.image = payment.getImage();
        this.isRetrace = payment.getIsRetrace();
    }

    public PaymentPluginDO(String methodName, String pluginId, Integer isRetrace) {
        this.methodName = methodName;
        this.pluginId = pluginId;
        this.isRetrace = isRetrace;
    }

    public PaymentPluginDO() {
    }

    @Override
    public String toString() {
        final StringBuilder json = new StringBuilder("{");
        json.append("\"methodName\":\"")
                .append(methodName).append('\"');
        json.append(" ,\"pluginId\":\"")
                .append(pluginId).append('\"');
        json.append(" ,\"image\":\"")
                .append(image).append('\"');
        json.append(" ,\"isRetrace\":")
                .append(isRetrace);
        json.append(" ,\"enableClient\":")
                .append(enableClient);
        json.append('}');
        return json.toString();
    }
}
