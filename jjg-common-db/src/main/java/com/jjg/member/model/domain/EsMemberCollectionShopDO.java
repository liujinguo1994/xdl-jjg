package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员收藏店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionShopDO implements Serializable {


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



}
