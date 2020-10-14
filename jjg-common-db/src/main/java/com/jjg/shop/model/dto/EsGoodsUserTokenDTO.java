package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-10 16:43:11
 */
@Data
public class EsGoodsUserTokenDTO implements Serializable {

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

	protected Serializable pkVal() {
		return this.userId;
	}

}
