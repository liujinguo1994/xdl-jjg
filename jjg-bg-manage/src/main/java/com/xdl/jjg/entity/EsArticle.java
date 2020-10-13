package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@TableName("es_article")
public class EsArticle extends Model<EsArticle> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Double id;

    /**
     * 文章名称
     */
    @TableField("article_name")
    private String articleName;

    /**
     * 分类id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 文章排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 外链url
     */
    @TableField("outside_url")
    private String outsideUrl;

    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    /**
     * 显示位置
     */
    @TableField("show_position")
    private String showPosition;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
