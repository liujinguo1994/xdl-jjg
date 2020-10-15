package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 小程序端余额明细查询
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-18 09:28:26
 */
@Data
@ApiModel
public class EsAppletDepositQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;


    @ApiModelProperty(required = true,value = "操作类型(1收入,2支出,3全部)")
    @NotNull(message = "操作类型不能为空")
    private String type;

}
