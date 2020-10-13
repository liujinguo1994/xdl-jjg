package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("店铺站内查询Form")
public class EsShopNoticeLogQueryForm extends QueryPageForm {
    /**
     * 是否已读 ：1已读   0 未读
     */
    @ApiModelProperty(value = "是否已读",example = "0")
    private Integer isRead;
}
