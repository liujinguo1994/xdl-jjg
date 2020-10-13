package com.xdl.jjg.web.service.Impl;

import com.shopx.common.model.result.DubboResult;
import com.shopx.goods.api.model.domain.EsGoodsTotalStatisticsDO;
import com.shopx.goods.api.service.IEsGoodsTotalStatisticsService;
import com.shopx.goods.dao.mapper.EsGoodsTotalStatisticsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsTotalStatisticsService.class, timeout = 50000)
public class EsGoodsTotalStatisticsServiceImpl implements IEsGoodsTotalStatisticsService {
    @Autowired
    private EsGoodsTotalStatisticsMapper esGoodsTotalStatisticsMapper;
    @Override
    public DubboResult<EsGoodsTotalStatisticsDO> adminGoodsTotalStatistics() {
        return DubboResult.success(esGoodsTotalStatisticsMapper.adminGoodsTotalStatistics());
    }
}
