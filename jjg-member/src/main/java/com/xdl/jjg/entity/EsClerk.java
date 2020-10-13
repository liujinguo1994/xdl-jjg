package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店员
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_clerk")
public class EsClerk extends Model<EsClerk> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 店员名称
     */
    @TableField("clerk_name")
	private String clerkName;
    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @TableField("is_admin")
	private Integer isAdmin;
    /**
     * 角色id
     */
    @TableField("role_id")
	private Long roleId;
    /**
     * 店员状态，0为正常，1为禁用
     */
	private Integer state;
    /**
     * 创建日期
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;


}
