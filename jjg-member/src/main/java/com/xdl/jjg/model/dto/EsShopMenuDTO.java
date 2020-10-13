package com.xdl.jjg.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsShopMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺菜单id
     */
	private Long id;

    /**
     * 菜单父id
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
     * 删除标记
     */
    @TableLogic
	private Integer isDel;

    /**
     * 菜单级别标识
     */
	private String path;

    /**
     * 菜单级别
     */
	private Integer grade;

	protected Serializable pkVal() {
		return this.id;
	}

}
