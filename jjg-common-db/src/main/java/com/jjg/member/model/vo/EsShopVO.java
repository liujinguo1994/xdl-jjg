package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShopVO implements Serializable {


    /**
     * 店铺ID
     */
    @ApiModelProperty(required = false, value = "店铺id", example = "1")
    private Long id;

    /**
     * 会员ID
     */
    @ApiModelProperty(required = false, value = "会员id", example = "1")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty(required = false,value = "会员名称")
    private String memberName;

    /**
     * 店铺状态
     */
    @ApiModelProperty(required = false,value = "店铺状态",example = "1")
    private String state;

    /**
     * 店铺名称
     */
    @ApiModelProperty(required = false,value = "店铺名称")
    private String shopName;

    /**
     * 店铺创建时间
     */
    @ApiModelProperty(required = false,value = "店铺创建时间",example = "1559303049597")
    private Long shopCreatetime;

    /**
     * 店铺关闭时间
     */
    @ApiModelProperty(required = false,value = "店铺关闭时间",example = "1559303049597")
    private Long shopEndtime;

    /**
     * 佣金比列
     */
    @ApiModelProperty(required = false,value = "佣金比列")
    private Double commission;


    /**
     * 手机号码
     */
    @ApiModelProperty(required = false,value = "手机号码")
    private String mobile;

    /**
     * 店铺详情
     */
    @ApiModelProperty(required = false,value = "店铺详情")
    private EsShopDetailVO shopDetailVO;
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
