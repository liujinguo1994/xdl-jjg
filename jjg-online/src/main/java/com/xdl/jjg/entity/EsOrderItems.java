package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_order_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOrderItems extends Model<EsOrderItems> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * skuID
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 产品sn
     */
    @TableField("goods_sn")
    private String goodsSn;
    /**
     * 已发货数量
     */
    @TableField("ship_num")
    private Integer shipNum;

    /**
     * 是否生鲜
     */
    @TableField("is_fresh")
    private Integer isFresh;
    /**
     * 订单编号
     */
    @TableField("trade_sn")
    private String tradeSn;
    /**
     * 子订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 图片
     */
    private String image;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 优惠后价格
     */
    private Double money;
    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;
    /**
     * 售后状态
     */
    private String state;
    /**
     * 支付快照id
     */
    @TableField("snapshot_id")
    private Long snapshotId;
    /**
     * 规格json
     */
    @TableField("spec_json")
    private String specJson;
    /**
     * 促销类型
     */
    @TableField("promotion_type")
    private String promotionType;
    /**
     * 活动ID
     */
    @TableField("promotion_id")
    private Long promotionId;
    /**
     * 发货单号
     */
    @TableField("ship_no")
    private String shipNo;
    /**
     * 签收人
     */
    @TableField("the_sign")
    private String theSign;
    /**
     * 物流公司ID
     */
    @TableField("logi_id")
    private Long logiId;
    /**
     * 物流公司名称
     */
    @TableField("logi_name")
    private String logiName;

    /**
     * 单品评价是否完成
     */
    @TableField("single_comment_status")
    private String singleCommentStatus;
    /**
     * 发货状态 0 未发货，1 已发货
     */
    @TableField("has_ship")
    private Integer hasShip;

    /**
     * 会员ID
     * 此字段 仅供会员查询评价列表是使用
     */
    @TableField(exist = false)
    private Long memberId;

    /**
     * 发票流水号
     */
    @TableField("invoice_number")
    private String invoiceNumber;

    @ApiModelProperty(value = "配送方式 notInScope 不在配送范围，express 快递，selfMention 自提")
    private String deliveryMethod;

    public EsOrderItems(EsGoodsSkuCO skuDO, Integer saleCount){
        this.setGoodsId(skuDO.getGoodsId());
        this.setSkuId(skuDO.getId());
        this.setImage(skuDO.getThumbnail());
        this.setName(skuDO.getGoodsName());
        this.setNum(saleCount);
        this.setMoney(skuDO.getMoney());
        this.setGoodsSn(skuDO.getGoodsSn());
        this.setCategoryId(skuDO.getCategoryId());
    }
    public EsOrderItems(){

    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
