package com.xdl.jjg.model.form;

import com.shopx.system.api.model.domain.vo.EsConfigItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 快递平台设置
 * </p>
 *
 * @author yuanj 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsExpressPlatForm implements Serializable {
    private static final long serialVersionUID = 1957382560310907842L;

    /**
     * 主键ID
     */
    @ApiModelProperty(required = true, value = "主键ID")
    private Long id;
    /**
     * 快递平台名称
     */
    @ApiModelProperty(required = true, value = "快递平台名称")
    private String name;
    /**
     * 是否开启快递平台,1开启，0未开启
     */
    @ApiModelProperty(required = true, value = "是否开启快递平台,1开启，0未开启")
    private Integer isOpen;
    /**
     * 快递平台配置
     */
    @ApiModelProperty(required = true, value = "快递平台配置")
    private List<EsConfigItemVO> configItems;
    /**
     * 快递平台beanid
     */
    @ApiModelProperty(required = true, value = "快递平台beanid")
    private String bean;
}
