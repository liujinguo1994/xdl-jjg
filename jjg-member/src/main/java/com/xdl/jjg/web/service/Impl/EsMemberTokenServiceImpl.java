package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberTokenDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberToken;
import com.xdl.jjg.mapper.EsMemberTokenMapper;
import com.xdl.jjg.model.domain.EsMemberTokenDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberTokenService;
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
 * 会员token信息服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-10 14:47:36
 */
@Service
public class EsMemberTokenServiceImpl extends ServiceImpl<EsMemberTokenMapper, EsMemberToken> implements IEsMemberTokenService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberTokenServiceImpl.class);

    @Autowired
    private EsMemberTokenMapper memberTokenMapper;

    /**
     * 插入数据
     *
     * @param memberTokenDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberToken(EsMemberTokenDTO memberTokenDTO) {
        try {
            EsMemberToken memberToken = new EsMemberToken();
            BeanUtil.copyProperties(memberTokenDTO, memberToken);
            this.memberTokenMapper.insert(memberToken);
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
     * @param memberTokenDTO DTO
     * @param id             主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberToken(EsMemberTokenDTO memberTokenDTO) {
        try {
            EsMemberToken memberToken = new EsMemberToken();
            BeanUtil.copyProperties(memberTokenDTO, memberToken);
            QueryWrapper<EsMemberToken> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberToken::getMemberId, memberTokenDTO.getMemberId());
            this.memberTokenMapper.update(memberToken, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @Override
    public DubboResult<EsMemberTokenDO> getMemberToken(Long id) {
        QueryWrapper<EsMemberToken> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberToken::getMemberId, id);
            EsMemberToken memberToken = memberTokenMapper.selectOne(queryWrapper);
            if (memberToken == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberTokenDO memberTokenDO = new EsMemberTokenDO();
            BeanUtil.copyProperties(memberToken, memberTokenDO);
            return DubboResult.success(memberTokenDO);
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
     * @param memberTokenDTO DTO
     * @param pageSize       页码
     * @param pageNum        页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    @Override
    public DubboPageResult<EsMemberTokenDO> getMemberTokenList(EsMemberTokenDTO memberTokenDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberToken> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsMemberToken> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberToken> iPage = this.page(page, queryWrapper);
            List<EsMemberTokenDO> memberTokenDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberTokenDOList = iPage.getRecords().stream().map(memberToken -> {
                    EsMemberTokenDO memberTokenDO = new EsMemberTokenDO();
                    BeanUtil.copyProperties(memberToken, memberTokenDO);
                    return memberTokenDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberTokenDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询条件查询
     *
     * @param memberTokenDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    public DubboResult<EsMemberTokenDO> getMemberTokenInfo(EsMemberTokenDTO memberTokenDTO) {

        QueryWrapper<EsMemberToken> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (null != memberTokenDTO.getMemberId()) {
                queryWrapper.lambda().eq(EsMemberToken::getMemberId, memberTokenDTO.getMemberId());
            }
            if (null != memberTokenDTO.getToken()) {
                queryWrapper.lambda().eq(EsMemberToken::getToken, memberTokenDTO.getToken());
            }
            EsMemberTokenDO esMemberTokenDO = new EsMemberTokenDO();
            EsMemberToken esMemberToken = this.memberTokenMapper.selectOne(queryWrapper);
            if (null == esMemberToken) {
                return DubboResult.success();
            }
            BeanUtil.copyProperties(esMemberToken, esMemberTokenDO);
            return DubboPageResult.success(esMemberTokenDO);
        } catch (ArgumentException ae) {
            logger.error("会员token表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员token表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberToken(Long id) {
        try {
            this.memberTokenMapper.deleteById(id);
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
