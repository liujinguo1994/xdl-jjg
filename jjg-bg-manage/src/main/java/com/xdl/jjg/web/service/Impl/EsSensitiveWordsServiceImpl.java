package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsSensitiveWordsDO;
import com.jjg.system.model.dto.EsSensitiveWordsDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSensitiveWords;
import com.xdl.jjg.mapper.EsSensitiveWordsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsSensitiveWordsService;
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
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSensitiveWordsServiceImpl extends ServiceImpl<EsSensitiveWordsMapper, EsSensitiveWords> implements IEsSensitiveWordsService {

    private static Logger logger = LoggerFactory.getLogger(EsSensitiveWordsServiceImpl.class);

    @Autowired
    private EsSensitiveWordsMapper sensitiveWordsMapper;

    /**
     * 插入数据
     *
     * @param sensitiveWordsDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSensitiveWords(EsSensitiveWordsDTO sensitiveWordsDTO) {
        try {
            if (sensitiveWordsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSensitiveWords sensitiveWords = new EsSensitiveWords();
            BeanUtil.copyProperties(sensitiveWordsDTO, sensitiveWords);
            this.sensitiveWordsMapper.insert(sensitiveWords);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param sensitiveWordsDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSensitiveWords(EsSensitiveWordsDTO sensitiveWordsDTO) {
        try {
            if (sensitiveWordsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSensitiveWords sensitiveWords = new EsSensitiveWords();
            BeanUtil.copyProperties(sensitiveWordsDTO, sensitiveWords);
            QueryWrapper<EsSensitiveWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSensitiveWords::getId, sensitiveWordsDTO.getId());
            this.sensitiveWordsMapper.update(sensitiveWords, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     */
    @Override
    public DubboResult<EsSensitiveWordsDO> getSensitiveWords(Long id) {
        try {
            QueryWrapper<EsSensitiveWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSensitiveWords::getId, id);
            EsSensitiveWords sensitiveWords = this.sensitiveWordsMapper.selectOne(queryWrapper);
            EsSensitiveWordsDO sensitiveWordsDO = new EsSensitiveWordsDO();
            if (sensitiveWords == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(sensitiveWords, sensitiveWordsDO);
            return DubboResult.success(sensitiveWordsDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param sensitiveWordsDTO DTO
     * @param pageSize          页码
     * @param pageNum           页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsSensitiveWordsDO>
     */
    @Override
    public DubboPageResult<EsSensitiveWordsDO> getSensitiveWordsList(EsSensitiveWordsDTO sensitiveWordsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSensitiveWords> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSensitiveWords> page = new Page<>(pageNum, pageSize);
            IPage<EsSensitiveWords> iPage = this.page(page, queryWrapper);
            List<EsSensitiveWordsDO> sensitiveWordsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                sensitiveWordsDOList = iPage.getRecords().stream().map(sensitiveWords -> {
                    EsSensitiveWordsDO sensitiveWordsDO = new EsSensitiveWordsDO();
                    BeanUtil.copyProperties(sensitiveWords, sensitiveWordsDO);
                    return sensitiveWordsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(sensitiveWordsDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSensitiveWords(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSensitiveWords> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSensitiveWords::getId, id);
            this.sensitiveWordsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
