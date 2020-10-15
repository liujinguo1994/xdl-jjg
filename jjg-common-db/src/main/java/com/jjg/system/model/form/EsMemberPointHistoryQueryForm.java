package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class EsMemberPointHistoryQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 5723077913104072066L;

    /**
     * 会员id
     */
    @ApiModelProperty(required = true, value = "会员id", example = "1")
    @NotNull(message = "会员id不能为空")
    private Long memberId;


}
