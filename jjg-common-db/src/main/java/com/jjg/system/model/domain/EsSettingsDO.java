package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSettingsDO implements Serializable {

    private static final long serialVersionUID = 2544178399678150593L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 系统配置信息
     */
    private String cfgValue;
    /**
     * 业务设置标识
     */
    private String cfgGroup;

}
