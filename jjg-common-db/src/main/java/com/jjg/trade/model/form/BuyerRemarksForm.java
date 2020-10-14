package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel
public class BuyerRemarksForm implements Serializable {

    /**
     * 备注内容
     */
    @ApiModelProperty(required = true,value = "备注内容")
    private Map<Long,String> remark;

}
