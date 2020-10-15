package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-07-02
 */
@Data
@Accessors(chain = true)
public class EsDeliveryDateDTO implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long id;

	/**
	 * 自提日期ID
	 */

	private Long dateId;

	/**
	 * 自提点ID
	 */

	private Long deliveryId;

	protected Serializable pkVal() {
		return this.id;
	}
}
