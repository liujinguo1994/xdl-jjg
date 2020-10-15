package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-提醒发货form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-28
 */
@Data
@ApiModel
public class EsWapShopNoticeLogForm implements Serializable {
    private static final long serialVersionUID = -5647609005688781505L;

    @ApiModelProperty(value = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderSn;

    @ApiModelProperty(value = "店铺Id")
    @NotNull(message = "店铺Id不能为空")
    private Long shopId;
}
