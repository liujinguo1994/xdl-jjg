package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 热门榜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@TableName("es_goods_ranking")
public class EsGoodsRanking extends Model<EsGoodsRanking> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 榜单名称
     */
    @TableField("ranking_name")
    private String rankingName;

    /**
     * 推荐商品链接
     */
    @TableField("goods_url")
    private String goodsUrl;

    /**
     * 推荐商品id
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商品分类id
     */
    @TableField("goods_category_id")
    private Long goodsCategoryId;

    /**
     * 榜单图片地址
     */
    @TableField("ranking_pic_url")
    private String rankingPicUrl;

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

    /**
     * 是否放首页（1是，2否）
     */
    @TableField("home_page")
    private Integer homePage;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 背景图片地址
     */
    @TableField("background_pic_url")
    private String backgroundPicUrl;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
