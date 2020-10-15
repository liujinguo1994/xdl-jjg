package com.jjg.member.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ShopRemarksForm implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(required = true,value = "主键id")
    private Long  id;
    /**
     * 备注内容
     */
    @ApiModelProperty(required = true,value = "备注内容")
    private String mark;

}
