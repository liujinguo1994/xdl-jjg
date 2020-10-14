package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 静态页设置
 */
@Data
@ApiModel
public class EsPageSettingVO implements Serializable {
    private static final long serialVersionUID = 2379525768234781784L;
    /**
     * pc静态页地址
     */
    @ApiModelProperty(value = "pc静态页地址")
    private String pcAddress;
    /**
     * wap静态页地址
     */
    @ApiModelProperty(value = "wap静态页地址")
    private String wapAddress;

}
