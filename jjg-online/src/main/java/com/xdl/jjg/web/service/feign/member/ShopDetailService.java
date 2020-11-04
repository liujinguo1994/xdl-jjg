package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsShopDetailDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface ShopDetailService {


    /**
     * 根据店铺id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult<EsShopDetailDO> getByShopId(@RequestParam("shopId") Long shopId);
}
