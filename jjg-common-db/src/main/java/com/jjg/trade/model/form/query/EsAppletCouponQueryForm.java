package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  小程序-优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-28
 */
@Data
@ApiModel
public class EsAppletCouponQueryForm extends QueryPageForm {
    private static final long serialVersionUID = -4900547578929199527L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
