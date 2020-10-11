package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 站点设置vo
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSiteVO implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    /**
     * 站点名称
     */
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    /**
     * 网站标题
     */
    @ApiModelProperty(value = "网站标题")
    private String title;
    /**
     * 网站关键字
     */
    @ApiModelProperty(value = "网站关键字")
    private String keywords;
    /**
     * 网站描述
     */
    @ApiModelProperty(value = "网站描述")
    private String descript;
    /**
     * 网站logo
     */
    @ApiModelProperty(value = "网站logo")
    private String logo;
    /**
     * 默认图片
     */
    @ApiModelProperty(value = "默认图片")
    private String defaultImg;
}
