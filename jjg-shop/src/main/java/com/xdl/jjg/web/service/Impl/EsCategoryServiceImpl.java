package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.*;
import com.shopx.goods.api.model.domain.cache.ESCategoryChildrenCO;
import com.shopx.goods.api.model.domain.cache.EsCategoryCO;
import com.shopx.goods.api.model.domain.dto.EsCategoryDTO;
import com.shopx.goods.api.model.domain.dto.EsGoodsQueryDTO;
import com.shopx.goods.api.model.domain.enums.GoodsCachePrefix;
import com.shopx.goods.api.model.domain.vo.EsCategoryVO;
import com.shopx.goods.api.service.IEsCategoryBrandService;
import com.shopx.goods.api.service.IEsCategoryService;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsParameterGroupService;
import com.shopx.goods.dao.entity.EsCategory;
import com.shopx.goods.dao.entity.EsCategorySpec;
import com.shopx.goods.dao.entity.EsParameterGroup;
import com.shopx.goods.dao.entity.EsSpecification;
import com.shopx.goods.dao.mapper.*;
import com.shopx.member.api.model.domain.EsDiscountDO;
import com.shopx.member.api.service.IEsDiscountService;
import com.shopx.system.api.model.enums.CachePrefix;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsCategoryService.class, timeout = 50000)
public class EsCategoryServiceImpl extends ServiceImpl<EsCategoryMapper, EsCategory> implements IEsCategoryService {

    private static Logger logger = LoggerFactory.getLogger(EsCategoryServiceImpl.class);

    @Autowired
    private EsCategoryMapper categoryMapper;
    @Autowired
    private IEsGoodsService esGoodsService;

    @Autowired
    private EsSpecificationMapper esSpecificationMapper;
    @Autowired
    private EsCategorySpecMapper esCategorySpecMapper;
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsDiscountService esDiscountService;
    @Autowired
    private IEsParameterGroupService esParameterGroupService;
    @Autowired
    private JedisCluster jedisCluster;

    private final String CATEGORY_CACHE_ALL = GoodsCachePrefix.GOODS_CAT.getPrefix() + "ALL";

    @Value("${zhuox.redis.expire}")
    private int TIME_OUT;

