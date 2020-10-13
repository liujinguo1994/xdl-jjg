package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@Api
public class EsOrderQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 814587740630307420L;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    /**
     * 买家账号
     */
    @ApiModelProperty(value = "买家账号")
    private String memberName;

    /**
     * 订单状态(CONFIRM待付款，PAID_OFF待发货，SHIPPED待收货，ROG已收货，COMPLETE已完成，CANCELLED已取消，AFTER_SERVICE售后中)
     */
    @ApiModelProperty(value = "订单状态(CONFIRM待付款，PAID_OFF待发货，SHIPPED待收货，ROG已收货，COMPLETE已完成，CANCELLED已取消，AFTER_SERVICE售后中)")
    private String orderState;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String shipName;

    /**
     * 下单时间开始
     */
    @ApiModelProperty(value = "下单时间开始")
    private Long createTimeStart;
    /**
     * 下单时间结束
     */
    @ApiModelProperty(value = "下单时间结束")
    private Long createTimeEnd;

    /**
     * 订单来源 (pc、wap、app)
     */
    @ApiModelProperty(value = "订单来源 (pc、wap、app)")
    private String clientType;

    private String order_status;
}
