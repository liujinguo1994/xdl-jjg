package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsPageDO;
import com.xdl.jjg.response.service.DubboResult;

public interface PageService {

    //使用客户端类型和页面类型查询一个楼层
    DubboResult<EsPageDO> getByType(String clientType, String pageType);

}
