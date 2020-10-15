package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 安全配置form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "安全配置form")
public class EsSecuritySettingsForm implements Serializable {

    private static final long serialVersionUID = -7415822798174683152L;
    /**
     * 注册初始积分
     */
    @ApiModelProperty(required = true, value = "注册初始积分", example = "1")
    @NotNull(message = "注册初始积分不能为空")
    private Integer integral;
    /**
     * 最大登录失败次数
     */
    @ApiModelProperty(required = true, value = "最大登录失败次数", example = "1")
    @NotNull(message = "最大登录失败次数不能为空")
    private Integer failedNumber;
    /**
     * 未支付失败时间(以分钟为单位)
     */
    @ApiModelProperty(required = true, value = "未支付失败时间(以分钟为单位)", example = "1")
    @NotNull(message = "未支付失败时间不能为空")
    private Integer time;

}
