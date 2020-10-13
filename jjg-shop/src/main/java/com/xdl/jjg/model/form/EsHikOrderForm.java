package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EsHikOrderForm implements Serializable {
    /**
     * 海康操作人姓名
     */
    @ApiModelProperty(name = "name", value = "海康操作人姓名", required = false)
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(name = "mobile", value = "手机号码", required = false)
    private String mobile;

    /**
     * 退款单号
     */
    @ApiModelProperty(name = "refund_sn", value = "退款单号", required = false)
    private Integer refundSn;
}
