package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class MemberUserTokenDO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;

    private Long userId;

    private String token;

    private Long expireTime;

    private Long updateTime;
}
