package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.EsBuyerGoodsParamsDO;
import com.shopx.goods.api.model.domain.vo.EsBuyerGoodsParamsVO;
import com.shopx.goods.api.model.domain.vo.EsPromotionGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapGoodsVO implements  Serializable{

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;
    /**
     * 品牌id
     */
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    /**
     * 商品类型normal普通point积分
     */
    @ApiModelProperty(value = "商品类型normal普通point积分")
    private String goodsType;
    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private Double weight;
    /**
     * 上架状态 0上架 1下架
     */
    @ApiModelProperty(value = "上架状态 1上架 2下架")
    private Integer marketEnable;
    /**
     * 详情
     */
    @ApiModelProperty(value = "详情")
    private String intro;
    /**
     * 成本价格
     */
    @ApiModelProperty(value = "成本价格")
    private Double cost;
    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格 SKU最小金额")
    private Double money;
    /**
     * 市场价格(参考价)
     */
    @ApiModelProperty(value = "市场价格(参考价)")
    private Double mktmoney;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private Long updateTime;
    /**
     * 是否被删除0未 删除 1删除
     */
    @ApiModelProperty(value = "是否被删除0未 删除 1删除")
    private Integer isDel;
    /**
     * 可用库存=SKU之和
     */
    @ApiModelProperty(value = "实际库存")
    private Integer quantity;
    /**
     * seo标题
     */
    @ApiModelProperty(value = "seo标题")
    private String pageTitle;
    /**
     * seo关键字
     */
    @ApiModelProperty(value = "seo关键字")
    private String metaKeywords;
    /**
     * seo描述
     */
    @ApiModelProperty(value = "seo描述")
    private String metaDescription;
    /**
     * 卖家id
     */
    @ApiModelProperty(value = "卖家id")
    private Long shopId;
    /**
     * 店铺分类id
     */
    @ApiModelProperty(value = "店铺分类id")
    private Long shopCatId;
    /**
     * 评论数量
     */
    @ApiModelProperty(value = "评论数量")
    private Integer commentNum;
    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    private Long templateId;
    /**
     * 谁承担运费0：买家承担，1：卖家承担
     */
    @ApiModelProperty(value = "谁承担运费1：买家承担，2：卖家承担")
    private Integer goodsTransfeeCharge;
    /**
     * 卖家名字
     */
    @ApiModelProperty(value = "卖家名字")
    private String shopName;
    /**
     * 0 待审核，1 审核通过 2 未通过
     */
    @ApiModelProperty(value = "0 待审核，1 审核通过 2 未通过")
    private Integer isAuth;
    /**
     * 审核信息
     */
    @ApiModelProperty(value = "审核信息")
    private String authMessage;
    /**
     * 是否自营0 是 1 否
     */
    @ApiModelProperty(value = "是否自营1 是 2 否")
    private Integer selfOperated;
    /**
     * 下架原因
     */
    @ApiModelProperty(value = "下架原因")
    private String underMessage;
    /**
     * 是否开票
     */
    @ApiModelProperty(value = "是否开票")
    private Integer isInvoice;
    /**
     * 1，专票2普票3专票+普票
     */
    @ApiModelProperty(value = "1，专票2普票3专票+普票")
    private Integer invoiceType;
    /**
     * 商品好评率
     */
    @ApiModelProperty(value = "商品好评率")
    private Double grade;
    /**
     * 浏览数量
     */
    @ApiModelProperty(value = "浏览数量")
    private Integer viewCount;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;
    /**
     * 原图路径
     */
    @ApiModelProperty(value = "原图路径")
    private String original;

    @ApiModelProperty(value = "sku")
    private List<EsWapGoodsSkuVO> skuList;

    /**
     * 卖家承诺ID
     */
    @ApiModelProperty(value = "卖家承诺ID")
    private Long promiseId;
    @ApiModelProperty(value = "卖家承诺text")
    private String promiseText;
    /**
     * 库存数量=SKU可用库存+虚拟库存
     */
    @ApiModelProperty(value = "SKU库存之和=SKU可用库存+虚拟库存")
    private Integer zsQuantity;

    /**
     * 商品自定义分组ID
     */
    @ApiModelProperty(value = "自定义分类ID 空为未绑定 非空为绑定")
    private Long customId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "是否生鲜（1生鲜，2非生鲜)")
    private Integer isFresh;
    @ApiModelProperty(value = "是否赠品 1 是 2 不是")
    private Integer isGifts;
    @ApiModelProperty(value = "商品参数")
    private List<EsBuyerGoodsParamsVO> paramsList;
    @ApiModelProperty(value = "商品活动")
    private List<EsPromotionGoodsVO> promotionList;
    @ApiModelProperty(value = "是否有卖家承诺 1 是 2 否")
    private Integer sellerPromise;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    private String afterService;
}
