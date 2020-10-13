package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 处理举报form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsHandleReportForm implements Serializable {
    private static final long serialVersionUID = 1583918226416768927L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = true, value = "主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

    /**
     * 操作状态(1:开始处理，2:处理完成)
     */
    @ApiModelProperty(required = true, value = "操作状态(1:开始处理，2:处理完成)")
    @NotNull(message = "操作状态不能为空")
    private Integer operationStatus;

    /**
     * 处理内容(只有点击处理完成的时候才传该字段)
     */
    @ApiModelProperty(value = "处理内容(只有点击处理完成的时候才传该字段)")
    private String handleContent;

}
