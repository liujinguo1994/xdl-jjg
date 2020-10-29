package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsSettingsDO;
import com.xdl.jjg.response.service.DubboResult;

public interface SettingsService {

    //根据业务标识查系统配置信息
    DubboResult<EsSettingsDO> getByCfgGroup(String cfgGroup);
}
