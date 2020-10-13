package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  小程序-搜索历史
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-23
 */
@ApiModel
@Data
public class AppletSearchKeyWordForm implements Serializable {
    private static final long serialVersionUID = 2819912674127510854L;

    @ApiModelProperty(value = "搜索词")
    private String searchKeyword;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
