package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *  移动端-看了又看推荐
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-04-16
 */
@ApiModel
@Data
public class EsWapGoodsSeeForm extends QueryPageForm implements Serializable {

    private static final long serialVersionUID = -7349392509774931388L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id",required = true)
    @NotNull(message = "店铺id不能为空")
    private Integer shopId;

    @ApiModelProperty(value = "分类id",required = true)
    @NotNull(message = "商品分类id不能为空")
    private Integer categoryId;

    @ApiModelProperty(value = "商品id",required = true)
    @NotNull(message = "商品id不能为空")
    private Long goodsId;

}
