package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器-移动端-售后订单查询参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-03
 */
@ApiModel
@Data
public class EsWapAfterSaleRecordQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;

    /**
     * 状态(1处理中，2已完成)
     */
    @ApiModelProperty(value = "状态(1处理中，2已完成)",required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

}
