package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序端绑定签约公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-18 09:28:26
 */
@Data
@ApiModel
public class EsAppletBindCompanyForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;


    @ApiModelProperty(required = true,value = "企业标识符")
    @NotBlank(message = "企业标识符不能为空")
    private String companyCode;

}
