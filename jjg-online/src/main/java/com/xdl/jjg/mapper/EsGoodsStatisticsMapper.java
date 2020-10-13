package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shopx.trade.api.model.domain.EsGoodsAveragePriceDO;
import com.shopx.trade.api.model.domain.EsGoodsHotSellDO;
import com.shopx.trade.api.model.domain.EsGoodsPaymentConversionRateDO;
import com.shopx.trade.api.model.domain.EsGoodsSalesDetailDO;
import com.shopx.trade.api.model.domain.dto.EsGoodsAveragePriceDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsHotSellDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsPaymentConversionRateDTO;
import com.shopx.trade.api.model.domain.dto.EsGoodsSalesDetailDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品统计Mapper
 */
public interface EsGoodsStatisticsMapper {
    /**
     * 商品销售明细
     * @param page 分页
     * @param goodsSalesDetailDTO 查询条件
     * @return
     */
    List<EsGoodsSalesDetailDO> getGoodsSalesDetailList(Page page, @Param("goodsSalesDetailDTO") EsGoodsSalesDetailDTO goodsSalesDetailDTO);

    /**
     * 商品热销榜
     */
    IPage<EsGoodsHotSellDO> getGoodsHotSell(Page page, @Param("goodsHotSell") EsGoodsHotSellDTO goodsHotSell);

    /**
     * 商品付款转换率
     */
    IPage<EsGoodsPaymentConversionRateDO> getGoodsPaymentConversionRate(Page page, @Param("goodsPaymentConversionRate") EsGoodsPaymentConversionRateDTO goodsPaymentConversionRateDTO);

    /**
     * 商品客单价排行
     */
    IPage<EsGoodsAveragePriceDO> getGoodsAveragePrice(Page page, @Param("goodsAveragePrice") EsGoodsAveragePriceDTO goodsAveragePriceDTO);
}
