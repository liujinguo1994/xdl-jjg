package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  短信平台
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSmsPlatformVO implements Serializable {

    private static final long serialVersionUID = 2950716548677460448L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",example = "1")
	private Long id;
    /**
     * 平台名称
     */
    @ApiModelProperty(value = "平台名称")
	private String name;
    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启(0：关闭，1：开启)",example = "1")
	private Integer open;
    /**
     * 配置
     */
    @ApiModelProperty(value = "配置")
	private String config;
    /**
     * bean
     */
    @ApiModelProperty(value = "bean")
	private String bean;

    /*短信配置项*/
    @ApiModelProperty(value = "短信配置项")
    private List<EsConfigItemVO> configItems;

}
