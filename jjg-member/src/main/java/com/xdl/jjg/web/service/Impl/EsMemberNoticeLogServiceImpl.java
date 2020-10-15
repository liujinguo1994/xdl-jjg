package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberNoticeLogDTO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberNoticeLog;
import com.xdl.jjg.mapper.EsMemberNoticeLogMapper;
import com.xdl.jjg.model.domain.EsMemberNoticeLogDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberNoticeLogService;
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
 * 会员站内消息 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberNoticeLogServiceImpl extends ServiceImpl<EsMemberNoticeLogMapper, EsMemberNoticeLog> implements IEsMemberNoticeLogService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberNoticeLogServiceImpl.class);

    @Autowired
    private EsMemberNoticeLogMapper memberNoticeLogMapper;

    /**
     * 插入会员站内消息数据
     *
     * @param memberNoticeLogDTO 会员站内消息DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberNoticeLog(EsMemberNoticeLogDTO memberNoticeLogDTO) {
        try {
            if (null == memberNoticeLogDTO) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberNoticeLog memberNoticeLog = new EsMemberNoticeLog();
            BeanUtil.copyProperties(memberNoticeLogDTO, memberNoticeLog);
            memberNoticeLog.setSendTime(System.currentTimeMillis());
            this.memberNoticeLogMapper.insert(memberNoticeLog);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员站内消息新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员站内消息新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员站内消息数据
     *
     * @param memberNoticeLogDTO 会员站内消息DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberNoticeLog(EsMemberNoticeLogDTO memberNoticeLogDTO) {
        try {
            if (memberNoticeLogDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberNoticeLog memberNoticeLog = new EsMemberNoticeLog();
            if (null != memberNoticeLogDTO.getIsRead()) {
                memberNoticeLog.setIsRead(memberNoticeLogDTO.getIsRead());
            }
            memberNoticeLog.setReceiveTime(System.currentTimeMillis());
            QueryWrapper<EsMemberNoticeLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberNoticeLog::getId, memberNoticeLogDTO.getId());
            this.memberNoticeLogMapper.update(memberNoticeLog, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员站内消息更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员站内消息更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员站内消息详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    @Override
    public DubboResult<EsMemberNoticeLogDO> getMemberNoticeLog(Long id) {
        try {
            QueryWrapper<EsMemberNoticeLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberNoticeLog::getId, id);
            EsMemberNoticeLog memberNoticeLog = this.memberNoticeLogMapper.selectOne(queryWrapper);
            EsMemberNoticeLogDO memberNoticeLogDO = new EsMemberNoticeLogDO();
            if (memberNoticeLog == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberNoticeLog, memberNoticeLogDO);
            return DubboResult.success(memberNoticeLogDO);
        } catch (ArgumentException ae) {
            logger.error("会员站内消息查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员站内消息查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员站内消息列表
     *
     * @param memberNoticeLogDTO 会员站内消息DTO
     * @param pageSize           页码
     * @param pageNum            页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberNoticeLogDO>
     */
    @Override
    public DubboPageResult<EsMemberNoticeLogDO> getMemberNoticeLogList(EsMemberNoticeLogDTO memberNoticeLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberNoticeLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMemberNoticeLog::getMemberId, memberNoticeLogDTO.getMemberId()).eq(EsMemberNoticeLog::getIsDel, MemberConstant.IsCommon);
            if (null != memberNoticeLogDTO.getIsRead() && !MemberConstant.AllMessage.equals(memberNoticeLogDTO.getIsRead())) {
                queryWrapper.lambda().eq(EsMemberNoticeLog::getIsRead, memberNoticeLogDTO.getIsRead());
            }
            Page<EsMemberNoticeLog> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberNoticeLog> iPage = this.page(page, queryWrapper);
            List<EsMemberNoticeLogDO> memberNoticeLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberNoticeLogDOList = iPage.getRecords().stream().map(memberNoticeLog -> {
                    EsMemberNoticeLogDO memberNoticeLogDO = new EsMemberNoticeLogDO();
                    BeanUtil.copyProperties(memberNoticeLog, memberNoticeLogDO);
                    return memberNoticeLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberNoticeLogDOList);
        } catch (ArgumentException ae) {
            logger.error("会员站内消息分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员站内消息分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员站内消息数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberNoticeLog(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberNoticeLog> deleteWrapper = new QueryWrapper<>();
            EsMemberNoticeLog esMemberNoticeLog = new EsMemberNoticeLog();
            esMemberNoticeLog.setIsDel(MemberConstant.IsDel);
            deleteWrapper.lambda().eq(EsMemberNoticeLog::getId, id);
            this.memberNoticeLogMapper.update(esMemberNoticeLog, deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员站内消息删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员站内消息删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
