package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 收藏店铺
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Data
public class EsCollectionShopInfoDO implements Serializable {


    /**
     * 收藏id
     */
	private Long id;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 店铺id
     */
	private Long shopId;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 收藏时间
     */
	private Long createTime;
    /**
     * 店铺logo
     */
	private String logo;
    /**
     * 店铺所在省
     */
	private String shopProvince;
    /**
     * 店铺所在市
     */
	private String shopCity;
    /**
     * 店铺所在县
     */
	private String shopRegion;
    /**
     * 店铺所在镇
     */
	private String shopTown;
    /**
     * 收藏店铺排序
     */
    private Integer sort;
    /**
     * 店铺备注
     */
    private String mark;

    /**
     * 店铺收藏数
     */
    private Integer shopCollect;

}
