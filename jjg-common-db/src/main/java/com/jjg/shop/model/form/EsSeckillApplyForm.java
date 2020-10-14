package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Api
public class EsSeckillApplyForm implements Serializable {


    @ApiModelProperty(value = "活动开始时间")
    @NotNull
    private Long startDay;

    @ApiModelProperty(value = "活动结束时间")
    @NotNull
    private Long endTime;

    @ApiModelProperty(value = "时刻")
    @NotNull
    private Long timeLine;

    @ApiModelProperty(value = "商品ID")
    @NotNull
    private Long goodsId;

    @ApiModelProperty(value = "商品SKU ID")
    private Long skuId;

    @ApiModelProperty("SKU规格")
    private String specs;

    @ApiModelProperty("SKU编号")
    private String skuSn;

    @ApiModelProperty(value = "商品名称")
    @NotBlank
    private String goodsName;

    @ApiModelProperty(value = "商品活动价格")
    @NotNull
    private Double money;

    @ApiModelProperty(value = "售空数量")
    @NotNull
    private  Integer soldQuantity;

    @ApiModelProperty(value = "商品原始价格")
    @NotNull
    private Double originalPrice;

    @ApiModelProperty(value = "商品图片(小图)")
    private String image;
}
