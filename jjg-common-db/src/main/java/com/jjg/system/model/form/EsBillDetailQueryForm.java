package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 结算账单详情查询form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel(description = "结算账单详情查询form")
public class EsBillDetailQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 2203668909286958944L;

    /**
     * 关键字(结算单号，店铺名称)
     */
    @ApiModelProperty(value = "关键字(结算单号,店铺名称)")
    private String keyword;

    /**
     * 结算状态 0 已结算 1 未结算
     */
    @ApiModelProperty(value = "结算状态 0 已结算,1 未结算")
    private Integer state;

    /**
     * 结算单编号
     */
    @ApiModelProperty(required = true, value = "结算单编号")
    @NotBlank(message = "结算单编号不能为空")
    private String billSn;

    /**
     * 结算类型(1店铺结算,2供应商结算,3签约公司结算)
     */
    @ApiModelProperty(required = true, value = "结算类型(1店铺结算,2供应商结算,3签约公司结算)")
    @NotNull(message = "结算类型不能为空")
    private Integer type;
}
