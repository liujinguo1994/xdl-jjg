package com.xdl.jjg.entity;


import com.baomidou.mybatisplus.annotation.*;
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
@TableName("es_goods_arch")
public class EsGoodsArch extends Model<EsGoodsArch> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    /**
     * 商品编号
     */
    @TableField("goods_sn")
    private String goodsSn;

    /**
     * 库存
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 商品SKUID
     */
    @TableField("skuid")
    private Long skuid;

    /**
     * 商品价格
     */
    @TableField("goods_money")
    private Double goodsMoney;

    /**
     * 成本价格
     */
    @TableField("cost")
    private Double cost;

    /**
     * 市场价格
     */
    @TableField("mktmoney")
    private Double mktmoney;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 重量
     */
    @TableField("weight")
    private Double weight;

    /**
     * 描述
     */
    @TableField("intro")
    private String intro;

    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Long supplierId;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;

    /**
     * 负责人姓名
     */
    @TableField("charge_person")
    private String chargePerson;

    /**
     * 负责人联系方式
     */
    @TableField("charge_mobile")
    private String chargeMobile;

    /**
     * 负责人邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 对接采购经理
     */
    @TableField("purchase_manager")
    private String purchaseManager;

    /**
     * 采购人联系方式
     */
    @TableField("purchase_mobile")
    private String purchaseMobile;

    /**
     * 采购人邮箱
     */
    @TableField("purchase_email")
    private String purchaseEmail;
    /**
     * 有效状态
     */
    @TableField("state")
    private Long state;

    /**
     * 是否生鲜 2 非生鲜 1 生鲜
     */
    @TableField("is_fresh")
    private Integer isFresh = 2;

    /**
     * 是否赠品 1 是 2 不是
     */
    @TableField("is_gifts")
    private Integer isGifts;

    /**
     * 税率
     */
    @TableField("tax_rate")
    private Double taxRate;

    /**
     * 分类编码
     */
    @TableField("cate_code")
    private String cateCode;
    /**
     * 商品单位
     */
    private String unit;

}
