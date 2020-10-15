package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Api
public class EsFreightTemplateDetailForm implements Serializable {

    /**
     * 运送地区
     */
    @ApiModelProperty(value = "运送地区")
    @NotBlank(message = "运送地区不能为空")
    private String area;

    @ApiModelProperty(value = "地区id‘，‘分隔  示例参数：1，2，3，4 ")
    private String areaId;

    @ApiModelProperty(value = "地区Json  示例参数：[山西，太原，天津，上海]  这个参数应该是用于前端地区选择器填充选择的数据的")
    private String areaJson;
    /**
     * 首重
     */
    @ApiModelProperty(value = "首重")
    @NotNull(message = "首重不能为空")
    private Double firstWeight;

    /**
     * 首费
     */
    @ApiModelProperty(value = "首费")
    @NotNull(message = "首费不能为空")
    private Double firstTip;

    /**
     * 续重
     */
    @ApiModelProperty(value = "续重")
    @NotNull(message = "续重不能为空")
    private Double sequelWeight;

    /**
     * 续费
     */
    @ApiModelProperty(value = "续费")
    @NotNull(message = "续费不能为空")
    private Double sequelTip;


    /**
     * 实付金额最小值
     */
    @ApiModelProperty(value = "实付金额最小值")
    @NotNull(message = "实付金额最小值不能为空")
    private Double minMoney;

    /**
     * 实付金额最大值
     */
    @ApiModelProperty(value = "实付金额最大值")
    @NotNull(message = "实付金额最大值不能为空")
    private Double maxMoney;
}
