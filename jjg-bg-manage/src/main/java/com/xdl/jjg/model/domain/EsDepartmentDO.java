package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
@Data
public class EsDepartmentDO implements Serializable {


	private static final long serialVersionUID = -5665489307504225325L;
	/**
     * 主键ID
     */
	private Long id;

    /**
     * 部门名称
     */
	private String departmentName;

    /**
     * 是否删除
     */
	private Integer isDel;

    /**
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 父id
	 */
	private Long parentId;

	/**
	 * 父子路径
	 */
	private String path;

	/**
	 * 子部门
	 */
	private List<EsDepartmentDO> children;

}
