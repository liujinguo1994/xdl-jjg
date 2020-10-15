package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsMemberReceiptDO;
import com.jjg.member.model.dto.EsMemberReceiptDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberReceipt;
import com.xdl.jjg.mapper.EsMemberReceiptMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberReceiptService;
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
 * 会员发票信息 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberReceiptServiceImpl extends ServiceImpl<EsMemberReceiptMapper, EsMemberReceipt> implements IEsMemberReceiptService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberReceiptServiceImpl.class);

    @Autowired
    private EsMemberReceiptMapper memberReceiptMapper;

    /**
     * 插入会员发票信息数据
     *
     * @param memberReceiptDTO 会员发票信息DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<Long> insertMemberReceipt(EsMemberReceiptDTO memberReceiptDTO) {
        try {
            if (memberReceiptDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberReceipt memberReceipt = new EsMemberReceipt();
            BeanUtil.copyProperties(memberReceiptDTO, memberReceipt);
            this.memberReceiptMapper.insert(memberReceipt);
            return DubboResult.success(memberReceipt.getId());
        } catch (ArgumentException ae) {
            logger.error("会员发票信息新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员发票信息新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员发票信息数据
     *
     * @param memberReceiptDTO 会员发票信息DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberReceipt(EsMemberReceiptDTO memberReceiptDTO) {
        try {
            if (memberReceiptDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberReceipt memberReceipt = new EsMemberReceipt();
            BeanUtil.copyProperties(memberReceiptDTO, memberReceipt);
            QueryWrapper<EsMemberReceipt> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberReceipt::getId, memberReceiptDTO.getId());
            this.memberReceiptMapper.update(memberReceipt, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员发票信息更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员发票信息更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员发票信息详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    @Override
    public DubboResult<EsMemberReceiptDO> getMemberReceipt(Long id) {
        try {
            QueryWrapper<EsMemberReceipt> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberReceipt::getId, id);
            EsMemberReceipt memberReceipt = this.memberReceiptMapper.selectOne(queryWrapper);
            EsMemberReceiptDO memberReceiptDO = new EsMemberReceiptDO();
            if (memberReceipt == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberReceipt, memberReceiptDO);
            return DubboResult.success(memberReceiptDO);
        } catch (ArgumentException ae) {
            logger.error("会员发票信息查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员发票信息查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员发票信息列表
     *
     * @param memberReceiptDTO 会员发票信息DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberReceiptDO>
     */
    @Override
    public DubboPageResult<EsMemberReceiptDO> getMemberReceiptList(EsMemberReceiptDTO memberReceiptDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberReceipt> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (null != memberReceiptDTO.getIsDefault()) {
                queryWrapper.lambda().eq(EsMemberReceipt::getMemberId, memberReceiptDTO.getMemberId()).eq(EsMemberReceipt::getIsDefault, memberReceiptDTO.getIsDefault());
            } else {
                queryWrapper.lambda().eq(EsMemberReceipt::getMemberId, memberReceiptDTO.getMemberId());
            }
            Page<EsMemberReceipt> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberReceipt> iPage = this.page(page, queryWrapper);
            List<EsMemberReceiptDO> memberReceiptDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberReceiptDOList = iPage.getRecords().stream().map(memberReceipt -> {
                    EsMemberReceiptDO memberReceiptDO = new EsMemberReceiptDO();
                    BeanUtil.copyProperties(memberReceipt, memberReceiptDO);
                    return memberReceiptDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),memberReceiptDOList);
        } catch (ArgumentException ae) {
            logger.error("会员发票信息分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员发票信息分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员发票信息数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberReceipt(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberReceipt> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberReceipt::getId, id);
            this.memberReceiptMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员发票信息删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员发票信息删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