    @Autowired
    private IEsCategoryBrandService esCategoryBrandService;
    @Autowired
    private EsParameterGroupMapper esParameterGroupMapper;
    @Autowired
    private EsParametersMapper esParametersMapper;
    /**
     * 新增商品分类
     *
     * @param categoryDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsCategoryDO> insertCategory(EsCategoryDTO categoryDTO) {
        try {
           //不是顶级分类 查询子分类
            EsCategory parentCategory = null;
            if(categoryDTO.getParentId() != null  && categoryDTO.getParentId() != 0){
              QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
              queryWrapper.lambda().eq(EsCategory::getId,categoryDTO.getParentId());
                parentCategory= this.categoryMapper.selectOne(queryWrapper);
              if(parentCategory == null ){
                  throw new ArgumentException(GoodsErrorCode.CATEGORY_NOT_EXIST.getErrorCode(), GoodsErrorCode.CATEGORY_NOT_EXIST.getErrorMsg());
              }
              String[]  temp = parentCategory.getCategoryPath().split("\\|");
              //商品分类不允许超过三级
              if(temp.length >3){
                  throw new ArgumentException(GoodsErrorCode.CATEGORY_MAX.getErrorCode(), GoodsErrorCode.CATEGORY_MAX.getErrorMsg());
              }
          }
            EsCategory esCategory = new EsCategory();
            BeanUtil.copyProperties(categoryDTO,esCategory);
            this.save(esCategory);
            if(parentCategory != null ){
                esCategory.setCategoryPath(parentCategory.getCategoryPath()+esCategory.getId()+"|");
            }else{
                esCategory.setCategoryPath("0|" + esCategory.getId() +"|");
            }
            this.updateById(esCategory);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增商品分类失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增商品分类失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据条件更新数据
     *
     * @param categoryDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsCategoryDO> updateCategory(EsCategoryDTO categoryDTO,Long id) {
        try {
            EsCategory tempCategory = this.getById(id);
            EsCategory parent = null;
            //更换上级分类
            if(categoryDTO.getParentId().longValue() != tempCategory.getParentId().longValue()){
                //查询是否有子类
                List<EsCategoryDO> esCategoryDOList = this.getCategoryParentList(id).getData().getList();
                if(CollectionUtils.isNotEmpty(esCategoryDOList)){
                    throw new ArgumentException(GoodsErrorCode.EXIST_CHILDREN.getErrorCode(), GoodsErrorCode.EXIST_CHILDREN.getErrorMsg());
                }else{
                    parent  = this.getById(categoryDTO.getParentId());
                    if(parent == null ){
                        throw new ArgumentException(GoodsErrorCode.CATEGORY_NOT_EXIST.getErrorCode(), GoodsErrorCode.CATEGORY_NOT_EXIST.getErrorMsg());
                    }
                    String[]  temp = parent.getCategoryPath().split("\\|");
                    if(temp.length > 3){
                        throw new ArgumentException(GoodsErrorCode.CATEGORY_MAX.getErrorCode(),"商品分类最大不能超过三级");
                    }
                    categoryDTO.setCategoryPath(parent.getCategoryPath() + categoryDTO.getId() + "|");
                }
            }
            EsCategory category = new EsCategory();
            BeanUtil.copyProperties(categoryDTO,category);
            category.setId(id);
            this.updateById(category);
            //根据分类ID获取折扣表分类是否存在 存在则修改分类名称
            DubboResult<EsDiscountDO> discountResult = this.esDiscountService.getByCategoryId(id);
            if(discountResult.isSuccess() && discountResult.getData() != null){
                this.esDiscountService.updateByCategoryId(id,category.getName());
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新商品分类失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新商品分类失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsCategoryDO> deleteCategory(Long id) {
        try {
            List<EsCategoryDO> categoryDOList = this.getCategoryChildrenList(id);
            //存在子分类不能删除
            if(CollectionUtils.isNotEmpty(categoryDOList)){
                throw new ArgumentException(GoodsErrorCode.EXIST_CHILDREN.getErrorCode(), "该分类下存在子分类不允许删除");
            }
            //根据分类ID获取折扣表分类是否存在 存在则不允许删除
            DubboResult<EsDiscountDO> discountResult = this.esDiscountService.getByCategoryId(id);
            if(discountResult.isSuccess() && discountResult.getData() != null){
                throw new ArgumentException(GoodsErrorCode.EXIST_DISCOUNT.getErrorCode(),GoodsErrorCode.EXIST_DISCOUNT.getErrorMsg());
            }
            //判断该分类下是否有商品
            EsGoodsQueryDTO esGoodsDTO = new EsGoodsQueryDTO();
            esGoodsDTO.setCategoryId(id);
            DubboPageResult<EsGoodsDO> esGoodsDOList= esGoodsService.sellerGetEsGoodsList(esGoodsDTO);
            if(CollectionUtils.isNotEmpty(esGoodsDOList.getData().getList())){
                throw new ArgumentException(GoodsErrorCode.CATEGORY_EXIST_GOODS.getErrorCode(), GoodsErrorCode.CATEGORY_EXIST_GOODS.getErrorMsg());
            }
            QueryWrapper<EsCategory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCategory::getId, id);
            this.categoryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品分类删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品分类删除失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    @Override
    public DubboResult<EsCategoryDO> getCategory(Long id) {
        try {
            EsCategory category = this.getById(id);
            EsCategoryDO categoryDO = new EsCategoryDO();
            if (category == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(category, categoryDO);

           DubboPageResult<ParameterGroupDO> result = esParameterGroupService.getParameterGroupList(id);
           if(result.isSuccess()){
               categoryDO.setParameterList(result.getData().getList());
           }
            return DubboResult.success(categoryDO);
        } catch (ArgumentException ae){
            logger.error("查询商品分类失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询商品分类失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param categoryId DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsCategoryDO>
     */
    @Override
    public DubboPageResult<EsCategoryDO> getCategoryList(Long categoryId) {
        QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().like(EsCategory::getCategoryPath,categoryId);
            // 查询条件
            List<EsCategory> categoryList = this.list(queryWrapper);
            List<EsCategoryDO> categoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(categoryList)) {
                categoryDOList = categoryList.stream().map(category -> {
                    EsCategoryDO categoryDO = new EsCategoryDO();
                    BeanUtil.copyProperties(category, categoryDO);
                    return categoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(categoryDOList);
        } catch (ArgumentException ae){
            logger.error("商品分类分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据父ID获取分类下面的子类
     * @param id 主键ID
     * @return
     */
    public DubboPageResult<EsCategoryDO> getCategoryParentList(Long id) {
        //根据父ID 查询分类
        try {
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCategory::getParentId,id);
            queryWrapper.lambda().orderByAsc(EsCategory::getCategoryOrder);
            List<EsCategory> esCategoryList = this.list(queryWrapper);
            List<EsCategoryDO> esCategoryDOList = esCategoryList.stream().map(category -> {
                EsCategoryDO esCategoryDO = new EsCategoryDO();
                BeanUtil.copyProperties(category,esCategoryDO);
                return esCategoryDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esCategoryDOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }
    /**
     * 查询所有分类 父子关系
     * @param parentId
     * @return
     */
    @Override
    public DubboPageResult<EsCategoryDO> getCategoryChildren(Long parentId) {
        try{
            //从缓存中获取商品分类
            List<EsCategoryDO> cateList = getCategory();
            List<EsCategoryDO> categoryDOList = cateList.stream().filter(
                    category -> (category.getParentId().compareTo(parentId)==0)
            ).map(cate -> {
                EsCategoryDO esCategoryDO = new EsCategoryDO();
                BeanUtil.copyProperties(cate,esCategoryDO);
                List<EsCategoryDO> list =this.getChildren(cateList,cate.getId());
                esCategoryDO.setChildren(list);
                return esCategoryDO;
            }).collect(Collectors.toList());
            return  DubboPageResult.success(categoryDOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 查询所有分类及子分类 树形结构
     * @return
     */
    @Override
    public DubboPageResult<EsCategoryCO> getCategoryList() {
        try{
            List<EsCategoryCO> dataList = new ArrayList<>();
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            //查询所有一级分类
            queryWrapper.lambda().eq(EsCategory::getParentId,0);
            List<EsCategory> list = this.list(queryWrapper);
            dataList = list.stream().map(esCategory ->{
                EsCategoryCO cacheDO = new EsCategoryCO();
                cacheDO.setValue(esCategory.getId());
                cacheDO.setLabel(esCategory.getName());
                //二级分类
                List<EsCategory> categoryList = this.queryCategiryList(esCategory.getId());
                    List<ESCategoryChildrenCO> childrenList = categoryList.stream().map(category -> {
                        ESCategoryChildrenCO childrenDO = new ESCategoryChildrenCO();
                        childrenDO.setLabel(category.getName());
                        childrenDO.setValue(category.getId());
                    //三级分类
                    List<EsCategory> esCategoryList =  this.queryCategiryList(category.getId());
                    List<ESCategoryChildrenCO> childrenDOList= esCategoryList.stream().map(cate -> {
                        ESCategoryChildrenCO categoryChildrenDO = new ESCategoryChildrenCO();
                        categoryChildrenDO.setLabel(cate.getName());
                        categoryChildrenDO.setValue(cate.getId());
                        return categoryChildrenDO;
                    }).collect(Collectors.toList());
                    childrenDO.setChildrenDO(childrenDOList);
                    return childrenDO;
                }).collect(Collectors.toList());
                cacheDO.setChildrenDO(childrenList);
                return cacheDO;
            }).collect(Collectors.toList());
            //将所有分类缓存 树形结构
            jedisCluster.set(CATEGORY_CACHE_ALL,JsonUtil.objectToJson(dataList));
            return DubboPageResult.success(dataList);
        }catch (ArgumentException ae){
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception th){
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 保存商品分类绑定规格
     * @param categoryId 分类ID
     * @param specId 规格ID
     * @return
     */
    @Override
    public DubboResult<EsCategorySpecDO> saveCategorySpec(Long categoryId, Long[] specId) {
        try{
            //查询分类是否存在
            EsCategory esCategory = this.getById(categoryId);
            if(esCategory == null ){
                throw  new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类不存在");
            }
            //查询规格是否存在
            QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSpecification::getId,specId);
            List<EsSpecification> specificationList = this.esSpecificationMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(specificationList) || ( specificationList.size() < specId.length)){
                throw  new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),"规格参数传入错误");
            }
            //先删除关系表数据
            QueryWrapper<EsCategorySpec> specQueryWrapper = new QueryWrapper<>();
            specQueryWrapper.lambda().eq(EsCategorySpec::getCategoryId,categoryId);
            this.esCategorySpecMapper.delete(specQueryWrapper);
            List<EsCategorySpecDO> cateList = new ArrayList<>();
            for (int i = 0; i < specId.length; i++) {
                EsCategorySpec esCategorySpec = new EsCategorySpec();
                EsCategorySpecDO esCategorySpecDO = new EsCategorySpecDO();
                esCategorySpec.setSpecId(specId[i]);
                esCategorySpec.setCategoryId(categoryId);
                this.esCategorySpecMapper.insert(esCategorySpec);
                BeanUtil.copyProperties(esCategorySpec,esCategorySpecDO);
                cateList.add(esCategorySpecDO);
            }
            return DubboPageResult.success(cateList);
        }catch (ArgumentException ae){
            logger.error("保存商品分类规格关系表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("保存商品分类规格关系表失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }



    public List<EsCategory> queryCategiryList(Long id){
        QueryWrapper<EsCategory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EsCategory::getParentId,id);
        List<EsCategory> categoryList = this.list(wrapper);
        return categoryList;
    }
    //获取某个分类下面的子类
    public List<EsCategoryDO> getCategoryChildrenList(Long parentId){
        List<EsCategoryDO> esCategoryList = getCategory();
        List<EsCategoryDO> categoryDOList = esCategoryList.stream().filter(
                category -> (category.getParentId().compareTo(parentId)==0)
        ).collect(Collectors.toList());
        return categoryDOList;

    }
    public List<EsCategoryDO> getChildren(List<EsCategoryDO> categoryDOList,Long parentId){
        List<EsCategoryDO> resultList = categoryDOList.stream().filter(
                category -> (category.getParentId().compareTo(parentId)==0)
        ).map(cate -> {
            EsCategoryDO esCategoryDO = new EsCategoryDO();
            BeanUtil.copyProperties(cate,esCategoryDO);
            esCategoryDO.setChildren(this.getChildren(categoryDOList,cate.getId()));
            return esCategoryDO;
        }).collect(Collectors.toList());
        return resultList;
    }
    public List<EsCategoryDO> getCategory(){
        List<EsCategory> esCategoryList = this.list();
        List<EsCategoryDO> categoryDOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(esCategoryList)){
            // entity 转换成DO
            categoryDOList = esCategoryList.stream().map(category -> {
                EsCategoryDO esCategoryDO = new EsCategoryDO();
                BeanUtil.copyProperties(category,esCategoryDO);
                return esCategoryDO;
            }).collect(Collectors.toList());
        }
        return categoryDOList;
    }

    @Override
    public DubboPageResult<EsBuyerCategoryDO> getBuyCategoryChildren(Long parentId) {
        try{
            //从缓存中获取商品分类
            List<EsBuyerCategoryDO> cateList = initCategory();
            List<EsBuyerCategoryDO> categoryDOList = cateList.stream().filter(
                    category -> (category.getParentId().compareTo(parentId)==0)
            ).map(cate -> {
                EsBuyerCategoryDO esCategoryDO = new EsBuyerCategoryDO();
                BeanUtil.copyProperties(cate,esCategoryDO);
                List<EsBuyerCategoryDO> list =this.getBuyerChildren(cateList,cate.getId());
                esCategoryDO.setChildren(list);
                DubboPageResult<EsBrandDO> result = esCategoryBrandService.getBrandsByCategoryList(cate.getId());
                if(result.isSuccess()){
                    List<EsBrandDO> brandList = result.getData().getList();
                    esCategoryDO.setBrandList(brandList);
                }
                return esCategoryDO;
            }).collect(Collectors.toList());
            return  DubboPageResult.success(categoryDOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 获取所有的一级分类
     * @return
     */
    @Override
    public DubboPageResult<EsCategoryDO> getFirstBrandList() {
        try {
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCategory::getParentId, 0);
            List<EsCategory> categoryList = this.list(queryWrapper);
            List<EsCategoryVO> categoryVOList = BeanUtil.copyList(categoryList,EsCategoryVO.class);
            return DubboPageResult.success(categoryVOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsCategoryDO> getFirstByName(String name) {
        try {
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCategory::getParentId, 0).like(EsCategory::getName,name);
            queryWrapper.last(" limit 0,1");
            EsCategory category = this.categoryMapper.selectOne(queryWrapper);
            if(category == null){
               throw  new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类不存在");
            }
            EsCategoryDO esCategoryDO = new EsCategoryDO();
            BeanUtil.copyProperties(category,esCategoryDO);
            return DubboResult.success(esCategoryDO);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCategoryDO> getCategoryByIds(List<Long> cateIds) {
        try{
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsCategory::getId,cateIds);
            List<EsCategory> categoryList  =  this.list(queryWrapper);
            List<EsCategoryDO> categoryDOList = BeanUtil.copyList(categoryList,EsCategoryDO.class);
            return DubboPageResult.success(categoryDOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<ParameterGroupDO> queryParams(Long goodsId, Long categoryId) {
        try{
            QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameterGroup::getCategoryId,categoryId);
            List<EsParameterGroup> parameterGroupList = esParameterGroupMapper.selectList(queryWrapper);
            List<EsParametersDO> parametersDOList = esParametersMapper.getParametersList(goodsId,categoryId);
            List<ParameterGroupDO> resList = this.convertParamList(parameterGroupList, parametersDOList);
            return DubboPageResult.success(resList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询参数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询参数失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<ParameterGroupDO> queryDraftParams(Long goodsId, Long categoryId) {
        try{
            QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameterGroup::getCategoryId,categoryId);
            List<EsParameterGroup> parameterGroupList = esParameterGroupMapper.selectList(queryWrapper);
            List<EsParametersDO> parametersDOList = esParametersMapper.getDraftParametersList(goodsId,categoryId);
            List<ParameterGroupDO> resList = this.convertParamList(parameterGroupList, parametersDOList);
            return DubboPageResult.success(resList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询参数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询参数失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    private List<ParameterGroupDO> convertParamList(List<EsParameterGroup> groupList, List<EsParametersDO> paramList){
        Map<Long, List<EsParametersDO>> map = new HashMap<>(16);
        for (EsParametersDO param : paramList) {
            param.setOptionList(param.getOptions().replaceAll("\r|\n", "").split(","));
            if (map.get(param.getGroupId()) != null) {
                map.get(param.getGroupId()).add(param);
            } else {
                List<EsParametersDO> list = new ArrayList<>();
                list.add(param);
                map.put(param.getGroupId(), list);
            }
        }
        List<ParameterGroupDO> resList = new ArrayList<>();
        for (EsParameterGroup group : groupList) {
            ParameterGroupDO list = new ParameterGroupDO();
            list.setGroupName(group.getGroupName());
            list.setGroupId(group.getId());
            list.setParams(map.get(group.getId()));
            resList.add(list);
        }
        return resList;
    }
    //初始化商品分类数据
    public List<EsBuyerCategoryDO> initCategory(){
        QueryWrapper<EsCategory> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderBy(true,false,"category_order");
        List<EsCategory> esCategoryList = this.list(queryWrapper);
        List<EsBuyerCategoryDO> categoryDOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(esCategoryList)){
            // entity 转换成DO
            categoryDOList = esCategoryList.stream().map(category -> {
                EsBuyerCategoryDO esCategoryDO = new EsBuyerCategoryDO();
                BeanUtil.copyProperties(category,esCategoryDO);
                jedisCluster.setex(CachePrefix.GOODS_CAT.getPrefix() + category.getId(),TIME_OUT, JsonUtil.objectToJson(category));
                return esCategoryDO;
            }).collect(Collectors.toList());
            categoryDOList.stream().sorted(Comparator.comparing(EsBuyerCategoryDO::getCategoryOrder).thenComparing(EsBuyerCategoryDO::getId));
            jedisCluster.setex(CATEGORY_CACHE_ALL,TIME_OUT, JsonUtil.objectToJson(categoryDOList));
        }
        return categoryDOList;
    }
    public List<EsBuyerCategoryDO> getBuyerChildren(List<EsBuyerCategoryDO> categoryDOList,Long parentId){
        List<EsBuyerCategoryDO> resultList = categoryDOList.stream().filter(
                category -> (category.getParentId().compareTo(parentId)==0)
        ).map(cate -> {
            EsBuyerCategoryDO esCategoryDO = new EsBuyerCategoryDO();
            BeanUtil.copyProperties(cate,esCategoryDO);
            esCategoryDO.setChildren(this.getBuyerChildren(categoryDOList,cate.getId()));
            return esCategoryDO;
        }).collect(Collectors.toList());
        return resultList;
    }


    /**
     * 查询所有分类及子分类 树形结构
     * @return
     */
    @Override
    public DubboPageResult<EsCategoryCO> getShopCategory(String  cateId) {
        try{
            List<Long> cas=new ArrayList<>();
            String[] split = cateId.split(",");
            for (String s:split
                 ) {
                cas.add(Long.parseLong(s));
            }
            List<EsCategoryCO> dataList = new ArrayList<>();
            QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
            //查询所有一级分类
            queryWrapper.lambda().in(EsCategory::getId,cas);
            List<EsCategory> list = this.categoryMapper.selectList(queryWrapper);
            dataList = list.stream().map(esCategory ->{
                EsCategoryCO cacheDO = new EsCategoryCO();
                cacheDO.setValue(esCategory.getId());
                cacheDO.setLabel(esCategory.getName());
                //二级分类
                List<EsCategory> categoryList = this.queryCategiryList(esCategory.getId());
                List<ESCategoryChildrenCO> childrenList = categoryList.stream().map(category -> {
                    ESCategoryChildrenCO childrenDO = new ESCategoryChildrenCO();
                    childrenDO.setLabel(category.getName());
                    childrenDO.setValue(category.getId());
                    //三级分类
                    List<EsCategory> esCategoryList =  this.queryCategiryList(category.getId());
                    List<ESCategoryChildrenCO> childrenDOList= esCategoryList.stream().map(cate -> {
                        ESCategoryChildrenCO categoryChildrenDO = new ESCategoryChildrenCO();
                        categoryChildrenDO.setLabel(cate.getName());
                        categoryChildrenDO.setValue(cate.getId());
                        return categoryChildrenDO;
                    }).collect(Collectors.toList());
                    childrenDO.setChildrenDO(childrenDOList);
                    return childrenDO;
                }).collect(Collectors.toList());
                cacheDO.setChildrenDO(childrenList);
                return cacheDO;
            }).collect(Collectors.toList());
            //将所有分类缓存 树形结构
            return DubboPageResult.success(dataList);
        }catch (ArgumentException ae){
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception th){
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

}
