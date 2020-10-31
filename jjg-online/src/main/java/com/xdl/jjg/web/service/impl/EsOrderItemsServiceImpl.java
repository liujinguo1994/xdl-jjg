package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsAutoCommentConfigDO;
import com.jjg.shop.model.domain.EsCategoryDO;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.*;
import com.jjg.trade.model.enums.*;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsDeliveryInfo;
import com.xdl.jjg.entity.EsOrder;
import com.xdl.jjg.entity.EsOrderItems;
import com.xdl.jjg.entity.EsServiceOrderItems;
import com.xdl.jjg.mapper.EsDeliveryInfoMapper;
import com.xdl.jjg.mapper.EsOrderItemsMapper;
import com.xdl.jjg.mapper.EsOrderMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsOrderItemsService;
import com.xdl.jjg.web.service.IEsOrderMetaService;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import com.xdl.jjg.web.service.feign.member.AutoCommentConfigService;
import com.xdl.jjg.web.service.feign.shop.CategoryService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单商品明细表-es_order_items 服务实现类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsOrderItemsService.class,timeout = 5000)
public class EsOrderItemsServiceImpl extends ServiceImpl<EsOrderItemsMapper, EsOrderItems> implements IEsOrderItemsService {
    private static Logger logger = LoggerFactory.getLogger(EsOrderItemsServiceImpl.class);


    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private CategoryService categoryService;

    @Autowired
    private AutoCommentConfigService iEsAutoCommentConfigService;

    @Autowired
    private IEsPromotionGoodsService promotionGoodsService;

    @Autowired
    private IEsOrderService iEsOrderService;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private IEsOrderMetaService iEsOrderMetaService;

    @Autowired
    private EsOrderMapper esOrderMapper;

    @Autowired
    private EsDeliveryInfoMapper esDeliveryInfoMapper;

    // 订单商品项ID
    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(7,7);

