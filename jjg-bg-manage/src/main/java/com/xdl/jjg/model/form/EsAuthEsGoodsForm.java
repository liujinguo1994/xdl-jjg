package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 审核商品form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "审核商品form")
public class EsAuthEsGoodsForm implements Serializable {


    private static final long serialVersionUID = 5454824566245415626L;

    /**
     * 状态
     */
    @ApiModelProperty(required = true, value = "状态(1:通过,2:驳回)",example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String message;
}
