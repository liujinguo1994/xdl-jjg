package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商品档案明细form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsGoodsArchDetailQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -2874258846764870633L;

    /**
     * 档案id
     */
    @ApiModelProperty(value = "档案id")
    @NotNull(message = "档案id不能为空")
    private Long id;

    /**
     * sku编号
     */
    @ApiModelProperty(value = "sku编号")
    private String skuSn;
}
