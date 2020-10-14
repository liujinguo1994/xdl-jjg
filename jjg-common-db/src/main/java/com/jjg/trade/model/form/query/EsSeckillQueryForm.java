package com.xdl.jjg.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSeckillQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
	@ApiModelProperty(value = "活动名称")
	private String seckillName;

    /**
     * 活动日期
     */
	@ApiModelProperty(value = "活动日期")
	private Long createTime;

    /**
     * 报名截至时间
     */
	@ApiModelProperty(value = "报名截至时间")
	private Long updateTime;

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

}
