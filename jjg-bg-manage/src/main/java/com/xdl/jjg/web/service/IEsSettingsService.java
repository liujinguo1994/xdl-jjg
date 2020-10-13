package com.xdl.jjg.web.service;

import com.xdl.jjg.model.domain.EsSettingsDO;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.model.enums.SettingGroup;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsSettingsService {


    //根据id获取数据
    DubboResult<EsSettingsDO> getSettings(Long id);

    //根据业务标识查系统配置信息
    DubboResult<EsSettingsDO> getByCfgGroup(String cfgGroup);

    //修改安全设置
    DubboResult updateSecuritySetting(SettingGroup group, EsSecuritySettingsDTO esSecuritySettingsDTO);

    //保存订单设置
    DubboResult saveOrderSetting(SettingGroup group, EsOrderSettingDTO esOrderSettingDTO);

    //静态页地址参数保存
    DubboResult saveStaticPageAddress(SettingGroup group, EsPageSettingDTO esPageSettingDTO);

    //保存结算周期设置
    DubboResult saveClearingCycle(SettingGroup group, EsClearingCycleSettingsDTO clearingCycleSettingsDTO);

    //保存站点设置
    DubboResult saveSite(SettingGroup group, EsSiteDTO dto);
}
