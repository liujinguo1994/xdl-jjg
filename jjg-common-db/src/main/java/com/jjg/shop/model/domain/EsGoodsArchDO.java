package com.jjg.member.model.domain;

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
public class EsGoodsArchDO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品编号
     */
    private String goodsSn;

    /**
     * 库存
     */
    private Integer quantity;

    /**
     * 商品SKUID
     */
    private Long skuid;

    /**
     * 商品价格
     */
    private Double goodsMoney;

    /**
     * 成本价格
     */
    private Double cost;

    /**
     * 市场价格
     */
    private Double mktmoney;

    /**
     * 创建时间
     */
    private Long createTime;

    private Long updateTime;
    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 描述
     */
    private String intro;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 负责人姓名
     */
    private String chargePerson;

    /**
     * 负责人联系方式
     */
    private String chargeMobile;

    /**
     * 负责人邮箱
     */
    private String email;

    /**
     * 对接采购经理
     */
    private String purchaseManager;

    /**
     * 采购人联系方式
     */
    private String purchaseMobile;

    /**
     * 采购人邮箱
     */
    private String purchaseEmail;
    /**
     * 有效状态
     */
    private Long state;

    private Integer isFresh;

    private List<EsGoodsSkuDO> skuList;
    private Integer isGifts;

    /**
     * 税率
     */
    private Double taxRate;
    /**
     * 分类编码
     */
    private String cateCode;
    /**
     * 商品单位
     */
    private String unit;
}
