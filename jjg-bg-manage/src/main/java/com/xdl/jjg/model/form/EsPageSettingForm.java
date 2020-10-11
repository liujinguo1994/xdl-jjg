package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * 静态页设置
 *
 */
@Data
@ApiModel
public class EsPageSettingForm implements Serializable {
    private static final long serialVersionUID = 2379525768234781784L;
    /**
     * pc静态页地址
     */
    @ApiModelProperty(required = true, value = "pc静态页地址")
    @NotEmpty(message = "pc静态页地址不能为空")
    private String pcAddress;
    /**
     * wap静态页地址
     */
    @ApiModelProperty(required = true, value = "wap静态页地址")
    @NotBlank(message = "wap静态页地址不能为空")
    private String wapAddress;

}
