package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.dto.EsHikOrderLogDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsHikOrderLog;
import com.xdl.jjg.mapper.EsHikOrderLogMapper;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsHikOrderLogService;
import org.springframework.stereotype.Service;
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
@Service
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
