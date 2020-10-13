package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 售后申请原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
@Data
@ApiModel
public class EsReturnReasonVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    /**
     * 原因
     */
    @ApiModelProperty(value = "reason")
    private String reason;

    /**
     * 售后类型
     */
    @ApiModelProperty(value = "refundType")
    private String refundType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "createTime")
    private Long createTime;

}
