package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCartNumDO;
import com.jjg.member.model.domain.EsCommercelItemsDO;
import com.jjg.member.model.dto.EsCommercelItemsDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCart;
import com.xdl.jjg.entity.EsCommercelItems;
import com.xdl.jjg.mapper.EsCommercelItemsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCartService;
import com.xdl.jjg.web.service.IEsCommercelItemsService;
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
 * 购物车项 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsCommercelItemsServiceImpl extends ServiceImpl<EsCommercelItemsMapper, EsCommercelItems> implements IEsCommercelItemsService {

    private static Logger logger = LoggerFactory.getLogger(EsCommercelItemsServiceImpl.class);

    @Autowired
    private EsCommercelItemsMapper commercelItemsMapper;

    @Autowired
    private IEsCartService cartService;

    /**
     * 插入购物车项数据
     *
     * @param commercelItemsDTO 购物车项DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/07/02 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCommercelItems(EsCommercelItemsDTO commercelItemsDTO) {
        try {
            if (commercelItemsDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsCommercelItems commercelItems = new EsCommercelItems();
            BeanUtil.copyProperties(commercelItemsDTO, commercelItems);
            this.commercelItemsMapper.insert(commercelItems);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车项新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("购物车项新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新购物车项数据
     *
     * @param commercelItemsDTO 购物车项DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:00:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommercelItems(EsCommercelItemsDTO commercelItemsDTO) {
        try {
            EsCommercelItems esCommercelItems = this.commercelItemsMapper.selectById(commercelItemsDTO.getId());
            if (esCommercelItems==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCommercelItems commercelItems = new EsCommercelItems();
            BeanUtil.copyProperties(commercelItemsDTO, commercelItems);
            QueryWrapper<EsCommercelItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommercelItems::getId, commercelItemsDTO.getId());
            this.commercelItemsMapper.update(commercelItems, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车项更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车项更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取购物车项详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:37:06
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    public DubboResult<EsCommercelItemsDO> getCommercelItems(Long id) {
        try {
            EsCommercelItems commercelItems = this.commercelItemsMapper.selectById(id);
            EsCommercelItemsDO commercelItemsDO = new EsCommercelItemsDO();
            if (commercelItems == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(commercelItems, commercelItemsDO);
            return DubboResult.success(commercelItemsDO);
        } catch (ArgumentException ae){
            logger.error("购物车项查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车项查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询购物车项列表
     *
     * @param commercelItemsDTO 购物车项DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:22:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommercelItemsDO>
     */
    @Override
    public DubboPageResult<EsCommercelItemsDO> getCommercelItemsList(EsCommercelItemsDTO commercelItemsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommercelItems> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            queryWrapper.lambda().eq(EsCommercelItems::getCartId,commercelItemsDTO.getCartId());
            Page<EsCommercelItems> page = new Page<>(pageNum, pageSize);
            IPage<EsCommercelItems> iPage = this.page(page, queryWrapper);
            List<EsCommercelItemsDO> commercelItemsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commercelItemsDOList = iPage.getRecords().stream().map(commercelItems -> {
                    EsCommercelItemsDO commercelItemsDO = new EsCommercelItemsDO();
                    BeanUtil.copyProperties(commercelItems, commercelItemsDO);
                    return commercelItemsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),commercelItemsDOList);
        } catch (ArgumentException ae){
            logger.error("购物车项分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车项分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除购物车项数据
     *
     * @param ids 主键ids
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 11:10:24
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommercelItems(Integer [] ids) {
        try {
            QueryWrapper<EsCommercelItems> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsCommercelItems::getId, ids);
            this.commercelItemsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("购物车项删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车项删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据购车id删除数据
     *
     * @param cartId 购物车id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 10:10:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteByCartId(Long cartId) {
        try {
            Assert.notNull(cartId,"购物车id不能为空");
            QueryWrapper<EsCommercelItems> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCommercelItems::getCartId, cartId);
            this.commercelItemsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车项删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车项删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据skuId删除数据
     *  @param memberId memberId
     * @param skuIds skuId
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 10:01:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteByskuId(Long memberId,Integer [] skuIds) {
        try {
            QueryWrapper<EsCart> query=new QueryWrapper<>();
            query.lambda().eq(EsCart::getMemberId,memberId);
            DubboResult<EsCartNumDO> byMemberId = this.cartService.getByMemberId(memberId);
            EsCartNumDO cart=null;
            if (byMemberId.isSuccess()){
                cart = byMemberId.getData();

            }
            if (cart == null){
                return DubboResult.success();
            }
            QueryWrapper<EsCommercelItems> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCommercelItems::getCartId,cart.getCartId()).in(EsCommercelItems::getSkuId, skuIds);
            this.commercelItemsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("根据skuId删除购物车项失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("根据skuId删除购物车项失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsCommercelItemsDO> getCommercelItemsListByMemeberId(Long memberId) {
        try {
            List<EsCommercelItemsDO> commercelItemsDOList = this.commercelItemsMapper.getListByMemberId(memberId);
            return DubboPageResult.success(commercelItemsDOList);
        } catch (ArgumentException ae){
            logger.error("购物车项分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车项分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据id获取购物车项详情
     *
     * @param skuId  skuId
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/05 09:37:06
     * @return: com.shopx.common.model.result.DubboResult<EsCommercelItemsDO>
     */
    @Override
    public DubboResult<EsCommercelItemsDO> getItemsBySkuId(Long skuId,Long cartId) {
        QueryWrapper<EsCommercelItems> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(EsCommercelItems::getSkuId,skuId).eq(EsCommercelItems::getCartId,cartId);
        EsCommercelItems esCommercelItems = this.commercelItemsMapper.selectOne(queryWrapper);
        EsCommercelItemsDO esCommercelItemsDO=new EsCommercelItemsDO();
        if (esCommercelItems==null){
            return DubboResult.success();
        }
        BeanUtil.copyProperties(esCommercelItems,esCommercelItemsDO);
        return DubboResult.success(esCommercelItemsDO);
    }



}
