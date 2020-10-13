package com.xdl.jjg.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_goods_sku")
public class EsGoodsSku extends Model<EsGoodsSku> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */

    @TableField("goods_id")
	private Long goodsId;
    /**
     * 商品名称
     */
    @TableField("goods_name")
	private String goodsName;
    /**
     * 商品编号
     */
    @TableField("goods_sn")
	private String goodsSn;
    /**
     * 条形码
     */
    @TableField("bar_code")
	private String barCode;
    /**
     * 真实库存(仓储系统可用库存)
     */
	private Integer quantity;
    /**
     * 可用库存=真实库存+虚拟库存-冻结库存
     */
    @TableField("enable_quantity")
	private Integer enableQuantity;
    /**
     * 冻结库存
     */
    @TableField("dj_quantity")
	private Integer djQuantity;
    /**
     * 虚拟库存
     */
    @TableField("xn_quantity")
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

    @TableField("shop_id")
	private Long shopId;
    /**
     * 卖家名称
     */
    @TableField("shop_name")
	private String shopName;
    /**
     * 分类id
     */

    @TableField("category_id")
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
    @TableField("is_self")
	private Integer isSelf;
    /**
     * 保修时间
     */
    @TableField("guarantee_time")
	private Integer guaranteeTime;
    /**
     * 质检状态
     */
    @TableField("quality_state")
    private Integer qualityState;

    /**
     * 质检报告
     */
    @TableField("quality_report")
    private String qualityReport;

    /**
     * 长
     */
    @TableField("sku_long")
    private String skuLong;

    /**
     * 宽
     */
    @TableField("wide")
    private String wide;

    /**
     * 高
     */
    @TableField("high")
    private String high;

    /**
     * 是否启用
     */
    @TableField("is_enable")
    private Integer isEnable;

    @TableField("warning_value")
    private Integer warningValue;
    /**
     * SKU编号
     */
    @TableField("sku_sn")
    private String skuSn;
    /**
     * 规格文本
     */
    @TableField("spec_text")
    private String specText;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
