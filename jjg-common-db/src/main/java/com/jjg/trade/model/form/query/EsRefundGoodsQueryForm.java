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
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsRefundGoodsQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退货(款)编号
     */
	@ApiModelProperty(value = "退货(款)编号")
	private String refundSn;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

    /**
     * SKUid
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "SKUid")
	private Long skuId;

    /**
     * 发货数量
     */
	@ApiModelProperty(value = "发货数量")
	private Integer shipNum;

    /**
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格")
	private Double money;

    /**
     * 退货数量
     */
	@ApiModelProperty(value = "退货数量")
	private Integer returnNum;

    /**
     * 退货入库数量
     */
	@ApiModelProperty(value = "退货入库数量")
	private Integer storageNum;

    /**
     * 商品编号
     */
	@ApiModelProperty(value = "商品编号")
	private String goodsSn;

    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

    /**
     * 商品图片
     */
	@ApiModelProperty(value = "商品图片")
	private String goodsImage;

    /**
     * 规格数据
     */
	@ApiModelProperty(value = "规格数据")
	private String specJson;

}
