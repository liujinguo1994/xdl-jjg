package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.trade.model.domain.EsGoodsAveragePriceDO;
import com.jjg.trade.model.domain.EsGoodsHotSellDO;
import com.jjg.trade.model.domain.EsGoodsPaymentConversionRateDO;
import com.jjg.trade.model.domain.EsGoodsSalesDetailDO;
import com.jjg.trade.model.dto.EsGoodsAveragePriceDTO;
import com.jjg.trade.model.dto.EsGoodsHotSellDTO;
import com.jjg.trade.model.dto.EsGoodsPaymentConversionRateDTO;
import com.jjg.trade.model.dto.EsGoodsSalesDetailDTO;
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
