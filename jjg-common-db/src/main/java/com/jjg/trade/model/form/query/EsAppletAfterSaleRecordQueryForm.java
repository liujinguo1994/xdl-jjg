package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器-小程序端-售后订单查询参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-16
 */
@ApiModel
@Data
public class EsAppletAfterSaleRecordQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;

    /**
     * 状态(1处理中，2已完成)
     */
    @ApiModelProperty(value = "状态(1处理中，2已完成)",required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
