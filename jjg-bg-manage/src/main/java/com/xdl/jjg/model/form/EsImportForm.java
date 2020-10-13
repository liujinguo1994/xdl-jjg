package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 上传form
 */
@ApiModel
@Data
public class EsImportForm implements Serializable {

    @ApiModelProperty(value = "url")
    @NotBlank(message = "url不能为空")
    private String url;

}
