package com.jjg.member.model.dto;

import com.shopx.trade.api.model.domain.vo.ClientConfigVO;
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
public class PaymentPluginDTO implements Serializable {

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
}
