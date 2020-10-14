package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员token信息表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-10 14:47:36
 */
@Data
public class EsMemberTokenDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	private Long memberId;

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
