package com.xdl.jjg.plugin.sms;


import com.xdl.jjg.model.dto.EsSmsSendDTO;
import com.xdl.jjg.model.vo.EsConfigItemVO;

import java.util.List;
import java.util.Map;

/**
 * 短信发送接口
 */
public interface SmsPlatformManage {
    /**
     * 配置各个存储方案的参数
     */
    List<EsConfigItemVO> definitionConfigItem();

    /**
     * 发送短信事件
     *
     * @param smsSendDTO 发送短信参数
     * @param param      短信平台配置
     */
    boolean onSend(EsSmsSendDTO smsSendDTO, Map param);

    /**
     * 获取插件ID
     *
     * @return 插件beanId
     */
    String getPluginId();

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getPluginName();

    /**
     * 短信网关是否开启
     *
     * @return 0 不开启  1 开启
     */
    Integer getIsOpen();
}
