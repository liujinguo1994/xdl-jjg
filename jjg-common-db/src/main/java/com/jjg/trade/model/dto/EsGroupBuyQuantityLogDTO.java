package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsGroupBuyQuantityLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 订单编号
     */
	private String orderSn;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsId;

    /**
     * 团购售空数量
     */
	private Integer quantity;

    /**
     * 操作时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long opTime;

    /**
     * 日志类型
     */
	private String logType;

    /**
     * 操作原因
     */
	private String reason;

    /**
     * 团购id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long gbId;

	protected Serializable pkVal() {
		return this.id;
	}

}
