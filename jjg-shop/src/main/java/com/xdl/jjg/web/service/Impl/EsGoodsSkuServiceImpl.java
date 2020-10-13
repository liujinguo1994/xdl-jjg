package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.model.co.EsGoodsSkuCO;
import com.xdl.jjg.model.domain.EsGoodsGalleryDO;
import com.xdl.jjg.model.domain.EsGoodsSkuDO;
import com.xdl.jjg.model.domain.EsSellerGoodsSkuDO;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.model.enums.GoodsCachePrefix;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsGoodsGalleryService;
import com.xdl.jjg.web.service.IEsGoodsSkuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsGoodsSkuServiceImpl extends ServiceImpl<EsGoodsSkuMapper, EsGoodsSku> implements IEsGoodsSkuService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsSkuServiceImpl.class);

    @Autowired
    private EsGoodsSkuMapper goodsSkuMapper;

    @Autowired
    private IEsGoodsGalleryService esGoodsGalleryService;

    @Autowired
    private EsGoodsMapper esGoodsMapper;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private EsCategoryMapper esCategoryMapper;

    @Autowired
    private EsSpecificationMapper esSpecificationMapper;
    @Autowired
    private EsSpecValuesMapper esSpecValuesMapper;

    @Value("${zhuox.redis.expire}")
    private int TIME_OUT;

    /**
     * 卖家中心更新商品SKU信息
     *
     * @param goodsSkuDTOList
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTOList, EsGoodsDTO goodsDTO) {
        try {
          goodsSkuDTOList.stream().forEach(esGoodsSkuDTO -> {
                EsGoodsSku esGoodsSku = new EsGoodsSku();
                esGoodsSku.setCost(esGoodsSkuDTO.getCost());
                esGoodsSku.setXnQuantity(esGoodsSkuDTO.getXnQuantity());
                esGoodsSku.setQuantity(esGoodsSkuDTO.getQuantity());
                Integer xnQuantity =  esGoodsSkuDTO.getXnQuantity() == null ? 0 : esGoodsSkuDTO.getXnQuantity();
                Integer quantity =  esGoodsSkuDTO.getQuantity() == null ? 0 : esGoodsSkuDTO.getQuantity();
                esGoodsSku.setEnableQuantity(quantity+xnQuantity);
                if( esGoodsSkuDTO.getIsEnable()== null){
                    esGoodsSkuDTO.setIsEnable(false);
                }
                esGoodsSku.setIsEnable(esGoodsSkuDTO.getIsEnable() == true ? 1 : 2);
                esGoodsSku.setMoney(esGoodsSkuDTO.getMoney());
                esGoodsSku.setId(esGoodsSkuDTO.getId());
                esGoodsSku.setShopId(goodsDTO.getShopId());
                esGoodsSku.setShopName(goodsDTO.getShopName());
                esGoodsSku.setCategoryId(goodsDTO.getCategoryId());
                esGoodsSku.setBarCode(esGoodsSkuDTO.getBarCode());
                if( esGoodsSkuDTO.getIsSelf()== null){
                    esGoodsSkuDTO.setIsSelf(false);
                }
                esGoodsSku.setIsSelf(esGoodsSkuDTO.getIsSelf() == true ? 1 : 2);
               if(esGoodsSkuDTO.getGoodsGallery() != null){
                   esGoodsSku.setThumbnail(esGoodsSkuDTO.getGoodsGallery().getGalleryList().get(0).getImage());
               }
                this.updateById(esGoodsSku);
                this.esGoodsGalleryService.insertGoodsGallery(esGoodsSkuDTO.getGoodsGallery(),esGoodsSku.getId());
                String sku = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                if(!StringUtils.isBlank(sku)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                }
            });
            //保存商品相册
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新商品SKU失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新商品SKU失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 卖家中心更新商品SKU信息
     *
     * @param goodsSkuDTOList
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuGift(List<EsGoodsSkuDTO> goodsSkuDTOList,Long shopId,Long goodsId) {
        try {
            goodsSkuDTOList.stream().forEach(esGoodsSkuDTO -> {
                EsGoodsSku esGoodsSku = new EsGoodsSku();
                //esGoodsSku.setCost(esGoodsSkuDTO.getCost());
                esGoodsSku.setXnQuantity(esGoodsSkuDTO.getXnQuantity());
                esGoodsSku.setQuantity(esGoodsSkuDTO.getQuantity());
                Integer xnQuantity =  esGoodsSkuDTO.getXnQuantity() == null ? 0 : esGoodsSkuDTO.getXnQuantity();
                Integer quantity =  esGoodsSkuDTO.getQuantity() == null ? 0 : esGoodsSkuDTO.getQuantity();
                esGoodsSku.setEnableQuantity(quantity+xnQuantity);
                if( esGoodsSkuDTO.getIsEnable()== null){
                    esGoodsSkuDTO.setIsEnable(false);
                }
                esGoodsSku.setIsEnable(esGoodsSkuDTO.getIsEnable() ==  true ? 1 : 2);
              //  esGoodsSku.setMoney(esGoodsSkuDTO.getMoney());
                esGoodsSku.setId(esGoodsSkuDTO.getId());
                esGoodsSku.setShopId(shopId);
                esGoodsSku.setBarCode(esGoodsSkuDTO.getBarCode());
                this.updateById(esGoodsSku);
                String skuStr = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                if(!StringUtils.isBlank(skuStr)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                }
            });
            QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsId);
            List<EsGoodsSku> goodsSkuList = this.list(queryWrapper);
            Integer quantity = goodsSkuList.stream().mapToInt(goodsSku->goodsSku.getEnableQuantity()).sum();
            EsGoods esGoods = new EsGoods();
            esGoods.setQuantity(quantity);
            esGoods.setId(goodsId);
            this.esGoodsMapper.updateById(esGoods);
            String goodsStr = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
            if(!StringUtils.isBlank(goodsStr)){
                jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新商品SKU失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新商品SKU失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    @Override
    public DubboResult<EsGoodsSkuCO> getGoodsSku(Long id) {
        try {
            String goodsSkuCache = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + id);
            if(!StringUtils.isBlank(goodsSkuCache)){
                EsGoodsSkuCO esGoodsSkuCO =JsonUtil.jsonToObject(goodsSkuCache,EsGoodsSkuCO.class);
                return DubboResult.success(esGoodsSkuCO);
            }
            EsGoodsSku goodsSku = this.getById(id);
            EsGoodsSkuCO goodsSkuCO = new EsGoodsSkuCO();
            if (goodsSku == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"SKU信息不存在或未启用");
            }
            BeanUtil.copyProperties(goodsSku, goodsSkuCO);
            jedisCluster.setex(GoodsCachePrefix.SKU.getPrefix() + id,TIME_OUT,JsonUtil.objectToJson(goodsSkuCO));
            DubboPageResult<EsGoodsGalleryDO> result =  esGoodsGalleryService.getGoodsGalleryBySkuId(id);
            if(result.isSuccess()){
                goodsSkuCO.setGalleryList(result.getData().getList());
            }
            return DubboResult.success(goodsSkuCO);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult<EsGoodsSkuCO> getGoodsSkuEnable(Long id) {
        try {
            String goodsSkuCache = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + id);
            if(!StringUtils.isBlank(goodsSkuCache)){
                EsGoodsSkuCO esGoodsSkuCO =JsonUtil.jsonToObject(goodsSkuCache,EsGoodsSkuCO.class);
                if (esGoodsSkuCO == null || esGoodsSkuCO.getIsEnable() == 2) {
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"SKU信息不存在或未启用");
                }
                return DubboResult.success(esGoodsSkuCO);
            }
            EsGoodsSku goodsSku = this.getById(id);
            EsGoodsSkuCO goodsSkuCO = new EsGoodsSkuCO();
            if (goodsSku == null || goodsSku.getIsEnable() == 2) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"SKU信息不存在或未启用");
            }
            BeanUtil.copyProperties(goodsSku, goodsSkuCO);
            jedisCluster.setex(GoodsCachePrefix.SKU.getPrefix() + id,TIME_OUT,JsonUtil.objectToJson(goodsSkuCO));
            return DubboResult.success(goodsSkuCO);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param goodsSkuDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsSkuDO>
     */
    @Override
    public DubboPageResult<EsGoodsSkuDO> getGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if(goodsSkuDTO.getGoodsId() !=null){
                queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsSkuDTO.getGoodsId());
            }
           if(!StringUtils.isBlank( goodsSkuDTO.getKeyword())){
               queryWrapper.lambda().eq(EsGoodsSku::getSkuSn,goodsSkuDTO.getKeyword());
           }
            //queryWrapper.lambda().eq(EsGoodsSku::getIsEnable,1);
            Page<EsGoodsSku> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsSku> iPage = this.page(page, queryWrapper);
            List<EsGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsSkuDOList = iPage.getRecords().stream().map(goodsSku -> {
                    EsGoodsSkuDO goodsSkuDO = new EsGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param ids 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> deleteGoodsSku(Integer[] ids) {
        try {
            //删除SKU时 先删除缓存信息再删除数据库信息
            QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoodsSku::getGoodsId,ids);
            List<EsGoodsSku> esGoodsSkuList = this.goodsSkuMapper.selectList(queryWrapper);
            this.goodsSkuMapper.delete(queryWrapper);
            if(CollectionUtils.isNotEmpty(esGoodsSkuList)){
                List<Long> sku_ids = esGoodsSkuList.stream().map(EsGoodsSku::getId).distinct().collect(Collectors.toList());
                long[] gallery_ids = sku_ids.stream().mapToLong(gallery_id ->gallery_id.longValue()).toArray();
                this.esGoodsGalleryService.deleteGoodsGallery(gallery_ids);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除商品SKU失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除商品SKU失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsGoodsSkuDO> getGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsSkuDTO.getGoodsId());
            queryWrapper.lambda().eq(EsGoodsSku::getIsEnable,1);
            List<EsGoodsSku> esGoodsSkuList =  this.list(queryWrapper);
            // 查询条件
            List<EsGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esGoodsSkuList)) {
                goodsSkuDOList = esGoodsSkuList.stream().map(goodsSku -> {
                    EsGoodsSkuDO goodsSkuDO = new EsGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsGoodsSkuDO> getAdminGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsSkuDTO.getGoodsId());
            List<EsGoodsSku> esGoodsSkuList =  this.list(queryWrapper);
            // 查询条件
            List<EsGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esGoodsSkuList)) {
                goodsSkuDOList = esGoodsSkuList.stream().map(goodsSku -> {
                    EsGoodsSkuDO goodsSkuDO = new EsGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsGoodsSkuDO> getGoodsSkuListGifts(EsGoodsSkuQueryDTO goodsSkuDTO) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsSkuDTO.getGoodsId());
            List<EsGoodsSku> esGoodsSkuList =  this.list(queryWrapper);
            // 查询条件
            List<EsGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esGoodsSkuList)) {
                goodsSkuDOList = esGoodsSkuList.stream().map(goodsSku -> {
                    EsGoodsSkuDO goodsSkuDO = new EsGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsSellerGoodsSkuDO> getGoodsSkuList(Long goodsId) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsId);
            List<EsGoodsSku> esGoodsSkuList =  this.list(queryWrapper);
            // 查询条件
            List<EsSellerGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esGoodsSkuList)) {
                goodsSkuDOList = esGoodsSkuList.stream().map(goodsSku -> {
                    EsSellerGoodsSkuDO goodsSkuDO = new EsSellerGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    goodsSkuDO.setIsEnable(goodsSku.getIsEnable() == 1 ? true :false);
                    if(goodsSku.getIsSelf() ==null){
                        goodsSku.setIsSelf(0);
                    }
                    goodsSkuDO.setIsSelf(goodsSku.getIsSelf() == 1 ? true :false);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<EsGoodsSkuDO> getSellerGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO) {
        QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsSkuDTO.getGoodsId());
            List<EsGoodsSku> esGoodsSkuList =  this.list(queryWrapper);
            // 查询条件
            List<EsGoodsSkuDO> goodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esGoodsSkuList)) {
                goodsSkuDOList = esGoodsSkuList.stream().map(goodsSku -> {
                    EsGoodsSkuDO goodsSkuDO = new EsGoodsSkuDO();
                    BeanUtil.copyProperties(goodsSku, goodsSkuDO);
                    return goodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsSkuDOList);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    /**
     * 卖家中心库存调整
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> sellerUpdateQuantity(EsGoodsSkuQuantityDTO skuQuantityDTO, Long goodsId) {
        try{

                EsGoodsSku sku = this.getById(skuQuantityDTO.getSkuId());
                if(sku == null){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"SKU不存在");
                }
                EsGoodsSku esGoodsSku = new EsGoodsSku();
                //可用库存= （实际库存+虚拟库存） - 冻结库存
                Integer enableQuantity = (sku.getQuantity() + skuQuantityDTO.getXnQuantity());
                esGoodsSku.setEnableQuantity(enableQuantity);
                esGoodsSku.setXnQuantity(skuQuantityDTO.getXnQuantity());
                esGoodsSku.setId(skuQuantityDTO.getSkuId());
                this.updateById(esGoodsSku);
                QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,goodsId);
                List<EsGoodsSku> goodsSkuList = this.list(queryWrapper);
                Integer quantity = goodsSkuList.stream().mapToInt(goodsSku->goodsSku.getEnableQuantity()).sum();
                //SKU 的可用库存之和= 商品可用库存
                EsGoods esGoods = new EsGoods();
                esGoods.setQuantity(quantity);
                esGoods.setId(goodsId);
                this.esGoodsMapper.updateById(esGoods);
                String skuStr = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                if(!StringUtils.isBlank(skuStr)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+esGoodsSku.getId());
                }
                String goodsStr = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
                if(!StringUtils.isBlank(goodsStr)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+goodsId);
                }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品SKU库存调整失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品SKU库存调整失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 买家中心根据商品ID 获取SKU信息
     * @param skuId
     * @param goodsId
     * @return
     */
    @Override
    public DubboResult<EsGoodsSkuDO> buyGetGoodsSku(Long skuId, Long goodsId) {
        try{
            EsGoods esGoods =  this.esGoodsMapper.selectById(goodsId);
            //商品已删除、已下架、商品不存在 不返还SKU数据
            if(esGoods == null ||  esGoods.getMarketEnable() == 2 || esGoods.getIsDel() == 1 || esGoods.getIsAuth() != 1){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品不存在或已下架");
            }

            QueryWrapper<EsGoodsSku> queryWrapper= new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsSku::getIsEnable,0).eq(EsGoodsSku::getId,skuId);
            EsGoodsSku esGoodsSku= this.goodsSkuMapper.selectOne(queryWrapper);
            if(esGoodsSku == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品不存在或已下架");
            }
            EsGoodsSkuDO goodsSkuDO =  new EsGoodsSkuDO();
            BeanUtil.copyProperties(esGoodsSku,goodsSkuDO);
            return DubboResult.success(goodsSkuDO);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    @Override
    public DubboResult<EsGoodsSkuCO> getSkuById(Long id) {
        try {
            EsGoodsSku goodsSku = this.getById(id);
            EsGoodsSkuCO goodsSkuCO = new EsGoodsSkuCO();
            if (goodsSku == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"SKU信息不存在或未启用");
            }
            BeanUtil.copyProperties(goodsSku, goodsSkuCO);
            jedisCluster.setex(GoodsCachePrefix.SKU.getPrefix() + id,TIME_OUT,JsonUtil.objectToJson(goodsSkuCO));
            DubboPageResult<EsGoodsGalleryDO> result =  esGoodsGalleryService.getGoodsGalleryBySkuId(id);
            if(result.isSuccess()){
                goodsSkuCO.setGalleryList(result.getData().getList());
            }
            return DubboResult.success(goodsSkuCO);
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsSkuDO> getSkuByIds(Long[] ids) {
        try{
            QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoodsSku::getId,ids).or().in(EsGoodsSku::getSkuSn,ids);
            List<EsGoodsSku> goodsSkus = this.list(queryWrapper);
            List<EsGoodsSkuDO> goodsDOList = BeanUtil.copyList(goodsSkus,EsGoodsSkuDO.class);
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException ae){
            logger.error("获取商品Sku信息失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品Sku信息失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 卖家中心库存预警数据获取
     * @param goodsSkuDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    public DubboPageResult<EsGoodsSkuDO> sellerGetGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO, int pageSize, int pageNum) {
        try{
               Long[] cateIds  = null;
               if(!StringUtils.isBlank(goodsSkuDTO.getCategoryPath())){
                   //需查询父类及子类下面的
                   QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
                   queryWrapper.lambda().like(EsCategory::getCategoryPath,goodsSkuDTO.getCategoryPath());
                   List<EsCategory> esCategoryList = this.esCategoryMapper.selectList(queryWrapper);
                   if(CollectionUtils.isEmpty(esCategoryList)){
                       throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类数据不存在");
                   }
                   //获取分类ID及子分类ID
                   List<Long> sku_ids = esCategoryList.stream().map(EsCategory::getId).collect(Collectors.toList());
                   cateIds =  sku_ids.stream().toArray(Long[]::new);
               }
               IPage<EsGoodsSkuDO> page = this.goodsSkuMapper.sellerGetGoodsSkuList(new Page(pageNum,pageSize),goodsSkuDTO,cateIds);
               return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("查询商品SKU失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询商品SKU失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

        }

    /**
     *  卖家中心 商品SKU 设置预警值
     * @param warningValue
     * @param skuId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuWarning(Integer warningValue, Long skuId) {
       try{
           EsGoodsSku esGoodsSku = this.getById(skuId);
           if(esGoodsSku == null){
               throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品SKU不存在");
           }
           esGoodsSku.setWarningValue(warningValue);
           this.updateById(esGoodsSku);
           return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("设置商品SKU预警值失败", ae);
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("设置商品SKU预警值失败", th);
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 管理后台更新SKU信息
     * @param goodsSkuDTOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> adminUpdateGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTOList, EsGoodsArchDTO esGoodsArchDTO) {
        try {
            List<EsGoodsSku> goodsSkuList = goodsSkuDTOList.stream().map(esGoodsSkuDTO -> {
                EsGoodsSku esGoodsSku = new EsGoodsSku();
                BeanUtil.copyProperties(esGoodsSkuDTO,esGoodsSku);
                List<EsSpecValuesDTO> specValuesList = esGoodsSkuDTO.getSpecList();
                //先插入规格 规格值
                specValuesList.stream().forEach(spec ->{

                    Long specId =  spec.getSpecId() ==null ? -1 : spec.getSpecId();
                    //规格
                    EsSpecification specification = new EsSpecification();
                    specification.setIsDel(0);
                    specification.setSpecName(spec.getSpecName());
                    if(specId == -1){
                        esSpecificationMapper.insert(specification);
                    }else{
                        specification.setId(spec.getSpecId());
                        esSpecificationMapper.updateById(specification);
                    }
                    //规格值
                    Long specValueId =  spec.getId() ==null ? -1 : spec.getId();
                    EsSpecValues esSpecValues = new EsSpecValues();
                    esSpecValues.setSpecValue(spec.getSpecValue());
                    esSpecValues.setSpecName(spec.getSpecName());
                    esSpecValues.setSpecId(specification.getId());
                    if(specValueId == -1){
                        esSpecValuesMapper.insert(esSpecValues);
                    }else{
                        esSpecValues.setId(specValueId);
                        esSpecValuesMapper.updateById(esSpecValues);
                    }
                    esGoodsSku.setGoodsId(esGoodsArchDTO.getId());
                    esGoodsSku.setGoodsName(esGoodsArchDTO.getGoodsName());
                    esGoodsSku.setGoodsSn(esGoodsArchDTO.getGoodsSn());
                    spec.setId(esSpecValues.getId());
                    spec.setSpecId(specification.getId());
                });
                String specJson =  JsonUtil.objectToJson(specValuesList) ;
                esGoodsSku.setSpecs(specJson);
               return esGoodsSku;
            }).collect(Collectors.toList());
            QueryWrapper<EsGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsSku::getGoodsId,esGoodsArchDTO.getId());
            this.remove(queryWrapper);
            this.saveBatch(goodsSkuList);
            //保存商品相册
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新商品SKU失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新商品SKU失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 管理后台新增SKU信息
     *
     * @param goodsSkuDTOList
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsSkuDO> adminInsertGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTOList, EsGoodsArchDTO esGoodsArchDTO) {

        try {
            List<EsGoodsSku> goodsSkuList =  goodsSkuDTOList.stream().map(esGoodsSkuDTO -> {
                EsGoodsSku goodsSku = new EsGoodsSku();
                BeanUtil.copyProperties(esGoodsSkuDTO, goodsSku);
                goodsSku.setGoodsId(esGoodsArchDTO.getId());
                goodsSku.setGoodsName(esGoodsArchDTO.getGoodsName());
                goodsSku.setGoodsSn(esGoodsArchDTO.getGoodsSn());
                goodsSku.setIsEnable(1);
                //默认预警值为0
                goodsSku.setWarningValue(0);
                List<EsSpecValuesDTO> specValuesList = esGoodsSkuDTO.getSpecList();
                if(CollectionUtils.isEmpty(specValuesList)){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数传入错误，规格不能为空");
                }
                //先插入规格 规格值
                specValuesList.stream().forEach(spec ->{
                    //规格
                    EsSpecification specification = new EsSpecification();
                    specification.setIsDel(0);
                    specification.setSpecName(spec.getSpecName());
                    QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(EsSpecification::getSpecName,spec.getSpecName()).eq(EsSpecification::getIsDel,0);
                    EsSpecification esSpecification = this.esSpecificationMapper.selectOne(queryWrapper);
                    if(esSpecification == null){
                        esSpecificationMapper.insert(specification);
                    }else{
                        BeanUtil.copyProperties(esSpecification,specification);
                    }
                    //规格值
                    EsSpecValues esSpecValues = new EsSpecValues();
                    esSpecValues.setSpecValue(spec.getSpecValue());
                    esSpecValues.setSpecName(spec.getSpecName());
                    esSpecValues.setSpecId(specification.getId());
                    esSpecValuesMapper.insert(esSpecValues);
                    //规格项ID
                    spec.setSpecId(specification.getId());
                    //规格值ID
                    spec.setId(esSpecValues.getId());
                });
                String specJson = JsonUtil.objectToJson(specValuesList);
                goodsSku.setSpecs(specJson);
                return goodsSku;
            }).collect(Collectors.toList());
            this.saveBatch(goodsSkuList);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增商品SKU失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增商品SKU失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
