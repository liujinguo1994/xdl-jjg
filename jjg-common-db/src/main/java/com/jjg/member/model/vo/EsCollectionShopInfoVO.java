package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class EsCollectionShopInfoVO implements Serializable {


    /**
     * 收藏id
     */
    @ApiModelProperty(value = "ID")
	private Long id;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
	private Long memberId;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
	private Long shopId;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
	private String shopName;
    /**
     * 收藏时间
     */
    @ApiModelProperty(value = "收藏时间")
	private Long createTime;
    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
	private String logo;
    /**
     * 店铺所在省
     */
    @ApiModelProperty(value = "店铺所在省")
	private String shopProvince;
    /**
     * 店铺所在市
     */
    @ApiModelProperty(value = "店铺所在市")
	private String shopCity;
    /**
     * 店铺所在县
     */
    @ApiModelProperty(value = "店铺所在县")
	private String shopRegion;
    /**
     * 店铺所在镇
     */
    @ApiModelProperty(value = "店铺所在镇")
	private String shopTown;
    /**
     * 收藏店铺排序
     */
    @ApiModelProperty(value = "收藏店铺排序")
    private Integer sort;
    /**
     * 店铺备注
     */
    @ApiModelProperty(value = "店铺备注")
    private String mark;

    /**
     * 店铺收藏数
     */
    @ApiModelProperty(value = "店铺收藏数")
    private Integer shopCollect;

}
