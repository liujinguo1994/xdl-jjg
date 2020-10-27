package com.xdl.jjg.web.service.feign.shop;

import com.jjg.member.model.domain.EsShopDO;
import com.xdl.jjg.response.service.DubboResult;

public interface ShopService {

    /**
     * 根据id获取店铺
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult<EsShopDO> getShop(Long id);
}
