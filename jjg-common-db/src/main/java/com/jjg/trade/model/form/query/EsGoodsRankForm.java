package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *  热门榜单
 * </p>
 *
 * @author yuanj 595831329
 * @since 2020-05-27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsRankForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long goodsId;


}
