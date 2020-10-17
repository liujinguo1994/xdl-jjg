package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsSupplierDO;
import com.jjg.shop.model.dto.EsSupplierDTO;
import com.xdl.jjg.entity.EsSupplier;
import com.xdl.jjg.mapper.EsSupplierMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.web.service.IEsSupplierService;
import org.apache.commons.lang3.StringUtils;
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
 * 供应商 服务实现类
 * </p>
 *
 * @author wangaf
 * @since 2019-05-29
 */
@Service
public class EsSupplierServiceImpl extends ServiceImpl<EsSupplierMapper, EsSupplier> implements IEsSupplierService {

    private Logger logger =  LoggerFactory.getLogger(EsSupplierServiceImpl.class);
    @Autowired
    private EsSupplierMapper esSupplierMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public  DubboResult<EsSupplierDO>  insertSupplier(EsSupplierDTO supplierDTO) {
        try{
            EsSupplier esSupplier = new EsSupplier();
            //DTO 转 Entity
            BeanUtil.copyProperties(supplierDTO,esSupplier);
            esSupplier.setState(0);
            esSupplier.setSupplierCode( new SnowflakeIdWorker(1,1).nextId()+"");
            this.esSupplierMapper.insert(esSupplier);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("供应商信息插入失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商信息插入失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return  DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改供应商
     * @param supplierDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public  DubboResult<EsSupplierDO>  updateSupplier(EsSupplierDTO supplierDTO,Long id) {
        try{
            EsSupplier esSupplier = this.getById(id);
            if(esSupplier == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"供应商信息不存在");
            }
            //DTO 转 Entity
            BeanUtil.copyProperties(supplierDTO,esSupplier);
            //根据供应商ID 修改供应商信息
            this.esSupplierMapper.updateById(esSupplier);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("供应商信息更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商信息更新失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 逻辑删除供应商
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsSupplierDO> deleteSupplier(Integer[] ids) {
        try{
            //修改供应商为无效状态
            QueryWrapper<EsSupplier> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSupplier::getId,ids);
            this.remove(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("供应商信息删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商信息删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
    @Override
    public DubboResult<EsSupplierDO> revertSupplier(Integer[] ids) {
        try{
            QueryWrapper<EsSupplier> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSupplier::getId,ids);
            List<EsSupplier> supplierList = this.esSupplierMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(supplierList)){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "供应商信息不存在，启用失败");
            }
            supplierList.stream().forEach(supplier -> {
                supplier.setState(0);
            });
            this.updateBatchById(supplierList,supplierList.size());
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("供应商信息删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商信息删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsSupplierDO> prohibitSupplier(Integer[] ids) {
        try{
            //修改供应商为无效状态
            QueryWrapper<EsSupplier> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSupplier::getId,ids);
            List<EsSupplier> supplierList = this.esSupplierMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(supplierList)){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "供应商信息不存在，删除失败");
            }
            supplierList.stream().forEach(supplier -> {
                supplier.setState(1);
            });
            this.updateBatchById(supplierList,supplierList.size());
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("供应商信息删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商信息删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据供应商编号获取供应商信息
     * @param id
     * @return
     */
    @Override
    public DubboResult<EsSupplierDO> getSupplier(Long id) {
       try{
           QueryWrapper<EsSupplier> queryWrapper = new QueryWrapper<>();
           queryWrapper.lambda().eq(EsSupplier::getId,id).eq(EsSupplier::getState,0);
           EsSupplier esSupplier =  this.esSupplierMapper.selectOne(queryWrapper);
           EsSupplierDO esSupplierDO = new EsSupplierDO();
           if(esSupplier == null){
               throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
           }
           BeanUtil.copyProperties(esSupplier,esSupplierDO);
           return DubboResult.success(esSupplierDO);
       }catch (ArgumentException ae){
           logger.error("获取供应商信息失败",ae);
           return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
       }catch(Throwable th){
           logger.error("获取供应商信息失败",th);
           return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
       }
    }

    @Override
    public DubboPageResult<EsSupplierDO> getSupplierList(EsSupplierDTO supplierDTO,int pageSize,int pageNum) {
        QueryWrapper<EsSupplier> queryWrapper = new QueryWrapper<>();
        try{
            //根据供应商ID排序 降序
            queryWrapper.orderByDesc("id");
            Page<EsSupplier> page = new Page<>(pageNum,pageSize);
            if(!StringUtils.isBlank(supplierDTO.getKeyword())){
                queryWrapper.lambda().eq(EsSupplier::getSupplierCode,supplierDTO.getKeyword()).or()
                        .eq(EsSupplier::getContactName,supplierDTO.getKeyword()).or()
                        .eq(EsSupplier::getMobile,supplierDTO.getKeyword());
            }
            //queryWrapper.lambda().eq(EsSupplier::getState,0);
            IPage<EsSupplier> supplierList =  this.page(page,queryWrapper);
            List<EsSupplierDO> suppliersDoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(supplierList.getRecords())){
                suppliersDoList = supplierList.getRecords().stream().map(esSupplier -> {
                    EsSupplierDO supplierDO = new EsSupplierDO();
                    BeanUtil.copyProperties(esSupplier,supplierDO);
                    return supplierDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(suppliersDoList);
        }catch (ArgumentException ae){
            logger.error("供应商分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("供应商分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
}
