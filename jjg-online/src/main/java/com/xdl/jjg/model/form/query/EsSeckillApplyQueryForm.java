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
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSeckillApplyQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动id")
	private Long seckillId;

    /**
     * 时刻
     */
	@ApiModelProperty(value = "时刻")
	private Integer timeLine;

    /**
     * 活动开始日期
     */
	@ApiModelProperty(value = "活动开始日期")
	private Long createTime;

    /**
     * 活动结束日期
     */
	@ApiModelProperty(value = "活动结束日期")
	private Long updateTime;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

    /**
     * 商家ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家ID")
	private Long shopId;

    /**
     * 商家名称
     */
	@ApiModelProperty(value = "商家名称")
	private String shopName;

    /**
     * 价格
     */
	@ApiModelProperty(value = "价格")
	private Double money;

    /**
     * 售空数量
     */
	@ApiModelProperty(value = "售空数量")
	private Integer soldQuantity;

    /**
     * 申请状态
     */
	@ApiModelProperty(value = "申请状态")
	private Integer state;

    /**
     * 驳回原因
     */
	@ApiModelProperty(value = "驳回原因")
	private String failReason;

    /**
     * 商品原始价格
     */
	@ApiModelProperty(value = "商品原始价格")
	private Double originalPrice;

    /**
     * 已售数量
     */
	@ApiModelProperty(value = "已售数量")
	private Integer salesNum;

}
