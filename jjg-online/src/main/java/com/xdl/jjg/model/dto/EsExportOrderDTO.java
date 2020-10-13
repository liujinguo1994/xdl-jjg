package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 卖家端订单导出
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
public class EsExportOrderDTO implements Serializable {

	private static final long serialVersionUID = -6846662008046371778L;
	/**
     * 订单状态
     */
	private String orderState;
	/**
	 * 开始时间
	 */
	private Long startTime;
	/**
	 * 结束时间
	 */
	private Long endTime;
	/**
	 * 店铺Id
	 */
	private Long shopId;
}
