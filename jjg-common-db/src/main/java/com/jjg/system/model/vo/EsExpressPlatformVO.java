package com.jjg.system.model.vo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jjg.system.model.domain.EsExpressPlatformDO;
import com.jjg.system.model.form.ExpressPlatform;
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
public class EsExpressPlatformVO implements Serializable {

    private static final long serialVersionUID = -5218286683378878352L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 快递平台名称
     */
    private String name;
    /**
     * 是否开启快递平台,1开启，0未开启
     */
    private Integer isOpen;
    /**
     * 快递平台配置
     */
    private String config;
    /**
     * 快递平台beanid
     */
    private String bean;

    /**
     * 快递平台配置项
     */
    @ApiModelProperty(name = "configItems", value = "快递平台配置项", required = true)
    private List<EsConfigItemVO> configItems;

    public EsExpressPlatformVO() {

    }

    public EsExpressPlatformVO(EsExpressPlatformDO expressPlatformDO) {
        this.id = expressPlatformDO.getId();
        this.name = expressPlatformDO.getName();
        this.isOpen = expressPlatformDO.getIsOpen();
        this.bean = expressPlatformDO.getBean();
        Gson gson = new Gson();
        this.configItems = gson.fromJson(expressPlatformDO.getConfig(), new TypeToken<List<EsConfigItemVO>>() {
        }.getType());
    }

    public EsExpressPlatformVO(ExpressPlatform expressPlatform) {
        this.id = 0l;
        this.name = expressPlatform.getPluginName();
        this.isOpen = expressPlatform.getIsOpen();
        this.bean = expressPlatform.getPluginId();
        this.configItems = expressPlatform.definitionConfigItem();
    }
}
