package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.cache.ESCustomChildrenCO;
import com.jjg.member.model.cache.EsCustomCO;
import com.jjg.member.model.domain.EsCustomDO;
import com.jjg.member.model.domain.EsGoodsDO;
import com.jjg.member.model.dto.EsCustomDTO;
import com.jjg.member.model.dto.EsGoodsQueryDTO;
import com.jjg.member.model.enums.CustomCachePrefix;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCustom;
import com.xdl.jjg.mapper.EsCustomMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsCustomService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺自定义分类 服务实现类
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-06-20
 */
@Service
public class EsCustomServiceImpl extends ServiceImpl<EsCustomMapper, EsCustom> implements IEsCustomService {

    private static Logger logger = LoggerFactory.getLogger(EsCustomServiceImpl.class);

    @Autowired
    private EsCustomMapper customMapper;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private GoodsService esGoodsService;

    private final String CUSTOM_CACHE_ALL = CustomCachePrefix.CUSTOM_CAT.getPrefix();

    private final Integer CACHE_TIME = 1800;

    /**
     * 插入店铺自定义分类数据
     *
     * @param customDTO 店铺自定义分类DTO
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/06/20 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCustom(EsCustomDTO customDTO) {
        EsCustom parent = null;
        try {
            if (customDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            parent = customMapper.selectById(customDTO.getParentId());
            if (customDTO.getParentId() == null || customDTO.getParentId() == 0) {
                customDTO.setParentId(0l);
            }else{
                if(parent == null){
                    throw new ArgumentException(MemberErrorCode.PARENT_NOT_EXIST.getErrorCode(), MemberErrorCode.PARENT_NOT_EXIST.getErrorMsg());
                }
            }
            //校验分组排序
            QueryWrapper<EsCustom> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsCustom::getSort,customDTO.getSort());
            EsCustom escustom = this.getOne(wrapper);
            if(escustom != null){
                throw new ArgumentException(MemberErrorCode.SORT_EXISTS.getErrorCode(), MemberErrorCode.SORT_EXISTS.getErrorMsg());
            }
            EsCustom esCustom = new EsCustom();
            BeanUtil.copyProperties(customDTO, esCustom);
            this.customMapper.insert(esCustom);

            if (esCustom.getParentId() == null || esCustom.getParentId() == 0) {
                esCustom.setCategoryPath("0|" + esCustom.getId());
            }else{
                esCustom.setCategoryPath(parent.getCategoryPath()  +"|" + esCustom.getId());
            }

            customMapper.updateById(esCustom);

//            EsCustom custom = null;
//
//            // 非顶级分类
//            if (customDTO.getParentId() != null && customDTO.getParentId() != 0) {
//                custom = this.customMapper.selectById(customDTO.getParentId());
//                if (custom == null) {
//                    throw new ArgumentException(MemberErrorCode.CUSTOM_NOTEXIT.getErrorCode(), MemberErrorCode.CUSTOM_NOTEXIT.getErrorMsg());
//                }
//                // 替换catPath 根据catPath规则来匹配级别
//                String catPath = custom.getCategoryPath().replace("|", ",");
//                String[] str = catPath.split(",");
//                // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
//                if (str.length >= 4) {
//                    throw new ArgumentException(MemberErrorCode.CUSTOM_OUTINDES.getErrorCode(), MemberErrorCode.CUSTOM_OUTINDES.getErrorMsg());
//                }
//            }
//            // 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
//            // 注意末尾都要加|，以防止查询子孙时出错
//            // 不是顶级类别，有父
//            if (custom != null) {
//                custom.setCategoryPath(custom.getCategoryPath() + customDTO.getId() + "|");
//            } else {// 是顶级类别
//                custom.setCategoryPath("0|" + customDTO.getId() + "|");
//            }
//
//            this.customMapper.insert(esCustom);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺自定义分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺自定义分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺自定义分类数据
     *
     * @param customDTO 店铺自定义分类DTO
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/06/20 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCustom(EsCustomDTO customDTO) {
        try {
            if (customDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            if (customDTO.getParentId() == null || customDTO.getParentId() == 0) {
                customDTO.setParentId(0l);
            }

            EsCustom parent = null;

            EsCustom esCustom = this.customMapper.selectById(customDTO.getId());

            if(esCustom == null){
                throw new ArgumentException(MemberErrorCode.SHOP_GROUP_NOT_EXIST.getErrorCode(), MemberErrorCode.SHOP_GROUP_NOT_EXIST.getErrorMsg());
            }

            if(esCustom.getParentId() == 0 && customDTO.getParentId() != 0){
                throw new ArgumentException(MemberErrorCode.TOP_GROUP_NOT_UPDATE.getErrorCode(), MemberErrorCode.TOP_GROUP_NOT_UPDATE.getErrorMsg());
            }

            if(customDTO.getParentId() != 0){
                parent= this.customMapper.selectById(customDTO.getParentId());
                if(parent == null){
                    throw new ArgumentException(MemberErrorCode.PARENT_NOT_EXIST.getErrorCode(), MemberErrorCode.PARENT_NOT_EXIST.getErrorMsg());
                }
                customDTO.setCategoryPath(parent.getCategoryPath() + "|" + customDTO.getId() );
            }

            if(customDTO.getParentId() == 0 || customDTO.getParentId() == null){
                customDTO.setCategoryPath("0|" + customDTO.getId());
            }
            EsCustom custom = new EsCustom();
            BeanUtil.copyProperties(customDTO, custom);
            //校验分组排序
            QueryWrapper<EsCustom> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsCustom::getSort,customDTO.getSort());
            EsCustom escustom = this.getOne(wrapper);
            if(escustom != null){
                throw new ArgumentException(MemberErrorCode.SORT_EXISTS.getErrorCode(), MemberErrorCode.SORT_EXISTS.getErrorMsg());
            }
            QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCustom::getId, customDTO.getId());
            this.customMapper.update(custom, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺自定义分类更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺自定义分类更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺自定义分类详情
     *
     * @param id 主键id
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/20 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    @Override
    public DubboResult<EsCustomDO> getCustom(Long id) {
        try {
            EsCustom custom = this.customMapper.selectById(id);
            EsCustomDO customDO = new EsCustomDO();
            if (custom == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(custom, customDO);
            return DubboResult.success(customDO);
        } catch (ArgumentException ae){
            logger.error("店铺自定义分类查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺自定义分类查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据id查询
     *
     * @param id 主键id
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/06/21 10:17:16
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    public List<EsCustom> getList(Long id) {
        try {
            QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCustom::getParentId, id);
            List<EsCustom> customs = this.customMapper.selectList(queryWrapper);
            return customs;
        } catch (ArgumentException ae){
            logger.error("店铺自定义分类查询失败", ae);
            return null;
        }  catch (Throwable th) {
            logger.error("店铺自定义分类查询失败", th);
            return null;
        }
    }

    /**
     * 根据查询店铺自定义分类列表
     *
     * @param customDTO 店铺自定义分类DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/06/20 15:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCustomDO>
     */
    @Override
    public DubboPageResult<EsCustomDO> getCustomList(EsCustomDTO customDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCustom> page = new Page<>(pageNum, pageSize);
            IPage<EsCustom> iPage = this.page(page, queryWrapper);
            List<EsCustomDO> customDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                customDOList = iPage.getRecords().stream().map(custom -> {
                    EsCustomDO customDO = new EsCustomDO();
                    BeanUtil.copyProperties(custom, customDO);
                    return customDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(customDOList);
        } catch (ArgumentException ae){
            logger.error("店铺自定义分类分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺自定义分类分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺自定义分类数据
     *
     * @param id 主键id
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/20 15:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCustom(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }

            EsCustom esCustom = this.customMapper.selectById(id);
            if (esCustom==null){
                throw new ArgumentException(MemberErrorCode.EXIST_CUST.getErrorCode(), MemberErrorCode.EXIST_CUST.getErrorMsg());
            }
            List<EsCustomDO> esCustomDOList = this.getCategoryChildrenList(id);
            //存在子分类不能删除
            if(CollectionUtils.isNotEmpty(esCustomDOList)){
                throw new ArgumentException(MemberErrorCode.EXIST_CUSTOM.getErrorCode(), MemberErrorCode.EXIST_CUSTOM.getErrorMsg());
            }
            //判断该分类下是否有商品
            EsGoodsQueryDTO esGoodsDTO = new EsGoodsQueryDTO();
            esGoodsDTO.setCustomId(id);
            esGoodsDTO.setShopId(esCustom.getShopId());
            DubboPageResult<EsGoodsDO> esGoodsList = esGoodsService.sellerGetEsGoodsList(esGoodsDTO);
            if(CollectionUtils.isNotEmpty(esGoodsList.getData().getList())){
                throw new ArgumentException(MemberErrorCode.EXIST_GOODS.getErrorCode(), MemberErrorCode.EXIST_GOODS.getErrorMsg());
            }
            QueryWrapper<EsCustom> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCustom::getId, id);
            this.customMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺自定义分类删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺自定义分类删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }



    /**
     * 查询所有分类及子分类 树形结构
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/21 15:40:44
     * @return
     */
    @Override
    public DubboPageResult<EsCustomCO> getCategoryList(Long shopId) {
        try{
            //先从缓存获取
            String cacheList = jedisCluster.get(CUSTOM_CACHE_ALL + shopId);
            logger.info("分类缓存"+cacheList);
            List<EsCustomCO> dataList = JsonUtil.jsonToList(cacheList, EsCustomCO.class);
            if(!CollectionUtils.isEmpty(dataList)){
                return DubboPageResult.success(dataList);
            }
//            List<EsCustomCO> dataList = new ArrayList<>();
//            QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
            //查询所有一级分类
//            queryWrapper.lambda().eq(EsCustom::getParentId,0);
//            List<EsCustom> list = this.list(queryWrapper);
            if(null == shopId){
                return DubboPageResult.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            List<EsCustom> list = customMapper.getShopClassifyTree(0L, shopId);
            dataList = list.stream().map(esCategory ->{
                EsCustomCO cacheDO = new EsCustomCO();
                cacheDO.setValue(esCategory.getId());
                cacheDO.setLabel(esCategory.getName());
                //二级分类
                List<EsCustom> categoryList = this.queryCategiryList(esCategory.getId());
                List<ESCustomChildrenCO> childrenList = categoryList.stream().map(category -> {
                    ESCustomChildrenCO childrenDO = new ESCustomChildrenCO();
                    childrenDO.setLabel(category.getName());
                    childrenDO.setValue(category.getId());
                    //三级分类
                    List<EsCustom> esCategoryList =  this.queryCategiryList(category.getId());
                    List<ESCustomChildrenCO> childrenDOList= esCategoryList.stream().map(cate -> {
                        ESCustomChildrenCO categoryChildrenDO = new ESCustomChildrenCO();
                        categoryChildrenDO.setLabel(cate.getName());
                        categoryChildrenDO.setValue(cate.getId());
                        return categoryChildrenDO;
                    }).collect(Collectors.toList());
                    childrenDO.setChildren(childrenDOList);
                    return childrenDO;
                }).collect(Collectors.toList());
                cacheDO.setChildren(childrenList);
                return cacheDO;
            }).collect(Collectors.toList());
            //将分类存在缓存
            String key = CUSTOM_CACHE_ALL + shopId;
            jedisCluster.setex(key, CACHE_TIME, JsonUtil.objectToJson(dataList));
            return DubboPageResult.success(dataList);
        }catch (ArgumentException ae){
            logger.error("卖家自定义商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception th){
            logger.error("卖家自定义商品分类查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    public List<EsCustom> queryCategiryList(Long id){
        QueryWrapper<EsCustom> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EsCustom::getParentId,id);
        List<EsCustom> categoryList = this.list(wrapper);
        return categoryList;
    }


    //获取某个分类下面的子类
    public List<EsCustomDO> getCategoryChildrenList(Long parentId){
        List<EsCustomDO> esCategoryList = initCategory();
        List<EsCustomDO> categoryDOList = esCategoryList.stream().filter(
                category -> (category.getParentId().compareTo(parentId)==0)
        ).collect(Collectors.toList());
        return categoryDOList;

    }


    /**
     * 根据父ID获取分类下面的子类
     * @param id 主键ID
     * @return
     */
    public DubboPageResult<EsCustomDO> getCategoryParentList(Long id) {
        //根据父ID 查询分类
        try {
            QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCustom::getParentId,id);
            queryWrapper.lambda().orderByAsc(EsCustom::getSort);
            List<EsCustom> EsCustomList = this.list(queryWrapper);
            List<EsCustomDO> EsCustomDOList = EsCustomList.stream().map(category -> {
                List<EsCustomDO> categoryDOList = getCategoryChildrenList(category.getId());
                EsCustomDO esCustomDO = new EsCustomDO();
                if(CollectionUtils.isNotEmpty(categoryDOList)){
                    esCustomDO.setChildren(categoryDOList);
                }
                BeanUtil.copyProperties(category,esCustomDO);
                return esCustomDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(EsCustomDOList);
        } catch (ArgumentException ae) {
            logger.error("商品分类查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品分类查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    /**
     * 初始化商品分类数据
     * @return
     */
    public List<EsCustomDO> initCategory(){
        //从数据库中获取所有分类
        QueryWrapper<EsCustom> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(EsCustom::getSort);
        List<EsCustom> esCustomList = this.list(queryWrapper);
        List<EsCustomDO> esCustomDOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(esCustomList)){
            // entity 转换成DO
            esCustomDOList = esCustomList.stream().map(category -> {
                EsCustomDO esCategoryDO = new EsCustomDO();
                BeanUtil.copyProperties(category,esCategoryDO);
                return esCategoryDO;
            }).collect(Collectors.toList());
        }
        return esCustomDOList;
    }


}
