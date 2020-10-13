package com.xdl.jjg.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_goods")
public class EsGoods extends Model<EsGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id")
	private Long id;
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
     * 品牌id
     */

    @TableField("brand_id")
	private Long brandId;
    /**
     * 分类id
     */

    @TableField("category_id")
	private Long categoryId;
    /**
     * 商品类型normal普通point积分
     */
    @TableField("goods_type")
	private String goodsType;
    /**
     * 重量
     */
	private Double weight;
    /**
     * 上架状态 2下架 1上架
     */
    @TableField("market_enable")
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
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 最后修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
    /**
     * 是否被删除0未 删除 1删除
     */
    @TableField("is_del")
	private Integer isDel;
    /**
     * 可用库存=SKU之和
     */
	private Integer quantity;
    /**
     * seo标题
     */
    @TableField("page_title")
	private String pageTitle;
    /**
     * seo关键字
     */
    @TableField("meta_keywords")
	private String metaKeywords;
    /**
     * seo描述
     */
    @TableField("meta_description")
	private String metaDescription;
    /**
     * 卖家id
     */

    @TableField("shop_id")
	private Long shopId;
    /**
     * 店铺分类id
     */

    @TableField("shop_cat_id")
	private Long shopCatId;
    /**
     * 评论数量
     */
    @TableField("comment_num")
	private Integer commentNum;
    /**
     * 运费模板id
     */

    @TableField("template_id")
	private Long templateId;
    /**
     * 谁承担运费0：买家承担，1：卖家承担
     */
    @TableField("goods_transfee_charge")
	private Integer goodsTransfeeCharge;
    /**
     * 卖家名字
     */
    @TableField("shop_name")
	private String shopName;
    /**
     * 0 待审核，1 审核通过 2 未通过
     */
    @TableField("is_auth")
	private Integer isAuth;
    /**
     * 审核信息
     */
    @TableField("auth_message")
	private String authMessage;
    /**
     * 是否自营0 是 1 否
     */
    @TableField("self_operated")
	private Integer selfOperated;
    /**
     * 下架原因
     */
    @TableField("under_message")
	private String underMessage;
    /**
     * 是否开票
     */
    @TableField("is_invoice")
	private Integer isInvoice;
    /**
     * 1，专票2普票3专票+普票
     */
    @TableField("invoice_type")
	private Integer invoiceType;
    /**
     * 商品好评率
     */
	private Double grade ;
    /**
     * 浏览数量
     */
    @TableField("view_count")
	private Integer viewCount;
    /**
     * 购买数量
     */
    @TableField("buy_count")
	private Integer buyCount ;
    /**
     * 原图路径
     */
	private String original;
    /**
     * 卖家承诺主键ID
     */
    @TableField("promise_id")
	private Long promiseId;

    /**
     * 自定义分组ID
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 是否生鲜 2 非生鲜 1 生鲜
     */
    @TableField("is_fresh")
    private Integer isFresh;

    @TableField("auth_time")
    private Integer authTime;
    /**
     * 是否赠品 1 是 2 不是
     */
    @TableField("is_gifts")
    private Integer isGifts;

    /**
     * 售后服务
     */
    @TableField("after_service")
    private String afterService;
    @TableField("seller_promise")
    private Integer sellerPromise;
    /**
     * 是否国寿 1 是 2 不是
     */
    @TableField("is_lfc")
    private Integer isLfc;

    /**
     * 是否虚拟 1 是 2 不是
     */
    @TableField("is_virtual")
    private Integer isVirtual;
}
