package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsShopQueryRoleForm extends QueryPageForm {
    @ApiModelProperty(value = "输入框输入值")
    private String keyword;
}
