package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsSiteNavigationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 导航名称
     */
	private String navigationName;

    /**
     * 导航地址
     */
	private String url;

    /**
     * 客户端类型
     */
	private String clientType;

    /**
     * 图片
     */
	private String image;

    /**
     * 排序
     */
	private Integer sort;


}
