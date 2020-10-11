package com.xdl.jjg.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */

@Data
@ApiModel(description = "安全配置VO")
public class EsSecuritySettingsVO implements Serializable {

    private static final long serialVersionUID = -7415822798174683152L;
    /**
     * 注册初始积分
     */
    @ApiModelProperty(value = "注册初始积分",example = "1")
    private Integer integral;
    /**
     * 最大登录失败次数
     */
    @ApiModelProperty(value = "最大登录失败次数",example = "1")
    private Integer failedNumber;
    /**
     * 未支付失败时间(以分钟为单位)
     */
    @ApiModelProperty(value = "未支付失败时间(以分钟为单位)",example = "1")
    private Integer time;

}
