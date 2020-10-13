package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSmtp;
import com.xdl.jjg.mapper.EsSmtpMapper;
import com.xdl.jjg.model.domain.EsSmtpDO;
import com.xdl.jjg.model.dto.EsSmtpDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsSmtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class EsSmtpServiceImpl extends ServiceImpl<EsSmtpMapper, EsSmtp> implements IEsSmtpService {

    private static Logger logger = LoggerFactory.getLogger(EsSmtpServiceImpl.class);

    @Autowired
    private EsSmtpMapper smtpMapper;

    /**
     * 插入数据
     *
     * @param smtpDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSmtp(EsSmtpDTO smtpDTO) {
        try {
            if (smtpDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSmtp smtp = new EsSmtp();
            BeanUtil.copyProperties(smtpDTO, smtp);
            smtp.setLastSendTime(System.currentTimeMillis());
            this.smtpMapper.insert(smtp);
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
     * @param smtpDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSmtp(EsSmtpDTO smtpDTO) {
        try {
            if (smtpDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSmtp smtp = new EsSmtp();
            BeanUtil.copyProperties(smtpDTO, smtp);
            QueryWrapper<EsSmtp> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSmtp::getId, smtpDTO.getId());
            this.smtpMapper.update(smtp, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    @Override
    public DubboResult<EsSmtpDO> getSmtp(Long id) {
        try {
            QueryWrapper<EsSmtp> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSmtp::getId, id);
            EsSmtp smtp = this.smtpMapper.selectOne(queryWrapper);
            EsSmtpDO smtpDO = new EsSmtpDO();
            if (smtp == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(smtp, smtpDO);
            return DubboResult.success(smtpDO);
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
     * @param smtpDTO  DTO
     * @param pageSize 页码
     * @param pageNum  页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsSmtpDO>
     */
    @Override
    public DubboPageResult<EsSmtpDO> getSmtpList(EsSmtpDTO smtpDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSmtp> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSmtp> page = new Page<>(pageNum, pageSize);
            IPage<EsSmtp> iPage = this.page(page, queryWrapper);
            List<EsSmtpDO> smtpDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                smtpDOList = iPage.getRecords().stream().map(smtp -> {
                    EsSmtpDO smtpDO = new EsSmtpDO();
                    BeanUtil.copyProperties(smtp, smtpDO);
                    return smtpDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), smtpDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSmtp(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSmtp> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSmtp::getId, id);
            this.smtpMapper.delete(deleteWrapper);
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

    @Override
    public DubboResult<EsSmtpDO> getCurrentSmtp() {
        try {
            QueryWrapper<EsSmtp> queryWrapper = new QueryWrapper<>();
            List<EsSmtp> smtpList = smtpMapper.selectList(queryWrapper);
            EsSmtpDO smtpDO = null;
            if (CollectionUtils.isNotEmpty(smtpList)) {
                for (EsSmtp smtp : smtpList) {
                    if (checkCount(smtp)) {
                        smtpDO = new EsSmtpDO();
                        BeanUtil.copyProperties(smtp, smtpDO);
                        break;
                    }
                }
            }
            if (smtpDO == null) {
                throw new ArgumentException(ErrorCode.SMTP_IS_NULL.getErrorCode(), "未找到可用smtp方案");
            }
            return DubboResult.success(smtpDO);
        } catch (ArgumentException ae) {
            logger.error("获取当前使用的smtp方案失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取当前使用的smtp方案失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 检测smtp服务器是否可以用
     *
     * @param smtp
     * @return 检查是否通过
     */
    private boolean checkCount(EsSmtp smtp) {
        //最后一次发送时间
        long lastSendTime = smtp.getLastSendTime();
        //已经不是今天
        if (!dateToString(new Date(lastSendTime), "yyyy-MM-dd").equals(dateToString(new Date(), "yyyy-MM-dd"))) {
            smtp.setSendCount(0);
        }
        return smtp.getSendCount() < smtp.getMaxCount();
    }

    /**
     * 把日期转换成字符串型
     */
    public String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateString = sdf.format(date);
        return dateString;
    }
}
