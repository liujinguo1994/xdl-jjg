package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsAdminTagGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface AdminTagGoodsService {

    /**
     * 根据shopId、tagId获取数据
     * @date: 2019/05/31 16:37:16
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @GetMapping("/getAdminGoodsTagsByShopId")
    DubboPageResult<EsAdminTagGoodsDO> getAdminGoodsTagsByShopId(@RequestParam("shopId") Long shopId,@RequestParam("tagId") Long tagId);

    /**
     * 根据标签获取商品数据
     * @param shopId
     * @param tagId
     * @param type 1 热门 2 新品
     * @return
     */
    @GetMapping("/getBuyerAdminGoodsTags")
    DubboPageResult<EsGoodsDO> getBuyerAdminGoodsTags(@RequestParam("shopId") Long shopId,@RequestParam("tagId") Long tagId,@RequestParam("type") Long type);
}
