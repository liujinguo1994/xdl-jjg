package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@Api
public class EsGoodsParamsForm implements Serializable {
    /**
     * 参数id
     */
    @ApiModelProperty(value = "参数ID")
    private Long paramId;
    /**
     * 参数名字
     */
    @ApiModelProperty("参数名字")
    private String paramName;
    /**
     * 参数值
     */
    @ApiModelProperty("参数值")
    private String paramValue;
}
