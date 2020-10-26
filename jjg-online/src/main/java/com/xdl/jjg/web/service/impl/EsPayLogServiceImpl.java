package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsPayLogDO;
import com.jjg.trade.model.dto.EsPayLogDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsPayLog;
import com.xdl.jjg.mapper.EsPayLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsPayLogService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 支付记录日志表 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsPayLogService.class, timeout = 50000)
public class EsPayLogServiceImpl extends ServiceImpl<EsPayLogMapper, EsPayLog> implements IEsPayLogService {

    private static Logger logger = LoggerFactory.getLogger(EsPayLogServiceImpl.class);

    @Autowired
    private EsPayLogMapper payLogMapper;

    /**
     * 插入支付记录日志表数据
     *
     * @param payLogDTO 支付记录日志表DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsPayLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertPayLog(EsPayLogDTO payLogDTO) {
        try {
            if (payLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsPayLog payLog = new EsPayLog();
            BeanUtil.copyProperties(payLogDTO, payLog);
            this.payLogMapper.insert(payLog);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("支付记录日志表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新支付记录日志表数据
     *
     * @param payLogDTO 支付记录日志表DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsPayLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updatePayLog(EsPayLogDTO payLogDTO) {
        try {
            if (payLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsPayLog payLog = new EsPayLog();
            BeanUtil.copyProperties(payLogDTO, payLog);
            QueryWrapper<EsPayLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPayLog::getId, payLogDTO.getId());
            this.payLogMapper.update(payLog, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("支付记录日志表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取支付记录日志表详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsPayLogDO>
     */
    @Override
    public DubboResult getPayLog(Long id) {
        try {
            QueryWrapper<EsPayLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPayLog::getId, id);
            EsPayLog payLog = this.payLogMapper.selectOne(queryWrapper);
            EsPayLogDO payLogDO = new EsPayLogDO();
            if (payLog == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(payLog, payLogDO);
            return DubboResult.success(payLogDO);
        } catch (Throwable th) {
            logger.error("支付记录日志表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询支付记录日志表列表
     *
     * @param payLogDTO 支付记录日志表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsPayLogDO>
     */
    @Override
    public DubboPageResult getPayLogList(EsPayLogDTO payLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsPayLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsPayLog> page = new Page<>(pageNum, pageSize);
            IPage<EsPayLog> iPage = this.page(page, queryWrapper);
            List<EsPayLogDO> payLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                payLogDOList = iPage.getRecords().stream().map(payLog -> {
                    EsPayLogDO payLogDO = new EsPayLogDO();
                    BeanUtil.copyProperties(payLog, payLogDO);
                    return payLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(payLogDOList);
        } catch (Throwable th) {
            logger.error("支付记录日志表查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除支付记录日志表数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsPayLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deletePayLog(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsPayLog> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsPayLog::getId, id);
            this.payLogMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("支付记录日志表查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
