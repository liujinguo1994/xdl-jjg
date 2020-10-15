package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


@Data
@Api
public class EsShopSildeForm {
    @ApiModelProperty(value = "幻灯片URL")
    @NotEmpty(message = "幻灯片URL不能为空")
    private String sildeUrl;
    @ApiModelProperty(value = "幻灯片图片")
    @NotEmpty(message = "幻灯片图片不能为空")
    private String img;
}
