package com.xdl.jjg.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class EsServiceOrderItemsVO extends Model<EsServiceOrderItemsVO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 店铺名称
     */
    @TableField("shop_name")
    private String shopName;
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
     * 已发货数量
     */
    @TableField("ship_num")
    private Integer shipNum;
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
     * 订单创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
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
     * 会员ID
     * 此字段 仅供会员查询评价列表是使用
     */
    @TableField(exist = false)
    private Long memberId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
