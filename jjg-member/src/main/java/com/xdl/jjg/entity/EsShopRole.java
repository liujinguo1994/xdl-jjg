package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺角色
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_role")
public class EsShopRole extends Model<EsShopRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 角色名称
     */
    @TableField("role_name")
	private String roleName;

    /**
     * 角色英文名称
     */
    @TableField("english_name")
    private String englishName;

    /**
     * 角色权限
     */
    @TableField("auth_ids")
	private String authIds;
    /**
     * 角色描述
     */
    @TableField("role_describe")
	private String roleDescribe;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;



}
