package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 移动端-修改密码
 * </p>
 */
@Data
@ApiModel
public class EsWapModifyPassWordForm implements Serializable {


    private static final long serialVersionUID = 6175566214559738099L;
    /**
     * 会员旧密码
     */
    @ApiModelProperty(required = true, value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 会员新密码
     */
    @ApiModelProperty(required = true, value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
