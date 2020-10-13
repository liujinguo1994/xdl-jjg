package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 邮件form
 */
@Data
@ApiModel
public class EsSmtpForm implements Serializable {

    private static final long serialVersionUID = 9787156257241506L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 主机
     */
    @ApiModelProperty(required = true, value = "主机")
    private String host;
    /**
     * 用户名
     */
    @ApiModelProperty(required = true, value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(required = true, value = "密码")
    private String password;
    /**
     * 最大发信数
     */
    @ApiModelProperty(required = true, value = "最大发信数", example = "1")
    private Integer maxCount;
    /**
     * 发信邮箱
     */
    @ApiModelProperty(required = true, value = "发信邮箱")
    private String mailFrom;
    /**
     * 端口
     */
    @ApiModelProperty(required = true, value = "端口", example = "1")
    private Integer port;
    /**
     * ssl是否开启(0:未开启，1:开启)
     */
    @ApiModelProperty(required = true, value = "ssl是否开启(0:未开启，1:开启)", example = "1")
    private Integer openSsl;

}