package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.domain.EsSellerGoodsSkuDO;
import com.jjg.shop.model.dto.EsGoodsSkuDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jjg-shop")
public interface GoodsSkuService {

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSku(@RequestParam("id") Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSkuEnable(@RequestParam("id") Long id);

    /**
     * 卖家中心 更新商品SKU信息
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuGift(@RequestBody List<EsGoodsSkuDTO> goodsSkuDTO, @RequestParam("shopId") Long shopId, @RequestParam("goodsId") Long goodsId);

    /**
     * 根据DTO 获取商品SKU信息集合
     * @param goodsId
     * @return
     */
    DubboPageResult<EsSellerGoodsSkuDO> getGoodsSkuList(@RequestParam("goodsId") Long goodsId);

    DubboPageResult<EsGoodsSkuDO> getSkuByIds(@RequestParam("id") Long[] id);
}
