package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsBuyerGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "jjg-shop")
public interface GoodsService {


    /**
     * 为你推荐
     * @return
     */
    @GetMapping("/getRecommendForYouGoods")
    DubboPageResult<EsGoodsDO> getRecommendForYouGoods(@RequestParam("goodsNames") String[] goodsNames);
    /**
     * 根据销量排行获取分类前五条数据
     * @return
     */
    @GetMapping("/getRecommendGoods")
    DubboPageResult<EsBuyerGoodsDO> getRecommendGoods();
    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    @GetMapping("/getEsGoods")
    DubboResult<EsGoodsCO> getEsGoods(@RequestParam("id") Long id);

    /**
     * 根据商品ID获取商品信息
     * @param ids
     * @return
     */
    @GetMapping("/getEsGoods")
    DubboPageResult<EsGoodsDO> getEsGoods(@RequestParam("ids") Long[] ids);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    @GetMapping("/getEsBuyerGoods")
    DubboResult<EsGoodsCO> getEsBuyerGoods(@RequestParam("id") Long id);

@PostMapping("/insertGiftsEsGoods")
    DubboResult<EsGoodsDO>  insertGiftsEsGoods(@RequestBody EsGoodsDTO esGoodsDTO);

@DeleteMapping("/deleteEsGoods")
    DubboResult<EsGoodsDO>  deleteEsGoods(@RequestParam("id") Long[] id);


    @GetMapping("/getGoodsList")
    DubboPageResult<EsGoodsDO> getGoodsList(@RequestParam("templateId") Long templateId);

    @GetMapping("/buyCheckGoods")
    DubboResult<Map<String,String>> buyCheckGoods(@RequestParam("goodsIds") Long[] goodsIds);

    /**
     * 根据商品IDList 查询猜你喜欢 商品
     */
    @GetMapping("/getGuessYouLike")
    DubboPageResult<EsGoodsDO> getGuessYouLike(@RequestParam("goodsIds") Long[] goodsIds, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    @GetMapping("/buyerGetGoodsByCategoryId")
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(@RequestParam("category") Long category, @RequestParam("shopId") Long shopId,@RequestParam("goodsId")  Long goodsId,@RequestParam("pageNum")  int pageNum, @RequestParam("pageSize") int pageSize);

}
