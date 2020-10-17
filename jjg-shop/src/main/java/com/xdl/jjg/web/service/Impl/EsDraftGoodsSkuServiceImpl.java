package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsDraftGoodsSkuDO;
import com.jjg.shop.model.dto.EsDraftGoodsDTO;
import com.jjg.shop.model.dto.EsDraftGoodsSkuDTO;
import com.xdl.jjg.entity.EsDraftGoodsSku;
import com.xdl.jjg.entity.EsGoodsSku;
import com.xdl.jjg.mapper.EsDraftGoodsSkuMapper;
import com.xdl.jjg.mapper.EsGoodsSkuMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsDraftGoodsSkuService;
import com.xdl.jjg.web.service.IEsGoodsGalleryService;
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
public class EsDraftGoodsSkuServiceImpl extends ServiceImpl<EsDraftGoodsSkuMapper, EsDraftGoodsSku> implements IEsDraftGoodsSkuService {

    private static Logger logger = LoggerFactory.getLogger(EsDraftGoodsSkuServiceImpl.class);

    @Autowired
    private EsDraftGoodsSkuMapper draftGoodsSkuMapper;
    @Autowired
    private EsGoodsSkuMapper esGoodsSkuMapper;
    @Autowired
    private IEsGoodsGalleryService esGoodsGalleryService;
    /**
     * 插入数据
     *
     * @param draftGoodsSkuDTOList
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsSkuDO> insertDraftGoodsSku(List<EsDraftGoodsSkuDTO> draftGoodsSkuDTOList, EsDraftGoodsDTO draftGoodsDTO) {
        try {
            if(CollectionUtils.isNotEmpty(draftGoodsSkuDTOList)) {
                draftGoodsSkuDTOList.stream().forEach(skuDTO -> {
                    QueryWrapper<EsDraftGoodsSku> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(EsDraftGoodsSku::getId, skuDTO.getId());
                    EsGoodsSku esGoodsSku = this.esGoodsSkuMapper.selectById(skuDTO.getId());
                    if(esGoodsSku == null ){
                        throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("商品档案SKU不存在,%s",skuDTO.getId()));
                    }
                    EsDraftGoodsSku draftGoodsSku = new EsDraftGoodsSku();
                    BeanUtil.copyProperties(esGoodsSku,draftGoodsSku);
                    draftGoodsSku.setCost(skuDTO.getCost());
                    Integer quantity =  skuDTO.getQuantity() == null ? 0 : skuDTO.getQuantity();
                    Integer xnQuantity =    skuDTO.getXnQuantity() == null ? 0 :   skuDTO.getXnQuantity();

                    Integer enableQuantity = (quantity + xnQuantity);
                    draftGoodsSku.setQuantity(skuDTO.getQuantity());
                    draftGoodsSku.setEnableQuantity(enableQuantity);
                    draftGoodsSku.setXnQuantity(skuDTO.getXnQuantity());
                    draftGoodsSku.setIsEnable(skuDTO.getIsEnable()==true ? 1 :2);
                    draftGoodsSku.setMoney(skuDTO.getMoney());
                    draftGoodsSku.setGoodsId(draftGoodsDTO.getId());
                    draftGoodsSku.setGoodsName(draftGoodsDTO.getGoodsName());
                    draftGoodsSku.setGoodsSn(draftGoodsDTO.getGoodsSn());
                    draftGoodsSku.setShopId(draftGoodsDTO.getShopId());
                    draftGoodsSku.setShopName(draftGoodsDTO.getShopName());
                    draftGoodsSku.setCategoryId(skuDTO.getCategoryId());
                    if( skuDTO.getIsSelf()== null){
                        skuDTO.setIsSelf(false);
                    }
                    draftGoodsSku.setIsSelf(skuDTO.getIsSelf() == true ? 1 : 2);
                    this.save(draftGoodsSku);
                    this.esGoodsGalleryService.insertGoodsGallery(skuDTO.getGoodsGallery(),draftGoodsSku.getId());
                });
            }

            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增草稿商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增草稿商品失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param draftGoodsSkuDTOList
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsSkuDO> updateDraftGoodsSku(List<EsDraftGoodsSkuDTO> draftGoodsSkuDTOList, EsDraftGoodsDTO draftGoodsDTO) {
        try {
            List<EsDraftGoodsSku> esDraftGoodsSkuList = draftGoodsSkuDTOList.stream().map(esGoodsSkuDTO -> {
                EsDraftGoodsSku draftGoodsSku = new EsDraftGoodsSku();
                draftGoodsSku.setCost(esGoodsSkuDTO.getCost());
                draftGoodsSku.setEnableQuantity(esGoodsSkuDTO.getEnableQuantity());
                draftGoodsSku.setIsEnable(esGoodsSkuDTO.getIsEnable() == true ?1  : 2);
                draftGoodsSku.setMoney(esGoodsSkuDTO.getMoney());
                draftGoodsSku.setId(esGoodsSkuDTO.getId());
                draftGoodsSku.setGoodsId(draftGoodsDTO.getId());
                draftGoodsSku.setGoodsName(draftGoodsDTO.getGoodsName());
                draftGoodsSku.setGoodsSn(draftGoodsDTO.getGoodsSn());
                draftGoodsSku.setShopId(draftGoodsDTO.getShopId());
                draftGoodsSku.setShopName(draftGoodsDTO.getShopName());
                this.esGoodsGalleryService.insertGoodsGallery(esGoodsSkuDTO.getGoodsGallery(),draftGoodsSku.getId());
                return draftGoodsSku;
            }).collect(Collectors.toList());
            this.updateBatchById(esDraftGoodsSkuList);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿商品SKU更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("草稿商品SKU更新失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    @Override
    public DubboResult<EsDraftGoodsSkuDO> getDraftGoodsSku(Long id) {
        try {
            QueryWrapper<EsDraftGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDraftGoodsSku::getId, id);
            EsDraftGoodsSku draftGoodsSku = this.draftGoodsSkuMapper.selectOne(queryWrapper);
            EsDraftGoodsSkuDO draftGoodsSkuDO = new EsDraftGoodsSkuDO();
            if (draftGoodsSku == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(draftGoodsSku, draftGoodsSkuDO);
            return DubboResult.success(draftGoodsSkuDO);
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsDraftGoodsSkuDO> getDraftGoodsSkuList(Long goodsId) {
        try {
            QueryWrapper<EsDraftGoodsSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDraftGoodsSku::getGoodsId, goodsId);
            List<EsDraftGoodsSku> draftGoodsSkuList = this.list(queryWrapper);
            List<EsDraftGoodsSkuDO> skuDOList =  BeanUtil.copyList(draftGoodsSkuList,EsDraftGoodsSkuDO.class);
            return DubboPageResult.success(skuDOList);
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据查询列表
     *
     * @param draftGoodsSkuDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsSkuDO>
     */
    @Override
    public DubboPageResult<EsDraftGoodsSkuDO> getDraftGoodsSkuList(EsDraftGoodsSkuDTO draftGoodsSkuDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDraftGoodsSku> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsDraftGoodsSku> page = new Page<>(pageNum, pageSize);
            IPage<EsDraftGoodsSku> iPage = this.page(page, queryWrapper);
            List<EsDraftGoodsSkuDO> draftGoodsSkuDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                draftGoodsSkuDOList = iPage.getRecords().stream().map(draftGoodsSku -> {
                    EsDraftGoodsSkuDO draftGoodsSkuDO = new EsDraftGoodsSkuDO();
                    BeanUtil.copyProperties(draftGoodsSku, draftGoodsSkuDO);
                    return draftGoodsSkuDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),draftGoodsSkuDOList);
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
     * @param goodsId 商品id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsSkuDO> deleteDraftGoodsSku(Long goodsId) {
        try {
            if (goodsId == null) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", goodsId));
            }
            QueryWrapper<EsDraftGoodsSku> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsDraftGoodsSku::getGoodsId, goodsId);
            this.draftGoodsSkuMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("草稿商品SKU删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("草稿商品SKU删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
