package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsBuyerGoodsDO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.util.Map;

@FeignClient(value = "jjg-shop")
public interface GoodsService {


    /**
     * 为你推荐
     * @return
     */
    DubboPageResult<EsGoodsDO> getRecommendForYouGoods(String[] goodsNames);
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
    DubboResult<EsGoodsCO> getEsGoods(Long id);

    /**
     * 根据商品ID获取商品信息
     * @param ids
     * @return
     */
    DubboPageResult<EsGoodsDO> getEsGoods(Long[] ids);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsBuyerGoods(Long id);


    DubboResult<EsGoodsDO>  insertGiftsEsGoods(EsGoodsDTO esGoodsDTO);


    DubboResult<EsGoodsDO>  deleteEsGoods(Long[] id);



    DubboPageResult<EsGoodsDO> getGoodsList(Long templateId);


    DubboResult<Map<String,String>> buyCheckGoods(Long[] goodsIds);

    /**
     * 根据商品IDList 查询猜你喜欢 商品
     */
    DubboPageResult<EsGoodsDO> getGuessYouLike(Long[] goodsIds, int pageNum, int pageSize);

    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(Long category, Long shopId, Long goodsId, int pageNum, int pageSize);

}
