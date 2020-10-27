package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommercelItemsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface CommercelItemsService {

    /**
     * 根据查询会员id查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 11:12:53
     * @param memberId  会员id
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommercelItemsDO>
     */
    DubboPageResult<EsCommercelItemsDO> getCommercelItemsListByMemeberId(Long memberId);


    /**
     * 根据skuId获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:37:18
     * @param skuId    skuId
     * @param cartId    cartId
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    DubboResult<EsCommercelItemsDO> getItemsBySkuId(Long skuId, Long cartId);
}
