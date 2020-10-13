package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
public class EsClerkForm implements Serializable {
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 店员名称
     */
    @ApiModelProperty(value = "店员名称")
    @NotBlank(message = "店员名称不能为空")
    private String clerkName;

    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @ApiModelProperty(value = "是否为超级管理员，1为超级管理员 0为其他管理员",example = "0")
    @NotNull(message = "是否为超级管理员不能为空")
    private Integer isAdmin;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
}
