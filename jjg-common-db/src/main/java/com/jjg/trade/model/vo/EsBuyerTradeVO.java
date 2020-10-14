package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class EsBuyerTradeVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 订单编号
     */
    @ApiModelProperty(required = false,value = "交易编号")
    private String tradeSn;

    /**
     * 拆分状态
     */
    @ApiModelProperty(value = "拆分状态")
    private String cfState;

    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间")
    private Long createTime;

    /**
     * 订单总额
     */
    @ApiModelProperty(value = "订单总额")
    private Double orderMoney;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String shipName;

	private List<EsBuyerItemsVO> tradeList;

}
