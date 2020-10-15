package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 我的卓付订单查询
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsOrdesStateForm implements Serializable {

    /**
     * 查询类型  CONFIRM:待付款, PAID_OFF:待发货, SHIPPED:待收货, ROG:待评价
     */
    @ApiModelProperty(required = false, value = "CONFIRM:待付款, PAID_OFF:待发货, SHIPPED:待收货, ROG:待评价")
    private String orderState;

    /**
     * 关键词
     */
    @ApiModelProperty(required = false, value = "关键词")
    private String keyword;

}
