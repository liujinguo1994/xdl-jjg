package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsOrderLogDO;
import com.jjg.trade.model.dto.EsOrderLogDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsOrderLog;
import com.xdl.jjg.mapper.EsOrderLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsOrderLogService;
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
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsOrderLogService.class, timeout = 50000)
public class EsOrderLogServiceImpl extends ServiceImpl<EsOrderLogMapper, EsOrderLog> implements IEsOrderLogService {

    private static Logger logger = LoggerFactory.getLogger(EsOrderLogServiceImpl.class);

    @Autowired
    private EsOrderLogMapper orderLogMapper;

    /**
     * 插入数据
     * @param orderLogDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsOrderLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertOrderLog(EsOrderLogDTO orderLogDTO) {
        try {
            if (orderLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsOrderLog orderLog = new EsOrderLog();
            BeanUtil.copyProperties(orderLogDTO, orderLog);
            this.orderLogMapper.insert(orderLog);
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
     * @param orderLogDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsOrderLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateOrderLog(EsOrderLogDTO orderLogDTO) {
        try {
            if (orderLogDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOrderLog orderLog = new EsOrderLog();
            BeanUtil.copyProperties(orderLogDTO, orderLog);
            QueryWrapper<EsOrderLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderLog::getId, orderLogDTO.getId());
            this.orderLogMapper.update(orderLog, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsOrderLogDO>
     */
    @Override
    public DubboResult<EsOrderLogDO> getOrderLog(Long id) {
        try {
            QueryWrapper<EsOrderLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderLog::getId, id);
            EsOrderLog orderLog = this.orderLogMapper.selectOne(queryWrapper);
            EsOrderLogDO orderLogDO = new EsOrderLogDO();
            if (orderLog == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(orderLog, orderLogDO);
            return DubboResult.success(orderLogDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param orderLogDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsOrderLogDO>
     */
    @Override
    public DubboPageResult<EsOrderLogDO> getOrderLogList(EsOrderLogDTO orderLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsOrderLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsOrderLog> page = new Page<>(pageNum, pageSize);
            IPage<EsOrderLog> iPage = this.page(page, queryWrapper);
            List<EsOrderLogDO> orderLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                orderLogDOList = iPage.getRecords().stream().map(orderLog -> {
                    EsOrderLogDO orderLogDO = new EsOrderLogDO();
                    BeanUtil.copyProperties(orderLog, orderLogDO);
                    return orderLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(orderLogDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsOrderLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteOrderLog(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsOrderLog> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsOrderLog::getId, id);
            this.orderLogMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
