package com.jjg.shop.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsMemberCommentQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "输入框输入值")
    private String keyword;
}
