package com.xdl.jjg.web.service.Impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsMemberLoginLogDO;
import com.jjg.member.model.dto.EsMemberLoginLogDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberLoginLog;
import com.xdl.jjg.mapper.EsMemberLoginLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberLoginLogService;
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
 * 会员登录日志 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-16
 */
@Service
public class EsMemberLoginLogServiceImpl extends ServiceImpl<EsMemberLoginLogMapper, EsMemberLoginLog> implements IEsMemberLoginLogService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberLoginLogServiceImpl.class);

    @Autowired
    private EsMemberLoginLogMapper memberLoginLogMapper;

    /**
     * 插入会员登录日志数据
     *
     * @param memberLoginLogDTO 会员登录日志DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 10:16:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberLoginLog(EsMemberLoginLogDTO memberLoginLogDTO) {
        try {
            if (memberLoginLogDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberLoginLog memberLoginLog = new EsMemberLoginLog();
            BeanUtil.copyProperties(memberLoginLogDTO, memberLoginLog);
            this.memberLoginLogMapper.insert(memberLoginLog);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("会员登录日志新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("会员登录日志新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员登录日志数据
     *
     * @param memberLoginLogDTO 会员登录日志DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 10:29:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberLoginLog(EsMemberLoginLogDTO memberLoginLogDTO) {
        try {
            EsMemberLoginLog esMemberLoginLog = this.memberLoginLogMapper.selectById(memberLoginLogDTO.getId());
            if (esMemberLoginLog==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberLoginLog memberLoginLog = new EsMemberLoginLog();
            BeanUtil.copyProperties(memberLoginLogDTO, memberLoginLog);
            QueryWrapper<EsMemberLoginLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberLoginLog::getId, memberLoginLogDTO.getId());
            this.memberLoginLogMapper.update(memberLoginLog, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("会员登录日志更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员登录日志更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员登录日志详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 09:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    @Override
    public DubboResult<EsMemberLoginLogDO> getMemberLoginLog(Long id) {
        try {
            Assert.notNull(id,"主键不能为空");
            EsMemberLoginLog memberLoginLog = this.memberLoginLogMapper.selectById(id);
            EsMemberLoginLogDO memberLoginLogDO = new EsMemberLoginLogDO();
            if (memberLoginLog == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberLoginLog, memberLoginLogDO);
            return DubboResult.success(memberLoginLogDO);
        } catch (ArgumentException ae){
            logger.error("会员登录日志查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员登录日志查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员登录日志列表
     *
     * @param memberLoginLogDTO 会员登录日志DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 11:22:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberLoginLogDO>
     */
    @Override
    public DubboPageResult<EsMemberLoginLogDO> getMemberLoginLogList(EsMemberLoginLogDTO memberLoginLogDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberLoginLog> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsMemberLoginLog> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberLoginLog> iPage = this.page(page, queryWrapper);
            List<EsMemberLoginLogDO> memberLoginLogDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberLoginLogDOList = iPage.getRecords().stream().map(memberLoginLog -> {
                    EsMemberLoginLogDO memberLoginLogDO = new EsMemberLoginLogDO();
                    BeanUtil.copyProperties(memberLoginLog, memberLoginLogDO);
                    return memberLoginLogDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberLoginLogDOList);
        } catch (ArgumentException ae){
            logger.error("会员登录日志分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员登录日志分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员登录日志数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 10:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberLoginLog(Long id) {
        try {
            Assert.notNull(id,"主键不能为空");
            this.memberLoginLogMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("会员登录日志删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员登录日志删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
