package com.jjg.trade.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@ApiModel(description = "结算单明细")
@Data
public class EsSettlementDetailVO extends Model<EsSettlementDetailVO> {

    private static final long serialVersionUID = 1L;

    /**
     * 退款编号
     */
    @ApiModelProperty(value = "退款编号")
    private String refundSn;

    /**
     * 商品ID
     */
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * skuID
     */
	@ApiModelProperty(value = "skuID")
	private Long skuId;

    /**
     * 数量
     */
	@ApiModelProperty(value = "数量")
	private Integer num;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "子订单编号")
	private String orderSn;

    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodName;

    /**
     * 优惠后价格
     */
	@ApiModelProperty(value = "优惠后价格")
	private Double saleMoney;

	/**
	 * 金额
	 */
	@ApiModelProperty(value = "金额")
	private Double money;

	/**
	 * 下单时间
	 */
	@ApiModelProperty(value = "下单时间")
	private Long creatTime;
}
