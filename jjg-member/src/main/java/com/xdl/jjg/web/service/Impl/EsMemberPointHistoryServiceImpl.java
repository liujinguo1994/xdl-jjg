package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberPointHistory;
import com.xdl.jjg.mapper.EsMemberPointHistoryMapper;
import com.xdl.jjg.model.domain.EsMemberPointHistoryDO;
import com.xdl.jjg.model.dto.EsMemberPointHistoryDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberPointHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员积分明细 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberPointHistoryServiceImpl extends ServiceImpl<EsMemberPointHistoryMapper, EsMemberPointHistory> implements IEsMemberPointHistoryService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberPointHistoryServiceImpl.class);

    @Autowired
    private EsMemberPointHistoryMapper memberPointHistoryMapper;

    /**
     * 插入会员积分明细数据
     *
     * @param memberPointHistoryDTO 会员积分明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberPointHistory(EsMemberPointHistoryDTO memberPointHistoryDTO) {
        if (memberPointHistoryDTO == null) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsMemberPointHistory memberPointHistory = new EsMemberPointHistory();
            BeanUtil.copyProperties(memberPointHistoryDTO, memberPointHistory);
            this.memberPointHistoryMapper.insert(memberPointHistory);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员积分明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员积分明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员积分明细数据
     *
     * @param memberPointHistoryDTO 会员积分明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberPointHistory(EsMemberPointHistoryDTO memberPointHistoryDTO) {
        try {
            if (memberPointHistoryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberPointHistory memberPointHistory = new EsMemberPointHistory();
            BeanUtil.copyProperties(memberPointHistoryDTO, memberPointHistory);
            QueryWrapper<EsMemberPointHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberPointHistory::getId, memberPointHistoryDTO.getId());
            this.memberPointHistoryMapper.update(memberPointHistory, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员积分明细更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员积分明细详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @Override
    public DubboResult<EsMemberPointHistoryDO> getMemberPointHistory(Long id) {
        try {
            QueryWrapper<EsMemberPointHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberPointHistory::getId, id);
            EsMemberPointHistory memberPointHistory = this.memberPointHistoryMapper.selectOne(queryWrapper);
            EsMemberPointHistoryDO memberPointHistoryDO = new EsMemberPointHistoryDO();
            if (memberPointHistory == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberPointHistory, memberPointHistoryDO);
            return DubboResult.success(memberPointHistoryDO);
        } catch (ArgumentException ae) {
            logger.error("会员积分明细查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryDetail(Long memberId, Integer gradePointType, int pageSize, int pageNum) {
        try {
            QueryWrapper<EsMemberPointHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberPointHistory::getMemberId, memberId);
            if(gradePointType != null){
                queryWrapper.lambda().eq(EsMemberPointHistory::getGradePointType, gradePointType);
            }
            Page<EsMemberPointHistory> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberPointHistory> iPage = this.page(page, queryWrapper);
            if (CollectionUtils.isEmpty(iPage.getRecords())) {
                return DubboPageResult.success(iPage.getTotal(), Arrays.asList());
            }
            List<EsMemberPointHistoryDO> list = BeanUtil.copyList(iPage.getRecords(), EsMemberPointHistoryDO.class);
            return DubboPageResult.success(iPage.getTotal(), list);
        } catch (ArgumentException ae) {
            logger.error("会员积分明细查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 后台/买家端-根据查询会员积分明细列表
     *
     * @param memberPointHistoryDTO 会员积分明细DTO
     * @param pageSize              页码
     * @param pageNum               页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberPointHistoryDO>
     */
    @Override
    public DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryList(EsMemberPointHistoryDTO memberPointHistoryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberPointHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        try {
            // 查询条件
            if(memberPointHistoryDTO.getMemberId() != null){
                queryWrapper.lambda().eq(EsMemberPointHistory::getMemberId, memberPointHistoryDTO.getMemberId());
            }
            if (memberPointHistoryDTO.getGradePointType() != null) {
                queryWrapper.lambda().eq(EsMemberPointHistory::getGradePointType, memberPointHistoryDTO.getGradePointType());
            }
            Page<EsMemberPointHistory> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberPointHistory> iPage = this.page(page, queryWrapper);
            List<EsMemberPointHistoryDO> memberPointHistoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberPointHistoryDOList = iPage.getRecords().stream().map(memberPointHistory -> {
                    EsMemberPointHistoryDO memberPointHistoryDO = new EsMemberPointHistoryDO();
                    BeanUtil.copyProperties(memberPointHistory, memberPointHistoryDO);
                    return memberPointHistoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),memberPointHistoryDOList);
        } catch (ArgumentException ae) {
            logger.error("会员积分明细分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员积分明细数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberPointHistory(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberPointHistory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberPointHistory::getId, id);
            this.memberPointHistoryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员积分明细删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsMemberPointHistoryDO> pointStatistics(Long memberId) {
        try {
            QueryWrapper<EsMemberPointHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberPointHistory::getMemberId, memberId);

            List<EsMemberPointHistory> list = this.list(queryWrapper);
            if (CollectionUtils.isEmpty(list)) {
                return DubboPageResult.success(Arrays.asList());
            }
            List<EsMemberPointHistoryDO> listDO = BeanUtil.copyList(list, EsMemberPointHistoryDO.class);
            return DubboPageResult.success(listDO);
        } catch (ArgumentException ae) {
            logger.error("会员积分明细查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员积分明细查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
