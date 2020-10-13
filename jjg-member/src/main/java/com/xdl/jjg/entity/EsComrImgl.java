package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 投诉举报图片表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
@Data
@TableName("es_comr_imgl")
public class EsComrImgl extends Model<EsComrImgl> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 类型，0投诉，1举报
     */
    @TableField("type")
	private Integer type;

    /**
     * 投诉举报id
     */
    @TableField("com_id")
    private Long comId;

    /**
     * 缩略图路径
     */
    @TableField("thumbnail")
    private String thumbnail;
    /**
     * 小图路径
     */
    @TableField("small")
    private String small;
    /**
     * 大图路径
     */
    @TableField("big")
    private String big;
    /**
     * 原图路径
     */
    @TableField("original")
    private String original;
    /**
     * 极小图路径
     */
    @TableField("tiny")
    private String tiny;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;



}
