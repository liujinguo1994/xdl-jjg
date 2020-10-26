package com.xdl.jjg.web.service.impl;/**
 * @author wangaf
 * @date 2020/3/25 9:07
 **/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsLfcAreaDO;
import com.shopx.trade.api.service.IEsLfcAreaService;
import com.shopx.trade.dao.entity.EsLfcArea;
import com.shopx.trade.dao.mapper.EsLfcAreaMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 @Author wangaf 826988665@qq.com
 @Date 2020/3/25
 @Version V1.0
 **/
@Service(version = "${dubbo.application.version}", interfaceClass = IEsLfcAreaService.class, timeout = 50000)
public class EsLfcAreaServiceImpl implements IEsLfcAreaService {
    private static Logger logger = LoggerFactory.getLogger(EsLfcAreaServiceImpl.class);
    @Autowired
    private EsLfcAreaMapper esLfcAreaMapper;
    @Override
    public DubboResult<EsLfcAreaDO> getByAreaId(String areaId) {
        try{
            QueryWrapper<EsLfcArea> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLfcArea::getAreaId,areaId);
            EsLfcArea esLfcArea = esLfcAreaMapper.selectOne(queryWrapper);
            return  DubboResult.success(esLfcArea);
        } catch (ArgumentException e){
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable ae) {
            logger.error("失败", ae);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
