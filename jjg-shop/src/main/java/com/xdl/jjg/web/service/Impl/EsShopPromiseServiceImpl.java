package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsShopPromiseDO;
import com.jjg.shop.model.dto.EsShopPromiseDTO;
import com.xdl.jjg.entity.EsShopPromise;
import com.xdl.jjg.mapper.EsShopPromiseMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsShopPromiseService;
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
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:17
 */
@Service
public class EsShopPromiseServiceImpl extends ServiceImpl<EsShopPromiseMapper, EsShopPromise> implements IEsShopPromiseService {

    private static Logger logger = LoggerFactory.getLogger(EsShopPromiseServiceImpl.class);

    @Autowired
    private EsShopPromiseMapper shopPromiseMapper;

    /**
     * 插入数据
     *
     * @param shopPromiseDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopPromise(EsShopPromiseDTO shopPromiseDTO) {
        try {
            EsShopPromise shopPromise = new EsShopPromise();
            BeanUtil.copyProperties(shopPromiseDTO, shopPromise);
            this.shopPromiseMapper.insert(shopPromise);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增卖家承诺失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增卖家承诺失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param shopPromiseDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopPromise(EsShopPromiseDTO shopPromiseDTO, Long id) {
        try {
            EsShopPromise esShopPromise = this.getById(id);
            if(esShopPromise == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            //无权限操作
            if(esShopPromise.getShopId() != shopPromiseDTO.getShopId()){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(), GoodsErrorCode.NO_AUTH.getErrorMsg());
            }
            BeanUtil.copyProperties(shopPromiseDTO,esShopPromise);
            this.shopPromiseMapper.updateById(esShopPromise);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新卖家承诺失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新卖家承诺失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    @Override
    public DubboResult<EsShopPromiseDO> getShopPromise(Long id) {
        try {
            EsShopPromise shopPromise = this.getById(id);
            EsShopPromiseDO shopPromiseDO = new EsShopPromiseDO();
            if (shopPromise == null || shopPromise.getState() == 2) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopPromise, shopPromiseDO);
            return DubboResult.success(shopPromiseDO);
        } catch (ArgumentException ae){
            logger.error("查询卖家承诺失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询卖家承诺失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *

     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopPromiseDO>
     */
    @Override
    public DubboPageResult<EsShopPromiseDO> getShopPromiseList(Long shopId) {
        try {
            QueryWrapper<EsShopPromise> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopPromise::getShopId,shopId);
            List<EsShopPromise> promiseList = this.list(queryWrapper);
            List<EsShopPromiseDO> shopPromiseDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(promiseList)) {
                shopPromiseDOList = promiseList.stream().map(shopPromise -> {
                    EsShopPromiseDO shopPromiseDO = new EsShopPromiseDO();
                    BeanUtil.copyProperties(shopPromise, shopPromiseDO);
                    return shopPromiseDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(shopPromiseDOList);
        } catch (ArgumentException ae){
            logger.error("查询卖家承诺列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询卖家承诺列表失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopPromiseDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopPromise(Long id,Long shopId) {
        try {
            QueryWrapper<EsShopPromise> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopPromise::getId, id).eq(EsShopPromise::getShopId,shopId);
            EsShopPromise esShopPromise = this.shopPromiseMapper.selectOne(queryWrapper);
            if(esShopPromise == null){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(), GoodsErrorCode.NO_AUTH.getErrorMsg());
            }
            this.shopPromiseMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除卖家承诺失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除卖家承诺失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
