package com.xdl.jjg.web.service.feign.shop;

import com.jjg.member.model.domain.EsShopAndDetailDO;
import com.jjg.member.model.domain.EsShopDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface ShopService {

    /**
     * 根据id获取店铺
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @GetMapping("/getShop")
    DubboResult<EsShopDO> getShop(@RequestParam("id") Long id);


    /**
     * 根据id获取店铺及店铺明细
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    @GetMapping("/getShopDetail")
    DubboResult<EsShopAndDetailDO> getShopDetail(@RequestParam("id") Long id);

    /**
     * 买家-商品详情页根据id获取店铺及店铺明细
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    @GetMapping("/getBuyerShopDetail")
    DubboResult<EsShopDO> getBuyerShopDetail(@RequestParam("id") Long id);


}
