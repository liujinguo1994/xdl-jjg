package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  获取图片验证码
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-24
 */
@Data
@ApiModel
public class EsWapCairForm implements Serializable {


    /**
     * 验证码类型 登录：REGISTER，重置密码：MODIFY_PASSWORD 修改手机号：VALIDATE_MOBILE，绑定手机：BIND_MOBILE，找回密码：FIND_PASSWORD
     */
    @ApiModelProperty(required = true, value = "验证码类型 登录：REGISTER，重置密码：MODIFY_PASSWORD 修改手机号：VALIDATE_MOBILE，绑定手机：BIND_MOBILE，找回密码：FIND_PASSWORD")
    @NotBlank(message = "验证码类型不能为空")
    private String type;
    /**
     * 唯一标识
     */
    @ApiModelProperty(required = true,value = "唯一标识")
    @NotBlank(message = "唯一标识不能为空")
    private String uuid;


}
