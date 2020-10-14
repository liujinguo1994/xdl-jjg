package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Api
public class EsSeckillApplyQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "活动ID")
    @NotNull
    private Long seckillId;

}
