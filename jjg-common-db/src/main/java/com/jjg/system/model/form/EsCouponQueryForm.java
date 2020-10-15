package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsCouponQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -7256020948191378135L;

    /**
     * 输入框输入值(用户名，手机号)
     */
    @ApiModelProperty(value = "输入框输入值(用户名，手机号)")
    private String keyword;

}
