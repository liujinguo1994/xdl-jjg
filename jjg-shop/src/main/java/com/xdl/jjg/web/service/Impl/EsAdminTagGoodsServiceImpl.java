package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsAdminTagGoods;
import com.xdl.jjg.entity.EsGoods;
import com.xdl.jjg.entity.EsTags;
import com.xdl.jjg.mapper.EsAdminTagGoodsMapper;
import com.xdl.jjg.mapper.EsGoodsMapper;
import com.xdl.jjg.model.domain.EsAdminTagGoodsDO;
import com.xdl.jjg.model.domain.EsGoodsDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAdminTagGoodsService;
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
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
@Service
public class EsAdminTagGoodsServiceImpl extends ServiceImpl<EsAdminTagGoodsMapper, EsAdminTagGoods> implements IEsAdminTagGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsAdminTagGoodsServiceImpl.class);
    @Autowired
    private EsTagsServiceImpl esTagsService;
    @Autowired
    private EsGoodsMapper esGoodsMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult insertAdminTagGoods(Long goodsId, Integer[] tagIds) {
        try {
            if(tagIds.length !=0){
                QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().in(EsTags::getId,tagIds);
                List<EsTags> esTagsList = this.esTagsService.list(queryWrapper);
                if(CollectionUtils.isEmpty(esTagsList)){
                    throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品标签不存在");
                }
                QueryWrapper<EsAdminTagGoods> goodsQueryWrapper = new QueryWrapper<>();
                goodsQueryWrapper.lambda().eq(EsAdminTagGoods::getGoodsId,goodsId);
                this.remove(goodsQueryWrapper);
                for(int i=0; i<tagIds.length;i++){
                    EsAdminTagGoods esTagGoods = new EsAdminTagGoods();
                    esTagGoods.setGoodsId(goodsId);
                    esTagGoods.setTagId(tagIds[i].longValue());
                    this.save(esTagGoods);
                }
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
    public DubboResult deleteAdminTagGoods(Long goodsId) {
        try{
            QueryWrapper<EsAdminTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdminTagGoods::getGoodsId,goodsId);
            this.remove(queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据删除失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据删除失败",th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult deleteAdminTagGoods(Integer[] goodsIds) {
        try{
            QueryWrapper<EsAdminTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsAdminTagGoods::getGoodsId,goodsIds);
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
    public DubboPageResult<EsAdminTagGoodsDO> getTagList(Long goodsId) {
        try{
            QueryWrapper<EsAdminTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsAdminTagGoods::getGoodsId,goodsId);
            List<EsAdminTagGoods> tagGoodsList = this.list(queryWrapper);
            List<EsAdminTagGoodsDO> goodsDOList = BeanUtil.copyList(tagGoodsList,EsAdminTagGoodsDO.class);
            return DubboPageResult.success(goodsDOList);
        }catch (ArgumentException  ae){
            logger.error("商品标签关系表数据删除失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品标签关系表数据删除失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }

    }

    @Override
    public DubboPageResult<EsAdminTagGoodsDO> getAdminGoodsTagsByShopId(Long shopId, Long tagId) {
        List<EsAdminTagGoodsDO> list = new ArrayList<>();
        try {
            QueryWrapper<EsGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoods::getShopId, shopId);
            List<EsGoods> goodList = esGoodsMapper.selectList(queryWrapper);
            List<Long> goodIdList = goodList.stream().map(EsGoods::getId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(goodIdList)){
                return DubboPageResult.success(list);
            }

            QueryWrapper<EsAdminTagGoods> queryWrapperTagGood = new QueryWrapper<>();
            queryWrapperTagGood.lambda().in(EsAdminTagGoods::getGoodsId, goodIdList);
            if(tagId != 0){
                queryWrapperTagGood.lambda().eq(EsAdminTagGoods::getTagId, tagId);
            }
            List<EsAdminTagGoods> tagGoodList = this.list(queryWrapperTagGood);
            list = BeanUtil.copyList(tagGoodList, EsAdminTagGoodsDO.class);
            return DubboPageResult.success(list);
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
