package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 结算账单查询form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel(description = "结算账单查询form")
public class EsBillQueryForm extends QueryPageForm {
    private static final long serialVersionUID = -9213159920518792808L;


    /**
     * 结算单编号
     */
    @ApiModelProperty(value = "结算单编号")
    private String billSn;

    /**
     * 出账时间开始
     */
    @ApiModelProperty(value = "出账时间开始")
    private Long startCreateTime;

    /**
     * 出账时间结束
     */
    @ApiModelProperty(value = "出账时间结束")
    private Long endCreateTime;

    /**
     * 结算类型(1店铺结算,2供应商结算,3签约公司结算)
     */
    @ApiModelProperty(required = true, value = "结算类型(1店铺结算,2供应商结算,3签约公司结算)")
    @NotNull(message = "结算类型不能为空")
    private Integer type;
}
