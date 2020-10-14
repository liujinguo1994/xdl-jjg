package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsSellerGoodsDO implements  Serializable {

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品名称
     */
	private String goodsName;
    /**
     * 商品编号
     */
	private String goodsSn;
    /**
     * 品牌id
     */
	private Long brandId;
    /**
     * 分类id
     */
	private Long categoryId;
    /**
     * 商品类型normal普通point积分
     */
	private String goodsType;
    /**
     * 重量
     */
	private Double weight;
    /**
     * 上架状态 1上架 0下架
     */
	private Integer marketEnable;
    /**
     * 详情
     */
	private String intro;
    /**
     * 成本价格
     */
	private Double cost;
    /**
     * 商品价格
     */
	private Double money;
    /**
     * 市场价格(参考价)
     */
	private Double mktmoney;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 最后修改时间
     */
	private Long updateTime;
    /**
     * 是否被删除0未 删除 1删除
     */
	private Integer isDel;
    /**
     * 可用库存=SKU之和
     */
	private Integer quantity;
    /**
     * seo标题
     */
	private String pageTitle;
    /**
     * seo关键字
     */
	private String metaKeywords;
    /**
     * seo描述
     */
	private String metaDescription;
    /**
     * 卖家id
     */
	private Long shopId;
    /**
     * 店铺分类id
     */
	private Long shopCatId;
    /**
     * 评论数量
     */
	private Integer commentNum;
    /**
     * 运费模板id
     */
	private Long templateId;
    /**
     * 谁承担运费0：买家承担，1：卖家承担
     */
	private Integer goodsTransfeeCharge;
    /**
     * 卖家名字
     */
	private String shopName;
    /**
     * 0 待审核，1 审核通过 2 未通过
     */
	private Integer isAuth;
    /**
     * 审核信息
     */
	private String authMessage;
    /**
     * 是否自营0 是 1 否
     */
	private Integer selfOperated;
    /**
     * 下架原因
     */
	private String underMessage;
    /**
     * 是否开票
     */
	private Integer isInvoice;
    /**
     * 1，专票2普票3专票+普票
     */
	private Integer invoiceType;
    /**
     * 商品好评率
     */
	private Double grade;
    /**
     * 浏览数量
     */
	private Integer viewCount;
    /**
     * 购买数量
     */
	private Integer buyCount;
    /**
     * 原图路径
     */
	private String original;

	/**
	 * 卖家承诺ID
	 */
	private Long promiseId;

	/**
	 * 页面显示库存（后台使用）
	 */
	private Integer zsQuantity;
	/**
	 * 销售价格
	 */
	private Double saleMoney;
	/**
	 * 商品自定义分组ID
	 */
	private Long customId;
	/**
	 * 供应商名称
	 */
	private String supplierName;

	private Integer isFresh;

	private Integer isGifts;

	private String categoryName;
	private List<EsSellerGoodsSkuDO> skuList;
	private String afterService;
	private Long[] tagsIds;
	private Integer sellerPromise;
	private Integer isLfc;
	private Integer isVirtual;
}
