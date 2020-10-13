package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-04-02
 */
@Data
public class EsWapRefundOrderItemsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "主键ID")
    private String shopName;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private Long skuId;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;
    /**
     * 已发货数量
     */
    @ApiModelProperty(value = "已发货数量")
    private Integer shipNum;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "主键ID")
    private String tradeSn;
    /**
     * 子订单编号
     */
    @ApiModelProperty(value = "子订单编号")
    private String orderSn;
    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间")
    private Long createTime;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;
    /**
     * 优惠后价格
     */
    @ApiModelProperty(value = "优惠后价格")
    private Double money;
    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    
    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 配送地区-省份
     */
    @ApiModelProperty(value = "配送地区-省份")
    private String shipProvince;
    /**
     * 配送地区-城市
     */
    @ApiModelProperty(value = "配送地区-城市")
    private String shipCity;
    /**
     * 配送地区-区(县)
     */
    @ApiModelProperty(value = "配送地区-区(县)")
    private String shipCounty;
    /**
     * 配送街道
     */
    @ApiModelProperty(value = "配送街道")
    private String shipTown;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String shipAddr;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String shipName;
    /**
     * 收货人手机
     */
    @ApiModelProperty(value = "收货人手机")
    private String shipMobile;


}
