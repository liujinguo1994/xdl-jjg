package com.jjg.trade.model.form;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 */
@Data
public class EsCompanyForm {

    @ApiModelProperty(required = true,value = "企业标识")
    private String companyCode;
}
