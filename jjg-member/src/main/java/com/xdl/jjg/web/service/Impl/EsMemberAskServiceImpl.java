package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberAsk;
import com.xdl.jjg.mapper.EsMemberAskMapper;
import com.xdl.jjg.model.domain.EsMemberAskDO;
import com.xdl.jjg.model.dto.EsMemberAskDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberAskService;
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
 * 咨询(手机端做) 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberAskServiceImpl extends ServiceImpl<EsMemberAskMapper, EsMemberAsk> implements IEsMemberAskService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberAskServiceImpl.class);

    @Autowired
    private EsMemberAskMapper memberAskMapper;

    /**
     * 插入咨询(手机端做)数据
     *
     * @param memberAskDTO 咨询(手机端做)DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberAsk(EsMemberAskDTO memberAskDTO) {
        try {
            if (memberAskDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberAsk memberAsk = new EsMemberAsk();
            BeanUtil.copyProperties(memberAskDTO, memberAsk);
            this.memberAskMapper.insert(memberAsk);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("咨询(手机端做)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("咨询(手机端做)新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新咨询(手机端做)数据
     *
     * @param memberAskDTO 咨询(手机端做)DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberAsk(EsMemberAskDTO memberAskDTO) {
        try {
            if (memberAskDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberAsk memberAsk = new EsMemberAsk();
            BeanUtil.copyProperties(memberAskDTO, memberAsk);
            QueryWrapper<EsMemberAsk> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAsk::getId, memberAskDTO.getId());
            this.memberAskMapper.update(memberAsk, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("咨询(手机端做)更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("咨询(手机端做)更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取咨询(手机端做)详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    @Override
    public DubboResult<EsMemberAskDO> getMemberAsk(Long id) {
        try {
            QueryWrapper<EsMemberAsk> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAsk::getId, id);
            EsMemberAsk memberAsk = this.memberAskMapper.selectOne(queryWrapper);
            EsMemberAskDO memberAskDO = new EsMemberAskDO();
            if (memberAsk == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberAsk, memberAskDO);
            return DubboResult.success(memberAskDO);
        } catch (ArgumentException ae){
            logger.error("咨询(手机端做)查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("咨询(手机端做)查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询咨询(手机端做)列表
     *
     * @param memberAskDTO 咨询(手机端做)DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAskDO>
     */
    @Override
    public DubboPageResult<EsMemberAskDO> getMemberAskList(EsMemberAskDTO memberAskDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberAsk> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberAsk> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberAsk> iPage = this.page(page, queryWrapper);
            List<EsMemberAskDO> memberAskDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberAskDOList = iPage.getRecords().stream().map(memberAsk -> {
                    EsMemberAskDO memberAskDO = new EsMemberAskDO();
                    BeanUtil.copyProperties(memberAsk, memberAskDO);
                    return memberAskDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberAskDOList);
        } catch (ArgumentException ae){
            logger.error("咨询(手机端做)分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("咨询(手机端做)分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除咨询(手机端做)数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberAsk(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberAsk> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberAsk::getId, id);
            this.memberAskMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("咨询(手机端做)删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("咨询(手机端做)删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
