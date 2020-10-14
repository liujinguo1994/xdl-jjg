package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: LNS 1220316142@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class MemberUserTokenDTO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;
    private Long userId;
    private String token;
    private Long expireTime;
    private Long updateTime;
}
