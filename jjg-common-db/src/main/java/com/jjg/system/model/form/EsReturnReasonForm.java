package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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
public class EsReturnReasonForm implements Serializable {
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
    @NotBlank(message = "原因不能为空")
    private String reason;

    /**
     * 售后类型
     */
    @ApiModelProperty(value = "售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货退款,CHANGE_GOODS:换货,REPAIR_GOODS:维修)")
    @NotBlank(message = "售后类型不能为空")
    private String refundType;
}
