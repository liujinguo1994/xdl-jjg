package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsMessageTemplate;
import com.xdl.jjg.mapper.EsMessageTemplateMapper;
import com.xdl.jjg.model.domain.EsMessageTemplateDO;
import com.xdl.jjg.model.dto.EsMessageTemplateDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMessageTemplateService;
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
public class EsMessageTemplateServiceImpl extends ServiceImpl<EsMessageTemplateMapper, EsMessageTemplate> implements IEsMessageTemplateService {

    private static Logger logger = LoggerFactory.getLogger(EsMessageTemplateServiceImpl.class);

    @Autowired
    private EsMessageTemplateMapper messageTemplateMapper;

    /**
     * 插入数据
     *
     * @param messageTemplateDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMessageTemplate(EsMessageTemplateDTO messageTemplateDTO) {
        try {
            if (messageTemplateDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMessageTemplate messageTemplate = new EsMessageTemplate();
            BeanUtil.copyProperties(messageTemplateDTO, messageTemplate);
            this.messageTemplateMapper.insert(messageTemplate);
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
     * @param messageTemplateDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMessageTemplate(EsMessageTemplateDTO messageTemplateDTO) {
        try {
            if (messageTemplateDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMessageTemplate messageTemplate = new EsMessageTemplate();
            BeanUtil.copyProperties(messageTemplateDTO, messageTemplate);
            QueryWrapper<EsMessageTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMessageTemplate::getId, messageTemplateDTO.getId());
            this.messageTemplateMapper.update(messageTemplate, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     */
    @Override
    public DubboResult<EsMessageTemplateDO> getMessageTemplate(Long id) {
        try {
            QueryWrapper<EsMessageTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMessageTemplate::getId, id);
            EsMessageTemplate messageTemplate = this.messageTemplateMapper.selectOne(queryWrapper);
            EsMessageTemplateDO messageTemplateDO = new EsMessageTemplateDO();
            if (messageTemplate == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(messageTemplate, messageTemplateDO);
            return DubboResult.success(messageTemplateDO);
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
     * @param messageTemplateDTO DTO
     * @param pageSize           页码
     * @param pageNum            页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsMessageTemplateVO>
     */
    @Override
    public DubboPageResult<EsMessageTemplateDO> getMessageTemplateList(EsMessageTemplateDTO messageTemplateDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMessageTemplate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMessageTemplate::getType, messageTemplateDTO.getType());
            Page<EsMessageTemplate> page = new Page<>(pageNum, pageSize);
            IPage<EsMessageTemplate> iPage = this.page(page, queryWrapper);
            List<EsMessageTemplateDO> messageTemplateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                messageTemplateDOList = iPage.getRecords().stream().map(messageTemplate -> {
                    EsMessageTemplateDO messageTemplateDO = new EsMessageTemplateDO();
                    BeanUtil.copyProperties(messageTemplate, messageTemplateDO);
                    return messageTemplateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), messageTemplateDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMessageTemplate(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMessageTemplate> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMessageTemplate::getId, id);
            this.messageTemplateMapper.delete(deleteWrapper);
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
