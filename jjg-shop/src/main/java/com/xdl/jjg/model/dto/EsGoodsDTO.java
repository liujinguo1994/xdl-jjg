package com.xdl.jjg.model.dto;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsDTO implements Serializable  {

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
	 * 上架状态 0上架 1下架
	 */
	private Integer marketEnable ;
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
	 * 可用库存=SKU之和
	 */
	private Integer quantity;
	/**
	 * 店铺分类id
	 */
	private Long shopCatId;
	/**
	 * 运费模板id
	 */
	private Long templateId;
	/**
	 * 谁承担运费0：买家承担，1：卖家承担
	 */
	private Integer goodsTransfeeCharge;

	/**
	 * 是否自营0 是 1 否
	 */
	private Integer selfOperated;

	/**
	 * 是否开票
	 */
	private Integer isInvoice;
	/**
	 * 1，专票2普票3专票+普票
	 */
	private Integer invoiceType;

	/**
	 * 原图路径
	 */
	private String original;
	/**
	 * 店铺Id
	 */
	private Long shopId;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 商品SKU 集合
	 */
	private List<EsGoodsSkuDTO> skuList;
	/**
	 * 商品标签集合
	 */
	private Integer[] tagsIds;

	private Long promiseId;

	private Integer[] goods_ids;

	/**
	 * 自定义分类ID
	 */
	private Long customId;
	//是否生鲜 2 非生鲜 1 生鲜
	private Integer isFresh ;

	private List<EsGoodsParamsDTO> paramsList;

	private Integer isDel;

	private Integer isGifts;

	private String afterService;

	private Integer sellerPromise;

	private Integer isLfc;
	private Integer isVirtual;
}
