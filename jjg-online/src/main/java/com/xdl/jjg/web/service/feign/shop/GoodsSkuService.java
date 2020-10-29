package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.domain.EsSellerGoodsSkuDO;
import com.jjg.shop.model.dto.EsGoodsSkuDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

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
    DubboResult<EsGoodsSkuCO> getGoodsSku(Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSkuEnable(Long id);

    /**
     * 卖家中心 更新商品SKU信息
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuGift(List<EsGoodsSkuDTO> goodsSkuDTO, Long shopId, Long goodsId);

    /**
     * 根据DTO 获取商品SKU信息集合
     * @param goodsId
     * @return
     */
    DubboPageResult<EsSellerGoodsSkuDO> getGoodsSkuList(Long goodsId);

    DubboPageResult<EsGoodsSkuDO> getSkuByIds(Long[] id);
}
