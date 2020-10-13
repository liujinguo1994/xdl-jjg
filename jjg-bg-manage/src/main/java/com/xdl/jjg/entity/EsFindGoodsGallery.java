package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发现好货相册
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-15
 */
@Data
@TableName("es_find_goods_gallery")
public class EsFindGoodsGallery extends Model<EsFindGoodsGallery> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发现好货ID
     */
    @TableField("find_goods_id")
    private Long findGoodsId;

    /**
     * 图片路径
     */
    @TableField("url")
    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
