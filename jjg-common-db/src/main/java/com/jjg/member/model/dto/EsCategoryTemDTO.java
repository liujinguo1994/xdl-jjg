package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 分类信息
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-27
 */
@Data
@ToString
public class EsCategoryTemDTO implements Serializable {


	/**分类id*/
	private Integer categoryId;
	/**分类名称*/
	private String name;


}
