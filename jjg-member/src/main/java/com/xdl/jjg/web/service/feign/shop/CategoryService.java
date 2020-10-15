package com.xdl.jjg.web.service.feign.shop;


import com.jjg.shop.model.domain.EsCategoryDO;
import com.xdl.jjg.response.service.DubboResult;

public interface CategoryService {

    /**
     *获取分类信息
     */
    DubboResult<EsCategoryDO> getCategory(Long id);
}
