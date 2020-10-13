package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsTagGoods;
import com.xdl.jjg.entity.EsTags;
import com.xdl.jjg.mapper.EsTagsMapper;
import com.xdl.jjg.model.domain.EsTagsDO;
import com.xdl.jjg.model.dto.EsTagsDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsTagsService;
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
public class EsTagsServiceImpl extends ServiceImpl<EsTagsMapper, EsTags> implements IEsTagsService {

    private static Logger logger = LoggerFactory.getLogger(EsTagsServiceImpl.class);

    @Autowired
    private EsTagsMapper tagsMapper;

    @Autowired
    private EsTagGoodsServiceImpl esTagGoodsService;

    /**
     * 插入数据
     *
     * @param tagsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsTagsDO> insertTags(EsTagsDTO tagsDTO) {
        try {
            EsTags tags = new EsTags();
            BeanUtil.copyProperties(tagsDTO, tags);
            QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTags::getSort,tagsDTO.getSort()).eq(EsTags::getShopId,tagsDTO.getShopId());
            List<EsTags> tagsList = this.list(queryWrapper);
            if(CollectionUtils.isNotEmpty(tagsList)){
                throw new ArgumentException(GoodsErrorCode.TAG_SORT_EXIST.getErrorCode(), GoodsErrorCode.TAG_SORT_EXIST.getErrorMsg());
            }
            this.tagsMapper.insert(tags);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品标签添加失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("商品标签添加失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param tagsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsTagsDO> updateTags(EsTagsDTO tagsDTO,Long id) {
        try {
            EsTags esTags = this.getById(id);
            if(esTags == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTags::getSort,tagsDTO.getSort()).eq(EsTags::getShopId,tagsDTO.getShopId());
            List<EsTags> tagsList = this.list(queryWrapper);
            if(CollectionUtils.isNotEmpty(tagsList)){
                throw new ArgumentException(GoodsErrorCode.TAG_SORT_EXIST.getErrorCode(), GoodsErrorCode.TAG_SORT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(tagsDTO, esTags);
            this.updateById(esTags);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品标签修改失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品标签修改失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    @Override
    public DubboResult<EsTagsDO> getTags(Long id) {
        try {
            EsTags tags = this.getById(id);
            EsTagsDO tagsDO = new EsTagsDO();
            if (tags == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(tags, tagsDO);
            return DubboResult.success(tagsDO);
        } catch (ArgumentException ae){
            logger.error("获取标签详情失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取标签详情失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsTagsDO>
     */
    @Override
    public DubboPageResult<EsTagsDO> getTagsList(Long shopId) {
        QueryWrapper<EsTags> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().orderByAsc(EsTags::getSort);
            //queryWrapper.lambda().eq(EsTags::getShopId,shopId);
            List<EsTags> esTagsList = this.list(queryWrapper);
            List<EsTagsDO> tagsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esTagsList)) {
                tagsDOList = esTagsList.stream().map(tags -> {
                    EsTagsDO tagsDO = new EsTagsDO();
                    BeanUtil.copyProperties(tags, tagsDO);
                    return tagsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(tagsDOList);
        } catch (ArgumentException ae){
            logger.error("标签信息查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("标签信息查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键ids
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsTagsDO> deleteTags(Long id) {
        try {
            QueryWrapper<EsTagGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsTagGoods::getTagId,id);
            this.removeById(id);
            this.esTagGoodsService.remove(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除标签失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除标签失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
