package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsGoodsAveragePriceDO;
import com.shopx.trade.api.model.domain.EsGoodsHotSellDO;
import com.shopx.trade.api.model.domain.EsGoodsPaymentConversionRateDO;
import com.shopx.trade.api.model.domain.EsGoodsSalesDetailDO;
import com.shopx.trade.api.model.domain.dto.EsGoodsAveragePriceDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsHotSellDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsPaymentConversionRateDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsSalesDetailDTO;
import com.shopx.trade.api.service.IEsGoodsStatisticsService;
import com.shopx.trade.dao.mapper.EsGoodsStatisticsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 商品统计实现类
 * </p>
 *
 * @author KL 1137429161@qq.com
 * @since 2020-5-14 11:18:55
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsStatisticsService.class, timeout = 50000)
public class EsGoodsStatisticsServiceImpl implements IEsGoodsStatisticsService {
    @Autowired
    private EsGoodsStatisticsMapper esGoodsStatisticsMapper;

    @Override
    public DubboPageResult<EsGoodsSalesDetailDO> getGoodsSalesDetailList(EsGoodsSalesDetailDTO esGoodsSalesDetailDTO, Page page) {
        List<EsGoodsSalesDetailDO> goodsSalesDetailList = esGoodsStatisticsMapper.getGoodsSalesDetailList(page, esGoodsSalesDetailDTO);
        return DubboPageResult.success(goodsSalesDetailList);
    }

    @Override
    public DubboResult<IPage<EsGoodsHotSellDO>> getGoodsHotSell(EsGoodsHotSellDTO esGoodsHotSellDTO, Page page) {
        IPage<EsGoodsHotSellDO> goodsHotSell = esGoodsStatisticsMapper.getGoodsHotSell(page, esGoodsHotSellDTO);
        return DubboPageResult.success(goodsHotSell);
    }

    @Override
    public DubboResult<IPage<EsGoodsPaymentConversionRateDO>> getGoodsPaymentConversionRate(EsGoodsPaymentConversionRateDTO esGoodsPaymentConversionRateDTO, Page page) {
        IPage<EsGoodsPaymentConversionRateDO> goodsPaymentConversionRate = esGoodsStatisticsMapper.getGoodsPaymentConversionRate(page, esGoodsPaymentConversionRateDTO);
        return DubboPageResult.success(goodsPaymentConversionRate);
    }

    @Override
    public DubboResult<IPage<EsGoodsAveragePriceDO>> getGoodsAveragePrice(EsGoodsAveragePriceDTO esGoodsAveragePriceDTO, Page page) {
        IPage<EsGoodsAveragePriceDO> goodsAveragePrice = esGoodsStatisticsMapper.getGoodsAveragePrice(page, esGoodsAveragePriceDTO);
        return DubboPageResult.success(goodsAveragePrice);
    }
}
