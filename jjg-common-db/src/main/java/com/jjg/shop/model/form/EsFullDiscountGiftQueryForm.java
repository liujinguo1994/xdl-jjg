package com.jjg.shop.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsFullDiscountGiftQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "赠品名称")
    private String giftName;
}
