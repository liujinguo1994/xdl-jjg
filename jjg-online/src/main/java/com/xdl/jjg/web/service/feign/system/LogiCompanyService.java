package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.response.service.DubboResult;

public interface LogiCompanyService {

    /**
     * 根据名字查询
     */
    DubboResult<EsLogiCompanyDO> getByName(String name);

}
