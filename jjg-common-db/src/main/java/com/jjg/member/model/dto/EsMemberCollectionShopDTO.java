package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsMemberCollectionShopDTO implements Serializable {


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
     * 店铺备注
     */
    private String mark;
    /**
     * 店铺置顶
     */
    private Integer sort;



}
