package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 收银台参数VO
 * @author Snow create in 2018/7/11
 * @version v2.0
 * @since v7.0.0
 */
@Data
@ApiModel(description = "收银台参数VO")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CashierVO {

    private String sn;

    private String shipMethod;

    @ApiModelProperty(name="ship_name",value="收货人姓名")
    private String shipName;

    @ApiModelProperty(name="ship_addr",value="收货地址")
    private String shipAddr;

    @ApiModelProperty(name="order_state",value="订单状态")
    private String orderState;

    @ApiModelProperty(name="ship_mobile",value="收货人手机")
    private String shipMobile;

    @ApiModelProperty(name="ship_tel",value="收货人电话")
    private String shipTel;

    @ApiModelProperty(name="ship_province",value="配送地区-省份")
    private String shipProvince;

    @ApiModelProperty(name="ship_city",value="配送地区-城市")
    private String shipCity;

    @ApiModelProperty(name="ship_county",value="配送地区-区(县)")
    private String shipCounty;

    @ApiModelProperty(name="ship_town",value="配送街道")
    private String shipTown;

    @ApiModelProperty(name="need_pay_price",value="应付金额")
    private Double needPayPrice;

    @ApiModelProperty(name="total_price",value="订单总金额")
    private Double totalPrice;

    @ApiModelProperty(name="pay_type_text",value="支付方式")
    private String payTypeText;

    @ApiModelProperty(value="商品名称集合")
    private List<String> goodsName;

    @ApiModelProperty(value="订单关闭剩余时间")
    private Long closeOrderTime;

    private String payUrl;


}
