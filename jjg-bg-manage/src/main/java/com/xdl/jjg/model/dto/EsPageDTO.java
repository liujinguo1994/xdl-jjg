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
public class EsPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 楼层名称
     */
	private String pageName;

    /**
     * 楼层数据
     */
	private String pageData;

    /**
     * 页面类型
     */
	private String pageType;

    /**
     * 客户端类型
     */
	private String clientType;

}
