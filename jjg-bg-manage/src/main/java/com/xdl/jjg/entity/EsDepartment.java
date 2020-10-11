package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
@Data
@TableName("es_department")
public class EsDepartment extends Model<EsDepartment> {


	private static final long serialVersionUID = -4664297017906659843L;
	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 部门名称
	 */
	@TableField("department_name")
	private String departmentName;

	/**
	 * 是否删除
	 */
	@TableField("is_del")
	@TableLogic(value="0",delval="1")
	private Integer isDel;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 父id
	 */
	@TableField("parent_id")
	private Long parentId;

	/**
	 * 父子路径
	 */
	@TableField("path")
	private String path;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
