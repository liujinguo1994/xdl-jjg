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
import com.shopx.goods.api.model.domain.EsBrandDO;
import com.shopx.goods.api.model.domain.dto.EsBrandDTO;
import com.shopx.goods.api.service.IEsBrandService;
import com.shopx.goods.dao.entity.EsBrand;
import com.shopx.goods.dao.entity.EsCategory;
import com.shopx.goods.dao.mapper.EsBrandMapper;
import com.shopx.goods.dao.mapper.EsCategoryMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
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
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsBrandService.class,timeout = 5000)
public class EsBrandServiceImpl extends ServiceImpl<EsBrandMapper, EsBrand> implements IEsBrandService {

    private Logger logger = LoggerFactory.getLogger(IEsBrandService.class);
    @Autowired
    private EsBrandMapper esBrandMapper;
    @Autowired
    private EsCategoryMapper esCategoryMapper;

    /**
     * 新增品牌
     * @param brandDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsBrandDO> insertBrand(EsBrandDTO brandDTO) {
       try{
           EsBrand esBrand = new EsBrand();
           BeanUtil.copyProperties(brandDTO,esBrand);
           esBrand.setIsDel(0);
           this.esBrandMapper.insert(esBrand);
           return DubboResult.success();
       }catch (ArgumentException ae){
           logger.error("品牌插入失败",ae);
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
       }catch (Throwable th){
           logger.error("品牌插入失败",th);
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
       }
    }

    /**
     * 根据品牌ID 修改品牌信息
     * @param brandDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsBrandDO> updateBrand(EsBrandDTO brandDTO,Long id) {
        try{
            EsBrand esBrand = new EsBrand();
            BeanUtil.copyProperties(brandDTO,esBrand);
            QueryWrapper<EsBrand> queryWrapper  = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsBrand::getId,id);
            this.esBrandMapper.update(esBrand,queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("品牌更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("品牌更新失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据品牌ID 逻辑删除品牌信息
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsBrandDO> deleteBrand(Integer[] ids) {
        try{
            QueryWrapper<EsBrand> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().in(EsBrand::getId,ids);
            this.esBrandMapper.delete(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("品牌删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("品牌删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据品牌ID 获取品牌详细信息
     * @param id
     * @return
     */
    @Override
    public DubboResult<EsBrandDO> getBrand(Long id) {
        try {
            EsBrand brand =this.getById(id);
            EsBrandDO brandDo = new EsBrandDO();
            if(brand == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(brand,brandDo);
            return DubboResult.success(brandDo);
        }catch (ArgumentException ae){
            logger.error("品牌查询失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("品牌查询失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    /**
     * 分页查询品牌
     * 支持按照 name 模糊查询
     * 支持按照 id 精确查询
     * @param brandDTO
     * @return
     */
    @Override
    public DubboPageResult<EsBrandDO> getBrandList(EsBrandDTO brandDTO,int pageSize,int pageNum) {
        QueryWrapper<EsBrand> queryWrapper = new QueryWrapper<>();
        try{
            if(StringUtils.isNotEmpty(brandDTO.getName())){
                queryWrapper.lambda().like(EsBrand::getName,brandDTO.getName());
            }
            Page<EsBrand> page = new Page<>(pageNum,pageSize);  // 查询第1页，每页返回5条
            IPage<EsBrand> iPage = this.page(page,queryWrapper);
            List<EsBrandDO> brandDoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(iPage.getRecords())){
                brandDoList = iPage.getRecords().stream().map(esBrand -> {
                    EsBrandDO brandDO = new EsBrandDO();
                    BeanUtil.copyProperties(esBrand,brandDO);
                    return brandDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),brandDoList);
        }catch (ArgumentException ae){
            logger.error("商品品牌分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品品牌分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsBrandDO> getBrandsByCategory(Long categoryId) {
        try{
            EsCategory category = esCategoryMapper.selectById(categoryId);
            List<EsBrandDO> brandDOList = new ArrayList<>();
            if(category != null){
                String[] path = category.getCategoryPath().split("\\|");
                if(path.length >= 2){
                    brandDOList =  this.esBrandMapper.getBrandsByCategory(Long.valueOf(path[1]));
                }
            }
            return DubboPageResult.success(brandDOList);
        }catch (ArgumentException ae){
            logger.error("商品品牌分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品品牌分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsBrandDO> getAllBrandList() {
        try{
            List<EsBrand> brandList =  this.list();
            List<EsBrandDO> brandDOList = BeanUtil.copyList(brandList,EsBrandDO.class);
            return DubboPageResult.success(brandDOList);
        }catch (ArgumentException ae){
            logger.error("商品品牌查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品品牌查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsBrandDO> getBrandByIds(List<Long> ids) {
        try{
            QueryWrapper<EsBrand> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsBrand::getId,ids);
            List<EsBrand> brandList =  this.list(queryWrapper);
            List<EsBrandDO> brandDOList = BeanUtil.copyList(brandList,EsBrandDO.class);
            return DubboPageResult.success(brandDOList);
        }catch (ArgumentException ae){
            logger.error("商品品牌查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品品牌查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
}
