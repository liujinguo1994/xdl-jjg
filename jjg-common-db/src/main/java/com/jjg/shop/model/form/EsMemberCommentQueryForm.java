package com.xdl.jjg.model.form;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsMemberCommentQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "输入框输入值")
    private String keyword;
}
