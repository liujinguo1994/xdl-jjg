package com.xdl.jjg.plugin.waybill;

import com.xdl.jjg.model.vo.EsConfigItemVO;

import java.util.List;

/**
 * 电子面单参数接口
 */
public interface WayBillEvent {


    /**
     * 配置各个电子面单的参数
     */
    List<EsConfigItemVO> definitionConfigItem();

    /**
     * 获取插件ID
     */
    String getPluginId();

    /**
     * 获取插件名称
     */
    String getPluginName();

    /**
     * 电子面单是否开启
     *
     * @return 0 不开启  1 开启
     */
    Integer getOpen();

}
