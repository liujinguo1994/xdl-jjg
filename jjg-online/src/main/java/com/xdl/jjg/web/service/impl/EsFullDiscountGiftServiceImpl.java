package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.redisson.annotation.DistributedLock;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsGoodsArchDO;
import com.shopx.goods.api.model.domain.EsGoodsQuantityLogDO;
import com.shopx.goods.api.model.domain.EsSellerGoodsSkuDO;
import com.shopx.goods.api.model.domain.dto.EsGoodsDTO;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuDTO;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuQuantityDTO;
import com.shopx.goods.api.service.*;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsFullDiscountGiftDO;
import com.shopx.trade.api.model.domain.dto.EsFullDiscountGiftDTO;
import com.shopx.trade.api.model.domain.dto.EsGiftSkuQuantityDTO;
import com.shopx.trade.api.service.IEsFullDiscountGiftService;
import com.shopx.trade.dao.entity.EsFullDiscount;
import com.shopx.trade.dao.entity.EsFullDiscountGift;
import com.shopx.trade.dao.mapper.EsFullDiscountGiftMapper;
import com.shopx.trade.dao.mapper.EsFullDiscountMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 满减赠品表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsFullDiscountGiftService.class, timeout = 50000)
public class EsFullDiscountGiftServiceImpl extends ServiceImpl<EsFullDiscountGiftMapper, EsFullDiscountGift> implements IEsFullDiscountGiftService {

    private static Logger logger = LoggerFactory.getLogger(EsFullDiscountGiftServiceImpl.class);

    private  static final  String    GIFT_QUANTITY_REDUCE = "gift_quantity_reduce";

