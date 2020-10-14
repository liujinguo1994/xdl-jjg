package com.jjg.member.model.dto;

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
public class EsGoodsRankingDTO implements Serializable {

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
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long updateTime;

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

}
