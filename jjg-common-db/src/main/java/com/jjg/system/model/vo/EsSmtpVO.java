package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 邮件
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSmtpVO implements Serializable {

    private static final long serialVersionUID = 7303165296339589116L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 主机
     */
    @ApiModelProperty(value = "主机")
    private String host;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 最后发信时间
     */
    @ApiModelProperty(value = "最后发信时间", example = "1")
    private Long lastSendTime;
    /**
     * 已发数
     */
    @ApiModelProperty(value = "已发数", example = "1")
    private Integer sendCount;
    /**
     * 最大发信数
     */
    @ApiModelProperty(value = "最大发信数", example = "1")
    private Integer maxCount;
    /**
     * 发信邮箱
     */
    @ApiModelProperty(value = "发信邮箱")
    private String mailFrom;
    /**
     * 端口
     */
    @ApiModelProperty(value = "端口", example = "1")
    private Integer port;
    /**
     * ssl是否开启(0:未开启，1:开启)
     */
    @ApiModelProperty(value = "ssl是否开启(0:未开启，1:开启)", example = "1")
    private Integer openSsl;
}
