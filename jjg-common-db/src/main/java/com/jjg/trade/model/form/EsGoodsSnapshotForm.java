package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商品快照Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsSnapshotForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private String goodsType;

    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private Double weight;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String intro;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private Double money;

    /**
     * 商品成本价
     */
    @ApiModelProperty(value = "商品成本价")
    private Double cost;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private Double mktmoney;

    /**
     * 参数json
     */
    @ApiModelProperty(value = "参数json")
    private String paramsJson;

    /**
     * 图片json
     */
    @ApiModelProperty(value = "图片json")
    private String imgJson;

    /**
     * 快照时间
     */
    @ApiModelProperty(value = "快照时间")
    private Long createTime;

    /**
     * 所属卖家
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "所属卖家")
    private Long shopId;

}
