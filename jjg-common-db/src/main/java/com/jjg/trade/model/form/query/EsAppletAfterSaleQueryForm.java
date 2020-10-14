package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  前端控制器-小程序-售后订单查询参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-12
 */
@ApiModel
@Data
public class EsAppletAfterSaleQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
