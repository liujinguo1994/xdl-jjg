package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
@Data
@Accessors(chain = true)
public class EsCouponReceiveCheckDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Integer id;

    /**
     * 手机号
     */
	private String mobile;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 更新时间
     */
	private Long updateTime;

    /**
     * 用户名
     */
	private String userName;

	protected Serializable pkVal() {
		return this.id;
	}

}
