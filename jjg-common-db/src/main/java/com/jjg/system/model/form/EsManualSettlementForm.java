package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 手动结算form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel(description = "手动结算form")
public class EsManualSettlementForm implements Serializable {

    private static final long serialVersionUID = 2464688048720250730L;
    /**
     * 时间开始
     */
    @ApiModelProperty(required = true, value = "时间开始")
    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    /**
     * 时间结束
     */
    @ApiModelProperty(required = true, value = "时间结束")
    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    /**
     * 结算类型(1店铺结算,2供应商结算,3签约公司结算)
     */
    @ApiModelProperty(required = true, value = "结算类型(1店铺结算,2供应商结算,3签约公司结算)")
    @NotNull(message = "结算类型不能为空")
    private Integer type;

    /**
     * 签约公司id
     */
    @ApiModelProperty(value = "签约公司id")
    private Long companyId;
}
