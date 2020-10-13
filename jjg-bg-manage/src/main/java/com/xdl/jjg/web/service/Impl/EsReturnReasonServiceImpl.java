package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsReturnReason;
import com.xdl.jjg.mapper.EsReturnReasonMapper;
import com.xdl.jjg.model.domain.EsReturnReasonDO;
import com.xdl.jjg.model.dto.EsReturnReasonDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsReturnReasonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 售后申请原因 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
@Service
public class EsReturnReasonServiceImpl extends ServiceImpl<EsReturnReasonMapper, EsReturnReason> implements IEsReturnReasonService {

    private static Logger logger = LoggerFactory.getLogger(EsReturnReasonServiceImpl.class);

    @Autowired
    private EsReturnReasonMapper returnReasonMapper;

    /**
     * 插入售后申请原因数据
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-12-16
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertReturnReason(EsReturnReasonDTO returnReasonDTO) {
        try {
            if (returnReasonDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsReturnReason returnReason = new EsReturnReason();
            BeanUtil.copyProperties(returnReasonDTO, returnReason);
            this.returnReasonMapper.insert(returnReason);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后申请原因新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("售后申请原因新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新售后申请原因数据
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-12-16
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateReturnReason(EsReturnReasonDTO returnReasonDTO) {
        try {
            if (returnReasonDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsReturnReason returnReason = new EsReturnReason();
            BeanUtil.copyProperties(returnReasonDTO, returnReason);
            QueryWrapper<EsReturnReason> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReturnReason::getId, returnReasonDTO.getId());
            this.returnReasonMapper.update(returnReason, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后申请原因更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后申请原因更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取售后申请原因详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-12-16
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     */
    @Override
    public DubboResult<EsReturnReasonDO> getReturnReason(Long id) {
        try {
            QueryWrapper<EsReturnReason> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReturnReason::getId, id);
            EsReturnReason returnReason = this.returnReasonMapper.selectOne(queryWrapper);
            EsReturnReasonDO returnReasonDO = new EsReturnReasonDO();
            if (returnReason == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(returnReason, returnReasonDO);
            return DubboResult.success(returnReasonDO);
        } catch (ArgumentException ae) {
            logger.error("售后申请原因查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后申请原因查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询售后申请原因列表
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-12-16
     * @return: com.shopx.common.model.result.DubboPageResult<EsReturnReasonDO>
     */
    @Override
    public DubboPageResult<EsReturnReasonDO> getReturnReasonList(EsReturnReasonDTO returnReasonDTO, int pageSize, int pageNum) {
        QueryWrapper<EsReturnReason> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsReturnReason> page = new Page<>(pageNum, pageSize);
            IPage<EsReturnReason> iPage = this.page(page, queryWrapper);
            List<EsReturnReasonDO> doList = BeanUtil.copyList(iPage.getRecords(), EsReturnReasonDO.class);
            return DubboPageResult.success(iPage.getTotal(), doList);
        } catch (ArgumentException ae) {
            logger.error("售后申请原因分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后申请原因分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除售后申请原因数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-12-16
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteReturnReason(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsReturnReason> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsReturnReason::getId, id);
            this.returnReasonMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("售后申请原因删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("售后申请原因删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据售后类型获取原因列表
    @Override
    public DubboPageResult<EsReturnReasonDO> getByType(String refundType) {
        try {
            if (StringUtil.isEmpty(refundType)) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), "参数错误");
            }
            QueryWrapper<EsReturnReason> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsReturnReason::getRefundType, refundType);
            List<EsReturnReason> reasonList = returnReasonMapper.selectList(queryWrapper);
            List<EsReturnReasonDO> doList = BeanUtil.copyList(reasonList, EsReturnReasonDO.class);
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae) {
            logger.error("根据售后类型获取原因列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据售后类型获取原因列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
