package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@TableName("es_menu")
public class EsMenu extends Model<EsMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 父id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("parent_id")
    private Long parentId;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单url
     */
    private String url;
    /**
     * 菜单唯一标识
     */
    private String identifier;
    /**
     * 权限表达式
     */
    @TableField("auth_expression")
    private String authExpression;
    /**
     * 删除标记 0 未删除 1删除
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
