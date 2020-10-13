package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 限时抢购
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
public class EsSeckillForm implements Serializable {


    private static final long serialVersionUID = -3155157300221045665L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;
    /**
     * 活动名称
     */
    @ApiModelProperty(required = true, value = "活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String seckillName;
    /**
     * 活动日期
     */
    @ApiModelProperty(required = true, value = "活动日期", example = "1")
    @NotNull(message = "活动日期不能为空")
    private Long startDay;
    /**
     * 报名截至时间
     */
    @ApiModelProperty(required = true, value = "报名截至时间", example = "1")
    @NotNull(message = "报名截至时间不能为空")
    private Long applyEndTime;
    /**
     * 申请规则
     */
    @ApiModelProperty(required = true, value = "申请规则")
    @NotBlank(message = "申请规则不能为空")
    private String seckillRule;
    /**
     * 活动时刻列表
     */
    @ApiModelProperty(required = true, name = "rangeList", value = "活动时刻列表")
    @Valid
    @NotNull(message = "活动时刻列表不能为空")
    @Size(min = 1, message = "至少有一个活动时刻")
    private List<Integer> rangeList;
}
