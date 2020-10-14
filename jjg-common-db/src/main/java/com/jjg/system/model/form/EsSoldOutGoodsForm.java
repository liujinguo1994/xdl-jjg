package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 下架品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Data
@ApiModel
public class EsSoldOutGoodsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = true, value = "主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

    /**
     * 下架原因
     */
    @ApiModelProperty(required = true, value = "下架原因")
    @NotBlank(message = "下架原因不能为空")
    private String underMessage;

}
