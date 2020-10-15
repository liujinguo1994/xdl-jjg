package com.jjg.shop.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author wangaf 826988665@qq
 * @since 2019-06-025 17:30:01
 */
@Data
public class EsGoodsQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
	private String goodsName;

    /**
     * 商品编号
     */
	private String goodsSn;


    /**
     * 分类id
     */
	private Long categoryId;

    /**
     * 上架状态 1上架 2下架
     */
	private Integer marketEnable;

    /**
     * 运费模板id
     */
	private Long templateId;

    /**
     * 谁承担运费0：买家承担，1：卖家承担
     */
	private Integer goodsTransfeeCharge;
    /**
     * 0 待审核，1 审核通过 2 未通过
     */
	private Integer isAuth;

    /**
     * 是否自营0 是 1 否
     */
	private Integer selfOperated;
	/**
	 * 开始时间
	 */
	private Long startTime;
	/**
	 * 结束时间
	 */
	private Long endTime;
	/**
	 * 分类路径
	 */
	private String categoryPath;
	/**
     * 起始价格
	 */
	private Double startMoney;
	/**
     * 截止价格
	 */
	private Double endMoney;

	/**
	 * 是否删除
	 */
	private Long isDel;

	/**
	 * 店铺ID
	 */
	private Long shopId;
	/**
	 * 店铺自定义分类ID
	 */
	private Long customId;

	/**
	 * 分类状态
	 */
	private String customState;
	/**
	 * 输入值
	 */
	private String keyword;

	private String shopName;

	private Integer isGifts;

	private String supplierName;

	private Long authTimeStart;

	private Long authTimeEnd;

	private int pageNum;

	private int pageSize;
	private Long isLfc;
	private Integer isVirtual;
}
