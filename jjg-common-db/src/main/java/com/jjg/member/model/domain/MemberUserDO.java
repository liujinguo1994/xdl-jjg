package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;


/**
 * @author: LNS 1220316142@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class MemberUserDO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;

    private Long id;

    private String loginName;

    private String name;

    private String password;

    private String salt;

    private Integer sex;

    private String phone;

    private String email;
}
