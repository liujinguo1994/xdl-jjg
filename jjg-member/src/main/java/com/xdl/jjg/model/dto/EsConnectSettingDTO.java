package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 第三方登录参数
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsConnectSettingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
	private Long id;

    /**
     * 参数配置名称
     */
	private String type;

    /**
     * 信任登录类型
     */
	private String config;

    /**
     * 信任登录配置参数
     */
	private String name;

	protected Serializable pkVal() {
		return this.id;
	}

}
