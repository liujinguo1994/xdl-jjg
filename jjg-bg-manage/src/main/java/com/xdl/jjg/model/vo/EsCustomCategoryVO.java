package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCustomCategoryVO implements Serializable {
    private static final long serialVersionUID = -5810578454543835296L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 自定义分类名称
     */

    @ApiModelProperty(value = "自定义分类名称")
    private String categoryName;

    /**
     * 所属专区
     */
    @ApiModelProperty(value = "所属专区")
    private Long zoneId;

    /**
     * 专区名称
     */
    @ApiModelProperty(value = "专区名称")
    private String zoneName;

    /**
     * 发现好货
     */
    @ApiModelProperty(value = "发现好货")
    private List<EsFindGoodsVO> findGoodsVOS;

    /**
     * 常卖清单
     */
    private List<EsOftenGoodsVO> oftenGoodsVOS;
}
