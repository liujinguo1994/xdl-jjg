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
 * 售后维护配置QueryForm
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAfterSaleQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品分类ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;

}
