package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsBuyerGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "jjg-shop")
public interface GoodsService {


    /**
     * 为你推荐
     * @return
     */
    DubboPageResult<EsGoodsDO> getRecommendForYouGoods(@RequestParam("goodsNames") String[] goodsNames);
    /**
     * 根据销量排行获取分类前五条数据
     * @return
     */
    DubboPageResult<EsBuyerGoodsDO> getRecommendGoods();
    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsGoods(@RequestParam("id") Long id);

    /**
     * 根据商品ID获取商品信息
     * @param ids
     * @return
     */
    DubboPageResult<EsGoodsDO> getEsGoods(@RequestParam("ids") Long[] ids);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsBuyerGoods(@RequestParam("id") Long id);


    DubboResult<EsGoodsDO>  insertGiftsEsGoods(@RequestBody EsGoodsDTO esGoodsDTO);


    DubboResult<EsGoodsDO>  deleteEsGoods(@RequestParam("id") Long[] id);



    DubboPageResult<EsGoodsDO> getGoodsList(@RequestParam("templateId") Long templateId);


    DubboResult<Map<String,String>> buyCheckGoods(@RequestParam("goodsIds") Long[] goodsIds);

    /**
     * 根据商品IDList 查询猜你喜欢 商品
     */
    DubboPageResult<EsGoodsDO> getGuessYouLike(@RequestParam("goodsIds") Long[] goodsIds, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(@RequestParam("category") Long category, @RequestParam("shopId") Long shopId,@RequestParam("goodsId")  Long goodsId,@RequestParam("pageNum")  int pageNum, @RequestParam("pageSize") int pageSize);

}
