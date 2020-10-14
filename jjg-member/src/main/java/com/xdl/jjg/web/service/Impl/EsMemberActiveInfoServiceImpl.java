package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberActiveInfoDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberActiveInfo;
import com.xdl.jjg.mapper.EsMemberActiveInfoMapper;
import com.xdl.jjg.model.domain.EsMemberActiveInfoDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberActiveInfoService;
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
 * 会员活跃信息服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:44
 */
@Service
public class EsMemberActiveInfoServiceImpl extends ServiceImpl<EsMemberActiveInfoMapper, EsMemberActiveInfo> implements IEsMemberActiveInfoService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberActiveInfoServiceImpl.class);

    @Autowired
    private EsMemberActiveInfoMapper memberActiveInfoMapper;

    /**
     * 插入数据
     *
     * @param memberActiveInfoDTOs DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberActiveInfo(List<EsMemberActiveInfoDTO> memberActiveInfoDTOs) {
        try {
            EsMemberActiveInfo memberActiveInfo = new EsMemberActiveInfo();
            for (EsMemberActiveInfoDTO es : memberActiveInfoDTOs) {
                BeanUtil.copyProperties(es, memberActiveInfo);
                this.memberActiveInfoMapper.insert(memberActiveInfo);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param memberActiveInfoDTO DTO
     * @param id                  主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberActiveInfo(EsMemberActiveInfoDTO memberActiveInfoDTO, Long id) {
        try {
            EsMemberActiveInfo memberActiveInfo = this.memberActiveInfoMapper.selectById(id);
            if (memberActiveInfo == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberActiveInfoDTO, memberActiveInfo);
            QueryWrapper<EsMemberActiveInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberActiveInfo::getId, id);
            this.memberActiveInfoMapper.update(memberActiveInfo, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    @Override
    public DubboResult<EsMemberActiveInfoDO> getMemberActiveInfo(Long id) {
        try {
            EsMemberActiveInfo memberActiveInfo = this.memberActiveInfoMapper.selectById(id);
            if (memberActiveInfo == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberActiveInfoDO memberActiveInfoDO = new EsMemberActiveInfoDO();
            BeanUtil.copyProperties(memberActiveInfo, memberActiveInfoDO);
            return DubboResult.success(memberActiveInfoDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param memberActiveInfoDTO DTO
     * @param pageSize            页码
     * @param pageNum             页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberActiveInfoDO>
     */
    @Override
    public DubboPageResult<EsMemberActiveInfoDO> getMemberActiveInfoList(EsMemberActiveInfoDTO memberActiveInfoDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberActiveInfo> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberActiveInfo> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberActiveInfo> iPage = this.page(page, queryWrapper);
            List<EsMemberActiveInfoDO> memberActiveInfoDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberActiveInfoDOList = iPage.getRecords().stream().map(memberActiveInfo -> {
                    EsMemberActiveInfoDO memberActiveInfoDO = new EsMemberActiveInfoDO();
                    BeanUtil.copyProperties(memberActiveInfo, memberActiveInfoDO);
                    return memberActiveInfoDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberActiveInfoDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据orderSn删除数据
     *
     * @param orderSns String
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberActiveInfo(List<String> orders) {
        try {
            for (String ts : orders) {
                QueryWrapper<EsMemberActiveInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsMemberActiveInfo::getOrderSn, ts);
                this.memberActiveInfoMapper.delete(queryWrapper);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
