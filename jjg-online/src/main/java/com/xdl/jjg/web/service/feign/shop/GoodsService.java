package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.util.Map;

@FeignClient(value = "jjg-shop")
public interface GoodsService {


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

}
