package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Api
public class EsShipTemplateForm implements Serializable {
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String modeName;
    /**
     * 是否生鲜（0生鲜，1非生鲜）
     */
    @ApiModelProperty(value = "是否生鲜（1生鲜，2非生鲜）")
    private Integer isFresh;
    /**
     * 物流公司ID
     */
    @ApiModelProperty(value = "物流公司ID")
    private Long logiId;
    /**
     * 物流公司名称
     */
    @ApiModelProperty(value = "物流公司名称")
    private String logiName;
    /**
     * 是否是活动(0是活动，1不是活动)
     */
    @ApiModelProperty(value = "是否是活动(0是活动，1不是活动)")
    private Integer sign;
    @ApiModelProperty(value = "运费模板详情集合")
    private Long isNg;
    private List<EsFreightTemplateDetailForm> freightDetailList;
}
