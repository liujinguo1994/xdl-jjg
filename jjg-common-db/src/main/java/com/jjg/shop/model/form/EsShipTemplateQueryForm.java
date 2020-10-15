package com.jjg.shop.model.form;
import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api
public class EsShipTemplateQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "模板名称")
    private String modeName;

}
