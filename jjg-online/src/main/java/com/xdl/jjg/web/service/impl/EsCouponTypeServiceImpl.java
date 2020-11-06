package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsCouponTypeDO;
import com.jjg.trade.model.dto.EsCouponTypeDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsCouponType;
import com.xdl.jjg.mapper.EsCouponTypeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCouponTypeService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:46
 */
@Service
public class EsCouponTypeServiceImpl extends ServiceImpl<EsCouponTypeMapper, EsCouponType> implements IEsCouponTypeService {

    private static Logger logger = LoggerFactory.getLogger(EsCouponTypeServiceImpl.class);

    @Autowired
    private EsCouponTypeMapper couponTypeMapper;

    /**
     * 插入数据
     *
     * @param couponTypeDTO DTO
     * @auther: LBW 981087977@qq.com
     * @date: 2019-11-21 10:38:46
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCouponType(EsCouponTypeDTO couponTypeDTO) {
        try {
            EsCouponType couponType = new EsCouponType();
            BeanUtil.copyProperties(couponTypeDTO, couponType);
            this.couponTypeMapper.insert(couponType);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param couponTypeDTO DTO
     * @param id                          主键id
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCouponType(EsCouponTypeDTO couponTypeDTO, Long id) {
        try {
            EsCouponType couponType = this.couponTypeMapper.selectById(id);
            if (couponType == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(couponTypeDTO, couponType);
            QueryWrapper<EsCouponType> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCouponType::getId, id);
            this.couponTypeMapper.update(couponType, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    @Override
    public DubboResult<EsCouponTypeDO> getCouponType(Long id) {
        try {
            EsCouponType couponType = this.couponTypeMapper.selectById(id);
            if (couponType == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsCouponTypeDO couponTypeDO = new EsCouponTypeDO();
            BeanUtil.copyProperties(couponType, couponTypeDO);
            return DubboResult.success(couponTypeDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: LBW 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponTypeDO>
     */
    @Override
    public DubboPageResult<EsCouponTypeDO> getCouponTypeList() {
        try {
            List<EsCouponType> couponTypeList = this.list();
            List<EsCouponTypeDO> couponTypeDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(couponTypeList)) {
                couponTypeDOList = couponTypeList.stream().map(couponType -> {
                    EsCouponTypeDO couponTypeDO = new EsCouponTypeDO();
                    BeanUtil.copyProperties(couponType, couponTypeDO);
                    return couponTypeDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponTypeDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCouponType(Long id) {
        try {
            this.couponTypeMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
