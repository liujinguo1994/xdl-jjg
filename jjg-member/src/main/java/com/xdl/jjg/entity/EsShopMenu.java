package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_menu")
public class EsShopMenu extends Model<EsShopMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺菜单id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 菜单父id
     */
    @TableField("parent_id")
	private Long parentId;
    /**
     * 菜单标题
     */
	private String title;
    /**
     * 菜单唯一标识
     */
	private String identifier;
    /**
     * 权限表达式
     */
    @TableField("auth_regular")
	private String authRegular;
    /**
     * 删除标记
     */
    @TableField("is_del")
    @TableLogic(value = "0",delval = "1")
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
