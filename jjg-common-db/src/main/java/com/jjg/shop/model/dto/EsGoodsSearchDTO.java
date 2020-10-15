package com.jjg.shop.model.dto;/**
 * @author wangaf
 * @date 2019/10/29 9:43
 **/

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/10/29
 @Version V1.0
 **/
@Data
public class EsGoodsSearchDTO implements Serializable {
    @ApiModelProperty(name = "page_no", value = "页码")
    private Integer pageNo;
    @ApiModelProperty(name = "page_size", value = "每页数量")
    private Integer pageSize;
    @ApiModelProperty(name = "keyword", value = "关键字")
    private String keyword;
    @ApiModelProperty(name = "categoryId", value = "分类")
    private Long categoryId;
    @ApiModelProperty(name = "brand", value = "品牌")
    private Integer brand;
    @ApiModelProperty(name = "money", value = "价格",example = "10_30")
    private String money;
    @ApiModelProperty(name = "sort", value = "排序:关键字_排序",allowableValues = "def_asc,def_desc,price_asc,price_desc,buynum_asc,buynum_desc,grade_asc,grade_desc")
    private String sort;
    @ApiModelProperty(name = "prop", value = "属性:参数名_参数值@参数名_参数值",example = "屏幕类型_LED@屏幕尺寸_15英寸")
    private String prop;
    @ApiModelProperty(name = "seller_id", value = "卖家id，搜索店铺商品的时候使用")
    private Integer sellerId;
    @ApiModelProperty(name = "shop_cat_id", value = "商家分组id，搜索店铺商品的时候使用")
    private Integer shopCatId;
}
