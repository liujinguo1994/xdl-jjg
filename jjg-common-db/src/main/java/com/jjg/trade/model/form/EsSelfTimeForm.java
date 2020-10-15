package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 自提时间
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSelfTimeForm implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;
	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Long startTime;
	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Long endTime;
	/**
	 * 人数
	 */
	@ApiModelProperty(value = "人数")
	private Integer personNumber;
	/**
	 * 自提日期ID
	 */
	@ApiModelProperty(value = "自提日期ID")
	private Long dateId;

    /**
     * 自提点日期ID
     */
	private Long deliveryId;


	protected Serializable pkVal() {
		return this.id;
	}

}
