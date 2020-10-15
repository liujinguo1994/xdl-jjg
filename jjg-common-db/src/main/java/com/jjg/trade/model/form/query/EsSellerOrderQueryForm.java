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
 * 订单明细表-esSellerOrderQueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSellerOrderQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父订单编号
     */
	@ApiModelProperty(value = "父订单编号")
	private String tradeSn;

    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "子订单订单编号")
	private String orderSn;
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
     * 商品总额(折扣后价格)
     */
	@ApiModelProperty(value = "商品总额(折扣后价格)")
	private Double goodsMoney;
    /**
     * 收货人姓名
     */
	@ApiModelProperty(value = "收货人姓名")
	private String shipName;
    /**
     * 订单来源 (pc、wap、app)
     */
	@ApiModelProperty(value = "订单来源 (pc、wap、app)")
	private String clientType;
    /**
     * 订单创建时间
     */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;

}
