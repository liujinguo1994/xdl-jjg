package com.xdl.jjg.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

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
public class EsAdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 管理员名称
     */
	private String username;

    /**
     * 管理员密码
     */
	private String password;

    /**
     * 盐
     */
	private String salt;

    /**
     * 部门
     */
	private Long department;

    /**
     * 角色ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;

    /**
     * 创建日期
     */
	private Long createTime;

    /**
     * 备注
     */
	private String remark;

    /**
     * 是否删除 0 未删除 1删除
     */
    @TableLogic
	private Integer isDel;

    /**
     * 管理员真实姓名
     */
	private String realName;

    /**
     * 头像
     */
	private String face;

    /**
     * 是否为超级管理员
     */
	private Integer isAdmin;

    /**
     * 联系方式
     */
	private String mobile;

	protected Serializable pkVal() {
		return this.id;
	}

}
