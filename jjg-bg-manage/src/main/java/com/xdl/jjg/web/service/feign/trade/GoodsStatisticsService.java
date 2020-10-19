package com.xdl.jjg.web.service.feign.trade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jjg.trade.model.domain.EsGoodsAveragePriceDO;
import com.jjg.trade.model.domain.EsGoodsHotSellDO;
import com.jjg.trade.model.domain.EsGoodsPaymentConversionRateDO;
import com.jjg.trade.model.domain.EsGoodsSalesDetailDO;
import com.jjg.trade.model.dto.EsGoodsAveragePriceDTO;
import com.jjg.trade.model.dto.EsGoodsHotSellDTO;
import com.jjg.trade.model.dto.EsGoodsPaymentConversionRateDTO;
import com.jjg.trade.model.dto.EsGoodsSalesDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface GoodsStatisticsService {

    /**
     * 商品销售详情Page
     */
    @GetMapping("/getGoodsSalesDetailList")
    DubboPageResult<EsGoodsSalesDetailDO> getGoodsSalesDetailList(@RequestBody EsGoodsSalesDetailDTO esGoodsSalesDetailDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 热销排行
     */
    @GetMapping("/getGoodsHotSell")
    DubboResult<IPage<EsGoodsHotSellDO>> getGoodsHotSell(@RequestBody EsGoodsHotSellDTO esGoodsHotSellDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 付款转换率
     */
    @GetMapping("/getGoodsPaymentConversionRate")
    DubboResult<IPage<EsGoodsPaymentConversionRateDO>> getGoodsPaymentConversionRate(@RequestBody EsGoodsPaymentConversionRateDTO esGoodsPaymentConversionRateDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum")int pageNum);

    /**
     * 商品客单价排行
     */
    @GetMapping("/getGoodsAveragePrice")
    DubboResult<IPage<EsGoodsAveragePriceDO>> getGoodsAveragePrice(@RequestBody EsGoodsAveragePriceDTO esGoodsAveragePriceDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
}