    /**
     * 根据id获取订单商品明细表-es_order_items详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsOrderItemsDO>
     */
    @Override
    public DubboResult<EsOrderItemsDO> getEsOrderItemsInfo(Long id) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getId,id);
        EsOrderItems esOrderItems = this.getOne(queryWrapper);
        //出仓对象转DO
        EsOrderItemsDO esOrderItemsDO = new EsOrderItemsDO();
        if(esOrderItems != null){
            BeanUtils.copyProperties(esOrderItems,esOrderItemsDO);
        }else {
            return DubboResult.fail(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        return DubboResult.success(esOrderItemsDO);
    }
    /**
     * 查询订单商品明细表分页
     * @param esOrderItemsDTO
     * @return
     */
    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsList(EsOrderItemsDTO esOrderItemsDTO, int pageSize, int pageNum) {
        if (esOrderItemsDTO == null){
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();

        try {
            queryWrapper.orderByDesc("id");
            Page<EsOrderItems> page = new Page<>(pageNum, pageSize);
            IPage<EsOrderItems> orderItemsList = this.page(page, queryWrapper);
            List<EsOrderItemsDO> orderItemsDoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(orderItemsList.getRecords())){
                orderItemsDoList = orderItemsList.getRecords().stream().map(
                        orderItems -> {
                            EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                            BeanUtils.copyProperties(orderItems,orderItemsDO);
                            return orderItemsDO;
                        }
                ).collect(Collectors.toList());
            }
            return DubboPageResult.success(orderItemsDoList);
        } catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    /**
     *保存订单商品明细表
     * @param esOrderItemsDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> insertOrderItems(EsOrderItemsDTO esOrderItemsDTO) {
        try {

            //todo 判断分类id 是否存在
            DubboResult<EsCategoryDO> category = categoryService.getCategory(esOrderItemsDTO.getCategoryId());
            if (category == null){
                throw new ArgumentException(category.getCode(),category.getMsg());
            }
            //todo 判断活动id 是否存在 如果存在拉取活动信息


            if (esOrderItemsDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(),"参数错误");
            }
            EsOrderItems esOrderItems = new EsOrderItems();
            BeanUtils.copyProperties(esOrderItemsDTO,esOrderItems);
            esOrderItems.setMoney(esOrderItemsDTO.getMoney());
            this.save(esOrderItems);
            return DubboResult.success();
        } catch (ArgumentException e) {
            logger.error("保存订单商品明细失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SAVE_ORDER_ERROR.getErrorCode(), TradeErrorCode.SAVE_ORDER_ERROR.getErrorMsg());
        } catch (Throwable th) {
            logger.error("保存订单商品明细失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 修改订单商品明细详情
     * @param esOrderItemsDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> updateOrderItemsMessage(EsOrderItemsDTO esOrderItemsDTO) {
        try {
            if (esOrderItemsDTO == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOrderItems esOrderItems = new EsOrderItems();

            BeanUtils.copyProperties(esOrderItemsDTO,esOrderItems);

            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getId,esOrderItemsDTO.getId());

            //判断该数据是否存在
            EsOrderItems orderItems = this.baseMapper.selectOne(queryWrapper);
            if (orderItems == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            this.baseMapper.update(esOrderItems, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单商品详情失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (BeansException be) {
            logger.error("修改订单商品详情失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> updateOrderItemsServiceStatusByGoodsIdAndSkuId(String orderSn, Long[] goodsIds,Long[] skuIds,String serviceStatus) {
        try {
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsOrderItems::getGoodsId,goodsIds);
            queryWrapper.lambda().in(EsOrderItems::getSkuId,skuIds).eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> esOrderItemsList = this.list(queryWrapper);
            if (CollectionUtils.isEmpty(esOrderItemsList)){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            esOrderItemsList.forEach(orderItems -> {
                orderItems.setState(serviceStatus);
                if (ServiceStatusEnum.NOT_APPLY.value().equals(serviceStatus)){
                    orderItems.setHasShip(0);
                }
            });
            this.updateBatchById(esOrderItemsList);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单商品售后状态失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (BeansException be) {
            logger.error("修改订单商品售后状态失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> updateOrderItemsServiceStatus(String orderSn, Long[] goodsIds,String serviceStatus) {
        try {
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsOrderItems::getGoodsId,goodsIds).eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> esOrderItemsList = this.list(queryWrapper);
            if (CollectionUtils.isEmpty(esOrderItemsList)){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(),TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            esOrderItemsList.forEach(orderItems -> {
                orderItems.setState(serviceStatus);
            });
            this.updateBatchById(esOrderItemsList);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改订单商品售后状态失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (BeansException be) {
            logger.error("修改订单商品售后状态失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 删除订单商品明细表数据
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> deleteOrderItemsMessage(Long id) {
        try {
            if (id == null){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            this.baseMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除订单商品明细失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        } catch (Throwable be) {
            logger.error("删除订单商品明细失败",be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据订单明细表OrderSn 获取订单商品明细表信息
     * @param orderSn
     * @return
     */
    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnNoZP(String orderSn) {
        try{
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> orderItemsList = this.list(queryWrapper);

            List<EsOrderItemsDO> esOrderItemsDOList = orderItemsList.stream().map(orderItems -> {
                EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                BeanUtils.copyProperties(orderItems,orderItemsDO);
                return orderItemsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esOrderItemsDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }


    /**
     * 根据订单明细表OrderSn 获取订单商品明细表信息
     * @param orderSn
     * @return
     */
    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSn(String orderSn) {
        try{
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> orderItemsList = this.list(queryWrapper);
            List<EsOrderItems> collect = orderItemsList.stream()
                    .filter(orderItems -> PromotionTypeEnum.FULL_DISCOUNT.name().equals(orderItems.getPromotionType())).collect(Collectors.toList());
            // 如果存在赠品则获取赠品信息
            List<EsOrderItemsDO> zpItemsDOList = new ArrayList<>();
            if (collect.size()>0){
                DubboResult<EsOrderMetaDO> orderMeta = iEsOrderMetaService.getOrderMetaByOrderSnAndMetaKey(orderSn, OrderMetaKeyEnum.GIFT.name());
                // 赠品list
                if (orderMeta.isSuccess()){
                    String metaValue = orderMeta.getData().getMetaValue();
                    List<EsFullDiscountGiftDO> esFullDiscountGiftDOS = JSONArray.parseArray(metaValue, EsFullDiscountGiftDO.class);
                    esFullDiscountGiftDOS.forEach(esFullDiscountGiftDO -> {
                        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                        orderItemsDO.setId(esFullDiscountGiftDO.getId());
                        orderItemsDO.setName(esFullDiscountGiftDO.getGiftName());
                        orderItemsDO.setImage(esFullDiscountGiftDO.getGiftImg());
                        orderItemsDO.setMoney(esFullDiscountGiftDO.getGiftMoney());
                        orderItemsDO.setGoodsId(esFullDiscountGiftDO.getGoodsId());
                        orderItemsDO.setSkuId(esFullDiscountGiftDO.getSkuiId());
                        orderItemsDO.setNum(1);
                        orderItemsDO.setIsGift(1);
                        zpItemsDOList.add(orderItemsDO);
                    });
                }
            }

            List<EsOrderItemsDO> esOrderItemsDOList = orderItemsList.stream().map(orderItems -> {
                EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                BeanUtils.copyProperties(orderItems,orderItemsDO);
                return orderItemsDO;
            }).collect(Collectors.toList());
            esOrderItemsDOList.addAll(zpItemsDOList);
            return DubboPageResult.success(esOrderItemsDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    public DubboPageResult<EsSellerOrderItemsDO> getSellerEsOrderItemsByOrderSn(String orderSn) {
        try{
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> orderItemsList = this.list(queryWrapper);

            List<EsSellerOrderItemsDO> esOrderItemsDOList = orderItemsList.stream().map(orderItems -> {
                EsSellerOrderItemsDO orderItemsDO = new EsSellerOrderItemsDO();
                BeanUtils.copyProperties(orderItems,orderItemsDO);
                orderItemsDO.setGoodsImage(orderItems.getImage());
                orderItemsDO.setSubtotal(orderItems.getMoney()*orderItems.getNum());
                orderItemsDO.setCartPrice(orderItems.getMoney());
                return orderItemsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esOrderItemsDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsByTradeSn(String tradeSn) {
        try{
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getTradeSn,tradeSn);
            List<EsOrderItems> orderItemsList = this.list(queryWrapper);
            List<EsOrderItems> collect = orderItemsList.stream()
                    .filter(orderItems -> PromotionTypeEnum.FULL_DISCOUNT.name().equals(orderItems.getPromotionType())).collect(Collectors.toList());
            List<EsOrderItemsDO> zpItemsDOList = new ArrayList<>();
            if (collect.size() > 0){
                List<String> orderSnList = orderItemsList.stream().map(EsOrderItems::getOrderSn).collect(Collectors.toList());
                // 赠品list
                orderSnList.forEach(s -> {
                    // 如果存在赠品则获取赠品信息
                    DubboResult<EsOrderMetaDO> orderMeta = iEsOrderMetaService.getOrderMetaByOrderSnAndMetaKey(s, OrderMetaKeyEnum.GIFT.name());
                    if (orderMeta.isSuccess()){
                        String metaValue = orderMeta.getData().getMetaValue();
                        List<EsFullDiscountGiftDO> esFullDiscountGiftDOS = JSONArray.parseArray(metaValue, EsFullDiscountGiftDO.class);
                        esFullDiscountGiftDOS.forEach(esFullDiscountGiftDO -> {
                            EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                            orderItemsDO.setId(esFullDiscountGiftDO.getId());
                            orderItemsDO.setName(esFullDiscountGiftDO.getGiftName());
                            orderItemsDO.setImage(esFullDiscountGiftDO.getGiftImg());
                            orderItemsDO.setMoney(esFullDiscountGiftDO.getGiftMoney());
                            orderItemsDO.setGoodsId(esFullDiscountGiftDO.getGoodsId());
                            orderItemsDO.setNum(1);
                            orderItemsDO.setIsGift(1);
                            zpItemsDOList.add(orderItemsDO);
                        });
                    }
                });
            }
            List<EsOrderItemsDO> esOrderItemsDOList = orderItemsList.stream().map(orderItems -> {
                EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
                BeanUtils.copyProperties(orderItems,orderItemsDO);
                return orderItemsDO;
            }).collect(Collectors.toList());
            esOrderItemsDOList.addAll(zpItemsDOList);
            return DubboPageResult.success(esOrderItemsDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult<EsOrderItemsDO>  getEsOrderItemsByOrderSnAndGoodsId(String orderSn, Long goodsId) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).eq(EsOrderItems::getGoodsId,goodsId).ne(EsOrderItems::getPromotionId,"");
        EsOrderItems esOrderItems = this.getOne(queryWrapper);
        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
        if (esOrderItems != null){
            BeanUtils.copyProperties(esOrderItems,orderItemsDO);
        }
        return DubboResult.success(orderItemsDO);
    }

    @Override
    public DubboResult<EsOrderItemsDO>  getEsAfterSaleOrderItemsByOrderSnAndSkuId(String orderSn, Long skuId) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).eq(EsOrderItems::getSkuId,skuId);
        EsOrderItems esOrderItems = this.getOne(queryWrapper);
        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
        if (esOrderItems != null){
            BeanUtils.copyProperties(esOrderItems,orderItemsDO);
        }
        return DubboResult.success(orderItemsDO);
    }

    @Override
    public DubboPageResult<EsOrderItemsDO> getEsRefundOrderItemsByOrderSnAnd(String orderSn) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
        queryWrapper.lambda().eq(EsOrderItems::getState, ServiceStatusEnum.NOT_APPLY);
        List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);
        esOrderItems.stream().forEach(orderItems -> {
            EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
            BeanUtils.copyProperties(orderItems,orderItemsDO);
        });
        return DubboPageResult.success(esOrderItems);
    }

    @Override
    public DubboResult<EsOrderItemsDO>  getBuyerEsOrderItemsByOrderSnAndGoodsId(String orderSn, Long goodsId) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).eq(EsOrderItems::getGoodsId,goodsId);
        EsOrderItems esOrderItems = this.getOne(queryWrapper);
        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
        if (esOrderItems != null){
            BeanUtils.copyProperties(esOrderItems,orderItemsDO);
        }
        return DubboResult.success(orderItemsDO);
    }

    /**
     * Liujg
     * 1.查询评价信息接口
     * @param memberId
     * @param singleCommentStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public DubboPageResult<EsBuyerOrderDO> getBuyerOrderCommentGoodsList(Long memberId, String reqOrderSn, String singleCommentStatus, int pageSize, int pageNum) {
        try {
            EsOrderItemsDTO esOrderItemsDTO = new EsOrderItemsDTO();
            esOrderItemsDTO.setMemberId(memberId);
            esOrderItemsDTO.setSingleCommentStatus(singleCommentStatus);
            if(StringUtils.isNotEmpty(reqOrderSn)){
                esOrderItemsDTO.setOrderSn(reqOrderSn);
            }
            IPage<EsBuyerOrderDO> buyerOrderCommentList = this.esOrderItemsMapper.getBuyerOrderCommentList(new Page(pageNum, pageSize), esOrderItemsDTO);
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            List<EsBuyerOrderDO> esBuyerOrderDOList = buyerOrderCommentList.getRecords();
            esBuyerOrderDOList.stream().map(esBuyerOrderDO -> {
                String orderSn = esBuyerOrderDO.getOrderSn();
                // 通过订单编号查询订单明细中已经评论过的 或者未评论的商品
                QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
                queryWrapper.lambda().eq(EsOrderItems::getState,ServiceStatusEnum.NOT_APPLY.value());
                queryWrapper.lambda().eq(EsOrderItems::getSingleCommentStatus,singleCommentStatus);
                List<EsOrderItems> esOrderItems = esOrderItemsMapper.selectList(queryWrapper);

                List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOList = new ArrayList<>();
                esBuyerOrderItemsDOList = esOrderItems.stream().map(orderItems -> {
                    EsBuyerOrderItemsDO esBuyerOrderItemsDO = new EsBuyerOrderItemsDO();
                    BeanUtils.copyProperties(orderItems,esBuyerOrderItemsDO);
                    esBuyerOrderItemsDO.setCreateTime(esBuyerOrderDO.getCreateTime());
                    esBuyerOrderItemsDO.setShopId(esBuyerOrderDO.getShopId());
                    return esBuyerOrderItemsDO;
                }).collect(Collectors.toList());

                esBuyerOrderItemsDOList.forEach(esBuyerOrderItemsDO -> {
                    String specJson = esBuyerOrderItemsDO.getSpecJson();
                    if (StringUtils.isNotEmpty(specJson)) {
                        List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(specJson, EsSpecValuesDO.class);
                        esBuyerOrderItemsDO.setSpecList(specValuesDOList);
                    }
                });
                /*if(CollectionUtils.isEmpty(esBuyerOrderItemsDOList)){
                    flag.set(false);
                }*/

                esBuyerOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOList);
                return esBuyerOrderDO;
            }).collect(Collectors.toList());
            //if (flag.get()){
                return DubboPageResult.success(buyerOrderCommentList.getTotal(), esBuyerOrderDOList);
           // }
           // return DubboPageResult.success(0l,new ArrayList<EsBuyerOrderDO>());
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    @Override
    public DubboResult<EsBuyerOrderDO> getBuyerCommentGoodsList(Long memberId,String reqOrderSn, String singleCommentStatus) {
        try {
            EsOrderItemsDTO esOrderItemsDTO = new EsOrderItemsDTO();
            esOrderItemsDTO.setMemberId(memberId);
            esOrderItemsDTO.setSingleCommentStatus(singleCommentStatus);
            if(StringUtils.isNotEmpty(reqOrderSn)){
                esOrderItemsDTO.setOrderSn(reqOrderSn);
            }
            EsBuyerOrderDO buyerOrderDO = this.esOrderItemsMapper.getBuyerCommentList(esOrderItemsDTO);

            String orderSn = buyerOrderDO.getOrderSn();
            // 通过订单编号查询订单明细中已经评论过的商品
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            queryWrapper.lambda().eq(EsOrderItems::getState,ServiceStatusEnum.NOT_APPLY.value());
            queryWrapper.lambda().eq(EsOrderItems::getSingleCommentStatus,singleCommentStatus);
            List<EsOrderItems> esOrderItems = esOrderItemsMapper.selectList(queryWrapper);

            List<EsBuyerOrderItemsDO> esBuyerOrderItemsDOList = new ArrayList<>();
            esBuyerOrderItemsDOList = esOrderItems.stream().map(orderItems -> {
                EsBuyerOrderItemsDO esBuyerOrderItemsDO = new EsBuyerOrderItemsDO();
                BeanUtils.copyProperties(orderItems,esBuyerOrderItemsDO);
                esBuyerOrderItemsDO.setCreateTime(buyerOrderDO.getCreateTime());
                return esBuyerOrderItemsDO;
            }).collect(Collectors.toList());
            esBuyerOrderItemsDOList.forEach(esBuyerOrderItemsDO -> {
                String specJson = esBuyerOrderItemsDO.getSpecJson();
                if (StringUtils.isNotEmpty(specJson)) {
                    List<EsSpecValuesDO> specValuesDOList = JsonUtil.jsonToList(specJson, EsSpecValuesDO.class);
                    esBuyerOrderItemsDO.setSpecList(specValuesDOList);
                }
            });
            buyerOrderDO.setEsBuyerOrderItemsDO(esBuyerOrderItemsDOList);

            return DubboResult.success(buyerOrderDO);
        } catch (ArgumentException ae) {
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("订单商品分页列表查询失败",e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }

    /**
     * 修改订单表 和订单商品明细表评价状态
     * @param orderSn
     * @param goodsId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult<EsOrderItemsDO> updateOrderCommentStatus(String orderSn, Long goodsId,Long skuId) {
        try {
            // 查询该订单商品详情是够存在，
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).eq(EsOrderItems::getGoodsId,goodsId).eq(EsOrderItems::getSkuId,skuId);
            List<EsOrderItems> esOrderItemsIsEexit = this.esOrderItemsMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esOrderItemsIsEexit)) {
                throw new ArgumentException(TradeErrorCode.GET_ORDER_GOODS_MESSAGE_ERROR.getErrorCode(),"订单商品明细不存在！");
            }
            //  若存在 进行订单评价状态更新
            EsOrderItems esOrderItems1 = new EsOrderItems();
            esOrderItems1.setSingleCommentStatus(CommentStatusEnum.FINISHED.name());
            this.baseMapper.update(esOrderItems1,queryWrapper);
            QueryWrapper<EsOrderItems> queryCommentNum = new QueryWrapper<>();
            queryCommentNum.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            List<EsOrderItems> esOrderItems2 = this.esOrderItemsMapper.selectList(queryCommentNum);
            List<EsOrderItems> collect1 = esOrderItems2.stream().filter(esOrderItems -> "FINISHED".equals(esOrderItems.getSingleCommentStatus())).collect(Collectors.toList());
            // 修改订单的最终状态
            if (collect1.size() == esOrderItems2.size()){
                QueryWrapper<EsOrder> orderQuery = new QueryWrapper<>();
                orderQuery.lambda().eq(EsOrder::getOrderSn,orderSn);
                EsOrder esOrder = esOrderMapper.selectOne(orderQuery);
                esOrder.setOrderState(OrderStatusEnum.COMPLETE.value());
                esOrder.setCommentStatus(CommentStatusEnum.FINISHED.value());
                esOrderMapper.updateById(esOrder);
            }

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error(ae.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error(th.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public List<EsOrderItemsDO> selectNotCommentGoods() {

            // 查询订单状态为已完成后 过了自动评价天数的订单列表
            DubboResult<EsAutoCommentConfigDO> autoCommentConfigList = iEsAutoCommentConfigService.getAutoCommentConfigList();
            EsAutoCommentConfigDO commentConfigDO = autoCommentConfigList.getData();

            int time = this.dayConversionSecond(commentConfigDO.getAutoDays());
            Long currentTime = System.currentTimeMillis();
            QueryWrapper<EsOrder> wrapper = new QueryWrapper<>();
            wrapper.lambda().lt(EsOrder::getCreateTime,currentTime-time)
                    .eq(EsOrder::getOrderState, OrderStatusEnum.COMPLETE.value());
            List<EsOrder> ordersList = this.esOrderMapper.selectList(wrapper);
            // 集合中筛选出订单编号，封装为list
            List<String> orderSnList = ordersList.stream().filter(esOrder -> esOrder.getOrderSn().equals(esOrder.getOrderSn()))
                    .map(EsOrder::getOrderSn).collect(Collectors.toList());
            QueryWrapper<EsOrderItems> wrapper1 = new QueryWrapper<>();
            wrapper1.lambda().in(EsOrderItems::getOrderSn,orderSnList)
                    .eq(EsOrderItems::getSingleCommentStatus,CommentStatusEnum.UNFINISHED.value());
            // 通过list 和订单商品明细表中的评论状态查出 所有未评论
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(wrapper1);
            List<EsOrderItemsDO> orderItemsDO = new ArrayList<>();

            if (esOrderItems.size() != 0){
                orderItemsDO = esOrderItems.stream().map(orderItems -> {
                    EsOrderItemsDO orderItemsDO1 = new EsOrderItemsDO();
                    BeanUtils.copyProperties(orderItems,orderItemsDO1);
                    return orderItemsDO1;
                }).collect(Collectors.toList());
            }

            return orderItemsDO;

    }

    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnList(List<String> collect) {
        try {
            QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(EsOrderItems::getOrderSn,collect);
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(wrapper);
            List<EsOrderItemsDO> esOrderItemsDOList = null;
            if (CollectionUtils.isNotEmpty(esOrderItems)){
                esOrderItemsDOList = BeanUtil.copyList(esOrderItems, EsOrderItemsDO.class);
            }
            return DubboPageResult.success(esOrderItemsDOList);
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsServiceOrderItemsDO> getEsServiceOrderItemsList(EsReFundQueryBuyerDTO esReFundQueryDTO, int pageSize, int pageNum) {
        try {
            Page<EsServiceOrderItems> page = new Page<>(pageNum, pageSize);
            IPage<EsServiceOrderItems> iPage = this.esOrderMapper.selectServiceItemsList(page, esReFundQueryDTO);
            List<EsServiceOrderItems> records = iPage.getRecords();
            List<EsServiceOrderItemsDO> esServiceOrderItemsDOS = BeanUtil.copyList(records, EsServiceOrderItemsDO.class);
            return DubboPageResult.success(iPage.getTotal(),esServiceOrderItemsDOS);
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }



    @Override
    public DubboPageResult<EsWapRefundOrderItemsDO> getEsWapOrderItemsList(EsReFundQueryBuyerDTO esReFundQueryDTO, int pageSize, int pageNum) {
        try {
            Page<EsWapRefundOrderItemsDO> page = new Page<>(pageNum, pageSize);
            IPage<EsWapRefundOrderItemsDO> iPage = this.esOrderMapper.getEsWapOrderItemsList(page, esReFundQueryDTO);
            List<EsWapRefundOrderItemsDO> records = iPage.getRecords();
            return DubboPageResult.success(iPage.getTotal(),records);
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Map<String, Double>> getEsOrderItemsMoney(String orderSn, Long[] skuIds) {
        QueryWrapper<EsOrderItems> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).in(EsOrderItems::getSkuId,skuIds);
        // 通过订单号 查询该订单的支付方式 订单余额 支付和 第三方支付的金额
        QueryWrapper<EsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrder::getOrderSn,orderSn);
        EsOrder esOrder = this.esOrderMapper.selectOne(queryWrapper);

        // 判断
        List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(wrapper);
        // 选中的商品项价格总和
        double sum = esOrderItems.stream().mapToDouble(EsOrderItems::getMoney).sum();
        Map<String, Double> map = null;
        // 判断 该订单 第三方支付金额 是否大于 该订单项商品价格总和
        if (esOrder.getPayMoney() >= sum){
            // 优先退回第三方支付金额
            map = new HashMap<>();
            map.put(esOrder.getPaymentMethodName(),sum);
        }else {
            // 优先退回第三方支付金额
            map = new HashMap<>();
            map.put(esOrder.getPaymentMethodName(),esOrder.getPayMoney());
            // 再退回余额支付金额
            map.put("余额",sum - esOrder.getPayMoney());
        }


        return DubboResult.success(map);
    }

    @Override
    public DubboResult<EsOrderItemsDO> queryBySnapshotId(Long snapshotId, Long goodsId) {
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getSnapshotId,snapshotId).eq(EsOrderItems::getGoodsId,goodsId);
        EsOrderItems esOrderItems = this.getOne(queryWrapper);
        EsOrderItemsDO orderItemsDO = new EsOrderItemsDO();
        if (esOrderItems != null){
            BeanUtils.copyProperties(esOrderItems,orderItemsDO);
        }
        return DubboResult.success(orderItemsDO);
    }

    @Override
    public DubboResult<EsOrderItemsDO> queryLfcItem(String orderSn) {
        try {
            QueryWrapper<EsOrderItems> wrapper=new QueryWrapper();
            wrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn);
            EsOrderItems esOrderItems = this.esOrderItemsMapper.selectOne(wrapper);
            EsOrderItemsDO orderItemsDO=new EsOrderItemsDO();
            if (esOrderItems !=null){
                BeanUtil.copyProperties(esOrderItems,orderItemsDO);
            }
            return DubboResult.success(orderItemsDO);
        } catch (ArgumentException ae) {
            logger.error("订单详情不存在！", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("订单详情不存在！", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsOrderItemsDO> getEsOrderItemsByOrderSnAndShipNo(String orderSn, String shipNo) {
        logger.info("订单号："+orderSn+ "======，快递单号"+shipNo);
        QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsOrderItems::getOrderSn,orderSn).eq(EsOrderItems::getShipNo,shipNo);
        List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);
        List<EsOrderItemsDO> esOrderItemsDOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(esOrderItems)){
            esOrderItemsDOList = BeanUtil.copyList(esOrderItems, EsOrderItemsDO.class);
        }
        return DubboPageResult.success((long)esOrderItems.size(),esOrderItemsDOList);
    }


    /**
     *
     *  订单商品明细表评价数量
     * @return
     */

    @Override
    public DubboResult<EsOrderCommDO> getCount(Long memberId) {
        EsOrderCommDO orderCommDO=new EsOrderCommDO();
        int has=0;
        int not=0;
        List<EsBuyerOrderDO> buyerOrderCommentCount = this.esOrderItemsMapper.getBuyerOrderCommentCount(memberId);
        for (EsBuyerOrderDO esBuyerOrderDO:buyerOrderCommentCount) {
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,esBuyerOrderDO.getOrderSn());
            queryWrapper.lambda().eq(EsOrderItems::getState,ServiceStatusEnum.NOT_APPLY.value());
            List<EsOrderItems> esOrderItems = esOrderItemsMapper.selectList(queryWrapper);
           if (StringUtil.isNotEmpty(esOrderItems)){
               for (EsOrderItems items: esOrderItems){
                   if (items.getSingleCommentStatus().equals(CommentStatusEnum.FINISHED.value())){
                       has ++;
                   }else{
                       not ++;
                   }
               }
           }

        }
        orderCommDO.setHasComm(has);
        orderCommDO.setNotComm(not);
        return DubboResult.success(orderCommDO);
    }

    /**
     * 将天数转换为响应的秒数
     * 如果是测试模式，默认为1秒
     * @param day
     * @return
     */
    private Integer dayConversionSecond(int day){
        Integer time = day * 24 * 60 * 60;

        return time;
    }


    public DubboResult saveOrderItemMessage(EsOrderDTO esOrder, CartItemsDTO cartItemsDTO) {
        EsOrderItems esOrderItems = new EsOrderItems();
        esOrderItems.setId(snowflakeIdWorker.nextId());
        esOrderItems.setGoodsSn(cartItemsDTO.getGoodsSn());
        esOrderItems.setTradeSn(esOrder.getTradeSn());
        esOrderItems.setOrderSn(esOrder.getOrderSn());
        esOrderItems.setGoodsId(cartItemsDTO.getGoodsId());
        esOrderItems.setSkuId(cartItemsDTO.getSkuId());
        esOrderItems.setIsFresh(cartItemsDTO.getIsFresh());
        esOrderItems.setNum(cartItemsDTO.getNum());
        esOrderItems.setImage(cartItemsDTO.getGoodsImage());
        esOrderItems.setName(cartItemsDTO.getName());
        esOrderItems.setCategoryId(cartItemsDTO.getCategoryId());
        esOrderItems.setMoney(cartItemsDTO.getCartPrice());
        esOrderItems.setDeliveryMethod(cartItemsDTO.getDeliveryMethod());
        // 设置分类名称
        DubboResult<EsCategoryDO> category = categoryService.getCategory(cartItemsDTO.getCategoryId());
        if(category.isSuccess()){
            esOrderItems.setCategoryName(category.getData().getName());
        }
        // 判断组合活动的选中状态，存活动信息
        List<TradePromotionGoodsDTO> promotionList = cartItemsDTO.getPromotionList();
        promotionList.forEach(tradePromotionGoodsVO -> {
            Integer isCheck = tradePromotionGoodsVO.getIsCheck();
            if (isCheck == 1){
                esOrderItems.setPromotionType(tradePromotionGoodsVO.getPromotionType());
                esOrderItems.setPromotionId(tradePromotionGoodsVO.getActivityId());
            }

        });

        //初始售后状态
        esOrderItems.setState(ServiceStatusEnum.NOT_APPLY.value());
        esOrderItems.setSpecJson(JsonUtil.objectToJson(cartItemsDTO.getSpecList()));
        esOrderItems.setSingleCommentStatus(CommentStatusEnum.UNFINISHED.value());
        this.esOrderItemsMapper.insert(esOrderItems);

        // 如果存在自提商品信息的情况 保存自提商品信息
        Integer isDelivery = cartItemsDTO.getIsDelivery();
        if (isDelivery == 1){
            EsDeliveryInfo esDeliveryInfo = new EsDeliveryInfo();
            esDeliveryInfo.setOrderSn(esOrder.getOrderSn());
            esDeliveryInfo.setOrderDetailSn(esOrderItems.getId());
            esDeliveryInfo.setContent(esOrder.getDeliveryText());
            esDeliveryInfo.setIsOk(2);
            esDeliveryInfo.setDeliveryTime(esOrder.getDeliveryTime());
            esDeliveryInfo.setSkuId(esOrderItems.getSkuId());
            esDeliveryInfo.setGoodsId(esOrderItems.getGoodsId());

            esDeliveryInfoMapper.insert(esDeliveryInfo);
        }
        return DubboResult.success();
    }

}
