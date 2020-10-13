package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsTotalStatisticsDO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 商品总数统计
 * <br/>
 * @author KThirty
 * @since 2020/5/27 14:41
 */
public interface IEsGoodsTotalStatisticsService {
    /**
     * 管理员使用商品总数统计
     * <br/>
     * @return com.shopx.common.model.result.DubboResult<com.shopx.goods.api.model.domain.EsGoodsTotalStatisticsDO>
     * @author KThirty
     * @since 2020/5/27 14:41
     */
    DubboResult<EsGoodsTotalStatisticsDO> adminGoodsTotalStatistics();
}
