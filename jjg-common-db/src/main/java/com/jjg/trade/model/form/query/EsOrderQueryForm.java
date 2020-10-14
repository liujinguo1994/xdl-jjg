package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单明细表-es_orderQueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsOrderQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父订单编号
     */
	@ApiModelProperty(value = "父订单编号")
	private String tradeSn;

	/**
	 * 关键字查询
	 */
	@ApiModelProperty(value = "关键字查询")
	private String keyWord;

    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "子订单订单编号")
	private String orderSn;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员ID")
	private Long memberId;

    /**
     * 买家账号
     */
	@ApiModelProperty(value = "买家账号")
	private String memberName;

    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态")
	private String orderState;

    /**
     * 付款状态
     */
	@ApiModelProperty(value = "付款状态")
	private String payState;

    /**
     * 货运状态
     */
	@ApiModelProperty(value = "货运状态")
	private String shipState;

    /**
     * 售后状态
     */
	@ApiModelProperty(value = "售后状态")
	private String serviceState;

    /**
     * 结算状态
     */
	@ApiModelProperty(value = "结算状态")
	private String billState;

    /**
     * 评论是否完成
     */
	@ApiModelProperty(value = "评论是否完成")
	private String commentStatus;

    /**
     * 支付方式类型
     */
	@ApiModelProperty(value = "支付方式类型")
	private String paymentType;

    /**
     * 支付时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "支付时间")
	private Long paymentTime;

    /**
     * 收货时间
     */
	@ApiModelProperty(value = "收货时间")
	private String receiveTime;

    /**
     * 订单备注
     */
	@ApiModelProperty(value = "订单备注")
	private String remark;

    /**
     * 订单取消原因
     */
	@ApiModelProperty(value = "订单取消原因")
	private String cancelReason;

    /**
     * 货物列表json
     */
	@ApiModelProperty(value = "货物列表json")
	private String itemsJson;

    /**
     * 会员收货地址ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员收货地址ID")
	private Long addressId;

    /**
     * 管理员备注
     */
	@ApiModelProperty(value = "管理员备注")
	private String adminRemark;

    /**
     * 完成时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "完成时间")
	private Long completeTime;

    /**
     * 确认收货签收时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "确认收货签收时间")
	private Long signingTime;

    /**
     * 发货时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "发货时间")
	private Long shipTime;

    /**
     * 订单来源 (pc、wap、app)
     */
	@ApiModelProperty(value = "订单来源 (pc、wap、app)")
	private String clientType;

    /**
     * 是否需要发票,0：否，1：是
     */
	@ApiModelProperty(value = "是否需要发票,0：否，1：是")
	private Integer needReceipt;

    /**
     * 订单创建时间
     */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;

    /**
     * 订单更新时间
     */
	@ApiModelProperty(value = "订单更新时间")
	private Long updateTime;

}
