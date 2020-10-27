package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.gson.Gson;
import com.jjg.trade.model.domain.EsOrderItemsDO;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_refund_goods")
public class EsRefundGoods extends Model<EsRefundGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 退货(款)编号
     */
    @TableField("refund_sn")
    private String refundSn;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * SKUid
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 发货数量
     */
    @TableField("ship_num")
    private Integer shipNum;
    /**
     * 商品价格
     */
    private Double money;
    /**
     * 退货数量
     */
    @TableField("return_num")
    private Integer returnNum;
    /**
     * 退货入库数量
     */
    @TableField("storage_num")
    private Integer storageNum;
    /**
     * 商品编号
     */
    @TableField("goods_sn")
    private String goodsSn;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品图片
     */
    @TableField("goods_image")
    private String goodsImage;
    /**
     * 规格数据
     */
    @TableField("spec_json")
    private String specJson;

    public EsRefundGoods(EsOrderItemsDO orderSkuDTO) {
        this.goodsId = orderSkuDTO.getGoodsId();
        this.goodsImage = orderSkuDTO.getImage();
        this.goodsName = orderSkuDTO.getName();
//        this.goodsSn = orderSkuDTO.getGoods
        this.skuId = orderSkuDTO.getSkuId();
        this.shipNum = orderSkuDTO.getShipNum();
        Gson gson = new Gson();
        this.specJson = gson.toJson(orderSkuDTO.getSpecJson());
    }
    public EsRefundGoods(){

    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
