package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 店铺模版
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "店铺模版")
public class EsShopThemesQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -6549163021718690704L;
    /**
     * 模版类型
     */
    @ApiModelProperty(value = "模版类型")
	private String type;

    /**
     * 模版名称
     */
    @ApiModelProperty(value = "模版名称")
    private String name;
}
