package com.jjg.shop.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EsClerkQueryParamForm extends QueryPageForm {

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "输入框输入值" )
    private String keyword;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "会员状态 0 正常 1禁用")
    private Integer state;
}
