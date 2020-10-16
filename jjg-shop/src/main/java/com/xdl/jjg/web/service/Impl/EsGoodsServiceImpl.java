package com.xdl.jjg.web.service.Impl;


import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.MQProducer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.jjg.member.model.domain.EsCustomDO;
import com.jjg.member.model.domain.EsShopDO;
import com.jjg.member.model.enums.ShopStatusEnums;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.constant.GoodsConstants;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.constant.GoodsOperate;
import com.jjg.shop.model.constant.GoodsOperateType;
import com.jjg.shop.model.domain.*;
import com.jjg.shop.model.dto.*;
import com.jjg.shop.model.enums.GoodsCachePrefix;
import com.jjg.shop.model.enums.GoodsEnum;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.*;
import org.apache.commons.collections.map.HashedMap;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Service
public class EsGoodsServiceImpl extends ServiceImpl<EsGoodsMapper, EsGoods> implements IEsGoodsService {


    private Logger logger = LoggerFactory.getLogger(EsGoodsServiceImpl.class);

    @Autowired
    private EsGoodsMapper esGoodsMapper;

    @Autowired
    private EsCategoryMapper esCategoryMapper;
    //商品SKU
    @Autowired
    private IEsGoodsSkuService esGoodsSkuService;

    @Autowired
    private EsGoodsSkuMapper esGoodsSkuMapper;

    @Autowired
    private JedisCluster jedisCluster;
    //商品档案
    @Autowired
    private EsGoodsArchMapper esGoodsArchMapper;
    //商品标签
    @Autowired
    private IEsTagGoodsService esTagGoodsService;
    @Autowired
    private EsTagGoodsMapper esTagGoodsMapper;
    //店铺
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsShopService esShopService;
    //系统设置
    @Autowired
    private IEsSettingsService esSettingsService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private  IEsGoodsFreightService esGoodsFreightService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsCustomService esCustomService;
    @Autowired
    private IEsGoodsParamsService esGoodsParamsService;
    @Value("${zhuox.redis.expire}")
    private int TIME_OUT;
    @Autowired
    private EsTagsMapper esTagsMapper;
    @Autowired
    private MQProducer mqProducer;
    @Value("${rocketmq.goods.topic}")
    private String goods_topic;

