package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 投诉原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-23 09:56:28
 */
@Data
@ApiModel
public class EsComplaintReasonConfigForm implements Serializable {


    private static final long serialVersionUID = 8505418590494395735L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;

    /**
     * 投诉原因
     */
    @ApiModelProperty(required = true, value = "投诉原因")
    @NotBlank(message = "投诉原因不能为空")
    private String complaintReason;

    /**
     * 投诉类型
     */
    @ApiModelProperty(required = true, value = "投诉类型")
    @NotNull(message = "投诉类型不能为空")
    private Long complainTypeId;
}
