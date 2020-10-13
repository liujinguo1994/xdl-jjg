package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员收藏店铺表
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMemberCollectionShopVO implements Serializable {


    /**
     * 收藏id
     */
    @ApiModelProperty(required = false,value = "主键id",example = "1")
	private Long id;
    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
	private Long memberId;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id",example = "1")
	private Long shopId;
    /**
     * 店铺名称
     */
    @ApiModelProperty(required = false,value = "店铺名称")
	private String shopName;
    /**
     * 收藏时间
     */
    @ApiModelProperty(required = false,value = "收藏时间",example = "1559303049597")
	private Long createTime;
    /**
     * 店铺logo
     */
    @ApiModelProperty(required = false,value = "店铺logo" )
	private String logo;
    /**
     * 店铺所在省
     */
    @ApiModelProperty(required = false,value = "店铺所在省" )
	private String shopProvince;
    /**
     * 店铺所在市
     */
    @ApiModelProperty(required = false,value = "店铺所在市")
	private String shopCity;
    /**
     * 店铺所在县
     */
    @ApiModelProperty(required = false,value = "店铺所在县")
	private String shopRegion;
    /**
     * 店铺所在镇
     */
    @ApiModelProperty(required = false,value = "店铺所在镇")
	private String shopTown;



}
