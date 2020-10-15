package com.jjg.shop.model.form;
import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api
public class EsShopThemesQueryForm extends QueryPageForm {
    /**
     * 模版名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "模板类型 PC WAP")
    private String type;
}
