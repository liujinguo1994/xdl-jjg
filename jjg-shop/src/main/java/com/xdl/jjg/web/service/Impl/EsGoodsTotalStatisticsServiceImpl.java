package com.xdl.jjg.web.service.Impl;


import com.jjg.shop.model.domain.EsGoodsTotalStatisticsDO;
import com.xdl.jjg.mapper.EsGoodsTotalStatisticsMapper;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsGoodsTotalStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsGoodsTotalStatisticsServiceImpl implements IEsGoodsTotalStatisticsService {
    @Autowired
    private EsGoodsTotalStatisticsMapper esGoodsTotalStatisticsMapper;
    @Override
    public DubboResult<EsGoodsTotalStatisticsDO> adminGoodsTotalStatistics() {
        return DubboResult.success(esGoodsTotalStatisticsMapper.adminGoodsTotalStatistics());
    }
}
