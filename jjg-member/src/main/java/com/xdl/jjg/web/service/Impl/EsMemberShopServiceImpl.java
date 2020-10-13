package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberShopDO;
import com.shopx.member.api.model.domain.dto.EsMemberShopDTO;
import com.shopx.member.api.service.IEsMemberShopService;
import com.xdl.jjg.entity.EsMemberShop;
import com.shopx.member.dao.mapper.EsMemberShopMapper;
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
 * 会员店铺关联表 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberShopServiceImpl extends ServiceImpl<EsMemberShopMapper, EsMemberShop> implements IEsMemberShopService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberShopServiceImpl.class);

    @Autowired
    private EsMemberShopMapper memberShopMapper;

    /**
     * 插入会员店铺关联表数据
     *
     * @param memberShopDTO 会员店铺关联表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberShop(EsMemberShopDTO memberShopDTO) {
        try {
            if (memberShopDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsMemberShop memberShop = new EsMemberShop();
            BeanUtil.copyProperties(memberShopDTO, memberShop);
            this.memberShopMapper.insert(memberShop);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("会员店铺关联表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("会员店铺关联表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员店铺关联表数据
     *
     * @param memberShopDTO 会员店铺关联表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberShop(EsMemberShopDTO memberShopDTO) {
        try {
            if (memberShopDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberShop memberShop = new EsMemberShop();
            BeanUtil.copyProperties(memberShopDTO, memberShop);
            QueryWrapper<EsMemberShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberShop::getMemberId, memberShopDTO.getMemberId());
            this.memberShopMapper.update(memberShop, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("会员店铺关联表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员店铺关联表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员店铺关联表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopDO>
     */
    @Override
    public DubboResult<EsMemberShopDO> getMemberShop(Long id) {
        try {
            QueryWrapper<EsMemberShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberShop::getMemberId, id);
            EsMemberShop memberShop = this.memberShopMapper.selectOne(queryWrapper);
            EsMemberShopDO memberShopDO = new EsMemberShopDO();
            if (memberShop == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberShop, memberShopDO);
            return DubboResult.success(memberShopDO);
        } catch (ArgumentException ae){
            logger.error("会员店铺关联表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员店铺关联表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员店铺关联表列表
     *
     * @param memberShopDTO 会员店铺关联表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberShopDO>
     */
    @Override
    public DubboPageResult<EsMemberShopDO> getMemberShopList(EsMemberShopDTO memberShopDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberShop> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMemberShop> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberShop> iPage = this.page(page, queryWrapper);
            List<EsMemberShopDO> memberShopDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberShopDOList = iPage.getRecords().stream().map(memberShop -> {
                    EsMemberShopDO memberShopDO = new EsMemberShopDO();
                    BeanUtil.copyProperties(memberShop, memberShopDO);
                    return memberShopDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberShopDOList);
        } catch (ArgumentException ae){
            logger.error("会员店铺关联表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员店铺关联表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员店铺关联表数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberShop(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberShop> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberShop::getMemberId, id);
            this.memberShopMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("会员店铺关联表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员店铺关联表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
