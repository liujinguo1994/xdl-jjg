package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运费模板详情表Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsFreightTemplateDetailForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联模板id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "关联模板id")
    private Long modeId;

    /**
     * 运送地区
     */
    @ApiModelProperty(value = "运送地区")
    private String area;

    /**
     * 首重
     */
    @ApiModelProperty(value = "首重")
    private Double firstWeight;

    /**
     * 首费
     */
    @ApiModelProperty(value = "首费")
    private Double firstTip;

    /**
     * 续重
     */
    @ApiModelProperty(value = "续重")
    private Double sequelWeight;

    /**
     * 续费
     */
    @ApiModelProperty(value = "续费")
    private Double sequelTip;

    /**
     * 是否删除(0否,1删除)
     */
    @ApiModelProperty(value = "是否删除(0否,1删除)")
    private Integer isDel;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Long updateTime;

    /**
     * 实付金额最小值
     */
    @ApiModelProperty(value = "实付金额最小值")
    private Double minMoney;

    /**
     * 实付金额最大值
     */
    @ApiModelProperty(value = "实付金额最大值")
    private Double maxMoney;

}
