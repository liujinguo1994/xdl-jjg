package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-12 13:23:46
 */
@Data
public class EsBrandShowDO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private String imgUrl;

	private String linkUrl;

	private Integer sort;

}