    @Autowired
    private EsFullDiscountGiftMapper fullDiscountGiftMapper;
    @Autowired
    private EsFullDiscountMapper fullDiscountMapper;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsQuantityLogService esGoodsQuantityLogService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsGoodsArchService goodsArchService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsGoodsSkuService esGoodsSkuService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsGoodsSkuQuantityService esGoodsSkuQuantityService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsGoodsService esGoodsService;
    /**
     * 插入满减赠品表数据
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertFullDiscountGift(EsFullDiscountGiftDTO fullDiscountGiftDTO) {
        try {
            // 校验赠品是否存在
            DubboResult<EsGoodsArchDO> result = goodsArchService.getGoodsArchGifts(fullDiscountGiftDTO.getId());
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            if (result.getData() == null) {
                throw new ArgumentException(TradeErrorCode.GIFTS_NOT_EXIST.getErrorCode(),
                        TradeErrorCode.GIFTS_NOT_EXIST.getErrorMsg());
            }
            EsGoodsArchDO esGoodsArchDO = result.getData();
            EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
            esGoodsDTO.setShopId(fullDiscountGiftDTO.getShopId());
            esGoodsDTO.setMoney(fullDiscountGiftDTO.getGiftMoney());
            esGoodsDTO.setIsLfc(2);

            BeanUtil.copyProperties(esGoodsArchDO,esGoodsDTO);
            EsFullDiscountGift fullDiscountGift = new EsFullDiscountGift();
            BeanUtil.copyProperties(fullDiscountGiftDTO, fullDiscountGift);
            fullDiscountGift.setGoodsId(fullDiscountGiftDTO.getGoodsId());
            fullDiscountGift.setEnableQuantity(fullDiscountGiftDTO.getXnQuantity()+fullDiscountGiftDTO.getQuantity());
            fullDiscountGift.setEnableStore(fullDiscountGift.getEnableQuantity());
            this.fullDiscountGiftMapper.insert(fullDiscountGift);
            esGoodsService.insertGiftsEsGoods(esGoodsDTO);
            List<EsGoodsSkuDTO> goodsSkuDTOList= BeanUtil.copyList(fullDiscountGiftDTO.getSkuList(), EsGoodsSkuDTO.class);
            esGoodsSkuService.sellerUpdateGoodsSkuGift(goodsSkuDTOList,fullDiscountGiftDTO.getShopId(),fullDiscountGift.getGoodsId());
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("满减赠品表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("满减赠品表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新满减赠品表数据
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateFullDiscountGift(EsFullDiscountGiftDTO fullDiscountGiftDTO, Long id) {
        try {

            EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(id);
            if (fullDiscountGift == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            // 校验赠品是否存在
            DubboResult result = goodsArchService.getGoodsArchGifts(fullDiscountGift.getGoodsId());
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            if (result.getData() == null) {
                throw new ArgumentException(TradeErrorCode.GIFTS_NOT_EXIST.getErrorCode(),
                        TradeErrorCode.GIFTS_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(fullDiscountGiftDTO, fullDiscountGift);
            QueryWrapper<EsFullDiscountGift> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFullDiscountGift::getId, id);
            Integer xuQuantity  = fullDiscountGiftDTO.getXnQuantity() == null ? 0 : fullDiscountGiftDTO.getXnQuantity();
            Integer quantity  = fullDiscountGiftDTO.getQuantity() == null ? 0 : fullDiscountGiftDTO.getQuantity();
            fullDiscountGift.setEnableQuantity(xuQuantity+quantity);
            fullDiscountGift.setEnableStore(xuQuantity+quantity);
            this.fullDiscountGiftMapper.update(fullDiscountGift, queryWrapper);
            List<EsGoodsSkuDTO> goodsSkuDTOList= BeanUtil.copyList(fullDiscountGiftDTO.getSkuList(), EsGoodsSkuDTO.class);
            esGoodsSkuService.sellerUpdateGoodsSkuGift(goodsSkuDTOList,fullDiscountGiftDTO.getShopId(),fullDiscountGift.getGoodsId());
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("满减赠品表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减赠品表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult updateFullDiscountGift(List<EsFullDiscountGiftDTO> giftDTOList) {
        try {
            if(CollectionUtils.isNotEmpty(giftDTOList)){
                giftDTOList.stream().forEach(gift->{
                    EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(gift.getId());
                    if (fullDiscountGift == null) {
                        throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
                    }
                    // 校验赠品是否存在
                    DubboResult result = goodsArchService.getGoodsArchGifts(fullDiscountGift.getGoodsId());
                    if (result.getData() == null) {
                        throw new ArgumentException(TradeErrorCode.GIFTS_NOT_EXIST.getErrorCode(),
                                TradeErrorCode.GIFTS_NOT_EXIST.getErrorMsg());
                    }
                    EsFullDiscountGift discountGift = new EsFullDiscountGift();
                    Integer xuQuantity  = gift.getXnQuantity() == null ? 0 : gift.getXnQuantity();
                    Integer quantity  = gift.getQuantity() == null ? 0 : gift.getQuantity();
                    gift.setEnableQuantity(xuQuantity+quantity);
                    gift.setEnableStore(xuQuantity+quantity);
                    BeanUtil.copyProperties(gift,discountGift);
                    this.fullDiscountGiftMapper.updateById(discountGift);
                    esGoodsSkuService.sellerUpdateGoodsSkuGift(gift.getSkuList(),gift.getShopId(),gift.getGoodsId());
                });
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("满减赠品表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减赠品表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取满减赠品表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    @Override
    public DubboResult<EsFullDiscountGiftDO> getFullDiscountGift(Long id) {
        try {
            EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(id);
            if (fullDiscountGift == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsFullDiscountGiftDO fullDiscountGiftDO = new EsFullDiscountGiftDO();
            BeanUtil.copyProperties(fullDiscountGift, fullDiscountGiftDO);
            return DubboResult.success(fullDiscountGiftDO);
        } catch (ArgumentException ae){
            logger.error("满减赠品表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("满减赠品表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult<EsFullDiscountGiftDO> getSellerFullDiscountGift(Long id) {
        try {
            EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(id);
            if (fullDiscountGift == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsFullDiscountGiftDO fullDiscountGiftDO = new EsFullDiscountGiftDO();
            BeanUtil.copyProperties(fullDiscountGift, fullDiscountGiftDO);
            DubboPageResult<EsSellerGoodsSkuDO> result = esGoodsSkuService.getGoodsSkuList(fullDiscountGift.getGoodsId());
            if(result.isSuccess()){
                fullDiscountGiftDO.setSkuList(result.getData().getList());
            }
            return DubboResult.success(fullDiscountGiftDO);
        } catch (ArgumentException ae){
            logger.error("满减赠品表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("满减赠品表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据查询满减赠品表列表
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFullDiscountGiftDO>
     */
    @Override
    public DubboPageResult<EsFullDiscountGiftDO> getFullDiscountGiftList(EsFullDiscountGiftDTO fullDiscountGiftDTO, int pageSize, int pageNum) {
        QueryWrapper<EsFullDiscountGift> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().like(StringUtils.isNotEmpty(fullDiscountGiftDTO.getGiftName()),
                    EsFullDiscountGift::getGiftName, fullDiscountGiftDTO.getGiftName());

            Page<EsFullDiscountGift> page = new Page<>(pageNum, pageSize);
            IPage<EsFullDiscountGift> iPage = this.page(page, queryWrapper);
            List<EsFullDiscountGiftDO> fullDiscountGiftDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                fullDiscountGiftDOList = iPage.getRecords().stream().map(fullDiscountGift -> {
                    EsFullDiscountGiftDO fullDiscountGiftDO = new EsFullDiscountGiftDO();
                    BeanUtil.copyProperties(fullDiscountGift, fullDiscountGiftDO);
                    return fullDiscountGiftDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(fullDiscountGiftDOList);
        } catch (ArgumentException ae){
            logger.error("满减赠品表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减赠品表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除满减赠品表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteFullDiscountGift(Long id) {
        try {
            this.fullDiscountGiftMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("满减赠品表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("满减赠品表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DistributedLock(value = GIFT_QUANTITY_REDUCE,expireSeconds = 60)
    public DubboResult reduceFullDiscountGiftNum(EsGiftSkuQuantityDTO giftQuantity) {
        String orderSn = giftQuantity.getOrderSn();
        Long id = giftQuantity.getId();

        try{
            EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(id);
            if (fullDiscountGift == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            //检查库存是否充足
            if(checkQuantity(giftQuantity)){
                throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),"赠品库存数量不足");
            };
            innerReduceGiftQuantity(giftQuantity,orderSn);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(String.format("赠品 库存扣减失败，订单号:[%s]",orderSn) , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(String.format("赠品 库存扣减失败，订单号:[%s]",orderSn) , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult insertFullDiscountGiftNum(EsGiftSkuQuantityDTO giftQuantity) {
        String orderSn = giftQuantity.getOrderSn();
        Long id = giftQuantity.getId();
        try{
            EsFullDiscountGift fullDiscountGift = this.fullDiscountGiftMapper.selectById(id);
            if (fullDiscountGift == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            insertGiftQuantity(giftQuantity,orderSn);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(String.format("赠品 库存扣减失败，订单号:[%s]",orderSn) , ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(String.format("赠品 库存扣减失败，订单号:[%s]",orderSn) , th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 批量删除数据
     *
     * @param ids id列表
     * @author: libw 981087977@qq.com
     * @date: 2019/08/13 14:20:01
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult batchDelete(Integer[] ids) {

        try {

            long timeMillis = System.currentTimeMillis();
            // 判断当前商品是否绑定正在进行满减满赠活动，
            QueryWrapper<EsFullDiscount> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsFullDiscount::getIsDel,0);
            wrapper.lambda().in(EsFullDiscount::getGiftId,ids);
            wrapper.lambda().lt(EsFullDiscount::getStartTime,timeMillis);
            wrapper.lambda().gt(EsFullDiscount::getEndTime,timeMillis);
            List<EsFullDiscount> esFullDiscounts = fullDiscountMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(esFullDiscounts)){
                throw new ArgumentException(TradeErrorCode.GIFTS_DEL_ERROE.getErrorCode(),TradeErrorCode.GIFTS_DEL_ERROE.getErrorMsg());
            }

            this.fullDiscountGiftMapper.deleteBatchIds(Arrays.asList(ids));
            QueryWrapper<EsFullDiscountGift> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsFullDiscountGift::getId,ids);
            List<EsFullDiscountGift> giftList = this.list(queryWrapper);
            List<Long> goodsList = giftList.stream().map(EsFullDiscountGift::getGoodsId).collect(Collectors.toList());
            this.esGoodsService.deleteEsGoods(goodsList.stream().toArray(Long[]::new));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("满减赠品表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("满减赠品表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult batchDelete(Long[] ids) {

        try {

            long timeMillis = System.currentTimeMillis();
            // 判断当前商品是否绑定正在进行满减满赠活动，
            QueryWrapper<EsFullDiscount> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsFullDiscount::getIsDel,0);
            wrapper.lambda().in(EsFullDiscount::getGiftId,ids);
            wrapper.lambda().lt(EsFullDiscount::getStartTime,timeMillis);
            wrapper.lambda().gt(EsFullDiscount::getEndTime,timeMillis);
            List<EsFullDiscount> esFullDiscounts = fullDiscountMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(esFullDiscounts)){
                return DubboResult.fail(TradeErrorCode.GIFTS_DEL_ERROE.getErrorCode(),TradeErrorCode.GIFTS_DEL_ERROE.getErrorMsg());
            }
            this.fullDiscountGiftMapper.deleteBatchIds(Arrays.asList(ids));
            QueryWrapper<EsFullDiscountGift> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsFullDiscountGift::getId,ids);
            List<EsFullDiscountGift> giftList = this.list(queryWrapper);
            List<Long> goodsList = giftList.stream().map(EsFullDiscountGift::getGoodsId).collect(Collectors.toList());
            this.esGoodsService.deleteEsGoods(goodsList.stream().toArray(Long[]::new));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("满减赠品表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("满减赠品表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 库存增加
     * @param giftQuantity
     * @param orderSn
     */
    private void insertGiftQuantity(EsGiftSkuQuantityDTO giftQuantity, String orderSn) {
        Long id = giftQuantity.getId();
        EsFullDiscountGift esFullDiscountGift = this.baseMapper.selectById(id);

        //赠品库存数量
        Integer enableStore = esFullDiscountGift.getEnableStore();
        //赠品 可用 库存
        Integer oldEnableQuantity = esFullDiscountGift.getEnableQuantity();
        //赠品 实际库存
        Integer oldQuantity = esFullDiscountGift.getQuantity();
        //赠品 虚拟库存
        Integer xnQuantity = esFullDiscountGift.getXnQuantity();
        Integer goodsNumber = 1;

        DubboResult<EsGoodsQuantityLogDO> result = esGoodsQuantityLogService.getGoodsQuantityLogByGoodsId(orderSn,giftQuantity.getGoodsId());

        logger.error("该订单的订单编号"+ orderSn);
        logger.error("该订单的赠品ID"+ giftQuantity.getGoodsId());
        logger.error("result信息"+result.getData());
        logger.error("result异常信息"+result.getCode() + "======"+result.getMsg());
        if(!result.isSuccess() || result.getData() == null){
            throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),String.format("获取库存扣减日志数据失败 %s",orderSn));
        }
        EsGoodsQuantityLogDO quantityLogDO = result.getData();
        Integer xnReduction =quantityLogDO.getXnQuantity();
        Integer xjReduction = quantityLogDO.getQuantity();
        oldQuantity = oldQuantity +xjReduction;
        xnQuantity = xnQuantity + xnReduction;

        //SKU 库存计算  可用库存=实际库存+虚拟库存
        oldEnableQuantity = oldQuantity + xnQuantity;

        esFullDiscountGift.setEnableStore(oldEnableQuantity);
        esFullDiscountGift.setQuantity(oldQuantity);
        esFullDiscountGift.setEnableQuantity(oldEnableQuantity);
        esFullDiscountGift.setXnQuantity(xnReduction);
        fullDiscountGiftMapper.updateById(esFullDiscountGift);

        List<EsGoodsSkuQuantityDTO> quantityDTO = new ArrayList<>();
        EsGoodsSkuQuantityDTO esGoodsSkuQuantityDTO = new EsGoodsSkuQuantityDTO();
        esGoodsSkuQuantityDTO.setGoodsId(esFullDiscountGift.getGoodsId());
        esGoodsSkuQuantityDTO.setGoodsNumber(1);
        esGoodsSkuQuantityDTO.setOrderSn(orderSn);
        esGoodsSkuQuantityDTO.setSkuId(quantityLogDO.getSkuId());
        quantityDTO.add(esGoodsSkuQuantityDTO);
        esGoodsSkuQuantityService.insertGoodsSkuQuantity(quantityDTO);
    }
    /*/**
     * @Description: 库存扣减
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/8 15:07
     * @param
     * @return       void
     * @exception
     *
     */
    private void innerReduceGiftQuantity(EsGiftSkuQuantityDTO giftQuantity, String orderSn) {
        Long id = giftQuantity.getId();

        EsFullDiscountGift esFullDiscountGift = this.baseMapper.selectById(id);
        //赠品库存数量
        Integer enableStore = esFullDiscountGift.getEnableStore();
        //赠品 可用 库存
        Integer oldEnableQuantity = esFullDiscountGift.getEnableQuantity();
        //赠品 实际库存
        Integer oldQuantity = esFullDiscountGift.getQuantity();
        //赠品 虚拟库存
        Integer oldXnQuantity = esFullDiscountGift.getXnQuantity();
        Integer xnReduction = 0;
        Integer sjReduction = 0;
        //如果实际库存大于0 就扣减实际库存 否则扣虚拟库存
        if (oldQuantity > 0){
            sjReduction = oldQuantity - 1;
        }else if(oldXnQuantity >0 && oldQuantity < 0){
            xnReduction = oldXnQuantity - 1;
        }
        oldEnableQuantity = sjReduction + xnReduction;
        // TODO 减库存操作
        esFullDiscountGift.setEnableStore(oldEnableQuantity);
        esFullDiscountGift.setQuantity(sjReduction);
        esFullDiscountGift.setEnableQuantity(oldEnableQuantity);
        esFullDiscountGift.setXnQuantity(xnReduction);
        fullDiscountGiftMapper.updateById(esFullDiscountGift);
        List<EsGoodsSkuQuantityDTO> quantityDTO = new ArrayList<>();
        EsGoodsSkuQuantityDTO esGoodsSkuQuantityDTO = new EsGoodsSkuQuantityDTO();
        esGoodsSkuQuantityDTO.setGoodsId(esFullDiscountGift.getGoodsId());
        esGoodsSkuQuantityDTO.setGoodsNumber(1);
        esGoodsSkuQuantityDTO.setOrderSn(orderSn);
        logger.info("赠品商品Id:"+esFullDiscountGift.getGoodsId());
       DubboPageResult<EsSellerGoodsSkuDO> result= esGoodsSkuService.getGoodsSkuList(esFullDiscountGift.getGoodsId());
       if(result.isSuccess()){
           List<EsSellerGoodsSkuDO> skuList = result.getData().getList();
           if(CollectionUtils.isEmpty(skuList)){
               throw new ArgumentException(GoodsErrorCode.QUANTITY_SHORTAGE.getErrorCode(),String.format("获取库存扣减日志数据失败 ，商品表未找到赠品信息  %s",orderSn));
           }
           skuList.sort((sku1,sku2)->sku2.getEnableQuantity().compareTo(sku1.getEnableQuantity()));
           esGoodsSkuQuantityDTO.setSkuId(skuList.get(0).getId());
           quantityDTO.add(esGoodsSkuQuantityDTO);
           esGoodsSkuQuantityService.reduceGoodsSkuQuantity(quantityDTO);
       }
    }

    private boolean checkQuantity(EsGiftSkuQuantityDTO giftQuantity) {
        Long id = giftQuantity.getId();
        EsFullDiscountGift esFullDiscountGift = this.baseMapper.selectById(id);
        Integer enableStore = esFullDiscountGift.getEnableStore();
        if(enableStore > 0){
            return false;
        }
        return true;
    }
}
