package com.xdl.jjg.web.service.Impl;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsCategory;
import com.xdl.jjg.entity.EsDraftGoods;
import com.xdl.jjg.entity.EsGoodsArch;
import com.xdl.jjg.mapper.EsCategoryMapper;
import com.xdl.jjg.mapper.EsDraftGoodsMapper;
import com.xdl.jjg.mapper.EsGoodsArchMapper;
import com.xdl.jjg.model.domain.*;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.*;
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
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsDraftGoodsServiceImpl extends ServiceImpl<EsDraftGoodsMapper, EsDraftGoods> implements IEsDraftGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsDraftGoodsServiceImpl.class);

    @Autowired
    private EsDraftGoodsMapper draftGoodsMapper;
    //商品档案
    @Autowired
    private EsGoodsArchMapper esGoodsArchMapper;
    //商品标签
    @Autowired
    private IEsTagGoodsService esTagGoodsService;
    //草稿商品SKU
    @Autowired
    private IEsDraftGoodsSkuService esDraftGoodsSkuService;
    //商品分类
    @Autowired
    private EsCategoryMapper esCategoryMapper;
    //商品
    @Autowired
    private IEsGoodsService esGoodsService;
    @Autowired
    private IEsGoodsGalleryService esGoodsGalleryService;
    @Autowired
    private IEsDraftGoodsParamsService esDraftGoodsParamsService;
    /**
     * 插入数据
     *
     * @param draftGoodsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsDO> insertDraftGoods(EsDraftGoodsDTO draftGoodsDTO) {
        try {
            Long goodsId = draftGoodsDTO.getId() == null ? -1 : draftGoodsDTO.getId();
            EsGoodsArch esGoodsArch = esGoodsArchMapper.selectById(goodsId);
            if(esGoodsArch == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品档案不存在");
            }
            EsDraftGoods   draftGoods = new EsDraftGoods();
            BeanUtil.copyProperties(draftGoodsDTO, draftGoods);
            draftGoods.setIsGifts(esGoodsArch.getIsGifts());
            if(CollectionUtils.isNotEmpty(draftGoodsDTO.getSkuList())){
                Double goodsMoney = draftGoodsDTO.getSkuList().stream().mapToDouble(EsDraftGoodsSkuDTO::getMoney).min().getAsDouble();
                draftGoods.setMoney(goodsMoney);
            }
            this.deleteDraftGoods(draftGoodsDTO.getId());
            //保存草稿商品
            this.draftGoodsMapper.insert(draftGoods);
            //标签绑定商品（商品标签关系表数据新增）
            if(draftGoodsDTO.getTagsIds().length>0){
                this.esTagGoodsService.insertTagGoods(draftGoodsDTO.getTagsIds(),draftGoodsDTO.getId());
            }
            //新增档案商品SKU信息
            this.esDraftGoodsSkuService.insertDraftGoodsSku(draftGoodsDTO.getSkuList(),draftGoodsDTO);
            //插入商品参数
            this.esDraftGoodsParamsService.insertDraftGoodsParams(draftGoodsDTO.getParamsList(),goodsId);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("草稿商品新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param draftGoodsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsDO> updateDraftGoods(EsDraftGoodsDTO draftGoodsDTO,Long id) {
        try {
            if(draftGoodsDTO == null){
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(),GoodsErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //判断该商品是否存在 是否有权限操作
            EsDraftGoods goods =  this.getById(id);
            //预留判断 卖家Id
            if(goods == null || (goods.getShopId()!= draftGoodsDTO.getShopId())){
                throw new ArgumentException(GoodsErrorCode.NO_AUTH.getErrorCode(),GoodsErrorCode.NO_AUTH.getErrorMsg());
            }
            BeanUtil.copyProperties(draftGoodsDTO,goods);
            this.updateById(goods);
            //标签绑定商品（商品标签关系表数据新增）
            if(draftGoodsDTO.getTagsIds().length>0){
                this.esTagGoodsService.insertTagGoods(draftGoodsDTO.getTagsIds(),draftGoodsDTO.getId());
            }
            //更新SKU信息(商品价格、成本价格、库存、是否启用)
            this.esDraftGoodsSkuService.updateDraftGoodsSku(draftGoodsDTO.getSkuList(),draftGoodsDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿箱商品更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("草稿箱商品更新失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    @Override
    public DubboResult<EsDraftGoodsDO> getDraftGoods(Long id) {
        try {

            EsDraftGoods draftGoods = this.getById(id);
            EsDraftGoodsDO draftGoodsDO = new EsDraftGoodsDO();
            if (draftGoods == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(draftGoods, draftGoodsDO);
            return DubboResult.success(draftGoodsDO);
        } catch (ArgumentException ae){
            logger.error("获取草稿商品信息失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取草稿商品信息失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult<EsSellerDraftGoodsDO> getSellerDraftGoods(Long id) {
        try {

            EsDraftGoods draftGoods = this.getById(id);
            EsSellerDraftGoodsDO draftGoodsDO = new EsSellerDraftGoodsDO();
            if (draftGoods == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(draftGoods, draftGoodsDO);
            DubboPageResult<EsDraftGoodsSkuDO> result =  esDraftGoodsSkuService.getDraftGoodsSkuList(id);
            if(result.isSuccess()){
                List<EsDraftGoodsSkuDO> skuDOList = result.getData().getList();
                List<EsSellerDraftGoodsSkuDO> draftGoodsSkuDOList =  skuDOList.stream().map(draftSku->{
                    EsSellerDraftGoodsSkuDO draftGoodsSkuDO = new EsSellerDraftGoodsSkuDO();
                    BeanUtil.copyProperties(draftSku,draftGoodsSkuDO);
                    if(draftSku.getIsEnable()!=null){
                        draftGoodsSkuDO.setIsEnable(draftSku.getIsEnable() == 1 ? true : false);
                    }
                    if(draftSku.getIsSelf()!=null){
                       draftGoodsSkuDO.setIsSelf(draftSku.getIsSelf() == 1 ? true : false);
                    }
                    DubboPageResult<EsGoodsGalleryDO> galleryList =   esGoodsGalleryService.getGoodsGalleryBySkuId(draftSku.getId());
                    if(galleryList.isSuccess()){
                        if(CollectionUtils.isNotEmpty(galleryList.getData().getList())){
                            draftGoodsSkuDO.setAlbumNo(galleryList.getData().getList().get(0).getAlbumNo());
                            draftGoodsSkuDO.setGoodsGallery(galleryList.getData().getList());
                        }
                    }
                    return draftGoodsSkuDO;
                }).collect(Collectors.toList());
                draftGoodsDO.setSkuList(draftGoodsSkuDOList);
            }
            EsCategory category = esCategoryMapper.selectById(draftGoodsDO.getCategoryId());
            draftGoodsDO.setCategoryName(category.getName());
            DubboPageResult<EsTagGoodsDO> tagResult =  esTagGoodsService.getTagList(id);
            if(tagResult.isSuccess()){
                List<Long> tagsIds = tagResult.getData().getList().stream().map(EsTagGoodsDO::getTagId).collect(Collectors.toList());
                draftGoodsDO.setTagsIds(tagsIds.stream().toArray(Long[]::new));
            }
            return DubboResult.success(draftGoodsDO);
        } catch (ArgumentException ae){
            logger.error("获取草稿商品信息失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取草稿商品信息失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据查询列表
     *
     * @param draftGoodsDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsDO>
     */
    @Override
    public DubboPageResult<EsDraftGoodsDO> getDraftGoodsList(EsDraftGoodsQueryDTO draftGoodsDTO, Long shopId, int pageSize, int pageNum) {

        try {
            //按照商品分类进行查找
            Long[] cateIds = null;
            if(!StringUtils.isBlank(draftGoodsDTO.getCategoryPath())){
                QueryWrapper<EsCategory> categoryQueryWrapper = new QueryWrapper<>();
                categoryQueryWrapper.lambda().like(EsCategory::getCategoryPath,draftGoodsDTO.getCategoryPath());
                List<EsCategory> esCategoryList = this.esCategoryMapper.selectList(categoryQueryWrapper);
                if(CollectionUtils.isEmpty(esCategoryList)){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类不存在");
                }
                //获取分类ID及子分类ID
                List<Long> sku_ids = esCategoryList.stream().map(EsCategory::getId).distinct().collect(Collectors.toList());
                cateIds =  sku_ids.stream().toArray(Long[]::new);
            }
            IPage<EsDraftGoodsDO> page =  this.draftGoodsMapper.getDraftEsGoodsPageList(new Page(pageNum,pageSize),draftGoodsDTO.getGoodsName(),
                    shopId,cateIds,draftGoodsDTO.getIsVirtual());
            if(page.getTotal() <= 0){
                return DubboPageResult.success(page.getTotal(),new ArrayList<>());
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("草稿商品分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("草稿商品分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsDO> deleteDraftGoods(Long id) {
        try {
            if (id == null) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            //删除草稿商品
            this.draftGoodsMapper.deleteById(id);
            //删除草稿商品SKU
            this.esDraftGoodsSkuService.deleteDraftGoodsSku(id);
            //删除标签关系表数据
            this.esTagGoodsService.deleteTagGoods(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除草稿箱商品失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除草稿箱商品失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 草稿商品发布（信息同步到商品主表、档案SKU信息更新、草稿商品信息删除）
     * @param draftGoodsDTO
     * @return
     */
    @Override
    public DubboResult<EsDraftGoodsDO> syncEsGoods(EsDraftGoodsDTO draftGoodsDTO) {
        try {
            EsDraftGoods esDraftGoods = this.getById(draftGoodsDTO.getId());
            if(esDraftGoods == null){
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误商品草稿信息为null",draftGoodsDTO));
            }
            if(CollectionUtils.isEmpty(draftGoodsDTO.getSkuList())){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品SKU信息数据不能为空");
            }
            EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
            BeanUtil.copyProperties(draftGoodsDTO,esGoodsDTO);
            //将草稿SKU信息同步到档案SKU表
            List<EsGoodsSkuDTO> goodsSkuDTOList = BeanUtil.copyList(draftGoodsDTO.getSkuList(),EsGoodsSkuDTO.class);
            List<EsGoodsParamsDTO> goodsParamsDTOList =  BeanUtil.copyList(draftGoodsDTO.getParamsList(),EsGoodsParamsDTO.class);
            esGoodsDTO.setSkuList(goodsSkuDTOList);
            esGoodsDTO.setParamsList(goodsParamsDTOList);
            this.deleteDraftGoods(draftGoodsDTO.getId());
            this.esGoodsService.insertEsGoods(esGoodsDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿箱商品发布失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("草稿箱商品发布失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
