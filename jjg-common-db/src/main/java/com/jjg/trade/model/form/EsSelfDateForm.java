package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自提日期
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSelfDateForm implements Serializable{

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;
	/**
	 * 自提日期
	 */
	@ApiModelProperty(value = "自提日期")
	private Long selfDate;
	/**
	 * 自提点ID
	 */
	@ApiModelProperty(value = "自提点ID")
	private Long deliveryId;
	/**
	 * 有效状态
	 */
	@ApiModelProperty(value = "有效状态")
	private Integer state;

	/**
	 * 限制总人数
	 */
	@ApiModelProperty(value = "限制总人数")
	private Integer personTotal;

//    /**
//     * 自提点ID
//     */
//	private Long deliveryId;
	@ApiModelProperty(value = "自提时间点list")
	private List<EsSelfTimeForm> selfTimeList;

	/**
	 * 是否选中
	 */
	private Boolean selected;


	protected Serializable pkVal() {
		return this.id;
	}

}
