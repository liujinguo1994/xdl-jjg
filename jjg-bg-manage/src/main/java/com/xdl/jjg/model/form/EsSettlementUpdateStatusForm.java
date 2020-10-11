package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 更新结算状态
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSettlementUpdateStatusForm implements Serializable {
    private static final long serialVersionUID = 1957382560310907842L;

    @ApiModelProperty(required = true,value = "结算单ID")
    @NotNull(message = "结算单ID不能为空")
    private Long settlementId;

    @ApiModelProperty(required = true,value = "结算类型(1店铺结算,2供应商结算,3签约公司结算)")
    @NotNull(message = "结算类型不能为空")
    private Integer type;

    @ApiModelProperty(required = true,value = "结算状态(1已出账,2已对账,3已审核,4已付款)")
    @NotNull(message = "结算状态不能为空")
    private Integer status;
}
