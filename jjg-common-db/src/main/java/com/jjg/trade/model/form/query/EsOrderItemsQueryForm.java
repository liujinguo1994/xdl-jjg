package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单商品明细表-es_order_itemsQueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsOrderItemsQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

    /**
     * skuID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "skuID")
	private Long skuId;

    /**
     * 数量
     */
	@ApiModelProperty(value = "数量")
	private Integer num;

    /**
     * 已发货数量
     */
	@ApiModelProperty(value = "已发货数量")
	private Integer shipNum;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String tradeSn;

    /**
     * 子订单编号
     */
	@ApiModelProperty(value = "子订单编号")
	private String orderSn;

    /**
     * 图片
     */
	@ApiModelProperty(value = "图片")
	private String image;

    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String name;

    /**
     * 优惠后价格
     */
	@ApiModelProperty(value = "优惠后价格")
	private Double money;

    /**
     * 分类ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "分类ID")
	private Long categoryId;

    /**
     * 售后状态
     */
	@ApiModelProperty(value = "售后状态")
	private String state;

    /**
     * 支付快照id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "支付快照id")
	private Long snapshotId;

    /**
     * 规格json
     */
	@ApiModelProperty(value = "规格json")
	private String specJson;

    /**
     * 促销类型
     */
	@ApiModelProperty(value = "促销类型")
	private String promotionType;

    /**
     * 活动ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动ID")
	private Long promotionId;

    /**
     * 发货单号
     */
	@ApiModelProperty(value = "发货单号")
	private String shipNo;

    /**
     * 签收人
     */
	@ApiModelProperty(value = "签收人")
	private String theSign;

    /**
     * 物流公司ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物流公司ID")
	private Long logiId;

    /**
     * 物流公司名称
     */
	@ApiModelProperty(value = "物流公司名称")
	private String logiName;

}
