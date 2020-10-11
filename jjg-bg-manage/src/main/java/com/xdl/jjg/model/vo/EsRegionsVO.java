package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 地区
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "地区")
public class EsRegionsVO implements Serializable {

    private static final long serialVersionUID = -1521957005724302988L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",example = "1")
	private Long id;
    /**
     * 父地区id
     */
    @ApiModelProperty(value = "父地区id",example = "1")
	private Long parentId;
    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
	private String regionPath;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别",example = "1")
	private Integer regionGrade;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
	private String localName;
    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编")
	private String zipcode;

    /**
     * 子对象集合
     */
    private List<EsRegionsVO> children;
}
