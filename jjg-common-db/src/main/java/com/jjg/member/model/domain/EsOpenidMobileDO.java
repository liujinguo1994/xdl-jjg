package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 微信关联手机号
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-09
 */
@Data
public class EsOpenidMobileDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 用户唯一标识
     */
	private String openid;

    /**
     * 手机号码
     */
	private String mobile;

    /**
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 最后修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;
}
