package com.jjg.system.model.domain;

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
public class EsGoodsRankingDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 榜单名称
     */
    private String rankingName;

    /**
     * 推荐商品链接
     */
    private String goodsUrl;

    /**
     * 推荐商品id
     */
    private Long goodsId;

    /**
     * 商品分类id
     */
    private Long goodsCategoryId;

    /**
     * 榜单图片地址
     */
    private String rankingPicUrl;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long updateTime;

    /**
     * 商品价格
     */
    private Double money;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品原图
     */
    private String original;

    /**
     * 是否放首页（1是，2否）
     */
    private Integer homePage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 背景图片地址
     */
    private String backgroundPicUrl;

    //榜单销量
    private Integer count;
}
