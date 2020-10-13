package com.xdl.jjg.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * LiuJG
 * 活动下架/过期对象
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartPromotionChangeMsg implements Serializable {

	private static final long serialVersionUID = 8915428082431868648L;

	@ApiModelProperty(value = "活动id")
	private Long activityId;

	@ApiModelProperty(value = "活动类型")
	private String promotionType;

	private String cron;
}
