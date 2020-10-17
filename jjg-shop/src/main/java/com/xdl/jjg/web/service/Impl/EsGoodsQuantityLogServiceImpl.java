package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsGoodsQuantityLogDO;
import com.jjg.shop.model.dto.EsGoodsQuantityLogDTO;
import com.xdl.jjg.entity.EsGoodsQuantityLog;
import com.xdl.jjg.mapper.EsGoodsQuantityLogMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGoodsQuantityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-16 09:52:07
 */
@Service
public class EsGoodsQuantityLogServiceImpl extends ServiceImpl<EsGoodsQuantityLogMapper, EsGoodsQuantityLog> implements IEsGoodsQuantityLogService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsQuantityLogServiceImpl.class);

    @Autowired
    private EsGoodsQuantityLogMapper goodsQuantityLogMapper;

    /**
     * 插入数据
     *
     * @param goodsQuantityLogDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsQuantityLogDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsQuantityLog(EsGoodsQuantityLogDTO goodsQuantityLogDTO) {
        try {
            EsGoodsQuantityLog goodsQuantityLog = new EsGoodsQuantityLog();
            BeanUtil.copyProperties(goodsQuantityLogDTO, goodsQuantityLog);
            this.goodsQuantityLogMapper.insert(goodsQuantityLog);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据id获取详情
     *
     * @param orderSn
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsQuantityLogDO>
     */
    @Override
    public DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLog(String orderSn, Long skuId) {
        try {
            QueryWrapper<EsGoodsQuantityLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getOrderSn, orderSn);
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getType,0);
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getSkuId,skuId);
            EsGoodsQuantityLog goodsQuantityLog = this.goodsQuantityLogMapper.selectOne(queryWrapper);
            EsGoodsQuantityLogDO goodsQuantityLogDO = new EsGoodsQuantityLogDO();
            if (goodsQuantityLog == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsQuantityLog, goodsQuantityLogDO);
            return DubboResult.success(goodsQuantityLogDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLogByGoodsId(String orderSn,Long goodsId) {
        try {
            QueryWrapper<EsGoodsQuantityLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getOrderSn, orderSn);
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getType,0);
            queryWrapper.lambda().eq(EsGoodsQuantityLog::getGoodsId,goodsId);
            EsGoodsQuantityLog goodsQuantityLog = this.goodsQuantityLogMapper.selectOne(queryWrapper);
            EsGoodsQuantityLogDO goodsQuantityLogDO = new EsGoodsQuantityLogDO();
            if (goodsQuantityLog == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsQuantityLog, goodsQuantityLogDO);
            return DubboResult.success(goodsQuantityLogDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
