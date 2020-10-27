package com.xdl.jjg.web.service.impl;/**
 * @author wangaf
 * @date 2020/3/25 9:07
 **/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.trade.model.domain.EsLfcAreaDO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsLfcArea;
import com.xdl.jjg.mapper.EsLfcAreaMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsLfcAreaService;
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
