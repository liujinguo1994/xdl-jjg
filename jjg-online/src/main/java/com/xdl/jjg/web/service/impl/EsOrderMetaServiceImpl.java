package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsOrderMetaDO;
import com.jjg.trade.model.dto.EsOrderMetaDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsOrderMeta;
import com.xdl.jjg.mapper.EsOrderMetaMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsOrderMetaService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * 订单拓展信息信息表 服务实现类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Service
public class EsOrderMetaServiceImpl extends ServiceImpl<EsOrderMetaMapper, EsOrderMeta> implements IEsOrderMetaService {

    private static Logger logger = LoggerFactory.getLogger(EsOrderMetaServiceImpl.class);

    /**
     * 插入订单拓展信息
     * @param esOrderMetaDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderMetaDO> insertOrderMeta(EsOrderMetaDTO esOrderMetaDTO) {
        try {

            //TODO 判断订单是否存在（订单删除状态）
            if (esOrderMetaDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(),"参数错误");
            }
            EsOrderMeta esOrderMeta = new EsOrderMeta();

            BeanUtils.copyProperties(esOrderMetaDTO,esOrderMeta);

            this.baseMapper.insert(esOrderMeta);
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("保存订单拓展信息失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SAVE_ORDER_ERROR.getErrorCode(), TradeErrorCode.SAVE_ORDER_ERROR.getErrorMsg());
        } catch (Throwable th) {
            logger.error("保存订单拓展信息失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 删除订单拓展信息
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderMetaDO> deleteOrderMeta(Integer id) {
        try {
            this.baseMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除订单拓展信息失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("删除订单拓展信息失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderMetaDO> deleteOrderMetaByOrderSn(String orderSn) {
        try {
            QueryWrapper<EsOrderMeta> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderMeta::getOrderSn,orderSn);
            this.baseMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除订单拓展信息失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("删除订单拓展信息失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderMetaDO> updateOrderMetaMessage(EsOrderMetaDTO esOrderMetaDTO) {
        try {
            if (esOrderMetaDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOrderMeta esOrderMeta = new EsOrderMeta();

            BeanUtils.copyProperties(esOrderMetaDTO,esOrderMeta);

            QueryWrapper<EsOrderMeta> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderMeta::getId,esOrderMetaDTO.getId());
            this.baseMapper.update(esOrderMeta,queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单拓展信息失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("修改订单拓展信息失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询订单拓展信息表
     * @param id
     * @return
     */
    @Override
    public DubboResult<EsOrderMetaDO> getOrderMetaInfo(Long id) {
        try {
            QueryWrapper<EsOrderMeta> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderMeta::getId,id);
            EsOrderMeta esOrderMeta = this.getOne(queryWrapper);
            //出仓对象转DO
            EsOrderMetaDO esOrderMetaDO = new EsOrderMetaDO();
            if(esOrderMeta != null){
                BeanUtils.copyProperties(esOrderMeta,esOrderMetaDO);
            }
            return DubboResult.success(esOrderMetaDO);
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }


    /**
     * @Description：
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/6 11:28
     * @param        orderSn 订单编号
     *                type 赠品Key
     * @return
     * @exception
     *
     */
    @Override
    public DubboResult<EsOrderMetaDO> getOrderMetaByOrderSnAndMetaKey(String orderSn, String type) {
        try {
            QueryWrapper<EsOrderMeta> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderMeta::getOrderSn,orderSn).eq(EsOrderMeta::getMetaKey,type);
            EsOrderMeta esOrderMeta = this.baseMapper.selectOne(queryWrapper);
//            if (esOrderMeta == null){
//                throw new ArgumentException(TradeErrorCode.META_IS_NOT_EXIST.getErrorCode(),TradeErrorCode.META_IS_NOT_EXIST.getErrorMsg());
//            }
            EsOrderMetaDO esOrderMetaDO = new EsOrderMetaDO();

            BeanUtils.copyProperties(esOrderMeta,esOrderMetaDO);

            return DubboResult.success(esOrderMetaDO);
        } catch (ArgumentException ae) {
            logger.error("订单拓展信息不存在",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
}
