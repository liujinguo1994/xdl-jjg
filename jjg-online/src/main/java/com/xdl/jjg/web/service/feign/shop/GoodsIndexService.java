package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsPcSelectSearchCO;
import com.jjg.shop.model.co.EsSelectSearchCO;
import com.jjg.shop.model.domain.EsGoodsIndexDO;
import com.jjg.shop.model.dto.EsGoodsIndexDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface GoodsIndexService {

    /**
     * 查询商品索引
     * @param esGoodsIndexDTO

     * @return
     */
    @GetMapping("/getEsGoodsIndex")
    DubboPageResult<EsGoodsIndexDO> getEsGoodsIndex(@RequestBody EsGoodsIndexDTO esGoodsIndexDTO, @RequestParam("pageSiz") int pageSiz, @RequestParam("pageNum") int pageNum);



    /**
     * 搜索选择器
     * @param goodsSearch
     * @return
     */
    @GetMapping("/getSelector")
    DubboResult<EsSelectSearchCO> getSelector(@RequestBody EsGoodsIndexDTO goodsSearch);
    @GetMapping("/getPcSelector")
    DubboPageResult<EsPcSelectSearchCO> getPcSelector(@RequestBody EsGoodsIndexDTO goodsSearch);
}
