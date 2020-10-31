package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsRegionsDO;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

public interface RegionsService {



    //获取某地区的子地区
    DubboResult<List<EsRegionsDO>> getChildrenById(Long id);
}
