package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@TableName("es_admin_user")
public class EsAdminUser extends Model<EsAdminUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Double id;

    /**
     * 管理员名称
     */
    @TableField("username")
    private String username;

    /**
     * 管理员密码
     */
    @TableField("password")
    private String password;

    /**
     * 盐
     */
    @TableField("salt")
    @JsonIgnore
    private String salt;

    /**
     * 部门
     */
    @TableField("department")
    private Long department;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否删除 0 未删除 1删除
     */
    @TableField("is_del")
    @TableLogic(value = "0", delval = "1")
    private Integer isDel;

    /**
     * 管理员真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 头像
     */
    @TableField("face")
    private String face;

    /**
     * 是否为超级管理员
     */
    @TableField("is_admin")
    private Integer isAdmin;

    /**
     * 联系方式
     */
    @TableField("mobile")
    private String mobile;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
