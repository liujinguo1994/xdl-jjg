package com.xdl.jjg.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.domain.EsSeckillTimetableDO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsPromotionGoods;
import com.xdl.jjg.mapper.EsPromotionGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import com.xdl.jjg.web.service.IEsSeckillApplyService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsPromotionGoodsService.class, timeout = 50000)
public class EsPromotionGoodsServiceImpl extends ServiceImpl<EsPromotionGoodsMapper, EsPromotionGoods> implements IEsPromotionGoodsService {

    @Reference(version = "${dubbo.application.version}", timeout = 10000)
    private IEsGoodsService esGoodsService;
    @Autowired
    private IEsSeckillApplyService iEsSeckillApplyService;

    private static Logger logger = LoggerFactory.getLogger(EsPromotionGoodsServiceImpl.class);

    @Autowired
    private EsPromotionGoodsMapper promotionGoodsMapper;

    /**
     * 插入数据
     *
     * @param goodsList         活动商品列表
     * @param promotionGoodsDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertPromotionGoods(List<EsPromotionGoodsDTO> goodsList, EsPromotionGoodsDTO promotionGoodsDTO) {
        try {
            if (promotionGoodsDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            if(goodsList != null && goodsList.isEmpty()){
                throw new ArgumentException(TradeErrorCode.NO_ACTIVE_GOODS.getErrorCode(),
                        TradeErrorCode.NO_ACTIVE_GOODS.getErrorMsg());
            }

            // 根据goodsId批量上传商品
            for (EsPromotionGoodsDTO goodsDTO : goodsList){

                EsPromotionGoods promotionGoods = new EsPromotionGoods();
                promotionGoods.setGoodsId(goodsDTO.getGoodsId());
                promotionGoods.setStartTime(promotionGoodsDTO.getStartTime());
                promotionGoods.setEndTime(promotionGoodsDTO.getEndTime());
                promotionGoods.setActivityId(promotionGoodsDTO.getActivityId());
                promotionGoods.setPromotionType(promotionGoodsDTO.getPromotionType());
                promotionGoods.setTitle(promotionGoodsDTO.getTitle());
                promotionGoods.setShopId(promotionGoodsDTO.getShopId());
                promotionGoods.setSkuId(goodsDTO.getSkuId());

                String promotionType =  promotionGoods.getPromotionType();
                // 因为限时抢购和团购活动为 平台发布,不是商家
                if(!PromotionTypeEnum.SECKILL.name().equals(promotionType)){
                    promotionGoods.setShopId(promotionGoodsDTO.getShopId());
                }
                promotionGoodsMapper.insert(promotionGoods);
            }

            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param goodsList         商品列表
     * @param promotionGoodsDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updatePromotionGoods(List<EsPromotionGoodsDTO> goodsList, EsPromotionGoodsDTO promotionGoodsDTO) {
        try {
            // 先删除以前的活动再进行编辑
            DubboResult deleteResult = deletePromotionGoods(promotionGoodsDTO.getActivityId(),
                    promotionGoodsDTO.getPromotionType());
            if (!deleteResult.isSuccess()) {
                throw new ArgumentException(deleteResult.getCode(), deleteResult.getMsg());
            }
            this.insertPromotionGoods(goodsList,promotionGoodsDTO);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据活动ID 查询该活动是否过期
     * @param id 活动id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    @Override
    public DubboResult<Boolean> getPromotionGoods(Long id) {
        try {
            long timeMillis = System.currentTimeMillis();
            int overdue = this.promotionGoodsMapper.getIsBeOverdueByActivityId(id, timeMillis);
            if (overdue > 0){
                return DubboResult.success(true);
            }
            return DubboResult.success(false);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
    }

    /**
     * 根据查询列表
     *
     * @param promotionGoodsDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsPromotionGoodsDO>
     */
    @Override
    public DubboPageResult getPromotionGoodsList(EsPromotionGoodsDTO promotionGoodsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsPromotionGoods> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsPromotionGoods> page = new Page<>(pageNum, pageSize);
            IPage<EsPromotionGoods> iPage = this.page(page, queryWrapper);
            List<EsPromotionGoodsDO> promotionGoodsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                promotionGoodsDOList = iPage.getRecords().stream().map(promotionGoods -> {
                    EsPromotionGoodsDO promotionGoodsDO = new EsPromotionGoodsDO();
                    BeanUtil.copyProperties(promotionGoods, promotionGoodsDO);
                    return promotionGoodsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(promotionGoodsDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据活动类型和活动id删除数据
     *
     * @param activityId    活动id
     * @param promotionType 活动类型
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsPromotionGoodsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deletePromotionGoods(Long activityId, String promotionType) {
        try {
            QueryWrapper<EsPromotionGoods> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsPromotionGoods::getActivityId, activityId);
            deleteWrapper.lambda().eq(EsPromotionGoods::getPromotionType, promotionType);
            this.promotionGoodsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 通过商品id获取活动信息,判断
     * @param id
     * @return
     */
    @Override
    public DubboResult<Boolean> checkPromotionByGoodsId(Long id) {
        // TODO 通过商品id获取活动信息，
        QueryWrapper<EsPromotionGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsPromotionGoods::getGoodsId, id);
        EsPromotionGoods promotionGoods = this.promotionGoodsMapper.selectOne(queryWrapper);
        // TODO 判断时间是否开始和过期，
        long timeMillis = System.currentTimeMillis();
        if (promotionGoods.getCreateTime() <= timeMillis && promotionGoods.getUpdateTime() >= timeMillis ){
            return DubboResult.success(true);
        }
        logger.error("该商品不在活动期间");
        // TODO
        return DubboResult.success(false);
    }

    /**
     *  获取所有活动
     * @param goodsId
     * @param currTime
     * @param shopId
     * @return
     */
    @Override
    public DubboResult getAllPromotionGoods(Long goodsId, Long currTime, Long shopId,Long skuId) {
        //根据GoodsId和SkuId，Sku为空时查询GoodsId（兼容满减满赠的商品对应），时间 获取指定的活动列表
        List<EsPromotionGoodsDO> EsPromotionGoodsList;
        try {
            QueryWrapper<EsPromotionGoods> queryWrapper = new QueryWrapper<>();
            if (goodsId == null){
                throw new ArgumentException(TradeErrorCode.PROMOTION_GOODS_NOT_NULL.getErrorCode(),
                        TradeErrorCode.PROMOTION_GOODS_NOT_NULL.getErrorMsg());
            }
            if (currTime == null){
                throw new ArgumentException(TradeErrorCode.PROMOTION_TIME_NOT_NULL.getErrorCode(),
                        TradeErrorCode.PROMOTION_TIME_NOT_NULL.getErrorMsg());
            }

            queryWrapper.lambda().le(EsPromotionGoods::getStartTime,currTime).ge(EsPromotionGoods::getEndTime,currTime);
            queryWrapper.lambda().eq(EsPromotionGoods::getGoodsId,goodsId);
            queryWrapper.lambda().eq(EsPromotionGoods::getIsLowerShelf,0);
            if(skuId != null && skuId != 0){
                queryWrapper.lambda().and(wrapper -> wrapper.eq(EsPromotionGoods::getSkuId,skuId).or().eq(EsPromotionGoods::getSkuId,-1));
            }
            List<EsPromotionGoods> resultList = promotionGoodsMapper.selectList(queryWrapper);

            //所有
            Integer totalGoodsId = -1;
            QueryWrapper<EsPromotionGoods> queryWrapperAll = new QueryWrapper<>();
            queryWrapperAll.lambda().eq(EsPromotionGoods::getGoodsId,totalGoodsId);
            if (shopId == null){
                throw new ArgumentException(TradeErrorCode.PROMOTION_SHOP_NOT_NULL.getErrorCode(),
                        TradeErrorCode.PROMOTION_SHOP_NOT_NULL.getErrorMsg());
            }
            queryWrapperAll.lambda().le(EsPromotionGoods::getStartTime,currTime).ge(EsPromotionGoods::getEndTime,currTime);
            queryWrapperAll.lambda().eq(EsPromotionGoods::getShopId,shopId);
            queryWrapperAll.lambda().ne(EsPromotionGoods::getPromotionType, PromotionTypeEnum.SECKILL.name());
            queryWrapperAll.lambda().eq(EsPromotionGoods::getIsLowerShelf,0);
            List<EsPromotionGoods> esPromotionGoods = promotionGoodsMapper.selectList(queryWrapperAll);
            resultList.addAll(esPromotionGoods);

            EsPromotionGoodsList = resultList.stream().map(esPromotionGoods1 -> {
                EsPromotionGoodsDO esPromotionGoodsDO = new EsPromotionGoodsDO();
                BeanUtils.copyProperties(esPromotionGoods1,esPromotionGoodsDO);
                return esPromotionGoodsDO;
            }).collect(Collectors.toList());
        } catch (ArgumentException e) {
           return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        }

        return DubboResult.success(EsPromotionGoodsList);
    }

    @Override
    public DubboPageResult<EsPromotionGoodsDO> getPromotionByGoodsId(List<Long> goodsIdList) {

        try {
            long currentTime = System.currentTimeMillis();
            List<EsPromotionGoods> promotionByGoodsId = this.promotionGoodsMapper.getPromotionByGoodsId(goodsIdList, currentTime);
            // 查询参与方式为所有商品的活动
            // 店铺排序，方便查询
            DubboPageResult<EsGoodsDO> goodsListResult = esGoodsService.getEsGoods(goodsIdList.toArray(new Long[goodsIdList.size()]));
            if (goodsListResult.isSuccess()) {
                List<EsGoodsDO> collect = goodsListResult.getData().getList().stream().sorted(Comparator.comparingLong(EsGoodsDO::getShopId)).collect(Collectors.toList());
                long shopId = 0;
                List<EsPromotionGoods> allPromotion = new ArrayList<>();
                for (EsGoodsDO esGoodsDO : collect) {
                    // 如果换店铺了，需要重新查询对应店铺的全部商品参与的活动列表
                    if(esGoodsDO.getShopId() != shopId){
                        shopId = esGoodsDO.getShopId();
                        QueryWrapper<EsPromotionGoods> queryWrapperAll = new QueryWrapper<>();
                        queryWrapperAll.lambda().eq(EsPromotionGoods::getGoodsId,-1);
                        queryWrapperAll.lambda().le(EsPromotionGoods::getStartTime,currentTime).ge(EsPromotionGoods::getEndTime,currentTime);
                        queryWrapperAll.lambda().eq(EsPromotionGoods::getShopId,shopId);
                        queryWrapperAll.lambda().ne(EsPromotionGoods::getPromotionType, PromotionTypeEnum.SECKILL.name());
                        queryWrapperAll.lambda().eq(EsPromotionGoods::getIsLowerShelf,0);
                        allPromotion = promotionGoodsMapper.selectList(queryWrapperAll);
                    }
                    // 修改商品ID，原ID为-1，表示全部商品，这里设置为实际商品ID,方便后续判断与分组
                    // 这里复制一个新的List，解决指针错乱问题
                    List<EsPromotionGoods> esPromotionGoods = BeanUtil.copyList(allPromotion, EsPromotionGoods.class);
                    esPromotionGoods.forEach(a -> a.setGoodsId(esGoodsDO.getId()));
                    promotionByGoodsId.addAll(esPromotionGoods);
                }
            }
            List<EsPromotionGoodsDO> promotionGoodsDOList = new ArrayList<>();
            promotionGoodsDOList = promotionByGoodsId.stream().map(esPromotionGoods -> {
                EsPromotionGoodsDO esPromotionGoodsDO = new EsPromotionGoodsDO();
                BeanUtils.copyProperties(esPromotionGoods,esPromotionGoodsDO);
                return esPromotionGoodsDO;
            }).collect(Collectors.toList());

            // 秒杀活动筛选（去除非秒杀时刻的秒杀活动）
            int inSeckillTimeline = -1;
            DubboResult<EsSeckillTimetableDO> inSeckillNow = iEsSeckillApplyService.getInSeckillNow();
            if(inSeckillNow.isSuccess()){
                inSeckillTimeline = inSeckillNow.getData().getTimeline();
            }
            int finalInSeckillTimeline = inSeckillTimeline;
            promotionGoodsDOList = promotionGoodsDOList.stream().filter(p -> {
                if (!PromotionTypeEnum.SECKILL.name().equals(p.getPromotionType())) {
                    return true;
                }
                int hour = DateUtil.hour(DateUtil.date(p.getStartTime()), true);
                return finalInSeckillTimeline == hour;
            }).collect(Collectors.toList());
            // 由于优惠活动对应SKU，所以商品活动可能会有多个，所以在这里去重一下
            List<String> tempGoodsIdActivityId = new ArrayList<>();
            promotionGoodsDOList = promotionGoodsDOList.stream().filter(p -> {
                String key = p.getGoodsId() + "_" + p.getActivityId();
                if(tempGoodsIdActivityId.contains(key)){
                    return false;
                }
                tempGoodsIdActivityId.add(key);
                return true;
            }).collect(Collectors.toList());

            return DubboPageResult.success(promotionGoodsDOList);
        } catch (Exception e) {
            logger.error("查询分页查询失败", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    public DubboPageResult<EsPromotionGoodsDO> getPromotionBySkuId(List<Long> skuIdList) {
        try {
            long currentTime = System.currentTimeMillis();
            List<EsPromotionGoods> promotionByGoodsId = this.promotionGoodsMapper.getPromotionBySkuId(skuIdList, currentTime);
            List<EsPromotionGoodsDO> promotionGoodsDOList = new ArrayList<>();
            promotionGoodsDOList = promotionByGoodsId.stream().map(esPromotionGoods -> {
                EsPromotionGoodsDO esPromotionGoodsDO = new EsPromotionGoodsDO();
                BeanUtils.copyProperties(esPromotionGoods,esPromotionGoodsDO);
                return esPromotionGoodsDO;
            }).collect(Collectors.toList());

            return DubboPageResult.success(promotionGoodsDOList);
        } catch (Exception e) {
            logger.error("查询分页查询失败", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsPromotionGoodsDO> getPromotionGoods(Long activityId, String promotionType) {
        try {
            QueryWrapper<EsPromotionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPromotionGoods::getActivityId,activityId).eq(EsPromotionGoods::getPromotionType,promotionType);
            List<EsPromotionGoods> promotionGoodsList = this.list(queryWrapper);
            List<EsPromotionGoodsDO> promotionGoodsDOS = BeanUtil.copyList(promotionGoodsList,EsPromotionGoodsDO.class);
            return DubboPageResult.success(promotionGoodsDOS);
        } catch (Exception e) {
            logger.error("查询分页查询失败", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<Boolean> getPromotionGoodsIsOld(Long activityId, String promotionType) {
        try {
            long currTime = System.currentTimeMillis();

            QueryWrapper<EsPromotionGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPromotionGoods::getActivityId,activityId)
                    .eq(EsPromotionGoods::getPromotionType,promotionType);
            queryWrapper.lambda().le(EsPromotionGoods::getEndTime,currTime);
            List<EsPromotionGoods> promotionGoodsList = this.list(queryWrapper);
            int size = promotionGoodsList.size();
            Boolean isOld = size>0 ? true: false;
            return DubboResult.success(isOld);
        } catch (Exception e) {
            logger.error("查询分页查询失败", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
