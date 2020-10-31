package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsPcSelectSearchCO;
import com.jjg.shop.model.co.EsSelectSearchCO;
import com.jjg.shop.model.domain.EsGoodsIndexDO;
import com.jjg.shop.model.dto.EsGoodsIndexDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface GoodsIndexService {

    /**
     * 查询商品索引
     * @param esGoodsIndexDTO

     * @return
     */
    DubboPageResult<EsGoodsIndexDO> getEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO, int pageSiz, int pageNum);



    /**
     * 搜索选择器
     * @param goodsSearch
     * @return
     */
    DubboResult<EsSelectSearchCO> getSelector(EsGoodsIndexDTO goodsSearch);
    DubboPageResult<EsPcSelectSearchCO> getPcSelector(EsGoodsIndexDTO goodsSearch);
}
