package com.xdl.jjg.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
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
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsModifyPassWordForm implements Serializable {


    /**
     * 会员旧密码
     */
    @ApiModelProperty(required = true, value = "会员旧密码")
    private String oldPassword;
    /**
     * 会员新密码
     */
    @ApiModelProperty(required = true, value = "会员新密码")
    private String newPassword;
    /**
     * 验证码
     */
    @ApiModelProperty(required = true, value = "验证码")
    private String passwordCode;
    /**
     * uuid
     */
    @ApiModelProperty(required = true,value = "uuid")
    private String uuid;


}
