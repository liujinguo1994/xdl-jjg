package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class EsUpdateBalanceForm implements Serializable {


    private static final long serialVersionUID = -1960131417626624356L;

    /**
     * 会员ID
     */
    @ApiModelProperty(required = true,value = "会员ID",example = "1")
    @NotNull(message = "会员id不能为空")
    private Long id;

    /**
     * 调整后余额
     */
    @ApiModelProperty(required = true,value = "调整后余额",example = "1")
    @NotNull(message = "调整后余额不能为空")
    private Double memberBalance;
}
