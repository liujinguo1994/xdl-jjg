package com.xdl.jjg.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 编辑个人信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsBindMobileForm implements Serializable {

    /**
     * 手机号
     */
    @ApiModelProperty(required = true,value = "手机号")
    public String mobile;


    /**
     * 验证码
     */
    @ApiModelProperty(required = true, value = "验证码")
    public String code;
}
