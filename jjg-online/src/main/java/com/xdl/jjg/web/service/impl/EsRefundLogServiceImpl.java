package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsRefundLogDO;
import com.jjg.trade.model.dto.EsRefundLogDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsRefundLog;
import com.xdl.jjg.mapper.EsRefundLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRefundLogService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
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
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service
public class EsRefundLogServiceImpl extends ServiceImpl<EsRefundLogMapper, EsRefundLog> implements IEsRefundLogService {

    private static Logger logger = LoggerFactory.getLogger(EsRefundLogServiceImpl.class);

    @Autowired
    private EsRefundLogMapper refundLogMapper;

    /**
     * 插入数据
     *
     * @param refundLogDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertRefundLog(EsRefundLogDTO refundLogDTO) {
        try {
            if (refundLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsRefundLog refundLog = new EsRefundLog();
            BeanUtil.copyProperties(refundLogDTO, refundLog);
            this.refundLogMapper.insert(refundLog);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param refundLogDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRefundLog(EsRefundLogDTO refundLogDTO) {
        try {
            if (refundLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsRefundLog refundLog = new EsRefundLog();
            BeanUtil.copyProperties(refundLogDTO, refundLog);
            QueryWrapper<EsRefundLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefundLog::getId, refundLogDTO.getId());
            this.refundLogMapper.update(refundLog, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    @Override
    public DubboResult getRefundLog(Long id) {
        try {
            QueryWrapper<EsRefundLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRefundLog::getId, id);
            EsRefundLog refundLog = this.refundLogMapper.selectOne(queryWrapper);
            EsRefundLogDO refundLogDO = new EsRefundLogDO();
            if (refundLog == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(refundLog, refundLogDO);
            return DubboResult.success(refundLogDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param refundLogDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundLogDO>
     */
    @Override
    public DubboPageResult getRefundLogList(EsRefundLogDTO refundLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsRefundLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsRefundLog> page = new Page<>(pageNum, pageSize);
            IPage<EsRefundLog> iPage = this.page(page, queryWrapper);
            List<EsRefundLogDO> refundLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                refundLogDOList = iPage.getRecords().stream().map(refundLog -> {
                    EsRefundLogDO refundLogDO = new EsRefundLogDO();
                    BeanUtil.copyProperties(refundLog, refundLogDO);
                    return refundLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(refundLogDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRefundLog(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsRefundLog> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsRefundLog::getId, id);
            this.refundLogMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
