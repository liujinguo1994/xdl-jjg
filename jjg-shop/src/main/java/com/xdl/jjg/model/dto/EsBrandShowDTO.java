package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsBrandShowDTO implements Serializable {

	private String name;

	private String imgUrl;

	private String linkUrl;

	private Integer sort;

}
