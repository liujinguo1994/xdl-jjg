package com.jjg.trade.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/** 
 * 优惠活动已选中的SKU信息
 * <br/>
 * @author KThirty
 * @since 2020/5/20 9:08
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESGoodsSkuSelectDO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 真实库存(仓储系统可用库存)
     */
    private Integer quantity;
    /**
     * 可用库存=真实库存+虚拟库存-冻结库存
     */
    private Integer enableQuantity;
    /**
     * 冻结库存
     */
    private Integer djQuantity;
    /**
     * 虚拟库存
     */
    private Integer xnQuantity;
    /**
     * 规格信息json
     */
    private String specs;
    /**
     * 商品价格
     */
    private Double money;
    /**
     * 成本价格
     */
    private Double cost;
    /**
     * 重量
     */
    private Double weight;
    /**
     * 卖家id
     */
    private Long shopId;
    /**
     * 卖家名称
     */
    private String shopName;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否支持自体
     */
    private Integer isSelf;
    /**
     * 保修时间
     */
    private Integer guaranteeTime;

    /**
     * 质检状态
     */
    private Integer qualityState;

    /**
     * 质检报告
     */
    private String qualityReport;

    /**
     * 长
     */
    private String skuLong;

    /**
     * 宽
     */
    private String wide;

    /**
     * 高
     */
    private String high;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 预警状态
     */
    private String stateName;
    /**
     * sku编号
     */
    private String skuSn;
    private String specText;
    private Integer warningValue;
    private List<EsSpecValuesDO> specList;
}
