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
 * @since 2019-05-29
 */
@Data
@ToString
public class EsMenuDTO implements Serializable {

    private static final long serialVersionUID = 2525125857614967315L;
    /**
     * 主键ID
     */
	private Long id;
    /**
     * 父id
     */
	private Long parentId;
    /**
     * 菜单标题
     */
	private String title;
    /**
     * 菜单url
     */
	private String url;
    /**
     * 菜单唯一标识
     */
	private String identifier;
    /**
     * 权限表达式
     */
	private String authExpression;
    /**
     * 删除标记 0 未删除 1删除
     */
	private Integer isDel;
    /**
     * 菜单级别标识
     */
	private String path;
    /**
     * 菜单级别
     */
	private Integer grade;

}
