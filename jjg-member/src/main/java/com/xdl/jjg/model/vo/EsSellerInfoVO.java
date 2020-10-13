package com.xdl.jjg.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsSellerInfoVO extends EsMemberSellerVO {

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    /**
     * 是否是店主
     */
    @ApiModelProperty(value = "是否是店主")
    private Integer founder;
}
