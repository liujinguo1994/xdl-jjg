package com.jjg.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Api
public class EsGoodsIndexDTO implements Serializable {
    /**
     * 商品主键ID
     */
    private Long  id ;

    /**
     *商品名称
     */
    private String goodsName;

    /**
     *原图路径
     */
    private String original;

    /**
     * 购买数量
     */
    private Integer buyCount;
    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     *店铺名称
     */
    private String shopName;
    /**
     * 评论数量
     */
    private Integer commentNum;
    /**
     *好评率
     */
    private Double grade;

    /**
     *商品价格
     */
    private Double money;

    /**
     * 品牌
     */
    private Long brandId;

    /**
     *分类ID
     */
    private Long categoryId;


    /**
     * 分类路径
     */
    private String categoryPath;

    /**
     * 删除状态
     */
    private Integer isDel;

    /**
     * 上架状态
     */
    private Integer marketEnable;

    /**
     *审核状态
     */
    private Integer isAuth;

    /**
     *描述
     */
    private String intro;
    /**
     * 输入值
     */
    private String keyword;
    /**
     * 排序 def 默认按照ID排序 buynum 销量
     */
    @ApiModelProperty(name = "sort", value = "排序:关键字_排序",allowableValues = "def_asc,def_desc,price_asc,price_desc,buynum_asc,buynum_desc,grade_asc,grade_desc")
    private String sort = "def_asc";

    private Integer isGifts;

    private Integer rank;

    private String prop;
    /**
     * 搜索价格 例如50_100
     */
    private String price;

    private List<String> brandList;

    private List<String> categoryIdList;

    private List<String> goodsList;

    private List<String> propList;
}
