package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsPageVO implements Serializable {


    private static final long serialVersionUID = 3678107626779679264L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;
    /**
     * 楼层名称
     */
    @ApiModelProperty(value = "楼层名称")
    private String pageName;
    /**
     * 楼层数据
     */
    @ApiModelProperty(value = "楼层数据")
    private String pageData;
    /**
     * 页面类型
     */
    @ApiModelProperty(value = "页面类型")
    private String pageType;
    /**
     * 客户端类型
     */
    @ApiModelProperty(value = "客户端类型")
    private String clientType;

}
