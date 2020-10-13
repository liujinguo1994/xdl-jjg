package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsOpenidMobileDTO implements Serializable {

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
	private Long createTime;

    /**
     * 最后修改时间
     */
	private Long updateTime;
}
