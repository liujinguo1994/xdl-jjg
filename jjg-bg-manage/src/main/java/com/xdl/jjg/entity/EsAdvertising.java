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
 * 广告位
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
@TableName("es_advertising")
public class EsAdvertising extends Model<EsAdvertising> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片名称
     */
    @TableField("pic_name")
    private String picName;

    /**
     * 位置
     */
    @TableField("location")
    private String location;

    /**
     * 连接
     */
    @TableField("link")
    private String link;

    /**
     * 图片地址
     */
    @TableField("pic_url")
    private String picUrl;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
