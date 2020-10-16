package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCartDO;
import com.jjg.member.model.domain.EsCartNumDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsCartDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCart;
import com.xdl.jjg.mapper.EsCartMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCartService;
import com.xdl.jjg.web.service.IEsCommercelItemsService;
import com.xdl.jjg.web.service.IEsMemberService;
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
 * 购物车 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-02
 */
@Service
public class EsCartServiceImpl extends ServiceImpl<EsCartMapper, EsCart> implements IEsCartService {

    private static Logger logger = LoggerFactory.getLogger(EsCartServiceImpl.class);

    @Autowired
    private EsCartMapper cartMapper;

    @Autowired
    private IEsMemberService iEsMemberService;

    @Autowired
    private IEsCommercelItemsService iEsCommercelItemsService;

    /**
     * 插入购物车数据
     *
     * @param cartDTO 购物车DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<Long> insertCart(EsCartDTO cartDTO) {
        try {
            QueryWrapper<EsCart> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCart::getMemberId,cartDTO.getMemberId());
            EsCart esCart = this.cartMapper.selectOne(queryWrapper);
            if (esCart!=null){
                throw new ArgumentException(MemberErrorCode.EXIST_CART.getErrorCode(), MemberErrorCode.EXIST_CART.getErrorMsg());
            }
            EsCart cart = new EsCart();
            BeanUtil.copyProperties(cartDTO, cart);
            this.cartMapper.insert(cart);
            return DubboResult.success(cart.getId());
        } catch (ArgumentException ae){
            logger.error("购物车新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("购物车新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新购物车数据
     *
     * @param cartDTO 购物车DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/04 10:10:10
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCart(EsCartDTO cartDTO) {
        try {
            QueryWrapper<EsCart> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCart::getMemberId,cartDTO.getMemberId());
            EsCart esCart = this.cartMapper.selectOne(queryWrapper);
            if (esCart==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCart cart = new EsCart();
            BeanUtil.copyProperties(cartDTO, cart);
            QueryWrapper<EsCart> wrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCart::getId, cartDTO.getId());
            this.cartMapper.update(cart, wrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取购物车详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    public DubboResult<EsCartDO> getCart(Long id) {
        try {
            EsCart cart = this.cartMapper.selectById(id);
            EsCartDO cartDO = new EsCartDO();
            if (cart != null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(cart, cartDO);
            return DubboResult.success(cartDO);
        } catch (ArgumentException ae){
            logger.error("购物车查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询购物车列表
     *
     * @param cartDTO 购物车DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCartDO>
     */
    @Override
    public DubboPageResult<EsCartDO> getCartList(EsCartDTO cartDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCart> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCart> page = new Page<>(pageNum, pageSize);
            IPage<EsCart> iPage = this.page(page, queryWrapper);
            List<EsCartDO> cartDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                cartDOList = iPage.getRecords().stream().map(cart -> {
                    EsCartDO cartDO = new EsCartDO();
                    BeanUtil.copyProperties(cart, cartDO);
                    return cartDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(cartDOList);
        } catch (ArgumentException ae){
            logger.error("购物车分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除购物车数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCart(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCart> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCart::getId, id);
            this.cartMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("购物车删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据会员id查询 会员购物车项数量 及 购物车id
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:16:44
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    public DubboResult<EsCartNumDO> getByMemberId(Long memberId) {
        try{
            EsCartNumDO cartNumDo = this.cartMapper.getCartNumBYMemberId(memberId);
            return DubboResult.success(cartNumDo);
        } catch (ArgumentException ae){
            logger.error("根据会员id查询会员购物车项数量失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("根据会员id查询会员购物车项数量失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    /**
     * 根据会员id清空购物车
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:36:44
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult clear(Long memberId) {
        try {
            DubboResult<EsMemberDO> member = iEsMemberService.getMember(memberId);
            EsMemberDO mem = member.getData();
            if (mem==null){
                throw new ArgumentException(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
            }
            QueryWrapper<EsCart> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCart::getMemberId,memberId);
            EsCart esCart = this.cartMapper.selectOne(queryWrapper);
            iEsCommercelItemsService.deleteByCartId(esCart.getId());
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("清空购物车失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("清空购物车失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


}
