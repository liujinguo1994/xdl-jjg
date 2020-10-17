package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsTagGoodsDO;
import com.xdl.jjg.entity.EsTagGoods;
import com.xdl.jjg.entity.EsTags;
import com.xdl.jjg.mapper.EsGoodsMapper;
import com.xdl.jjg.mapper.EsTagGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsTagGoodsService;
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
public class EsTagGoodsServiceImpl extends ServiceImpl<EsTagGoodsMapper, EsTagGoods> implements IEsTagGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsTagGoodsServiceImpl.class);

    @Autowired
    private EsTagGoodsMapper tagGoodsMapper;
    @Autowired
    private EsTagsServiceImpl esTagsService;

    @Autowired
    private EsGoodsMapper esGoodsMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsTagGoodsDO> insertTagGoods(Integer[] tagIds, Long goodsId) {
        try{
            QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsTags::getId,tagIds);
            List<EsTags> esTagsList = this.esTagsService.list(queryWrapper);
            if(CollectionUtils.isNotEmpty(esTagsList)) {
                QueryWrapper<EsTagGoods> tagGoodsQueryWrapper = new QueryWrapper<>();
                tagGoodsQueryWrapper.lambda().eq(EsTagGoods::getGoodsId, goodsId);
                this.remove(tagGoodsQueryWrapper);
                for (int i = 0; i < tagIds.length; i++) {
                    EsTagGoods esTagGoods = new EsTagGoods();
                    esTagGoods.setGoodsId(goodsId);
                    esTagGoods.setTagId(tagIds[i].longValue());
                    this.save(esTagGoods);
                }
            }
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品标签关系表数据新增失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据新增失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    /**
     * 商品取消参与标签
     * @param tagId
     * @param goodsIds
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsTagGoodsDO> deleteTagGoods(Long tagId,Long[] goodsIds) {
        try{
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsTagGoods::getGoodsId,goodsIds).eq(EsTagGoods::getTagId,tagId);
            this.remove(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据更新失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsTagGoodsDO> insertTagGoods(Long tagId, Long[] goodsIds) {
        try{
            EsTags esTags = this.esTagsService.getById(tagId);
            if(esTags == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            if(goodsIds.length <=0){
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("商品ID不能为空 %s",goodsIds));
            }
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTagGoods::getTagId,tagId);
            this.remove(queryWrapper);
            for(int i=0; i<goodsIds.length;i++){
                EsTagGoods esTagGoods = new EsTagGoods();
                esTagGoods.setGoodsId(goodsIds[i].longValue());
                esTagGoods.setTagId(tagId);
                this.save(esTagGoods);
            }
            return DubboResult.success();
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据新增失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据新增失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsTagGoodsDO> deleteTagGoods(Long goodsId) {
        try{
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTagGoods::getGoodsId,goodsId);
             this.remove(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> queryTagGoods(Integer shopId, Integer num, String mark) {
        try{
           List<EsGoodsDO> goodsList = tagGoodsMapper.queryTagGoods(shopId,num,mark);
            return DubboPageResult.success(goodsList);
        }catch (ArgumentException  ae){
            logger.error("获取商品标签数据失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取商品标签数据失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> queryTagGoodsById( Long tagId, Long shopId ,int pageSize, int pageNum) {
        try{
            IPage<EsGoodsDO> page =  this.tagGoodsMapper.queryTagGoodsById(new Page(pageNum,pageSize),tagId,shopId);
            if(page.getTotal() <= 0){
                return DubboPageResult.success(page.getTotal(),new ArrayList<>());
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        }catch (ArgumentException  ae){
            logger.error("获取商品标签数据失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取商品标签数据失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsTagGoodsDO> getTagList(Long goodsId) {
        try{
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsTagGoods::getGoodsId,goodsId);
            List<EsTagGoods> tagGoodsList = this.list(queryWrapper);
            List<EsTagGoodsDO> goodsDOList = BeanUtil.copyList(tagGoodsList,EsTagGoodsDO.class);
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据获取失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据获取失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult deleteTagGoods(Integer[] goodsIds) {
        try{
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsTagGoods::getGoodsId,goodsIds);
            this.remove(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboPageResult<EsGoodsDO> getBuyerAdminGoodsTags(Long shopId, Long tagId, Long type) {
        try {
            List<EsGoodsDO> goodList = esGoodsMapper.getBuyerAdminGoods(shopId,tagId,type);
            return DubboPageResult.success(goodList);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

}

