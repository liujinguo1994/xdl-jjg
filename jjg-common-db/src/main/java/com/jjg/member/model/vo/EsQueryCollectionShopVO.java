package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsQueryCollectionShopVO implements Serializable {

    /**
     * 收藏商品ID
     */
    @ApiModelProperty(required = false,value = "收藏商品ID",example = "1")
    private Long goodsId;
    /**
     * 收藏id
     */
    @ApiModelProperty(required = false,value = "收藏id",example = "1")
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
     * 店铺logo
     */
    @ApiModelProperty(required = false,value = "店铺logo")
	private String logo;
    /**
     * 店铺备注
     */
    @ApiModelProperty(required = false,value = "店铺备注")
    private String mark;
    /**
     * 店铺标签列表
     */
    @ApiModelProperty(required = false,value = "店铺标签列表")
    private List<EsMemberCollectionShopLabelVO> collectionShopLabelVOList;
    /**
     * 热门商品列表
     */
    @ApiModelProperty(required = false,value = "热门商品列表")
    private List<EsMemberGoodsVO> hotGoodList;

    /**
     * 上新商品列表
     */
    @ApiModelProperty(required = false,value = "上新商品列表")
    private List<EsMemberGoodsVO> newGoodList;
}
