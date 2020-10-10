package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Data
@Accessors(chain = true)
@TableName("es_role")
public class EsRole extends Model<EsRole> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 权限名称
	 */
	@TableField("role_name")
	private String roleName;

	/**
	 * 角色拼音名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 权限集合
	 */
	@TableField("auth_ids")
	private String authIds;

	/**
	 * 角色描述
	 */
	@TableField("role_describe")
	private String roleDescribe;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
