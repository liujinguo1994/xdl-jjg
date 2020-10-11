package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 自提时间
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSelfTimeForm implements Serializable {


	private static final long serialVersionUID = -2990174074784634761L;

    /**
     * 开始时间
     */
    @ApiModelProperty(required = true, value = "开始时间",example = "1")
	@NotNull(message = "开始时间不能为空")
	private Long startTime;
    /**
     * 结束时间
     */
	@ApiModelProperty(required = true, value = "结束时间",example = "1")
	@NotNull(message = "结束时间不能为空")
	private Long endTime;
    /**
     * 人数
     */
	@ApiModelProperty(required = true, value = "人数",example = "1")
	@NotNull(message = "人数不能为空")
	private Integer personNumber;

}
