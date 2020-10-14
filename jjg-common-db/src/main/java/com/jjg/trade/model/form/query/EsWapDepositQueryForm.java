package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * app端余额明细查询
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-04-16
 */
@Data
@ApiModel
public class EsWapDepositQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 操作类型(1收入,2支出,3全部)
     */
    @ApiModelProperty(required = true,value = "操作类型(1收入,2支出,3全部)")
    @NotNull(message = "操作类型不能为空")
    private String type;


}
