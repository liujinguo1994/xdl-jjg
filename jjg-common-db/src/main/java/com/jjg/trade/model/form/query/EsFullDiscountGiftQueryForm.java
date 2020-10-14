package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 满减赠品表QueryForm
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsFullDiscountGiftQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赠品名称
     */
    @ApiModelProperty(value = "赠品名称")
    private String giftName;

    /**
     * 赠品金额
     */
    @ApiModelProperty(value = "赠品金额")
    private Double giftMoney;

    /**
     * 赠品图片
     */
    @ApiModelProperty(value = "赠品图片")
    private String giftImg;

    /**
     * 赠品类型
     */
    @ApiModelProperty(value = "赠品类型")
    private Integer giftType;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    private Integer enableStore;

    /**
     * 档案SKUid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "档案SKUid")
    private Long skuiId;

    /**
     * 店铺id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

}
