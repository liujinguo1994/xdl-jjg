package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 *档案管理-规格
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSpecForm implements Serializable {
    private static final long serialVersionUID = -8280060627424175739L;
    /**
     * 规格值id
     */
    @ApiModelProperty(value = "规格值id",example = "1")
    private Long id;
    /**
     * 规格项id
     */
    @ApiModelProperty(value = "规格项id",example = "1")
    private Long specId;
    /**
     * 规格名称
     */
    @ApiModelProperty(required = true, value = "规格名称")
    @NotBlank(message = "规格名称不能为空")
    private String specName;
    /**
     * 规格值
     */
    @ApiModelProperty(required = true, value = "规格值")
    @NotBlank(message = "规格值不能为空")
    private String specValue;


}
