package com.xdl.jjg.model.domain;

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
public class EsSmtpDO implements Serializable {

    private static final long serialVersionUID = -4160273762835466997L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 主机
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 最后发信时间
     */
    private Long lastSendTime;
    /**
     * 已发数
     */
    private Integer sendCount;
    /**
     * 最大发信数
     */
    private Integer maxCount;
    /**
     * 发信邮箱
     */
    private String mailFrom;
    /**
     * 端口
     */
    private Integer port;
    /**
     * ssl是否开启
     */
    private Integer openSsl;
}
