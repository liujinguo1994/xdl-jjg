package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsWarnStockDO;
import com.shopx.goods.api.model.domain.dto.EsWarnStockDTO;
import com.shopx.goods.api.service.IEsWarnStockService;
import com.shopx.goods.dao.entity.EsWarnStock;
import com.shopx.goods.dao.mapper.EsWarnStockMapper;
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
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsWarnStockService.class, timeout = 50000)
public class EsWarnStockServiceImpl extends ServiceImpl<EsWarnStockMapper, EsWarnStock> implements IEsWarnStockService {

    private static Logger logger = LoggerFactory.getLogger(EsWarnStockServiceImpl.class);

    @Autowired
    private EsWarnStockMapper warnStockMapper;

    /**
     * 插入数据
     *
     * @param warnStockDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsWarnStockDO> insertWarnStock(EsWarnStockDTO warnStockDTO) {
        try {
            EsWarnStock warnStock = new EsWarnStock();
            BeanUtil.copyProperties(warnStockDTO, warnStock);
            this.warnStockMapper.insert(warnStock);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param warnStockDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsWarnStockDO> updateWarnStock(EsWarnStockDTO warnStockDTO,Long id) {
        try {
            EsWarnStock warnStock = new EsWarnStock();
            BeanUtil.copyProperties(warnStockDTO, warnStock);
            QueryWrapper<EsWarnStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsWarnStock::getId,id);
            this.warnStockMapper.update(warnStock, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    @Override
    public DubboResult<EsWarnStockDO> getWarnStock(Long id) {
        try {
            QueryWrapper<EsWarnStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsWarnStock::getId, id);
            EsWarnStock warnStock = this.warnStockMapper.selectOne(queryWrapper);
            EsWarnStockDO warnStockDO = new EsWarnStockDO();
            if (warnStock == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(warnStock, warnStockDO);
            return DubboResult.success(warnStockDO);
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param warnStockDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsWarnStockDO>
     */
    @Override
    public DubboPageResult<EsWarnStockDO> getWarnStockList(EsWarnStockDTO warnStockDTO, int pageSize, int pageNum) {
        QueryWrapper<EsWarnStock> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsWarnStock> page = new Page<>(pageNum, pageSize);
            IPage<EsWarnStock> iPage = this.page(page, queryWrapper);
            List<EsWarnStockDO> warnStockDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                warnStockDOList = iPage.getRecords().stream().map(warnStock -> {
                    EsWarnStockDO warnStockDO = new EsWarnStockDO();
                    BeanUtil.copyProperties(warnStock, warnStockDO);
                    return warnStockDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),warnStockDOList);
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsWarnStockDO> deleteWarnStock(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsWarnStock> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsWarnStock::getId, id);
            this.warnStockMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
