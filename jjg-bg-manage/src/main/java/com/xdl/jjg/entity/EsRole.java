package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
@TableName("es_role")
public class EsRole extends Model<EsRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 权限名称
     */
    @TableField("role_name")
	private String roleName;
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

	/**
	 * 角色拼音名称
	 */
	@TableField("name")
	private String name;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
