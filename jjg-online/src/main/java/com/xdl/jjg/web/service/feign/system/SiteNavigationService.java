package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsSiteNavigationDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface SiteNavigationService {



    //根据客户端类型查询导航菜单(PC,MOBILE)
    DubboPageResult<EsSiteNavigationDO> getByClientType(String clientType);
}
