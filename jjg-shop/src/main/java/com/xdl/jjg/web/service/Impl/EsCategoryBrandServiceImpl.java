package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsBrand;
import com.xdl.jjg.entity.EsCategory;
import com.xdl.jjg.entity.EsCategoryBrand;
import com.xdl.jjg.mapper.EsBrandMapper;
import com.xdl.jjg.mapper.EsCategoryBrandMapper;
import com.xdl.jjg.mapper.EsCategoryMapper;
import com.xdl.jjg.model.domain.EsBrandDO;
import com.xdl.jjg.model.domain.EsBrandSelectDO;
import com.xdl.jjg.model.domain.EsCategoryBrandDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCategoryBrandService;
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
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsCategoryBrandServiceImpl extends ServiceImpl<EsCategoryBrandMapper, EsCategoryBrand> implements IEsCategoryBrandService {

    private static Logger logger = LoggerFactory.getLogger(EsCategoryBrandServiceImpl.class);

    @Autowired
    private EsCategoryBrandMapper categoryBrandMapper;
    @Autowired
    private EsCategoryMapper esCategoryMapper;
    @Autowired
    private EsBrandMapper esBrandMapper;
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboPageResult<EsCategoryBrandDO> saveCategoryBrand(Long categoryId, Integer[] brandId) {
        try{
            //查询分类是否存在
            EsCategory esCategory = this.esCategoryMapper.selectById(categoryId);
            if(esCategory == null ){
                throw  new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类不存在");
            }
            //查询品牌是否存在
            QueryWrapper<EsBrand> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsBrand::getId,brandId);
            List<EsBrand> esBrandList = this.esBrandMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(esBrandList) || ( esBrandList.size() < brandId.length)){
                throw  new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),"品牌参数传入错误");
            }
            //先删除关系表数据
            QueryWrapper<EsCategoryBrand> brandQueryWrapper = new QueryWrapper<>();
            brandQueryWrapper.lambda().eq(EsCategoryBrand::getCategoryId,categoryId);
            this.categoryBrandMapper.delete(brandQueryWrapper);
            List<EsCategoryBrandDO> cateList = new ArrayList<>();
            for (int i = 0; i < brandId.length; i++) {
                EsCategoryBrand esCategoryBrand = new EsCategoryBrand();
                EsCategoryBrandDO categoryBrandDO = new EsCategoryBrandDO();
                esCategoryBrand.setBrandId(brandId[i].longValue());
                esCategoryBrand.setCategoryId(categoryId);
                this.categoryBrandMapper.insert(esCategoryBrand);
                BeanUtil.copyProperties(esCategoryBrand,categoryBrandDO);
                cateList.add(categoryBrandDO);
            }
            return DubboPageResult.success(cateList);
        }catch (ArgumentException ae){
            logger.error("保存商品分类品牌关系表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("保存商品分类品牌关系表失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查询分类品牌，所有品牌，分类绑定的品牌为已选中状态
     * @param cateId
     * @return
     */
    @Override
    public DubboPageResult<EsBrandSelectDO> getCategoryList(Long cateId) {
        try{
            List<EsBrandSelectDO>  selectDOList = this.categoryBrandMapper.getCateBrandSelect(cateId);
            return DubboPageResult.success(selectDOList);
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌，包括未选中的", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌，包括未选中的", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsBrandDO> getBrandsByCategoryList(Long cateId) {
        try{
            List<EsBrandDO>  brandDOList = this.categoryBrandMapper.getBrandsByCategoryList(cateId);
            return DubboPageResult.success(brandDOList);
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
