package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 投诉类型
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-23 09:56:28
 */
@Data
@ApiModel
public class EsComplaintTypeConfigForm implements Serializable {

    private static final long serialVersionUID = -6137356443389659733L;

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(required = true, value = "投诉类型")
    @NotBlank(message = "投诉类型不能为空")
    private String complainType;

}
