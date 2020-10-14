package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsMemberTokenDTO implements Serializable {

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
