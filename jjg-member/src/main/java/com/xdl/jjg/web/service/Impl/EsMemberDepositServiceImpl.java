package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDepositDO;
import com.shopx.member.api.model.domain.dto.EsMemberBalanceDTO;
import com.shopx.member.api.model.domain.dto.EsMemberDepositDTO;
import com.shopx.member.api.model.domain.dto.EsQueryAdminMemberDepositDTO;
import com.shopx.member.api.model.domain.enums.ConsumeEnumType;
import com.shopx.member.api.service.IEsMemberDepositService;
import com.xdl.jjg.entity.EsMemberDeposit;
import com.shopx.member.dao.mapper.EsMemberDepositMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员余额明细 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberDepositServiceImpl extends ServiceImpl<EsMemberDepositMapper, EsMemberDeposit> implements IEsMemberDepositService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberDepositServiceImpl.class);

    @Autowired
    private EsMemberDepositMapper memberDepositMapper;

    /**
     * 插入会员余额明细数据
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberDeposit(EsMemberDepositDTO memberDepositDTO) {
        try {
            if (null == memberDepositDTO) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberDeposit memberDeposit = new EsMemberDeposit();
            BeanUtil.copyProperties(memberDepositDTO, memberDeposit);
            if (null != memberDepositDTO.getMemberBalance()) {
                memberDeposit.setMemberBalance(memberDepositDTO.getMemberBalance());
            }
            this.memberDepositMapper.insert(memberDeposit);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量插入数据
     *
     * @param esMemberDepositDTOList 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    public DubboResult insertMemberBatchDeposit(List<EsMemberDepositDTO> esMemberDepositDTOList) {
        try {
            if (CollectionUtils.isEmpty(esMemberDepositDTOList)) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            List<EsMemberDeposit> lists = new ArrayList<>();
            esMemberDepositDTOList.forEach(es -> {
                EsMemberDeposit esMemberDeposit = new EsMemberDeposit();
                BeanUtil.copyProperties(es, esMemberDeposit);
                esMemberDeposit.setType(MemberConstant.recharge);
                if (null != es.getMemberBalance()) {
                    esMemberDeposit.setMemberBalance(es.getMemberBalance());
                }
                if (null != es.getMoney()) {
                    esMemberDeposit.setMoney(es.getMoney());
                }
                lists.add(esMemberDeposit);
            });
            this.saveBatch(lists, lists.size());
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 插入数据会员余额明细
     *
     * @param esMemberBalanceDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberDepositBalance(EsMemberBalanceDTO esMemberBalanceDTO) {
        try {
            if (null == esMemberBalanceDTO) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberDeposit esMemberDeposit = new EsMemberDeposit();
            BeanUtil.copyProperties(esMemberBalanceDTO, esMemberDeposit);
            esMemberDeposit.setType(esMemberBalanceDTO.getType().value());
            esMemberDeposit.setMoney(esMemberBalanceDTO.getMoney() != null? esMemberBalanceDTO.getMoney() : 0);
            esMemberDeposit.setCreateTime(System.currentTimeMillis());
            this.memberDepositMapper.insert(esMemberDeposit);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员余额明细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员余额明细数据
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberDeposit(EsMemberDepositDTO memberDepositDTO) {
        try {
            if (memberDepositDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberDeposit memberDeposit = new EsMemberDeposit();
            BeanUtil.copyProperties(memberDepositDTO, memberDeposit);
            if (null != memberDepositDTO.getMemberBalance()) {
                memberDeposit.setMemberBalance(memberDepositDTO.getMemberBalance());
            }
            QueryWrapper<EsMemberDeposit> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberDeposit::getId, memberDepositDTO.getId());
            this.memberDepositMapper.update(memberDeposit, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员余额明细更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员余额明细详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    @Override
    public DubboResult<EsMemberDepositDO> getMemberDeposit(Long id) {
        try {
            QueryWrapper<EsMemberDeposit> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberDeposit::getId, id);
            EsMemberDeposit memberDeposit = this.memberDepositMapper.selectOne(queryWrapper);
            EsMemberDepositDO memberDepositDO = new EsMemberDepositDO();
            if (null == memberDeposit) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberDeposit, memberDepositDO);
            return DubboResult.success(memberDepositDO);
        } catch (ArgumentException ae) {
            logger.error("会员余额明细查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员余额明细列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    @Override
    public DubboPageResult<EsMemberDepositDO> getMemberDepositList(EsMemberDepositDTO memberDepositDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            Page<EsMemberDepositDO> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberDepositDO> esMemberDepositDOIPage = memberDepositMapper.getRecentlyThreeMonth(page, memberDepositDTO);
            List<EsMemberDepositDO> esMemberDepositList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberDepositDOIPage.getRecords())) {
                esMemberDepositList = esMemberDepositDOIPage.getRecords();
            }
            return DubboPageResult.success(esMemberDepositDOIPage.getTotal(), esMemberDepositList);
        } catch (ArgumentException ae) {
            logger.error("会员余额明细分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 手机端根据查询会员余额明细列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/04/07 16:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    @Override
    public DubboPageResult<EsMemberDepositDO> getWapMemberDepositList(EsMemberDepositDTO memberDepositDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            Page<EsMemberDepositDO> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberDepositDO> esMemberDepositDOIPage = memberDepositMapper.getWapBalance(page, memberDepositDTO);
            List<EsMemberDepositDO> esMemberDepositList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberDepositDOIPage.getRecords())) {
                esMemberDepositList = esMemberDepositDOIPage.getRecords();
            }
            return DubboPageResult.success(esMemberDepositDOIPage.getTotal(), esMemberDepositList);
        } catch (ArgumentException ae) {
            logger.error("会员余额明细分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员余额明细列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    @Override
    public DubboPageResult<EsMemberDepositDO> getAdminMemberDepositList(EsQueryAdminMemberDepositDTO memberDepositDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberDeposit> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMemberDeposit::getMemberId, memberDepositDTO.getMemberId());
            if (null != memberDepositDTO && null != memberDepositDTO.getType()) {
                queryWrapper.lambda().eq(EsMemberDeposit::getType, memberDepositDTO.getType());
            }
            if (null != memberDepositDTO && null != memberDepositDTO.getCreateTimeStart() && null != memberDepositDTO.getCreateTimeEnd()) {
                queryWrapper.between("create_time", memberDepositDTO.getCreateTimeStart(), memberDepositDTO.getCreateTimeEnd());
            }
            Page<EsMemberDeposit> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberDeposit> iPage = this.page(page, queryWrapper);
            List<EsMemberDepositDO> result = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                result = iPage.getRecords().stream().map(esDo -> {
                    EsMemberDepositDO es = new EsMemberDepositDO();
                    BeanUtil.copyProperties(esDo, es);
                    //消费
                    if(es.getType().equals(ConsumeEnumType.CONSUME.value())){
                        es.setType("消费");
                    }else if(es.getType().equals(ConsumeEnumType.RECHARGE.value())){
                        es.setType("充值");
                    }else{
                        es.setType("退款");
                    }
                    return es;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), result);
        } catch (ArgumentException ae) {
            logger.error("会员余额明细分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员余额明细数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberDeposit(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberDeposit> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberDeposit::getId, id);
            this.memberDepositMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员余额明细删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员余额明细删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
