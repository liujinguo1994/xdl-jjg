package com.jjg.system.model.form;

import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * 快递平台设置
 *
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2018年3月23日 下午3:07:05
 */
public interface ExpressPlatform {
    /**
     * 配置各个存储方案的参数
     *
     * @return 参数列表
     */
    List<EsConfigItemVO> definitionConfigItem();

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
     * 快递平台是否开启
     *
     * @return 0 不开启  1 开启
     */
    Integer getIsOpen();

    /**
     * 查询物流信息
     *
     * @param abbreviation 快递公司简称
     * @param num          快递单号
     * @param config       参数
     * @return 物流详细
     */
    DubboResult<ExpressDetailVO> getExpressDetail(String abbreviation, String num, Map config);
}
