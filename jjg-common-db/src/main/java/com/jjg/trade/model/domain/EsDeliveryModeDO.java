package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 配送方式
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsDeliveryModeDO extends Model<EsDeliveryModeDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 配送方式ID
     */
	private Long deliveryModeId;

    /**
     * 配送方式名称(自提，配送)
     */
	private String deliveryModeName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
