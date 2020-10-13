package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsAgreement;
import com.xdl.jjg.mapper.EsAgreementMapper;
import com.xdl.jjg.model.domain.EsAgreementDO;
import com.xdl.jjg.model.dto.EsAgreementDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAgreementService;
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
 * 协议维护 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsAgreementServiceImpl extends ServiceImpl<EsAgreementMapper, EsAgreement> implements IEsAgreementService {

    private static Logger logger = LoggerFactory.getLogger(EsAgreementServiceImpl.class);

    @Autowired
    private EsAgreementMapper agreementMapper;

    /**
     * 插入协议维护数据
     *
     * @param agreementDTO 协议维护DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAgreement(EsAgreementDTO agreementDTO) {
        try {
            if (agreementDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsAgreement agreement = new EsAgreement();
            BeanUtil.copyProperties(agreementDTO, agreement);
            this.agreementMapper.insert(agreement);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("协议维护新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("协议维护新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新协议维护数据
     *
     * @param agreementDTO 协议维护DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAgreement(EsAgreementDTO agreementDTO) {
        try {
            if (agreementDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsAgreement> queryWrapper = new QueryWrapper<>();
            EsAgreement esAgreement = agreementMapper.selectOne(queryWrapper);
            if (esAgreement == null) {
                EsAgreement agreement = new EsAgreement();
                agreement.setContent(agreementDTO.getContent());
                agreementMapper.insert(agreement);
            } else {
                EsAgreement agreement = new EsAgreement();
                agreement.setId(esAgreement.getId());
                agreement.setContent(agreementDTO.getContent());
                agreementMapper.updateById(agreement);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("协议维护更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("协议维护更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取协议维护详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    @Override
    public DubboResult<EsAgreementDO> getAgreement(Long id) {
        try {
            QueryWrapper<EsAgreement> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAgreement::getId, id);
            EsAgreement agreement = this.agreementMapper.selectOne(queryWrapper);
            EsAgreementDO agreementDO = new EsAgreementDO();
            if (agreement == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(agreement, agreementDO);
            return DubboResult.success(agreementDO);
        } catch (ArgumentException ae) {
            logger.error("协议维护查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("协议维护查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询协议维护列表
     *
     * @param agreementDTO 协议维护DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsAgreementDO>
     */
    @Override
    public DubboPageResult<EsAgreementDO> getAgreementList(EsAgreementDTO agreementDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAgreement> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsAgreement> page = new Page<>(pageNum, pageSize);
            IPage<EsAgreement> iPage = this.page(page, queryWrapper);
            List<EsAgreementDO> agreementDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                agreementDOList = iPage.getRecords().stream().map(agreement -> {
                    EsAgreementDO agreementDO = new EsAgreementDO();
                    BeanUtil.copyProperties(agreement, agreementDO);
                    return agreementDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(agreementDOList);
        } catch (ArgumentException ae) {
            logger.error("协议维护分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("协议维护分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除协议维护数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAgreement(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsAgreement> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsAgreement::getId, id);
            this.agreementMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("协议维护删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("协议维护删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsAgreementDO> getEsAgreement() {
        try {
            QueryWrapper<EsAgreement> queryWrapper = new QueryWrapper<>();
            EsAgreement agreement = agreementMapper.selectOne(queryWrapper);
            EsAgreementDO agreementDO = new EsAgreementDO();
            if (agreement != null) {
                BeanUtil.copyProperties(agreement, agreementDO);
            }
            return DubboResult.success(agreementDO);
        } catch (ArgumentException ae) {
            logger.error("协议维护查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("协议维护查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
