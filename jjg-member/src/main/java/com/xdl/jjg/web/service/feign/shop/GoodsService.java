package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface GoodsService {

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/getGoodsById")
    DubboResult<EsGoodsCO> getEsGoods(@RequestParam(value = "id") Long id);

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/getGoodsByIds")
    DubboPageResult<EsGoodsDO> getEsGoods(@RequestParam(value = "ids") Long[] ids);

    /**
     * 根据商品DTO 查询商品集合
     */
    @GetMapping("/sellerGetEsGoodsList")
    DubboPageResult<EsGoodsDO> sellerGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO);

    /**
     * 店铺关闭时（店铺商品全部下架）g根据店铺ID
     */
    @PostMapping("/adminUnderEsGoods")
    DubboResult<EsGoodsDO> adminUnderEsGoods(Long shopId);

    /**
     * 修改商品浏览数量
     */
    @PostMapping("/updateViewCount")
    DubboResult<EsGoodsDO> updateViewCount(Long goodsId);

    /**
     * 修改商品评论数量
     * @param goodsId
     * @return
     */
    @PostMapping("/updateCommenCount")
    DubboResult<EsGoodsDO> updateCommenCount(Long goodsId);

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/buyGetEsGoods")
    DubboResult<EsGoodsDO> buyGetEsGoods(Long id);
}
