package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsGoodsWordsDO;
import com.shopx.goods.api.model.domain.dto.EsGoodsWordsDTO;
import com.shopx.goods.api.service.IEsGoodsWordsService;
import com.shopx.goods.dao.entity.EsGoodsWords;
import com.shopx.goods.dao.mapper.EsGoodsWordsMapper;
import com.shopx.system.api.constant.ErrorCode;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义分词 服务实现类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:19
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsWordsService.class, timeout = 50000)
public class EsGoodsWordsServiceImpl extends ServiceImpl<EsGoodsWordsMapper, EsGoodsWords> implements IEsGoodsWordsService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsWordsServiceImpl.class);

    @Autowired
    private EsGoodsWordsMapper goodsWordsMapper;

    /**
     * 插入自定义分词数据
     *
     * @param goodsWordsDTO 自定义分词DTO
     * @auther:   * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsWordsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsWords(EsGoodsWordsDTO goodsWordsDTO) {
        try {
            QueryWrapper<EsGoodsWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsWords::getWords,goodsWordsDTO.getWords());
            List<EsGoodsWords> goodsWordsList = this.list(queryWrapper);
            //如果分词不存在则新增
            EsGoodsWords goodsWords = new EsGoodsWords();
            BeanUtil.copyProperties(goodsWordsDTO, goodsWords);
            if(CollectionUtils.isEmpty(goodsWordsList) ){
                goodsWords.setGoodsNum(1L);
                this.goodsWordsMapper.insert(goodsWords);
            }else{
               this.goodsWordsMapper.updateGoodsWords(goodsWordsDTO);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自定义分词新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("自定义分词新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult updateGoodsWords(EsGoodsWordsDTO goodsWordsDTO, Long id) {
        return null;
    }


    /**
     * 根据主键删除自定义分词数据
     *
     * @param words
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsWordsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsWords(List<String> words) {
        try {
            QueryWrapper<EsGoodsWords> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsGoodsWords::getWords, words);
            this.goodsWordsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("自定义分词删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自定义分词删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsWordsDO> deleteGoodsWords() {
        try {
            QueryWrapper<EsGoodsWords> queryWrapper = new QueryWrapper<>();
            this.goodsWordsMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自定义分词删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自定义分词删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    public DubboPageResult<EsGoodsWordsDO> getGoodsWords(String keyword) {
        try {
            QueryWrapper<EsGoodsWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().like(EsGoodsWords::getWords,keyword)
                    .or(obj1 -> obj1.like(EsGoodsWords::getQuanpin,keyword))
                    .or(obj2 -> obj2.like(EsGoodsWords::getSzm,keyword))
                    .orderByDesc(EsGoodsWords::getGoodsNum);
            queryWrapper.last("LIMIT 15");
            List<EsGoodsWords> esGoodsWords = this.goodsWordsMapper.selectList(queryWrapper);
            List<EsGoodsWordsDO> esGoodsWordsDOS = esGoodsWords.stream().map(esGoodsWords1 -> {
                EsGoodsWordsDO esGoodsWordsDO = new EsGoodsWordsDO();
                BeanUtils.copyProperties(esGoodsWords1,esGoodsWordsDO);
                return esGoodsWordsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esGoodsWordsDOS);
        }catch (Throwable th) {
            logger.error("商品分词失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
