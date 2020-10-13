package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCouponReceiveCheck;
import com.xdl.jjg.mapper.EsCouponReceiveCheckMapper;
import com.xdl.jjg.model.domain.EsCouponReceiveCheckDO;
import com.xdl.jjg.model.dto.EsCouponReceiveCheckDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCouponReceiveCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
@Service
public class EsCouponReceiveCheckServiceImpl extends ServiceImpl<EsCouponReceiveCheckMapper, EsCouponReceiveCheck> implements IEsCouponReceiveCheckService {

    private static Logger logger = LoggerFactory.getLogger(EsCouponReceiveCheckServiceImpl.class);

    @Autowired
    private EsCouponReceiveCheckMapper couponReceiveCheckMapper;

    /**
     * 插入数据
     *
     * @param couponReceiveCheckDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCouponReceiveCheck(EsCouponReceiveCheckDTO couponReceiveCheckDTO) {
        try {
            if (couponReceiveCheckDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            // 验证唯一性
            QueryWrapper<EsCouponReceiveCheck> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCouponReceiveCheck::getMobile,couponReceiveCheckDTO.getMobile());
            Integer count = this.couponReceiveCheckMapper.selectCount(queryWrapper);
            if (count > 0){
                throw new ArgumentException(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.hashCode(),"用户已存在");
            }
            EsCouponReceiveCheck couponReceiveCheck = new EsCouponReceiveCheck();
            BeanUtil.copyProperties(couponReceiveCheckDTO, couponReceiveCheck);
            this.couponReceiveCheckMapper.insert(couponReceiveCheck);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.hashCode(), ae.getMessage());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param couponReceiveCheckDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCouponReceiveCheck(EsCouponReceiveCheckDTO couponReceiveCheckDTO) {
        try {
            if (couponReceiveCheckDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCouponReceiveCheck couponReceiveCheck = new EsCouponReceiveCheck();
            BeanUtil.copyProperties(couponReceiveCheckDTO, couponReceiveCheck);
            QueryWrapper<EsCouponReceiveCheck> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCouponReceiveCheck::getId, couponReceiveCheckDTO.getId());
            this.couponReceiveCheckMapper.update(couponReceiveCheck, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    @Override
    public DubboResult getCouponReceiveCheck(Long id) {
        try {
            QueryWrapper<EsCouponReceiveCheck> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCouponReceiveCheck::getId, id);
            EsCouponReceiveCheck couponReceiveCheck = this.couponReceiveCheckMapper.selectOne(queryWrapper);
            EsCouponReceiveCheckDO couponReceiveCheckDO = new EsCouponReceiveCheckDO();
            if (couponReceiveCheck != null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(couponReceiveCheck, couponReceiveCheckDO);
            return DubboResult.success(couponReceiveCheckDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param couponReceiveCheckDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponReceiveCheckDO>
     */
    @Override
    public DubboPageResult getCouponReceiveCheckList(EsCouponReceiveCheckDTO couponReceiveCheckDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCouponReceiveCheck> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

//            Page<EsCouponReceiveCheck> page = new Page<>(pageNum, pageSize);
//            IPage<EsCouponReceiveCheck> iPage = this.page(page, queryWrapper);
            List<EsCouponReceiveCheckDO> couponReceiveCheckDOList = new ArrayList<>();
//            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
//                couponReceiveCheckDOList = iPage.getRecords().stream().map(couponReceiveCheck -> {
//                    EsCouponReceiveCheckDO couponReceiveCheckDO = new EsCouponReceiveCheckDO();
//                    BeanUtil.copyProperties(couponReceiveCheck, couponReceiveCheckDO);
//                    return couponReceiveCheckDO;
//                }).collect(Collectors.toList());
//            }
            return DubboPageResult.success(couponReceiveCheckDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCouponReceiveCheck(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCouponReceiveCheck> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCouponReceiveCheck::getId, id);
            this.couponReceiveCheckMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Boolean> getCouponReceiveCheckByMobile(String mobile) {
        try {
            QueryWrapper<EsCouponReceiveCheck> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCouponReceiveCheck::getMobile, mobile);
            EsCouponReceiveCheck couponReceiveCheck = this.couponReceiveCheckMapper.selectOne(queryWrapper);
            if (couponReceiveCheck != null){
                return DubboResult.success(true);
            }
            return DubboResult.success(false);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
