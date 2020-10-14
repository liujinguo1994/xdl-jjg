package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2020-03-11 14:39:38
 */
@Data
public class EsUploaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 存储名称
     */
	private String name;

    /**
     * 是否开启
     */
	private Integer open;

    /**
     * 存储配置
     */
	private String config;

    /**
     * 存储插件id
     */
	private String bean;

	protected Serializable pkVal() {
		return this.id;
	}

}
