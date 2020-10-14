package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 更新密码form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel
public class EsUpdatePasswordForm implements Serializable {
    private static final long serialVersionUID = 1318248416464019082L;
    /**
     * 管理员密码
     */
    @ApiModelProperty(value = "管理员密码")
    private String password;
    /**
     * 管理员旧密码
     */
    @ApiModelProperty(value = "管理员旧密码")
    private String oldPassword;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @NotBlank(message = "头像不能为空")
    private String face;

}
