package com.jjg.shop.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsFullDiscountQueryForm extends QueryPageForm {

    /**
     *
     */
    @ApiModelProperty(value = "活动名称")
    private  String title;
}
