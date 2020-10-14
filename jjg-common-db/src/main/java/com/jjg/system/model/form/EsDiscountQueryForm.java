package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 公司折扣
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsDiscountQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 6942417795393432160L;

    /**
     * 公司ID
     */
    @ApiModelProperty(required = true, value = "公司ID", example = "1")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;
}
