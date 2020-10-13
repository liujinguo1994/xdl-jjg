package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 导航菜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSiteNavigationVO implements Serializable {

    private static final long serialVersionUID = -1983506529503756634L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 导航名称
     */
    @ApiModelProperty(value = "导航名称")
    private String navigationName;
    /**
     * 导航地址
     */
    @ApiModelProperty(value = "导航地址")
    private String url;
    /**
     * 客户端类型
     */
    @ApiModelProperty(value = "客户端类型")
    private String clientType;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;


}
