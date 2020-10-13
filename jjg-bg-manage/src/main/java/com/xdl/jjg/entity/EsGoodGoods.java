package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Data
@TableName("es_good_goods")
public class EsGoodGoods extends Model<EsGoodGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 商品链接
     */
    @TableField("goods_url")
    private String goodsUrl;

    /**
     * 图片地址
     */
    @TableField("pic_url")
    private String picUrl;

    /**
     * 状态(1:待发布,2.已发布,3:已下架)
     */
    @TableField("state")
    private Integer state;

    /**
     * 下架原因
     */
    @TableField("under_message")
    private String underMessage;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
