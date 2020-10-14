package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EsReportQueryForm extends QueryPageForm {
    private static final long serialVersionUID = 566901432812624422L;

    @ApiModelProperty(value = "商家名称")
    private String shopName;

    @ApiModelProperty(value = "处理状态(WAIT:待处理，APPLYING:处理中，APPLYED:已处理)")
    private String state;
}
