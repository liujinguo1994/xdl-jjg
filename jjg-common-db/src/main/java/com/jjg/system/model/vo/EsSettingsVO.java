package com.jjg.member.model.vo;

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
public class EsSettingsVO implements Serializable {

    private static final long serialVersionUID = -2617116468504295590L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 系统配置信息
     */
    @ApiModelProperty(value = "系统配置信息")
    private String cfgValue;
    /**
     * 业务设置标识
     */
    @ApiModelProperty(value = "业务设置标识")
    private String cfgGroup;

}
