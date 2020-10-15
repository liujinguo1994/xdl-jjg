package com.jjg.trade.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 修改手机号码
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
public class EsModifyMobileForm implements Serializable {


    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    private String mobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = true, value = "验证码")
    private String code;


}
