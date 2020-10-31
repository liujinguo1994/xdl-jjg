package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBrandDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface BrandService {

    DubboPageResult<EsBrandDO> getBrandsByCategory(Long categoryId);
}
