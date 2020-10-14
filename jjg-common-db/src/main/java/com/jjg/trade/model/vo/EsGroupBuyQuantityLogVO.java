package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 团购商品库存日志表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGroupBuyQuantityLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

    /**
     * 团购售空数量
     */
	@ApiModelProperty(value = "团购售空数量")
	private Integer quantity;

    /**
     * 操作时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "操作时间")
	private Long opTime;

    /**
     * 日志类型
     */
	@ApiModelProperty(value = "日志类型")
	private String logType;

    /**
     * 操作原因
     */
	@ApiModelProperty(value = "操作原因")
	private String reason;

    /**
     * 团购id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "团购id")
	private Long gbId;

	protected Serializable pkVal() {
		return this.id;
	}

}
