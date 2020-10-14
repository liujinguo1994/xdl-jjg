package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class EsGoodsSkuQuantityForm implements Serializable {
    /**
     * SKU 主键ID
     */
    @ApiModelProperty(value = "SKU主键ID")
    @NotNull
    private Long skuId;

    /**
     * 调整后的虚拟库存
     */
    @ApiModelProperty(value = "调整后的虚拟库存")
    @NotNull
    private Integer xnQuantity;

}
