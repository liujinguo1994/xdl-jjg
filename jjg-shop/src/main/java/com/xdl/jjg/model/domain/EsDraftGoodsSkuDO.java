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
public class EsDraftGoodsSkuDO implements  Serializable {

    private static final long serialVersionUID = 1L;

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
     * 商品SKU预警值
     */
    private Integer warningValue;
    /**
     * 规格文本
     */
    private String specText;

    private String skuSn;
    private String albumNo;

    private List<EsGoodsGalleryDO> goodsGallery;

    protected Serializable pkVal() {
		return this.id;
	}

}