    @Value("${rocketmq.goods.change.topic}")
    private String goods_change_topic;
    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsPromotionGoodsService esPromotionGoodsService;
    @Autowired
    private IEsGoodsGalleryService esGoodsGalleryService;
    @Autowired
    private IEsGoodsIndexService esGoodsIndexService;
    @Autowired
    private IEsShopPromiseService esShopPromiseService;
    @Autowired
    private EsLfcGoodsMapper esLfcGoodsMapper;
    /**
     * 根据商品ID 获取商品详细信息
     * @param id 商品ID
     * @return 商品信息 EsGoodsDO
     */
    public DubboResult<EsGoodsCO> getEsGoods(Long id) {
        if(id == null){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), GoodsErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try{
            String goodsCoStr = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+id);
            if(StringUtils.isNotBlank(goodsCoStr)){
                EsGoodsCO esgoodsCo= JsonUtil.jsonToObject(goodsCoStr ,EsGoodsCO.class);
                if(esgoodsCo == null || esgoodsCo.getIsDel() == 1|| esgoodsCo.getIsAuth() != 1 || esgoodsCo.getMarketEnable() == 2 || esgoodsCo.getIsGifts() == 1){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品不存在或已下架");
                }
                return DubboResult.success(esgoodsCo);
            }
            EsGoods esGoods =  this.getById(id);
            EsGoodsCO esGoodsCO = new EsGoodsCO();
            //删除 审核未通过 已下架 赠品
            if(esGoods == null || esGoods.getIsDel() == 1|| esGoods.getIsAuth() != 1 || esGoods.getMarketEnable() == 2 || esGoods.getIsGifts() == 1){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品不存在或已下架");
            }
            BeanUtil.copyProperties(esGoods,esGoodsCO);
            EsGoodsSkuQueryDTO esGoodsSkuQueryDTO = new EsGoodsSkuQueryDTO();
            esGoodsSkuQueryDTO.setGoodsId(id);
            DubboPageResult<EsGoodsSkuDO> skuList = esGoodsSkuService.getGoodsSkuList(esGoodsSkuQueryDTO);
            if(skuList.isSuccess()){
                List<EsGoodsSkuDO> skusList = skuList.getData().getList();
                if(CollectionUtils.isEmpty(skusList)){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品不存在或已下架");
                }
                List<EsGoodsSkuCO>  goodsSkuCOList =  skuList.getData().getList().stream().map(esGoodsSkuDO->{
                    EsGoodsSkuCO esGoodsSkuCO = new EsGoodsSkuCO();
                    BeanUtil.copyProperties(esGoodsSkuDO,esGoodsSkuCO);
                    DubboPageResult<EsGoodsGalleryDO> result =   esGoodsGalleryService.getGoodsGalleryBySkuId(esGoodsSkuDO.getId());
                    if(result.isSuccess()){
                        esGoodsSkuCO.setGalleryList(result.getData().getList());
                    }
                    return esGoodsSkuCO;
               }).collect(Collectors.toList());
                esGoodsCO.setSkuList(goodsSkuCOList);

            }
            DubboPageResult<EsBuyerGoodsParamsDO> paramsList =  esGoodsParamsService.queryGoodsParams(esGoods.getCategoryId(),esGoods.getId());
            if(skuList.isSuccess() && CollectionUtils.isNotEmpty(paramsList.getData().getList())){
                esGoodsCO.setParamsList(paramsList.getData().getList());
            }else{
                EsBuyerGoodsParamsDO buyerGoodsParamsDO = new EsBuyerGoodsParamsDO();
                List<EsBuyerGoodsParamsDO> paramsDOList = new ArrayList<>();
                paramsDOList.add(buyerGoodsParamsDO);
                esGoodsCO.setParamsList(paramsDOList);
            }
            EsCategory category = esCategoryMapper.selectById(esGoodsCO.getCategoryId());
            if(category != null){
                esGoodsCO.setCategoryName(category.getName());
            }
            //如果有卖家承诺
            if(esGoodsCO.getSellerPromise() == 1){
               DubboResult<EsShopPromiseDO> result= esShopPromiseService.getShopPromise(esGoodsCO.getPromiseId());
               if(result.isSuccess()){
                   esGoodsCO.setPromiseText(result.getData().getContent());
               }
            }
            jedisCluster.setex(GoodsCachePrefix.GOODS.getPrefix()+id,TIME_OUT, JsonUtil.objectToJson(esGoodsCO));
            return DubboResult.success(esGoodsCO);
        }catch (ArgumentException ae){
            logger.error("获取商品信息失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品信息失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据商品ID 获取商品详细信息
     * @param id 商品ID
     * @return 商品信息 EsGoodsDO
     */
    public DubboResult<EsGoodsCO> getEsBuyerGoods(Long id) {
        if(id == null){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), GoodsErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try{
            String goodsCoStr = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+id);
            if(StringUtils.isNotBlank(goodsCoStr)){
                EsGoodsCO esgoodsCo= JsonUtil.jsonToObject(goodsCoStr ,EsGoodsCO.class);
                return DubboResult.success(esgoodsCo);
            }
            EsGoods esGoods =  this.getById(id);
            if(esGoods == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGoodsCO esGoodsCO = new EsGoodsCO();
            BeanUtil.copyProperties(esGoods,esGoodsCO);
            EsGoodsSkuQueryDTO esGoodsSkuQueryDTO = new EsGoodsSkuQueryDTO();
            esGoodsSkuQueryDTO.setGoodsId(id);
            DubboPageResult<EsGoodsSkuDO> skuList = esGoodsSkuService.getGoodsSkuList(esGoodsSkuQueryDTO);
            if(skuList.isSuccess() && CollectionUtils.isNotEmpty(skuList.getData().getList())){
                List<EsGoodsSkuCO>  goodsSkuCOList =  skuList.getData().getList().stream().map(esGoodsSkuDO->{
                    EsGoodsSkuCO esGoodsSkuCO = new EsGoodsSkuCO();
                    BeanUtil.copyProperties(esGoodsSkuDO,esGoodsSkuCO);
                    DubboPageResult<EsGoodsGalleryDO> result =   esGoodsGalleryService.getGoodsGalleryBySkuId(esGoodsSkuDO.getId());
                    if(result.isSuccess()){
                        esGoodsSkuCO.setGalleryList(result.getData().getList());
                    }
                    return esGoodsSkuCO;
                }).collect(Collectors.toList());
                esGoodsCO.setSkuList(goodsSkuCOList);
            }
            DubboPageResult<EsBuyerGoodsParamsDO> paramsList =  esGoodsParamsService.queryGoodsParams(esGoods.getCategoryId(),esGoods.getId());
            if(skuList.isSuccess() && CollectionUtils.isNotEmpty(paramsList.getData().getList())){
                esGoodsCO.setParamsList(paramsList.getData().getList());
            }
            DubboPageResult<EsPromotionGoodsDO> result = esPromotionGoodsService.getPromotionByGoodsId(Arrays.asList(id));
            if(result.isSuccess()){
                List<EsPromotionGoodsVO> promotionGoodsVOList = BeanUtil.copyList(result.getData().getList(),EsPromotionGoodsVO.class);
                esGoodsCO.setPromotionList(promotionGoodsVOList);
            }
            jedisCluster.setex(GoodsCachePrefix.GOODS.getPrefix()+id,TIME_OUT, JsonUtil.objectToJson(esGoodsCO));
            return DubboResult.success(esGoodsCO);
        }catch (ArgumentException ae){
            logger.error("获取商品信息失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品信息失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    public DubboResult<EsSellerGoodsDO> getSellerEsGoods(Long id) {
        try{
            EsGoods esGoods =  this.getById(id);
            EsSellerGoodsDO sellerGoodsDO = new EsSellerGoodsDO();
            BeanUtil.copyProperties(esGoods,sellerGoodsDO);
            DubboPageResult<EsSellerGoodsSkuDO> skuList = esGoodsSkuService.getGoodsSkuList(id);
            if(skuList.isSuccess() && CollectionUtils.isNotEmpty(skuList.getData().getList())){
                List<EsSellerGoodsSkuDO>  goodsSkuCOList =  skuList.getData().getList().stream().map(esGoodsSkuDO->{
                    EsSellerGoodsSkuDO skuDO = new EsSellerGoodsSkuDO();
                    BeanUtil.copyProperties(esGoodsSkuDO,skuDO);
                    DubboPageResult<EsGoodsGalleryDO> result =   esGoodsGalleryService.getGoodsGalleryBySkuId(esGoodsSkuDO.getId());
                    if(result.isSuccess()){
                        if(CollectionUtils.isNotEmpty(result.getData().getList())){
                            skuDO.setAlbumNo(result.getData().getList().get(0).getAlbumNo());
                            skuDO.setGoodsGallery(result.getData().getList());
                        }
                    }
                    return skuDO;
                }).collect(Collectors.toList());
                sellerGoodsDO.setSkuList(goodsSkuCOList);
            }
            EsCategory category = esCategoryMapper.selectById(esGoods.getCategoryId());
            if(category!=null){
                sellerGoodsDO.setCategoryName(category.getName());
            }
            DubboPageResult<EsTagGoodsDO> result =  esTagGoodsService.getTagList(sellerGoodsDO.getId());
            if(result.isSuccess()){
                List<Long> tagsIds = result.getData().getList().stream().map(EsTagGoodsDO::getTagId).collect(Collectors.toList());
                sellerGoodsDO.setTagsIds(tagsIds.stream().toArray(Long[]::new));
            }
            return DubboResult.success(sellerGoodsDO);
        }catch (ArgumentException ae){
            logger.error("获取商品信息失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品信息失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> getEsGoods(Long[] ids) {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids).or().in(EsGoods::getGoodsSn,ids);
            List<EsGoods> goodsList = this.list(queryWrapper);
            List<EsGoodsDO> goodsDOList = BeanUtil.copyList(goodsList,EsGoodsDO.class);
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException ae){
            logger.error("获取商品信息失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品信息失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsGoodsDO> buyGetEsGoods(Long id) {
        try {
            EsGoodsDO goodsDO =  this.esGoodsMapper.buyGetEsGoods(id);
            return DubboResult.success(goodsDO);
        }catch (ArgumentException ae){
            logger.error("获取商品信息失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("获取商品信息失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 卖家中心商品分页查询
     * @param esGoodsDTO  商品DTO
     * @param pageSize 页数
     * @param pageNum 页码
     * @return 商品集合
     */
    @Override
    public DubboPageResult<EsGoodsDO> sellerGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO, Long shopId , int pageSize, int pageNum) {
        try{
            //店铺ID
            esGoodsDTO.setShopId(shopId);
            Long[] cateIds = null;
            if(StringUtils.isNotBlank(esGoodsDTO.getCategoryPath())){
                QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().like(EsCategory::getCategoryPath,esGoodsDTO.getCategoryPath());
                List<EsCategory> esCategoryList = this.esCategoryMapper.selectList(queryWrapper);
                if(CollectionUtils.isNotEmpty(esCategoryList)){
                    List<Long> sku_ids = esCategoryList.stream().map(EsCategory::getId).collect(Collectors.toList());
                    cateIds =  sku_ids.stream().toArray(Long[]::new);
                }
            }
            IPage<EsGoodsDO> page =  this.esGoodsMapper.getEsGoodsPageList(new Page(pageNum,pageSize),esGoodsDTO,cateIds);
            if(page.getTotal() <= 0){
                return DubboPageResult.success(page.getTotal(),new ArrayList<>());
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        }catch (ArgumentException ae){
            logger.error("商品分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }
    /**
     * 管理后台 商品分页查询
     * @param esGoodsDTO  商品DTO
     * @param pageSize 页数
     * @param pageNum 页码
     * @return 商品集合
     */
    @Override
    public DubboPageResult<EsGoodsDO> adminGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO, int pageSize, int pageNum) {
        try{
            Long[] cateIds = null;
            if(StringUtils.isNotBlank(esGoodsDTO.getCategoryPath())){
                QueryWrapper<EsCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().like(EsCategory::getCategoryPath,esGoodsDTO.getCategoryPath());
                List<EsCategory> esCategoryList = this.esCategoryMapper.selectList(queryWrapper);
                if(CollectionUtils.isNotEmpty(esCategoryList)){
                    List<Long> sku_ids = esCategoryList.stream().map(EsCategory::getId).collect(Collectors.toList());
                    cateIds =  sku_ids.stream().toArray(Long[]::new);
                }
            }
            esGoodsDTO.setIsDel(esGoodsDTO.getIsDel() == null ? 0L : esGoodsDTO.getIsDel()) ;
            IPage<EsGoodsDO> page =  this.esGoodsMapper.adminGetEsGoodsPageList(new Page(pageNum,pageSize),esGoodsDTO,cateIds);
            if(page.getTotal() ==0){
                return DubboPageResult.success(page.getTotal(),new ArrayList<>());
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        }catch (ArgumentException ae){
            logger.error("商品分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }

    @Override
    public DubboPageResult<EsGoodsDO> adminGetEsGoodsList(int pageSize, int pageNum) {
          try{
                Page<EsGoods> page = new Page<>(pageNum,pageSize);
                QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoods::getIsAuth,1).
                        eq(EsGoods::getMarketEnable,1)
                        .eq(EsGoods::getIsDel,0).eq(EsGoods::getIsGifts,2);
                IPage<EsGoods> esGoodsList =  this.page(page,queryWrapper);
                List<EsGoodsDO> goodsDoList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(esGoodsList.getRecords())){
                    goodsDoList = esGoodsList.getRecords().stream().map(esGoods -> {
                        EsGoodsDO esGoodsDO = new EsGoodsDO();
                        BeanUtil.copyProperties(esGoods,esGoodsDO);
                        return esGoodsDO;
                    }).collect(Collectors.toList());
                }
            return DubboPageResult.success(page.getTotal(),goodsDoList);
        }catch (ArgumentException ae){
            logger.error("商品分页查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品分页查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult adminGetEsGoodsCount() {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getIsGifts,2);
            Integer count = this.esGoodsMapper.selectCount(queryWrapper);
            return DubboResult.success(count);
        } catch (ArgumentException ae){
            logger.error("商品统计失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品统计失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }

    @Override
    public DubboResult<Map<String, String>> buyCheckGoods(Long[] goodsIds) {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,goodsIds).eq(EsGoods::getIsDel,0);
            List<EsGoods> goodsList = this.list(queryWrapper);
            Map<Long,List<EsGoods>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(EsGoods::getShopId,Collectors.toList()));
            Map<String,String> resultMap = new HashedMap();
            if(goodsMap.size() >= 2){
                resultMap.put("result","false");
                return DubboResult.success(resultMap);
            }
            resultMap.put("result","true");
            return DubboResult.success(resultMap);
        } catch (ArgumentException ae){
            logger.error("商品所属卖家一致性校验失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品所属卖家一致性校验失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }

    @Override
    public DubboResult<List<Long>> getUnderGoods(Integer[] ids) {
        try {
            EsGoodsDTO goodsDTO = new EsGoodsDTO();
            goodsDTO.setGoods_ids(ids);
            goodsDTO.setMarketEnable(2);
            goodsDTO.setIsDel(1);
            List<EsGoods> goodsList = this.esGoodsMapper.getEsGoodsList(goodsDTO,null);
            List<Long> goodsIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(goodsList)){
                goodsIdList   = goodsList.stream().map(EsGoods::getId).collect(Collectors.toList());
            }
            return DubboResult.success(goodsIdList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *
     * @param shopId
     * @param tagId
     * @return
     */
    @Override
    public DubboPageResult<EsGoodsDO> getTagGoods(Long shopId, Long tagId,int pageSize,int pageNum) {
        try{
            QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTags::getShopId,shopId).eq(EsTags::getId,tagId);
            EsTags esTags = this.esTagsMapper.selectOne(queryWrapper);
            if(esTags == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品标签不存在或无权限操作");
            }
            QueryWrapper<EsTagGoods> tagGoodsWrapper = new QueryWrapper<>();
            tagGoodsWrapper.lambda().eq(EsTagGoods::getTagId,tagId);
            //获取标签商品数据
            List<EsTagGoods> tagGoodsList =  this.esTagGoodsMapper.selectList(tagGoodsWrapper);
            if(CollectionUtils.isEmpty(tagGoodsList)){
                return DubboPageResult.success(0L,new ArrayList<>());
            }
            //获取商品、商品SKU信息
            List<Long> goodsList = tagGoodsList.stream().map(EsTagGoods::getGoodsId).collect(Collectors.toList());
            QueryWrapper<EsGoods> goodsQuery = new QueryWrapper<>();
            goodsQuery.lambda().eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getShopId,shopId).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsGifts,2).
                    eq(EsGoods::getIsAuth,1).
                    in(EsGoods::getId,goodsList);

            Page<EsGoods> page = new Page<>(pageNum,pageSize);
            IPage<EsGoods> esGoodsList =  this.esGoodsMapper.selectPage(page,goodsQuery);
            if(CollectionUtils.isEmpty(esGoodsList.getRecords())){
                return DubboPageResult.success(page.getTotal(),new ArrayList<>());
            }
            List<EsGoodsDO> esGoodsDOList = BeanUtil.copyList(esGoodsList.getRecords(),EsGoodsDO.class);
            esGoodsDOList.forEach(goods->{
                EsGoodsSkuQueryDTO esGoodsSkuQueryDTO = new EsGoodsSkuQueryDTO();
                esGoodsSkuQueryDTO.setGoodsId(goods.getId());
                DubboPageResult<EsGoodsSkuDO> result = esGoodsSkuService.getGoodsSkuList(esGoodsSkuQueryDTO);
                if(result.isSuccess()){
                    goods.setSkuList(result.getData().getList());
                }
            });
            return DubboPageResult.success(page.getTotal(),esGoodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult getSelfGoods(List<Long>ids) {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids).eq(EsGoods::getSelfOperated,1);
            List<EsGoods> esGoodsList = this.list(queryWrapper);
            List<EsGoodsDO> goodsDList = BeanUtil.copyList(esGoodsList,EsGoodsDO.class);
            return DubboPageResult.success(goodsDList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsGoodsDO> updateBuyCountUseMq(Long goodsId, Integer goodsNum) {
        try {
            Map<String,Map<String,Object>> mqContent = Maps.newHashMap();
            Map<String,Object> buyCountMap = Maps.newHashMap();
            buyCountMap.put("goodsId",goodsId);
            buyCountMap.put("goodsNum",goodsNum);
            mqContent.put(GoodsOperateType.UPDATE_BUY_COUNT.name(),buyCountMap);
            mqProducer.send(goods_topic,JsonUtil.objectToJson(mqContent));
            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品更新失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    public DubboResult<EsGoodsDO> updateBuyCount(Long goodsId, Integer goodsNum) {
        try{
            this.esGoodsMapper.updateBuyCount(goodsId,goodsNum);
            EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
            EsGoods goods = this.esGoodsMapper.selectById(goodsId);
            BeanUtil.copyProperties(goods,esGoodsIndexDTO);
            this.esGoodsIndexService.updateEsGoodsIndex(esGoodsIndexDTO);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品购买数量修改失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品购买数量修改失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsGoodsDO> updateViewCount(Long goodsId) {
        try{
            this.esGoodsMapper.updateViewCount(goodsId);
            EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
            EsGoods goods = this.esGoodsMapper.selectById(goodsId);
            BeanUtil.copyProperties(goods,esGoodsIndexDTO);
            this.esGoodsIndexService.updateEsGoodsIndex(esGoodsIndexDTO);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品浏览数量修改失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品浏览数量修改失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsGoodsDO> updateCommenCount(Long goodsId) {
        try{
            this.esGoodsMapper.updateCommenCount(goodsId);
            EsGoodsIndexDTO esGoodsIndexDTO = new EsGoodsIndexDTO();
            EsGoods goods = this.esGoodsMapper.selectById(goodsId);
            BeanUtil.copyProperties(goods,esGoodsIndexDTO);
            this.esGoodsIndexService.updateEsGoodsIndex(esGoodsIndexDTO);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品浏览数量修改失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品浏览数量修改失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(Long category) {
      try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getCategoryId,category).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getIsGifts,2);
            List<EsGoods> goodsList =  this.list(queryWrapper);
            List<EsGoodsDO> goodsDOList = goodsList.stream().map(goods->{
                  EsGoodsDO goodsDO = new EsGoodsDO();
                  BeanUtil.copyProperties(goods,goodsDO);
                  return goodsDO;
              }).collect(Collectors.toList());
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException ae){
            logger.error("根据分类ID获取商品信息查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("根据分类ID获取商品信息查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(Long category,Long shopId,Long goodsId,int pageNum, int pageSize) {
        try{
            Page<EsGoods> page = new Page<>(pageNum,pageSize);
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getIsDel,0).
                    ne(EsGoods::getId,goodsId).
                    eq(EsGoods::getIsGifts,2);
            if(category != null ){
                queryWrapper.lambda().eq(EsGoods::getCategoryId,category);
            }
            if(shopId != null){
                queryWrapper.lambda().eq(EsGoods::getShopId,shopId);
            }
            queryWrapper.lambda().orderByDesc(EsGoods::getBuyCount);

            IPage<EsGoods> esGoodsList =  this.page(page,queryWrapper);
            List<EsGoodsDO> goodsDOList = esGoodsList.getRecords().stream().map(goods->{
                EsGoodsDO goodsDO = new EsGoodsDO();
                BeanUtil.copyProperties(goods,goodsDO);
                return goodsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(page.getTotal(),goodsDOList);
        }catch (ArgumentException ae){
            logger.error("根据分类ID获取商品信息查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("根据分类ID获取商品信息查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据DTO 获取商品列表信息
     * @param esGoodsDTO
     * @return
     */
    @Override
    public DubboPageResult<EsGoodsDO> sellerGetEsGoodsList(EsGoodsQueryDTO esGoodsDTO) {
        try{
            QueryWrapper<EsGoods> queryWrapper =joinGoodsWhere(esGoodsDTO);
            List<EsGoods> esGoodsList =  this.list(queryWrapper);
            List<EsGoodsDO> esGoodsDOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(esGoodsList)){
                esGoodsDOList = esGoodsList.stream().map(esGoods -> {
                    EsGoodsDO esGoodsDO = new EsGoodsDO();
                    BeanUtil.copyProperties(esGoods,esGoodsDO);
                    return esGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(esGoodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    public DubboPageResult<EsGoodsDO> sellerDiscountEsGoodsList(EsGoodsQueryDTO esGoodsDTO) {
        try{
            Page<EsGoods> page = new Page<>(esGoodsDTO.getPageNum(),esGoodsDTO.getPageSize());
            QueryWrapper<EsGoods> queryWrapper =joinGoodsWhere(esGoodsDTO);
            IPage<EsGoods> esGoodsList =  this.page(page,queryWrapper);
            List<EsGoodsDO> esGoodsDOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(esGoodsList.getRecords())){
                esGoodsDOList = esGoodsList.getRecords().stream().map(esGoods -> {
                    EsGoodsDO esGoodsDO = new EsGoodsDO();
                    BeanUtil.copyProperties(esGoods,esGoodsDO);
                    return esGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(esGoodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    /**
     * 商品新增
     * @param esGoodsDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> insertEsGoods(EsGoodsDTO esGoodsDTO) {
        try{
            Long goodsId = esGoodsDTO.getId() == null ? -1 : esGoodsDTO.getId();
            EsGoodsArch esGoodsArch = esGoodsArchMapper.selectById(goodsId);
            if(esGoodsArch == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品档案不存在");
            }
           EsGoods esGoods = this.getById(goodsId);
            if(esGoods != null){
                throw new ArgumentException(GoodsErrorCode.GOODS_EXIST.getErrorCode(),GoodsErrorCode.GOODS_EXIST.getErrorMsg());
            }
            esGoods = new EsGoods();
            //先将档案数据同步到商品表
            BeanUtil.copyProperties(esGoodsArch,esGoods);
            //商品前置条件检测
            DubboResult<EsGoodsDO> result = this.checkGoodsCondition(esGoodsDTO);
            if(!result.isSuccess()){
                return result;
            }
            BeanUtil.copyProperties(esGoodsDTO,esGoods);
            //判断是否自营商品
            esGoods.setSelfOperated(esGoodsDTO.getSelfOperated() == 1 ? 1 : 2);
            //取SKU价格中最小的一个当商品价格
            Double goodsMoney = esGoodsDTO.getSkuList().stream().mapToDouble(EsGoodsSkuDTO::getMoney).min().getAsDouble();
            esGoods.setMoney(goodsMoney);
            esGoods.setIsDel(0);
            esGoods.setMarketEnable(1);
            esGoods.setIsAuth(0);
            esGoods.setViewCount(0);
            esGoods.setBuyCount(0);
            esGoods.setCommentNum(0);
            esGoods.setIsLfc(esGoodsDTO.getIsLfc());
            //插入商品
            this.esGoodsMapper.insert(esGoods);
            //标签绑定商品（商品标签关系表数据新增）
            //this.esTagGoodsService.insertAdminTagGoods(esGoodsDTO.getId(),esGoodsDTO.getTagsIds());
            if(esGoodsDTO.getTagsIds().length>0){
                this.esTagGoodsService.insertTagGoods(esGoodsDTO.getTagsIds(),esGoodsDTO.getId());
            }
            //更新SKU信息(商品价格、成本价格、库存、是否启用)
            this.esGoodsSkuService.sellerUpdateGoodsSku(esGoodsDTO.getSkuList(),esGoodsDTO);
            //插入商品参数
            this.esGoodsParamsService.insertGoodsParams(esGoodsDTO.getParamsList(),esGoodsDTO.getId());
            //如果是国寿的商品将数据插入到国寿商品表
            if(esGoodsDTO.getIsLfc() == 1){
                EsLfcGoods esLfcGoods = new EsLfcGoods();
                esLfcGoods.setGoodsId(esGoods.getId());
                esLfcGoods.setIsAuth(0);
                this.esLfcGoodsMapper.insert(esLfcGoods);
            }
            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品新增失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品新增失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> insertGiftsEsGoods(EsGoodsDTO esGoodsDTO) {
        try{
            Long goodsId = esGoodsDTO.getId() == null ? -1 : esGoodsDTO.getId();
            EsGoodsArch esGoodsArch = esGoodsArchMapper.selectById(goodsId);
            if(esGoodsArch == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品档案不存在");
            }
            EsGoods esGoods = this.getById(goodsId);
            if(esGoods != null){
                throw new ArgumentException(GoodsErrorCode.GOODS_EXIST.getErrorCode(),GoodsErrorCode.GOODS_EXIST.getErrorMsg());
            }
            esGoods = new EsGoods();
            //先将档案数据同步到商品表
            BeanUtil.copyProperties(esGoodsArch,esGoods);
            BeanUtil.copyProperties(esGoodsDTO,esGoods);
            //判断是否自营商品
            esGoods.setSelfOperated(1);
            //取SKU价格中最小的一个当商品价格
            esGoods.setMoney(esGoodsDTO.getMoney());
            esGoods.setIsDel(0);
            esGoods.setMarketEnable(1);
            esGoods.setIsAuth(0);
            esGoods.setViewCount(0);
            esGoods.setBuyCount(0);
            esGoods.setCommentNum(0);
            esGoods.setIsLfc(esGoodsDTO.getIsLfc());
            //插入商品
            this.esGoodsMapper.insert(esGoods);

            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品新增失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品新增失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 商品更新
     * @param esGoodsDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> sellerUpdateEsGoods(EsGoodsDTO esGoodsDTO,Long id) {
        try{
            if(esGoodsDTO == null){
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),GoodsErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //判断该商品是否存在 是否有权限操作
            EsGoods goods =  this.getById(id);
            //预留判断 卖家Id
            if(goods == null || (goods.getShopId().longValue()!= esGoodsDTO.getShopId().longValue())){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(),GoodsErrorCode.NO_AUTH.getErrorMsg());
            }
            this.checkGoodsCondition(esGoodsDTO);
            //商品前置条件检测
            DubboResult<EsGoodsDO> result = this.checkGoodsCondition(esGoodsDTO);
            if(!result.isSuccess()){
                return result;
            }
            BeanUtil.copyProperties(esGoodsDTO,goods);
            goods.setSelfOperated(esGoodsDTO.getSelfOperated() == 1 ? 1 : 2);

            List<Double> skuMoney =  esGoodsDTO.getSkuList().stream().map(EsGoodsSkuDTO::getMoney).collect(Collectors.toList());
            DoubleSummaryStatistics money = skuMoney.stream().mapToDouble(x->x).summaryStatistics();
            goods.setMoney(money.getMin());
            goods.setMarketEnable(1);
            goods.setIsAuth(0);
            //插入商品
            this.updateById(goods);
            //标签绑定商品（商品标签关系表数据新增）
            if(esGoodsDTO.getTagsIds().length>0){
                this.esTagGoodsService.insertTagGoods(esGoodsDTO.getTagsIds(),esGoodsDTO.getId());
            }
            //更新SKU信息(商品价格、成本价格、库存、是否启用)
            this.esGoodsSkuService.sellerUpdateGoodsSku(esGoodsDTO.getSkuList(),esGoodsDTO);
            //插入商品参数
            this.esGoodsParamsService.insertGoodsParams(esGoodsDTO.getParamsList(),esGoodsDTO.getId());

            EsGoodsIndexTO goodsIndexTO = new EsGoodsIndexTO();
            BeanUtil.copyProperties(goods,goodsIndexTO);
            //发送MQ消息
            Map<String,EsGoodsIndexTO> goodsMap = new HashMap<String, EsGoodsIndexTO>();
            goodsMap.put(GoodsOperateType.UPDATE.name(),goodsIndexTO);
            //删除缓存信息
            jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
            mqProducer.send(goods_topic,JsonUtil.objectToJson(goodsMap));
            EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
            goodsIndexDTO.setId(Long.valueOf(id));
            esGoodsIndexService.deleteEsGoodsIndex(goodsIndexDTO);
            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品更新失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 商品删除（物理删除）
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> deleteEsGoods(Integer[] ids, Long shopId) {
        try{

            //判断要删除的商品中是否存在不属于该用户的，是否存在不允许删除的商品
            this.checkPermission(ids,shopId,GoodsEnum.DELETE);
            this.esGoodsMapper.deleteEsGoods(ids,shopId);
            //删除标签关系表数据
            this.esTagGoodsService.deleteTagGoods(ids);
            Arrays.stream(ids).forEach(id->{
                EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
                goodsIndexDTO.setId(Long.valueOf(id));
                esGoodsIndexService.deleteEsGoodsIndex(goodsIndexDTO);
                jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
            });

            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> deleteEsGoods(Long[] ids) {
        try{

            //判断要删除的商品中是否存在不属于该用户的，是否存在不允许删除的商品
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getId,ids);
            this.esGoodsMapper.delete(queryWrapper);
            //删除标签关系表数据
            QueryWrapper<EsTagGoods> tagGoodsQueryWrapper = new QueryWrapper<>();
            tagGoodsQueryWrapper.lambda().in(EsTagGoods::getGoodsId,ids);
            this.esTagGoodsMapper.delete(tagGoodsQueryWrapper);
            Arrays.stream(ids).forEach(id->{
                EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
                goodsIndexDTO.setId(Long.valueOf(id));
                esGoodsIndexService.deleteEsGoodsIndex(goodsIndexDTO);
                jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
            });

            return DubboResult.success();
        }catch(ArgumentException ae){
            logger.error("商品删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(Throwable th){
            logger.error("商品删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 卖家中心商品下架 支持批量下架
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> sellerUnderEsgoods(Integer[] ids,Long shopId) {
        try{
            //判断卖家权限是否能操作该商品
            this.checkPermission(ids,shopId,GoodsEnum.UNDER);
            EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
            esGoodsDTO.setGoods_ids(ids);
            List<EsGoods> esGoodsList = this.esGoodsMapper.getEsGoodsList(esGoodsDTO,shopId);
            //判断该商品状态是否能够下架
            esGoodsList.stream().forEach(esGoods -> {
                GoodsOperate goodsOperate = new  GoodsOperate(esGoods.getMarketEnable(),esGoods.getIsDel());
                //判断是否能够下架 （未删除且已上架状态能够下架）
                if(!goodsOperate.getAllowUnder()){
                    throw new ArgumentException (GoodsErrorCode.UN_DER.getErrorCode(), String.format("存在不能下架的商品%s",esGoods.getId()));
                }
                esGoods.setMarketEnable(GoodsConstants.UN_DER);
            });
            this.updateBatchById(esGoodsList,esGoodsList.size());
            //根据商品ID更新SKU为未启用
            QueryWrapper<EsGoodsSku> skuQueryWrapper = new QueryWrapper<>();
            skuQueryWrapper.lambda().in(EsGoodsSku::getGoodsId,ids);
            EsGoodsSku esGoodsSku = new EsGoodsSku();
            esGoodsSku.setIsEnable(2);
            esGoodsSkuMapper.update(esGoodsSku,skuQueryWrapper);
            List<EsGoodsSku> goodsSkuList = this.esGoodsSkuMapper.selectList(skuQueryWrapper);
            goodsSkuList.forEach(sku->{
                String skustr = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix()+sku.getId());
                if(StringUtils.isNotBlank(skustr)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+sku.getId());
                }
            });
            Arrays.stream(ids).forEach(id->{
                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix() + id);
                if(StringUtils.isNotBlank(goodsCache)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
                }
            });
            esGoodsList.forEach(goods->{
                EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
                goodsIndexDTO.setId(goods.getId());
                esGoodsIndexService.deleteEsGoodsIndex(goodsIndexDTO);
            });
            List<Long> lfcIds = esGoodsList.stream().filter(goods->goods.getIsLfc()==1).map(EsGoods::getId).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(lfcIds)){
                QueryWrapper<EsLfcGoods> deleteWrapper = new QueryWrapper<>();
                deleteWrapper.lambda().in(EsLfcGoods::getGoodsId,lfcIds);
                esLfcGoodsMapper.delete(deleteWrapper);
           }
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品下架失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品下架失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 管理后台商品下架
     * @param ids
     * @return
     */
    @Override
    public DubboResult<EsGoodsDO> adminUnderEsGoods(Integer[] ids) {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids).eq(EsGoods::getIsDel,0);
            List<EsGoods> esGoodsList = this.esGoodsMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(esGoodsList)){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品数据不存在");
            }
            esGoodsList.stream().forEach(esGoods -> {
                esGoods.setMarketEnable(GoodsConstants.AB_NORMAL);
            });
            this.updateBatchById(esGoodsList,esGoodsList.size());
            //根据商品ID更新SKU为未启用
            QueryWrapper<EsGoodsSku> skuQueryWrapper = new QueryWrapper<>();
            skuQueryWrapper.lambda().in(EsGoodsSku::getGoodsId,ids);
            EsGoodsSku esGoodsSku = new EsGoodsSku();
            esGoodsSku.setIsEnable(2);
            esGoodsSkuMapper.update(esGoodsSku,skuQueryWrapper);
            List<EsGoodsSku> goodsSkuList = this.esGoodsSkuMapper.selectList(skuQueryWrapper);
            goodsSkuList.forEach(sku->{
                String goodsSkuCache = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + sku.getId());
                if(StringUtils.isNotBlank(goodsSkuCache)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+sku.getId());

                }
            });
            Arrays.stream(ids).forEach(id->{
                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix() + id);
                if(StringUtils.isNotBlank(goodsCache)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
                }
            });
            esGoodsList.forEach(goods->{
                EsGoodsIndexDTO goodsIndexDTO = new EsGoodsIndexDTO();
                goodsIndexDTO.setId(goods.getId());
                esGoodsIndexService.deleteEsGoodsIndex(goodsIndexDTO);
            });
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品下架失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品下架失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 店铺商品全部下架
     * @param shopId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> adminUnderEsGoods(Long shopId) {
        try{
            List<EsGoods> esGoodsList = this.esGoodsMapper.getEsGoodsList(new EsGoodsDTO(),shopId);
            //判断该商品状态是否能够下架
            esGoodsList.stream().forEach(esGoods -> {
                esGoods.setMarketEnable(GoodsConstants.AB_NORMAL);
            });
            this.updateBatchById(esGoodsList,esGoodsList.size());
            //根据商品ID更新SKU为未启用
            QueryWrapper<EsGoodsSku> skuQueryWrapper = new QueryWrapper<>();
            skuQueryWrapper.lambda().eq(EsGoodsSku::getShopId,shopId);
            EsGoodsSku esGoodsSku = new EsGoodsSku();
            esGoodsSku.setIsEnable(2);
            esGoodsSkuMapper.update(esGoodsSku,skuQueryWrapper);
            List<EsGoodsSku> goodsSkuList = this.esGoodsSkuMapper.selectList(skuQueryWrapper);
            goodsSkuList.forEach(sku->{
                String goodsSkuCache = jedisCluster.get(GoodsCachePrefix.SKU.getPrefix() + sku.getId());
                if(StringUtils.isNotBlank(goodsSkuCache)){
                    jedisCluster.del(GoodsCachePrefix.SKU.getPrefix()+sku.getId());
                }
            });
            esGoodsList.stream().forEach(id->{
                String goodsCache = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix() + id);
                if(StringUtils.isNotBlank(goodsCache)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+id);
                }
            });
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品下架失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品下架失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 商品新增前置条件判断
     * @param esGoodsDTO
     * @return
     */
    public DubboResult<EsGoodsDO> checkGoodsCondition(EsGoodsDTO esGoodsDTO) {
        try{
                Long shopId = esGoodsDTO.getShopId() == null ? 0L : esGoodsDTO.getShopId();
                DubboResult<EsShopDO> esShopDO =  esShopService.getShop(shopId);
                //如果该商品店铺不存在或者店铺关闭状态 则不能发布商品
                if( !esShopDO.isSuccess() || !StringUtils.equals(esShopDO.getData().getState().toString(),ShopStatusEnums.OPEN.value())){
                    throw new ArgumentException(GoodsErrorCode.SHOP_NOT_EXIST.getErrorCode(),GoodsErrorCode.SHOP_NOT_EXIST.getErrorMsg());
                }
                //判断商品分类是否为空 或者商品分类是否存在
                Long categoryId = esGoodsDTO.getCategoryId() == null ? -1 : esGoodsDTO.getCategoryId();
                EsCategory esCategory = this.esCategoryMapper.selectById(categoryId);
                if(esCategory == null){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("商品未绑定分类[%s]",esGoodsDTO.getId()));
                }
                //如果是买家承担运费 判断运费模板是否存在
                if(esGoodsDTO.getGoodsTransfeeCharge() == 1  && esGoodsDTO.getTemplateId()  == null){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("商品未绑定运费模板[%s]",esGoodsDTO.getId()));
                }
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品新增失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品新增失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 回收站商品还原
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> revertEsGoods(Integer[] ids,Long shopId) {
        try{
            this.checkPermission(ids,shopId,GoodsEnum.REVRET);
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids);
            EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
            esGoodsDTO.setGoods_ids(ids);
           List<EsGoods> esGoodsList = this.esGoodsMapper.getEsGoodsList(esGoodsDTO,shopId);
            esGoodsList.stream().forEach(esGoods -> {
                esGoods.setIsDel(0);
            });
            this.updateBatchById(esGoodsList,esGoodsList.size());
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品回收站还原失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品回收站还原失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 放入回收站(逻辑删除)
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> inRecycleEsGoods(Integer[] ids,Long shopId) {
        try{
            this.checkPermission(ids,shopId,GoodsEnum.RECYCLE);
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids);
            List<EsGoods> goodsList = this.esGoodsMapper.selectList(queryWrapper);
            goodsList.forEach(goods->{
                goods.setIsDel(1);
                String goodsStr =jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+goods.getId());
                if(StringUtils.isNotBlank(goodsStr)){
                    jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+goods.getId());
                }
            });
            this.updateBatchById(goodsList);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品放入回收站失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品放入回收站失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 商品审核
     * @param ids 商品id数组
     * @param status 审核状态
     * @param message 审核原因
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> authEsGoods(Integer[] ids, Integer status, String message) {
        try{
            //审核拒绝
            if(status == 2 && StringUtils.isBlank(message)){
                throw new ArgumentException(GoodsErrorCode.REASONS_FOR_REFUSAL.getErrorCode(),GoodsErrorCode.REASONS_FOR_REFUSAL.getErrorMsg());
            }
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,ids);
            List<EsGoods> goodsList = this.esGoodsMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(goodsList)){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            goodsList.stream().forEach(esGoods -> {
                //已经审核
                if(esGoods.getIsAuth() != 0){
                    throw new ArgumentException(GoodsErrorCode.DATA_AUDITED.getErrorCode(),GoodsErrorCode.DATA_AUDITED.getErrorMsg());
                }
                esGoods.setAuthMessage(message);
                esGoods.setIsAuth(status);
                esGoods.setCreateTime(new Date().getTime());
                esGoods.setUpdateTime(new Date().getTime());
                if(status == 1){
                    esGoods.setMarketEnable(1);
                    String goodsStr = jedisCluster.get(GoodsCachePrefix.GOODS.getPrefix()+esGoods.getId());
                    if(StringUtils.isNotBlank(goodsStr)){
                        jedisCluster.del(GoodsCachePrefix.GOODS.getPrefix()+esGoods.getId());
                    }
                }
            });
            this.updateBatchById(goodsList,goodsList.size());
            if(status == 1){
                List<EsGoodsIndexDTO> goodsIndeList = BeanUtil.copyList(goodsList,EsGoodsIndexDTO.class);
                esGoodsIndexService.insertEsGoodsIndex(goodsIndeList);
                List<EsGoodsMqDO> goodsMqDOList = BeanUtil.copyList(goodsList,EsGoodsMqDO.class);
                mqProducer.send(goods_change_topic,JsonUtil.objectToJson(goodsMqDOList));
            }
            List<Long> lfcIds = goodsList.stream().filter(goods->goods.getIsLfc()==1).map(EsGoods::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(lfcIds)){
                QueryWrapper<EsLfcGoods> lfcWrapper = new QueryWrapper<>();
                lfcWrapper.lambda().in(EsLfcGoods::getGoodsId,lfcIds);
                List<EsLfcGoods> lfcGoodsList = this.esLfcGoodsMapper.selectList(lfcWrapper);
                lfcGoodsList.forEach(esLfcGoods -> {
                    if(status == 1){
                        esLfcGoods.setIsAuth(1);
                    }else{
                        esLfcGoods.setIsAuth(2);
                    }
                    this.esLfcGoodsMapper.updateById(esLfcGoods);
                });
            }
            return DubboResult.success();
        }catch (ArgumentException ae){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("商品审核失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("商品审核失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 自定义分类绑定商品
     * @param shopId 店铺ID
     * @param customId 自定义分类ID
     * @param goodsIds 商品id数组
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> bindGoodsCustom(Long shopId, Long customId, Integer[] goodsIds) {
        try {
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,goodsIds).eq(EsGoods::getShopId,shopId);
            List<EsGoods> esGoodsList = this.list(queryWrapper);
            if(esGoodsList.size() != goodsIds.length){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(),"无权限操作或商品数据不存在");
            }
            this.esGoodsMapper.updateBathByCustom(customId);
            DubboResult<EsCustomDO> esCustomDO = esCustomService.getCustom(customId);
            if(!esCustomDO.isSuccess()){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品自定义分类不存在");
            }
            for(int i=0;i<goodsIds.length;i++){
                EsGoods esGoods = new EsGoods();
                esGoods.setId(goodsIds[i].longValue());
                esGoods.setCustomId(customId);
                this.updateById(esGoods);
            }
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品绑定自定义分类失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品绑定自定义分类失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }
    /**
     * 自定义分类取消绑定商品
     * @param shopId 店铺ID
     * @param goodsIds 商品id数组
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsDO> unBindGoodsCustom(Long shopId, Integer[] goodsIds) {
        try {
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsGoods::getId,goodsIds).eq(EsGoods::getShopId,shopId);
            List<EsGoods> esGoodsList = this.list(queryWrapper);
            if(esGoodsList.size() != goodsIds.length){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(),"无权限操作");
            }
            for(int i=0;i<goodsIds.length;i++){
                EsGoods esGoods = new EsGoods();
                esGoods.setId(goodsIds[i].longValue());
                esGoods.setCustomId(null);
                this.updateById(esGoods);
            }
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品取消绑定自定义分类失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品取消绑定自定义分类失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }
    /**
     * 商品操作校验
     * @param ids
     * @param shopId
     * @param goodsEnum
     */
    public void checkPermission(Integer[] ids,Long shopId, GoodsEnum goodsEnum){
        EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
        esGoodsDTO.setGoods_ids(ids);
        List<EsGoods> esGoodsList = this.esGoodsMapper.getEsGoodsList(esGoodsDTO,shopId);
        if(esGoodsList.size() != ids.length){
            throw new ArgumentException(GoodsErrorCode.DATA_NOT_BELONG.getErrorCode(), GoodsErrorCode.DATA_NOT_BELONG.getErrorMsg());
        }
        esGoodsList.stream().forEach(esGoods -> {
            GoodsOperate goodsOperate = new  GoodsOperate(esGoods.getMarketEnable(),esGoods.getIsDel());
            switch (goodsEnum) {
                case DELETE:
                    if (!goodsOperate.getAllowDelete()) {
                        throw new ArgumentException(GoodsErrorCode.DATA_NOT_DELETE.getErrorCode(), GoodsErrorCode.DATA_NOT_DELETE.getErrorMsg());
                    }
                    break;
                case RECYCLE:
                    if (!goodsOperate.getAllowRecycle()) {
                        throw new ArgumentException(GoodsErrorCode.DATA_NOT_RECYCLE.getErrorCode(), GoodsErrorCode.DATA_NOT_RECYCLE.getErrorMsg());
                    }
                    break;
                case REVRET:
                    if (!goodsOperate.getAllowRevert()) {
                        throw new ArgumentException(GoodsErrorCode.DATA_NOT_REVERT.getErrorCode(), GoodsErrorCode.DATA_NOT_REVERT.getErrorMsg());
                    }
                    break;
                case UNDER:
                    if (!goodsOperate.getAllowUnder()) {
                        throw new ArgumentException(GoodsErrorCode.UN_DER.getErrorCode(), GoodsErrorCode.UN_DER.getErrorMsg());
                    }
                    break;
                default:
                    break;
            }
        });
    }
    public QueryWrapper<EsGoods> joinGoodsWhere(EsGoodsQueryDTO esGoodsDTO){
        if(esGoodsDTO == null){
            throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsGoods::getIsGifts,2).eq(EsGoods::getIsDel,0);
        //根据ID排序 降序
        queryWrapper.orderByDesc("id");
        //上下架状态
        if(esGoodsDTO.getMarketEnable() !=null ){
            queryWrapper.lambda().eq(EsGoods::getMarketEnable,esGoodsDTO.getMarketEnable());
        }
        //商品名称 模糊查询 或者按照商品编号模糊查询 暂定
        if(!StringUtils.isBlank(esGoodsDTO.getGoodsName())){
            queryWrapper.lambda().like(EsGoods::getGoodsName,esGoodsDTO.getGoodsName())
                    .or().like(EsGoods::getGoodsSn,esGoodsDTO.getGoodsSn());
        }
        //商品分类路径 查询父子分类
        if(!StringUtils.isBlank(esGoodsDTO.getCategoryPath())){
            //需查询父类及子类下面的
            QueryWrapper<EsCategory> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.lambda().like(EsCategory::getCategoryPath,esGoodsDTO.getCategoryPath());
            List<EsCategory> esCategoryList = this.esCategoryMapper.selectList(categoryQueryWrapper);
            if(CollectionUtils.isEmpty(esCategoryList)){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"分类不存在");
            }
            //获取分类ID及子分类ID
            List<Long> sku_ids = esCategoryList.stream().map(EsCategory::getId).collect(Collectors.toList());
            queryWrapper.lambda().in(EsGoods::getCategoryId,sku_ids);
        }
        //查询单个分类
        if(esGoodsDTO.getCategoryId() != null){
            queryWrapper.lambda().in(EsGoods::getCategoryId,esGoodsDTO.getCategoryId());
        }
        //审核状态
        if(esGoodsDTO.getIsAuth() != null ){
            queryWrapper.lambda().eq(EsGoods::getIsAuth,esGoodsDTO.getIsAuth());
        }
        //更新时间 需传开始时间 结束时间
        if(esGoodsDTO.getStartTime() != null && esGoodsDTO.getEndTime() != null){
            queryWrapper.lambda().between(EsGoods::getUpdateTime,esGoodsDTO.getStartTime(),esGoodsDTO.getEndTime());
        }
        //起始金额 截止金额
        if(esGoodsDTO.getStartMoney()!=null && esGoodsDTO.getEndMoney()!=null){
            queryWrapper.lambda().between(EsGoods::getMoney,esGoodsDTO.getStartMoney(),esGoodsDTO.getEndMoney());
        }
        if(esGoodsDTO.getCustomId() !=null){
            queryWrapper.lambda().eq(EsGoods::getCustomId,esGoodsDTO.getCustomId());
        }
        if(esGoodsDTO.getShopId() !=null){
            queryWrapper.lambda().eq(EsGoods::getShopId,esGoodsDTO.getShopId());
        }
        if(StringUtils.isNotBlank(esGoodsDTO.getKeyword())){
            queryWrapper.lambda().like(EsGoods::getGoodsName,esGoodsDTO.getKeyword()).or()
                    .like(EsGoods::getGoodsSn,esGoodsDTO.getKeyword());
        }
        if(StringUtils.isNotBlank(esGoodsDTO.getCustomState())){
          if(StringUtils.equals(esGoodsDTO.getCustomState(),"2") ){
              //查询自定义分组为空
              queryWrapper.lambda().isNull(EsGoods::getCustomId);
          }else if(StringUtils.equals(esGoodsDTO.getCustomState(),"1")){
              //查询自定义分组不为空
              queryWrapper.lambda().isNotNull(EsGoods::getCustomId);
          }
        }
        return queryWrapper;
    }
    @Override
    public DubboPageResult<EsGoodsDO> getGuessYouLike(Long[] goodsIds,int pageNo, int pageSize) {
        try {
            List<EsGoodsDO> esGoodsDOList = new ArrayList<>();
            Page<EsGoods> page = new Page<>(pageNo, pageSize);
            // 判断该数组是否为空
            if (goodsIds.length == 0){
                // 通过商品销量进行筛选
                QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().
                        eq(EsGoods::getIsDel,0).
                        eq(EsGoods::getMarketEnable,1).
                        eq(EsGoods::getIsAuth,1).
                        eq(EsGoods::getIsGifts,2)
                        .orderByDesc(EsGoods::getBuyCount);
                IPage<EsGoods> iPage = this.page(page, queryWrapper);
                esGoodsDOList = iPage.getRecords().stream().map(esGoods -> {
                    EsGoodsDO esGoodsDO = new EsGoodsDO();
                    BeanUtil.copyProperties(esGoods, esGoodsDO);
                    return esGoodsDO;
                }).collect(Collectors.toList());
                return DubboPageResult.success(iPage.getTotal(),esGoodsDOList);
            }

            List<Long> list = Stream.of(goodsIds).collect(Collectors.toList());
            // 通过goodsId 集合 获取商品分类ID 集合
            List<EsGoods> esGoodsList = this.esGoodsMapper.selectBatchIds(list);

            List<Long> categoryIdList = esGoodsList.stream().map(EsGoods::getCategoryId).distinct().collect(Collectors.toList());
            // 通过去重后的分类ID 集合获取商品集合
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().
                    eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getIsGifts,2);

            if(CollectionUtils.isNotEmpty(categoryIdList)){
                queryWrapper.lambda().in(EsGoods::getCategoryId, categoryIdList);
            }
            IPage<EsGoods> iPage = this.page(page, queryWrapper);
            esGoodsDOList = iPage.getRecords().stream().map(esGoods -> {
                EsGoodsDO esGoodsDO = new EsGoodsDO();
                BeanUtil.copyProperties(esGoods, esGoodsDO);
                return esGoodsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(iPage.getTotal(),esGoodsDOList);

        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> getGuessLook(Long[] cateIds, String keyword) {
        try{
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().
                    eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getIsGifts,2);
                    if(StringUtils.isNotBlank(keyword)){
                        queryWrapper.lambda().like(EsGoods::getGoodsName,keyword);;
                    }
                    if(cateIds.length > 0){
                        queryWrapper.lambda(). in(EsGoods::getCategoryId, cateIds);
                    }
            queryWrapper.last("LIMIT 0,15");
            List<EsGoods> esGoodsList = this.list(queryWrapper);
            List<EsGoodsDO> goodsDOList = BeanUtil.copyList(esGoodsList,EsGoodsDO.class);
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsBuyerGoodsDO> getRecommendGoods() {
        try{
            List<Long> cateIds = esGoodsMapper.getRecommendGoods();
           List<EsBuyerGoodsDO>  buyerGoodsDOList = cateIds.stream().map(cate->{
                EsBuyerGoodsDO esBuyerGoodsDO = new EsBuyerGoodsDO();
                QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoods::getIsDel,0).
                        eq(EsGoods::getMarketEnable,1).
                        eq(EsGoods::getIsAuth,1).
                        eq(EsGoods::getIsGifts,2).eq(EsGoods::getCategoryId,cate).orderByDesc(EsGoods::getBuyCount);
                queryWrapper.last("LIMIT 3");
                List<EsGoods> goodsList = esGoodsMapper.selectList(queryWrapper);
                EsCategory category = esCategoryMapper.selectById(cate);
                List<EsGoodsDO> goodsDOList = BeanUtil.copyList(goodsList,EsGoodsDO.class);
                esBuyerGoodsDO.setCategoryId(cate);
                esBuyerGoodsDO.setGoodsList(goodsDOList);
               esBuyerGoodsDO.setCategoryName(category.getName());
                return esBuyerGoodsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(buyerGoodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<EsGoodsDO> getRecommendForYouGoods(String[] goodsNames) {
        try{
            StringBuilder sql = new StringBuilder();
            if(goodsNames.length !=0){
                String name = " t.goods_name like '%{0}%' ";
                for (int i = 0; i < goodsNames.length; i++) {
                    if(i!=0){
                        sql.append(" or ");
                    }
                    sql.append(name.replace("{0}",goodsNames[i]));
                }
            }
            if(StringUtils.isNotBlank(sql)){
                sql.insert(0,"(");
                sql.append(")");
            }
            List<EsGoodsDO> goodsList = this.esGoodsMapper.getRecommendForYouGoods(sql.toString());
            goodsList = goodsList.stream().distinct().collect(Collectors.toList());
            return DubboPageResult.success(goodsList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<EsGoodsDO> getCustomGoodsList(Long customId,int pageSize,int pageNum) {
        try{
            QueryWrapper<EsGoods> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getCustomId,customId).eq(EsGoods::getIsDel,0).
                    eq(EsGoods::getMarketEnable,1).
                    eq(EsGoods::getIsAuth,1).
                    eq(EsGoods::getIsGifts,2);
            Page<EsGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsGoods> iPage = this.page(page, queryWrapper);
            if(iPage.getRecords().size() <=0 ){
                return DubboPageResult.success(iPage.getTotal(),new ArrayList<>());
            }
            List<EsGoodsDO> goodsDOList = BeanUtil.copyList(iPage.getRecords(),EsGoodsDO.class);
            return DubboPageResult.success(iPage.getTotal(),goodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public DubboPageResult<EsGoodsDO> getGoodsList(Long templateId) {
        try{
            QueryWrapper<EsGoods> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getTemplateId,templateId);
            List<EsGoods> goodsList = this.list(queryWrapper);
            List<EsGoodsDO> goodsDOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(goodsList)){
                goodsDOList = BeanUtil.copyList(goodsList,EsGoodsDO.class);
            }
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional
    public DubboResult<EsGoodsDO> updateByTemplateId(Long newTemplateId, Long oldTemplateId) {
        try{
            this.esGoodsMapper.updateByTemplateId(newTemplateId,oldTemplateId);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品修改失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品修改失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsSalesRankingGoodsDO> getByCategoryId(Long categoryId,Long goodsId) {
        List<EsSalesRankingGoodsDO> list =new ArrayList<>();
        EsSalesRankingGoodsDO rankingGoodsDO=new EsSalesRankingGoodsDO();
        try{
            DubboResult<EsGoodsCO> result = this.getEsBuyerGoods(goodsId);
            if (result.isSuccess()){
                BeanUtil.copyProperties(result.getData(),rankingGoodsDO);
                list.add(rankingGoodsDO);
            }
            List<EsSalesRankingGoodsDO> doList = esGoodsMapper.getByCategoryId(categoryId);
            list.addAll(doList);
        return DubboPageResult.success(list);
        }catch (ArgumentException ae){
            logger.error("商品查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("商品查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


}
