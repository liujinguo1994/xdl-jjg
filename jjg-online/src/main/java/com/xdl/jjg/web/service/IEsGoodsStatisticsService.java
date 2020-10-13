package com.xdl.jjg.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.model.domain.EsGoodsAveragePriceDO;
import com.xdl.jjg.model.domain.EsGoodsHotSellDO;
import com.xdl.jjg.model.domain.EsGoodsPaymentConversionRateDO;
import com.xdl.jjg.model.domain.EsGoodsSalesDetailDO;
import com.xdl.jjg.model.dto.EsGoodsAveragePriceDTO;
import com.xdl.jjg.model.dto.EsGoodsHotSellDTO;
import com.xdl.jjg.model.dto.EsGoodsPaymentConversionRateDTO;
import com.xdl.jjg.model.dto.EsGoodsSalesDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 商品统计接口
 * </p>
 *
 * @author KL 1137429161@qq.com
 * @since 2020-5-14 11:18:55
 */
public interface IEsGoodsStatisticsService {
    /**
     * 商品销售详情Page
     */
    DubboPageResult<EsGoodsSalesDetailDO> getGoodsSalesDetailList(EsGoodsSalesDetailDTO esGoodsSalesDetailDTO, Page page);

    /**
     * 热销排行
     */
    DubboResult<IPage<EsGoodsHotSellDO>> getGoodsHotSell(EsGoodsHotSellDTO esGoodsHotSellDTO, Page page);

    /**
     * 付款转换率
     */
    DubboResult<IPage<EsGoodsPaymentConversionRateDO>> getGoodsPaymentConversionRate(EsGoodsPaymentConversionRateDTO esGoodsPaymentConversionRateDTO, Page page);

    /**
     * 商品客单价排行
     */
    DubboResult<IPage<EsGoodsAveragePriceDO>> getGoodsAveragePrice(EsGoodsAveragePriceDTO esGoodsAveragePriceDTO, Page page);
}
