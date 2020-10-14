package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSeckillVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 活动名称
     */
	@ApiModelProperty(value = "活动名称")
	private String seckillName;
	/**
	 * 活动日期
	 */
	@ApiModelProperty(value = "活动日期")
	private Long startDay;
	/**
	 * 报名截至时间
	 */
	@ApiModelProperty(value = "报名截至时间")
	private Long applyEndTime;
    /**
     * 申请规则
     */
	@ApiModelProperty(value = "申请规则")
	private String seckillRule;
    /**
     * 商家id集合以逗号分隔
     */
	@ApiModelProperty(value = "商家id集合以逗号分隔")
	private String shopIds;
    /**
     * 状态（1编辑中2已发布3已结束）
     */
	@ApiModelProperty(value = "状态（1编辑中2已发布3已结束）")
	private Integer state;

	/**
	 * 活动时刻列表
	 */
	@ApiModelProperty(value = "活动时刻列表")
	private List<Integer> rangeList;

	/**
	 * 卖家端报名状态
	 */
	@ApiModelProperty(value = "卖家端报名状态")
	private String applyStatus;

	protected Serializable pkVal() {
		return this.id;
	}

}
