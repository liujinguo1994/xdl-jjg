package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel
public class EsSupplierQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 7054068478466538248L;
    /**
     * 关键字(供应商名称，联系人，联系方式)
     */
    @ApiModelProperty(value = "关键字(供应商名称，联系人，联系方式)")
    private String keyword;

}
