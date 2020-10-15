package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 签约公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCompanyQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 4218143836452851876L;

    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;
}
