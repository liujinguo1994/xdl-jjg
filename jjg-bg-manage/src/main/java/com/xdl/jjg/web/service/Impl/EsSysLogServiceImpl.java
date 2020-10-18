package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsSysLogDO;
import com.jjg.system.model.dto.EsSysLogDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSysLog;
import com.xdl.jjg.mapper.EsSysLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsSysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统操作日志 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSysLogServiceImpl extends ServiceImpl<EsSysLogMapper, EsSysLog> implements IEsSysLogService {

    private static Logger logger = LoggerFactory.getLogger(EsSysLogServiceImpl.class);

    @Autowired
    private EsSysLogMapper sysLogMapper;

    /**
     * 插入系统操作日志数据
     *
     * @param sysLogDTO 系统操作日志DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSysLog(EsSysLogDTO sysLogDTO) {
        try {
            if (sysLogDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSysLog sysLog = new EsSysLog();
            BeanUtil.copyProperties(sysLogDTO, sysLog);
            this.sysLogMapper.insert(sysLog);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("系统操作日志新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("系统操作日志新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新系统操作日志数据
     *
     * @param sysLogDTO 系统操作日志DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSysLog(EsSysLogDTO sysLogDTO) {
        try {
            if (sysLogDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSysLog sysLog = new EsSysLog();
            BeanUtil.copyProperties(sysLogDTO, sysLog);
            QueryWrapper<EsSysLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSysLog::getId, sysLogDTO.getId());
            this.sysLogMapper.update(sysLog, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("系统操作日志更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("系统操作日志更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取系统操作日志详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    @Override
    public DubboResult<EsSysLogDO> getSysLog(Long id) {
        try {
            QueryWrapper<EsSysLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSysLog::getId, id);
            EsSysLog sysLog = this.sysLogMapper.selectOne(queryWrapper);
            EsSysLogDO sysLogDO = new EsSysLogDO();
            if (sysLog == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(sysLog, sysLogDO);
            return DubboResult.success(sysLogDO);
        } catch (ArgumentException ae) {
            logger.error("系统操作日志查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("系统操作日志查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询系统操作日志列表
     *
     * @param sysLogDTO 系统操作日志DTO
     * @param pageSize  页码
     * @param pageNum   页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsSysLogDO>
     */
    @Override
    public DubboPageResult<EsSysLogDO> getSysLogList(EsSysLogDTO sysLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSysLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSysLog> page = new Page<>(pageNum, pageSize);
            IPage<EsSysLog> iPage = this.page(page, queryWrapper);
            List<EsSysLogDO> sysLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                sysLogDOList = iPage.getRecords().stream().map(sysLog -> {
                    EsSysLogDO sysLogDO = new EsSysLogDO();
                    BeanUtil.copyProperties(sysLog, sysLogDO);
                    return sysLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(sysLogDOList);
        } catch (ArgumentException ae) {
            logger.error("系统操作日志分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("系统操作日志分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除系统操作日志数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSysLog(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSysLog> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSysLog::getId, id);
            this.sysLogMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("系统操作日志删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("系统操作日志删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
