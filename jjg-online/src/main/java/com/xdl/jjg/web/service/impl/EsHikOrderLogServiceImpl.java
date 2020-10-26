package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.dto.EsHikOrderLogDTO;
import com.shopx.trade.api.service.IEsHikOrderLogService;
import com.shopx.trade.dao.entity.EsHikOrderLog;
import com.shopx.trade.dao.mapper.EsHikOrderLogMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * 海康异常反馈 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsHikOrderLogService.class, timeout = 50000)
public class EsHikOrderLogServiceImpl extends ServiceImpl<EsHikOrderLogMapper, EsHikOrderLog> implements IEsHikOrderLogService {

    private static Logger logger = LoggerFactory.getLogger(EsHikOrderLogServiceImpl.class);

    @Autowired
    private EsHikOrderLogMapper logMapper;

    /**
     * 插入数据
     * @param hikOrderLogDTO DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/05/09 10:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsHikExceptionOrder>
     */
    @Override
    public DubboResult insertHikOrderLog(EsHikOrderLogDTO hikOrderLogDTO) {

        try {
            EsHikOrderLog hikOrderLog = new EsHikOrderLog();
            BeanUtil.copyProperties(hikOrderLogDTO, hikOrderLog);
            this.logMapper.insert(hikOrderLog);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }



}
