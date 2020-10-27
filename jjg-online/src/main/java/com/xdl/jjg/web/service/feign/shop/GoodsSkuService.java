package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-shop")
public interface GoodsSkuService {

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSku(Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSkuEnable(Long id);
}
