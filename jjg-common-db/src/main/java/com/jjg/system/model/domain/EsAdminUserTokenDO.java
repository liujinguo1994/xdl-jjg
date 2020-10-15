package com.jjg.system.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 管理员用户token信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-09
 */
@Data
public class EsAdminUserTokenDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
