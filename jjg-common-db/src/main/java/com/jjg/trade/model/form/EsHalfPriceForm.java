package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 第二件半价活动表Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsHalfPriceForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    private Long createTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Long updateTime;

    /**
     * 活动标题
     */
    @ApiModelProperty(value = "活动标题")
    private String title;

    /**
     * 商品参与方式1全部商品：2，部分商品
     */
    @ApiModelProperty(value = "商品参与方式1全部商品：2，部分商品")
    private Integer rangeType;

    /**
     * 是否停用 0.没有停用 1.停用
     */
    @ApiModelProperty(value = "是否停用 0.没有停用 1.停用")
    private Integer isDel;

    /**
     * 活动说明
     */
    @ApiModelProperty(value = "活动说明")
    private String description;

    /**
     * 商家id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商家id")
    private Long shopId;

}
