package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.dto.EsGoodsSkuQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface GoodsSkuService {

    /**
     * 根据查询条件分页查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param goodsSkuDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsSkuDO>
     */
    @GetMapping("/getGoodsSkuList")
    DubboPageResult<EsGoodsSkuDO> getGoodsSkuList(@RequestBody EsGoodsSkuQueryDTO goodsSkuDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
}
