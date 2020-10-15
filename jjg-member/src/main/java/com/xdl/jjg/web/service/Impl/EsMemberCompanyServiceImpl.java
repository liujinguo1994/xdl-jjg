package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberCompanyDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberCompany;
import com.xdl.jjg.mapper.EsMemberCompanyMapper;
import com.xdl.jjg.model.domain.EsMemberCompanyDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberCompanyService;
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
 *  服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberCompanyServiceImpl extends ServiceImpl<EsMemberCompanyMapper, EsMemberCompany> implements IEsMemberCompanyService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberCompanyServiceImpl.class);

    @Autowired
    private EsMemberCompanyMapper memberCompanyMapper;

    /**
     * 插入数据
     *
     * @param memberCompanyDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberCompany(EsMemberCompanyDTO memberCompanyDTO) {
        try {
            if (memberCompanyDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberCompany memberCompany = new EsMemberCompany();
            BeanUtil.copyProperties(memberCompanyDTO, memberCompany);
            this.memberCompanyMapper.insert(memberCompany);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param memberCompanyDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberCompany(EsMemberCompanyDTO memberCompanyDTO) {
        try {
            if (memberCompanyDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberCompany memberCompany = new EsMemberCompany();
            BeanUtil.copyProperties(memberCompanyDTO, memberCompany);
            QueryWrapper<EsMemberCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCompany::getCompanyId, memberCompanyDTO.getMemberId());
            this.memberCompanyMapper.update(memberCompany, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    @Override
    public DubboResult<EsMemberCompanyDO> getMemberCompany(Long id) {
        try {
            QueryWrapper<EsMemberCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCompany::getCompanyId, id);
            EsMemberCompany memberCompany = this.memberCompanyMapper.selectOne(queryWrapper);
            EsMemberCompanyDO memberCompanyDO = new EsMemberCompanyDO();
            if (memberCompany == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberCompany, memberCompanyDO);
            return DubboResult.success(memberCompanyDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param memberCompanyDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCompanyDO>
     */
    @Override
    public DubboPageResult<EsMemberCompanyDO> getMemberCompanyList(EsMemberCompanyDTO memberCompanyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberCompany> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberCompany> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberCompany> iPage = this.page(page, queryWrapper);
            List<EsMemberCompanyDO> memberCompanyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberCompanyDOList = iPage.getRecords().stream().map(memberCompany -> {
                    EsMemberCompanyDO memberCompanyDO = new EsMemberCompanyDO();
                    BeanUtil.copyProperties(memberCompany, memberCompanyDO);
                    return memberCompanyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberCompanyDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberCompany(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberCompany> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberCompany::getCompanyId, id);
            this.memberCompanyMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
