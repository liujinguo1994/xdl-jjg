package com.jjg.member.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 售后维护配置Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAfterSaleForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退货时间
     */
    @ApiModelProperty(value = "退货时间")
    private Integer returnGoodsTime;

    /**
     * 换货时间
     */
    @ApiModelProperty(value = "换货时间")
    private Integer changeGoodsTime;

    /**
     * 商品分类ID
     */
    @NotNull(message = "商品分类不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;

}
