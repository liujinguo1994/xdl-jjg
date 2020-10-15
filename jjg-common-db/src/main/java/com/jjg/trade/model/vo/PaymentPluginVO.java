package com.jjg.trade.model.vo;

import com.shopx.trade.api.model.domain.EsPaymentMethodDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
@ApiModel
public class PaymentPluginVO implements Serializable {

    private static final long serialVersionUID = 8418953348138006672L;

    @ApiModelProperty(value = "支付方式名称", hidden = true)
    private String methodName;

    @ApiModelProperty(value = "支付插件id", hidden = true)
    private String pluginId;

    @ApiModelProperty(value = "支付方式图片")
    private String image;

    @ApiModelProperty(value = "是否支持原路退回，0不支持  1支持")
    @NotNull(message = "请选择是否支持原路退回")
    @Min(value = 0, message = "是否支持原路退回值不正确")
    @Max(value = 1, message = "是否支持原路退回值不正确")
    private Integer isRetrace;

    @ApiModelProperty(value = "配置项")
    @NotNull(message = "客户端开启情况不能为空")
    @Valid
    private List<ClientConfigVO> enableClient;


    public PaymentPluginVO(EsPaymentMethodDO payment) {
        this.methodName = payment.getMethodName();
        this.pluginId = payment.getPluginId();
        this.image = payment.getImage();
        this.isRetrace = payment.getIsRetrace();
    }

    public PaymentPluginVO(String methodName, String pluginId, Integer isRetrace) {
        this.methodName = methodName;
        this.pluginId = pluginId;
        this.isRetrace = isRetrace;
    }

    public PaymentPluginVO() {
    }
}
