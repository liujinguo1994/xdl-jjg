package com.xdl.jjg.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import com.shopx.trade.api.model.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;


/**
 * LiuJG
 * 订单变化消息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusChangeMsg implements Serializable {

	private static final long serialVersionUID = 8915428082431868648L;

	/**
	 * 变化的订单信息
	 */
	private EsOrderDTO esOrderDTO;

	/**
	 * 原状态
	 */
	private OrderStatusEnum oldStatus;

	/**
	 * 新状态
	 */
	private OrderStatusEnum newStatus;

}
