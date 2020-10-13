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
import com.shopx.goods.api.model.domain.EsSpecValuesDO;
import com.shopx.goods.api.model.domain.dto.EsSpecValuesDTO;
import com.shopx.goods.api.service.IEsSpecValuesService;
import com.shopx.goods.dao.entity.EsSpecValues;
import com.shopx.goods.dao.entity.EsSpecification;
import com.shopx.goods.dao.mapper.EsSpecValuesMapper;
import com.shopx.goods.dao.mapper.EsSpecificationMapper;
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
@Service(version = "${dubbo.application.version}", interfaceClass = IEsSpecValuesService.class, timeout = 50000)
public class EsSpecValuesServiceImpl extends ServiceImpl<EsSpecValuesMapper, EsSpecValues> implements IEsSpecValuesService {

    private static Logger logger = LoggerFactory.getLogger(EsSpecValuesServiceImpl.class);

    @Autowired
    private EsSpecValuesMapper specValuesMapper;
    @Autowired
    private EsSpecificationMapper specificationMapper;

    /**
     *
     * @param specId
     * @param specValues
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecValuesDO> insertSpecValues(Long specId,String[] specValues) {
        try {
           EsSpecification esSpecification = specificationMapper.selectById(specId);
           if(esSpecification == null ){
               throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "规格不存在");
           }
           for(String value:specValues){
               EsSpecValues specification = new EsSpecValues();
               specification.setSpecId(specId);
               specification.setSpecName(esSpecification.getSpecName());
               specification.setSpecValue(value);
               this.save(specification);
           }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增规格值失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("新增规格值失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 新增规格值
     * @param esSpecValuesDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecValuesDO> insertSpecValues(EsSpecValuesDTO esSpecValuesDTO) {
        try{
            EsSpecValues esSpecValues = new EsSpecValues();
            BeanUtil.copyProperties(esSpecValuesDTO,esSpecValues);
            this.save(esSpecValues);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("新增规格值失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("新增规格值失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param specValuesDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecValuesDO> updateSpecValues(EsSpecValuesDTO specValuesDTO,Long id) {
        try {
            EsSpecValues specValues = new EsSpecValues();
            BeanUtil.copyProperties(specValuesDTO, specValues);
            QueryWrapper<EsSpecValues> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSpecValues::getId, id);
            this.specValuesMapper.update(specValues, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("修改规格值失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改规格值失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    @Override
    public DubboResult<EsSpecValuesDO> getSpecValues(Long id) {
        try {
            QueryWrapper<EsSpecValues> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSpecValues::getId, id);
            EsSpecValues specValues = this.specValuesMapper.selectOne(queryWrapper);
            EsSpecValuesDO specValuesDO = new EsSpecValuesDO();
            if (specValues == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(specValues, specValuesDO);
            return DubboResult.success(specValuesDO);
        } catch (ArgumentException ae){
            logger.error("查询规格值失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询规格值失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param specValuesDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsSpecValuesDO>
     */
    @Override
    public DubboPageResult<EsSpecValuesDO> getSpecValuesList(EsSpecValuesDTO specValuesDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSpecValues> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSpecValues> page = new Page<>(pageNum, pageSize);
            IPage<EsSpecValues> iPage = this.page(page, queryWrapper);
            List<EsSpecValuesDO> specValuesDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                specValuesDOList = iPage.getRecords().stream().map(specValues -> {
                    EsSpecValuesDO specValuesDO = new EsSpecValuesDO();
                    BeanUtil.copyProperties(specValues, specValuesDO);
                    return specValuesDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),specValuesDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询规格值失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询规格值失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsSpecValuesDO> getSpecValuesList(Long specId) {
        try {
            QueryWrapper<EsSpecValues> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSpecValues::getSpecId,specId);
            List<EsSpecValues> esSpecValuesList = this.list(queryWrapper);
            List<EsSpecValuesDO> esSpecValuesDOList = esSpecValuesList.stream().map(esSpecValues -> {
                EsSpecValuesDO esSpecValuesDO = new EsSpecValuesDO();
                BeanUtil.copyProperties(esSpecValues,esSpecValuesDO);
                return esSpecValuesDO;
            }).collect(Collectors.toList());
            return  DubboPageResult.success(esSpecValuesDOList);
        } catch (ArgumentException ae){
            logger.error("查询某个规格的规格值失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询某个规格的规格值失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecValuesDO> deleteSpecValues(Long id) {
        try {
            QueryWrapper<EsSpecValues> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSpecValues::getId, id);
            this.specValuesMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除规格值失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除规格值失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
