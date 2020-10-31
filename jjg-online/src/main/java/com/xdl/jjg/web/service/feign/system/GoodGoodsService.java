package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsGoodGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface GoodGoodsService {



    /**
     * 分页查询发布中的品质好货
     */
    DubboPageResult<EsGoodGoodsDO> getList(int pageSize, int pageNum);
}
