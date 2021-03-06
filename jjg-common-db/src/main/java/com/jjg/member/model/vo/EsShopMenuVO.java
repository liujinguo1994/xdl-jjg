package com.jjg.member.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShopMenuVO implements Serializable {


    /**
     * 店铺菜单id
     */
	private Long id;
    /**
     * 菜单父id
     */
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
	private String authRegular;

    /**
     * 解绑标识1删除0未删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("is_del")
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
