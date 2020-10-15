package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsGroupBuyQuantityLogDO extends Model<EsGroupBuyQuantityLogDO> {

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
	private Long goodsId;

    /**
     * 团购售空数量
     */
	private Integer quantity;

    /**
     * 操作时间
     */
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
	private Long gbId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
