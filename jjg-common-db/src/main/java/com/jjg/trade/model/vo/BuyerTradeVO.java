package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.operateChecker.OrderOperateAllowable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单主表-es_trade
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyerTradeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tradeSn;

    /**
     * 总价格
     */
    @ApiModelProperty(value = "总价格")
    private BigDecimal totalMoney;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsMoney;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal freightMoney;

    /**
     * 优惠的金额
     */
    @ApiModelProperty(value = "优惠的金额")
    private BigDecimal discountMoney;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String consigneeTelephone;

    /**
     * 交易创建时间
     */
    @ApiModelProperty(value = "交易创建时间")
    private Long createTime;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private String tradeStatus;

    /**
     * 订单集合
     */
    @ApiModelProperty(value = "订单集合")
    private List<EsOrderVO> orderList;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    /**
     * 收货国家
     */
    @ApiModelProperty(value = "收货国家")
    private String consigneeCountry;

    /**
     * 收货省
     */
    @ApiModelProperty(value = "收货省")
    private String consigneeProvince;

    /**
     * 收货市
     */
    @ApiModelProperty(value = "收货市")
    private String consigneeCity;

    /**
     * 收货区
     */
    @ApiModelProperty(value = "收货区")
    private String consigneeCounty;

    /**
     * 收货镇
     */
    @ApiModelProperty(value = "收货镇")
    private String consigneeTown;

    /**
     * 收货详细地址
     */
    @ApiModelProperty(value = "收货详细地址")
    private String consigneeAddress;

    /**
     * 操作权限
     */
    @ApiModelProperty(value = "操作权限")
    private OrderOperateAllowable orderOperateAllowable;
}
