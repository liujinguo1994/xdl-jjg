package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Api
@Data
public class EsNavigationForm implements Serializable {
    @ApiModelProperty(value = "导航栏名称")
    @NotBlank
    private String name;
    @ApiModelProperty(value = "导航栏排序")
    @NotNull
    private Integer sort;

    @ApiModelProperty(value = "导航栏链接")
    @NotBlank
    private String navUrl;
    @ApiModelProperty(value = "导航栏目标")
    @NotBlank
    private String target;
    @ApiModelProperty(value = "是否显示 1 显示 2 不显示")
    private Integer isDel;
}
