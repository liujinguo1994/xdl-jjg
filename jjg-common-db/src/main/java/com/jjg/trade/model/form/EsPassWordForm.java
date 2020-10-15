package com.jjg.trade.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 修改密码
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsPassWordForm implements Serializable {


    /**
     * 会员新密码
     */
    @ApiModelProperty(required = true, value = "会员新密码")
    private String newPassword;

    /**
     * 手机号
     */
    @ApiModelProperty(required = true,value = "手机号")
    private String mobile;


}
