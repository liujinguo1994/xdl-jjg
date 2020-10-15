package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsWaybillVO implements Serializable {

    private static final long serialVersionUID = 4033831807180622704L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 是否开启(0未开启，1开启)
     */
    @ApiModelProperty(value = "是否开启(0未开启，1开启)", example = "1")
    private Integer open;
    /**
     * 电子面单配置
     */
    @ApiModelProperty(value = "电子面单配置")
    private String config;
    /**
     * 电子面单bean
     */
    @ApiModelProperty(value = "电子面单bean")
    private String bean;

    /**
     * 电子面单配置项
     */
    @ApiModelProperty(value = "电子面单配置项")
    private List<EsConfigItemVO> configItems;

}
