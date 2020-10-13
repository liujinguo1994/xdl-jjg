package com.xdl.jjg.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-21 09:49:23
 */
@Data
@Accessors(chain = true)
@TableName("es_draft_goods")
public class EsDraftGoods extends Model<EsDraftGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
    @TableField("weight")
    private Double weight;

    /**
     * 上架状态 1上架 2下架
     */
    @TableField("market_enable")
    private Integer marketEnable;

    /**
     * 详情
     */
    @TableField("intro")
    private String intro;

    /**
     * 成本价格
     */
    @TableField("cost")
    private Double cost;

    /**
     * 商品价格
     */
    @TableField("money")
    private Double money;

    /**
     * 市场价格(参考价)
     */
    @TableField("mktmoney")
    private Double mktmoney;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /**
     * 是否删除0 删除 1未删除
     */
    @TableField("is_del")
    private Integer isDel;

    /**
     * 实际库存
     */
    @TableField("quantity")
    private Integer quantity;

    @TableField("page_title")
    private String pageTitle;

    @TableField("meta_keywords")
    private String metaKeywords;

    @TableField("meta_description")
    private String metaDescription;

    /**
     * 店铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 卖家分类ID
     */
    @TableField("shop_cat_id")
    private Long shopCatId;

    /**
     * 评论数量
     */
    @TableField("comment_num")
    private Integer commentNum;

    /**
     * 运费模板ID
     */
    @TableField("template_id")
    private Integer templateId;

    /**
     * 谁承担运费1：买家承担，2：卖家承担
     */
    @TableField("goods_transfee_charge")
    private Integer goodsTransfeeCharge;

    /**
     * 店铺名称
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
     * 是否自营1 是 2 否
     */
    @TableField("self_operated")
    private Integer selfOperated;

    /**
     * 下架原因
     */
    @TableField("under_message")
    private String underMessage;

    /**
     * 是否开票（1 支持 2 不支持）
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
    @TableField("grade")
    private Double grade;

    /**
     * 浏览数量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 购买数量
     */
    @TableField("buy_count")
    private Integer buyCount;

    /**
     * 原图路径
     */
    @TableField("original")
    private String original;

    /**
     * 卖家承诺ID
     */
    @TableField("promise_id")
    private Long promiseId;

    /**
     * 卖家自定义商品分类
     */
    @TableField("custom_id")
    private String customId;

    /**
     * 是否生鲜（1生鲜，2非生鲜）
     */
    @TableField("is_fresh")
    private Integer isFresh;

    /**
     * 审核时间
     */
    @TableField("auth_time")
    private Long authTime;

    /**
     * 是否赠品 1 是 2不是
     */
    @TableField("is_gifts")
    private Integer isGifts;

    /**
     * 售后服务
     */
    @TableField("after_service")
    private String afterService;

    /**
     * 是否有卖家承诺 1 是 2 否
     */
    @TableField("seller_promise")
    private Integer sellerPromise;
    @TableField("is_lfc")
    private Integer isLfc;
    /**
     * 是否虚拟 1 是 2 不是
     */
    @TableField("is_virtual")
    private Integer isVirtual;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
