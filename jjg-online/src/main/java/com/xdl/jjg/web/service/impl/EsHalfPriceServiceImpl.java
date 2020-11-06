package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.trade.model.domain.ESGoodsSkuSelectDO;
import com.jjg.trade.model.domain.EsHalfPriceDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.dto.EsHalfPriceDTO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.EsHalfPriceVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsHalfPrice;
import com.xdl.jjg.manager.PromotionRuleManager;
import com.xdl.jjg.mapper.EsHalfPriceMapper;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.plugin.PromotionValid;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsHalfPriceService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import com.xdl.jjg.web.service.job.ResponseEntityMsg;
import com.xdl.jjg.web.service.job.execute.XXLHttpClient;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 第二件半价活动表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service
public class EsHalfPriceServiceImpl extends ServiceImpl<EsHalfPriceMapper, EsHalfPrice> implements IEsHalfPriceService {

    private static Logger logger = LoggerFactory.getLogger(EsHalfPriceServiceImpl.class);

    @Autowired
    private EsHalfPriceMapper halfPriceMapper;

    @Autowired
    private PromotionRuleManager promotionRuleManager;

    @Autowired
    private IEsPromotionGoodsService promotionGoodsService;

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    private GoodsSkuService esGoodsSkuService;
    /**
     * 插入第二件半价活动表数据
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertHalfPrice(EsHalfPriceDTO halfPriceDTO) {
        try {
            PromotionValid.paramValid(halfPriceDTO.getStartTime(), halfPriceDTO.getEndTime(), halfPriceDTO.getRangeType(),
                    halfPriceDTO.getGoodsList());

            promotionRuleManager.verifyTime(halfPriceDTO.getStartTime(), halfPriceDTO.getEndTime(), PromotionTypeEnum.HALF_PRICE,
                    null, halfPriceDTO.getShopId());

            //初步形成商品的DTO列表
            List<EsPromotionGoodsDTO> goodsListDTO = new ArrayList<>();
            //是否是全部商品参与
            if (halfPriceDTO.getRangeType() == 1) {
                EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
                goodsDTO.setGoodsId(-1L);
                goodsDTO.setGoodsName("全部商品");
                goodsDTO.setThumbnail("path");
                goodsListDTO.add(goodsDTO);
                halfPriceDTO.setGoodsList(goodsListDTO);
            }
            //检测活动规则
            promotionRuleManager.verifyRule(halfPriceDTO.getGoodsList(), PromotionTypeEnum.HALF_PRICE,
                    halfPriceDTO.getStartTime()
                    , halfPriceDTO.getEndTime(), halfPriceDTO.getId());

            EsHalfPrice halfPrice = new EsHalfPrice();
            BeanUtil.copyProperties(halfPriceDTO, halfPrice);
            halfPrice.setIsDel(0);
            this.halfPriceMapper.insert(halfPrice);

            // 获取活动Id
            Long id = halfPrice.getId();

            EsHalfPriceVO halfPriceVO = new EsHalfPriceVO();
            BeanUtil.copyProperties(halfPrice, halfPriceVO);
            halfPriceVO.setIsDel(0);

            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(halfPriceVO.getStartTime());
            detailDTO.setEndTime(halfPriceVO.getEndTime());
            detailDTO.setActivityId(halfPriceVO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.HALF_PRICE.name());
            detailDTO.setTitle(halfPriceVO.getTitle());
            detailDTO.setShopId(halfPriceDTO.getShopId());
            //将活动商品入库
            DubboResult result = this.promotionGoodsService.insertPromotionGoods(halfPriceDTO.getGoodsList(),
                    detailDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }

            // 将活动放入缓存中
            jedisCluster.set(PromotionCacheKeys.getHalfPriceKey(id), JSON.toJSONString(halfPriceVO));
            //第二件半价定时任务添加
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "第二件半价过期定时任务";
            String s2 = XXLHttpClient.addJob(addresses,id,PromotionTypeEnum.HALF_PRICE.name(),halfPriceVO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            ResponseEntityMsg responseEntity1 = JsonUtil.jsonToObject(s2, ResponseEntityMsg.class);
            // 第三方xxl_job返回的任务ID
            String content = responseEntity1.getContent();
            //  修改当前活动的定时任务关联的主键ID 用于后期活动删除操作
            EsHalfPrice esHalfPrice = halfPriceMapper.selectById(id);
            esHalfPrice.setJobId(Long.valueOf(content));
            halfPriceMapper.updateById(esHalfPrice);

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("第二件半价活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("第二件半价活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新第二件半价活动表数据
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @param id           主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateHalfPrice(EsHalfPriceDTO halfPriceDTO, Long id) {
        try {
            // 验证活动状态
            this.verifyStatus(id);

            // 判断是不是全部商品参加
            this.allGoods(halfPriceDTO);

            promotionRuleManager.verifyRule(halfPriceDTO.getGoodsList(), PromotionTypeEnum.FULL_DISCOUNT,
                    halfPriceDTO.getStartTime(), halfPriceDTO.getEndTime(), id);

            EsHalfPrice halfPrice = this.halfPriceMapper.selectById(id);
            if (halfPrice == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(halfPriceDTO, halfPrice);
            QueryWrapper<EsHalfPrice> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsHalfPrice::getId, id);
            halfPrice.setUpdateTime(new Date().getTime());
            this.halfPriceMapper.update(halfPrice, queryWrapper);
            // 删除之前的活动与商品的对照关系
            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(halfPriceDTO.getStartTime());
            detailDTO.setEndTime(halfPriceDTO.getEndTime());
            detailDTO.setActivityId(halfPriceDTO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.HALF_PRICE.name());
            detailDTO.setTitle(halfPriceDTO.getTitle());
            detailDTO.setShopId(halfPriceDTO.getShopId());
            // 将活动商品入库
            this.promotionGoodsService.updatePromotionGoods(halfPriceDTO.getGoodsList(), detailDTO);
            jedisCluster.set(PromotionCacheKeys.getFullDiscountKey(id), JSON.toJSONString(halfPriceDTO));

            /*
             * 修改定时任务信息
             */
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "第二件半价过期定时任务";
            Long jobId = halfPrice.getJobId();
            XXLHttpClient.update(addresses,id,jobId,PromotionTypeEnum.FULL_DISCOUNT.name(),halfPriceDTO.getEndTime(),promotionName,cartPromotionChangeJobHandler);

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("第二件半价活动表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("第二件半价活动表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取第二件半价活动表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    @Override
    public DubboResult<EsHalfPriceDO> getHalfPrice(Long id) {
        try {
            EsHalfPrice halfPrice = this.halfPriceMapper.selectById(id);
            if (halfPrice == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsHalfPriceDO halfPriceDO = new EsHalfPriceDO();
            BeanUtil.copyProperties(halfPrice, halfPriceDO);
            return DubboResult.success(halfPriceDO);
        } catch (ArgumentException ae) {
            logger.error("第二件半价活动表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("第二件半价活动表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询第二件半价活动表列表
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHalfPriceDO>
     */
    @Override
    public DubboPageResult<EsHalfPriceDO> getHalfPriceList(EsHalfPriceDTO halfPriceDTO, int pageSize, int pageNum) {
        QueryWrapper<EsHalfPrice> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsHalfPrice::getShopId,halfPriceDTO.getShopId());
            if (StringUtils.isNotEmpty(halfPriceDTO.getTitle())){
                queryWrapper.lambda().like(EsHalfPrice::getTitle,halfPriceDTO.getTitle());
            }
            Page<EsHalfPrice> page = new Page<>(pageNum, pageSize);
            IPage<EsHalfPrice> iPage = this.page(page, queryWrapper);
            List<EsHalfPriceDO> halfPriceDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                halfPriceDOList = iPage.getRecords().stream().map(halfPrice -> {
                    EsHalfPriceDO halfPriceDO = new EsHalfPriceDO();
                    BeanUtil.copyProperties(halfPrice, halfPriceDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < halfPrice.getStartTime().longValue() ){
                        halfPriceDO.setStatusText("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(halfPrice.getStartTime().longValue() < nowTime && nowTime < halfPrice.getEndTime() ){
                        halfPriceDO.setStatusText("正在进行中");
                    }else{
                        halfPriceDO.setStatusText("活动已失效");
                        halfPriceDO.setStatus("expired");
                    }

                    return halfPriceDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(halfPriceDOList);
        } catch (Throwable th) {
            logger.error("第二件半价活动表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除第二件半价活动表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteHalfPrice(Long id) {
        try {
            EsHalfPrice esHalfPrice = halfPriceMapper.selectById(id);
            this.halfPriceMapper.deleteById(id);
            this.jedisCluster.del(PromotionCacheKeys.getHalfPriceKey(id));
            // 处理该活动商品表对应的 下架状态
            promotionGoodsService.deletePromotionGoods(id,PromotionTypeEnum.HALF_PRICE.name());
            // 发送活动下架mq
            CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
            cartPromotionChangeMsg.setActivityId(id);
            cartPromotionChangeMsg.setPromotionType(PromotionTypeEnum.HALF_PRICE.name());
            // 活动下架 删除对应的定时任务数据
            XXLHttpClient.deleteJob(addresses,esHalfPrice.getJobId().intValue());
            mqProducer.send(promotion_change_topic,JSON.toJSONString(cartPromotionChangeMsg));
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("第二件半价活动表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("第二件半价活动表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 从缓存里根据id获取第二件半价活动id
     *
     * @param id 第二件半价活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 16:21:21
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult getHalfPriceForCache(Long id) {
        String  halfPricestr = this.jedisCluster.get(PromotionCacheKeys.getHalfPriceKey(id));
        EsHalfPriceDO halfPriceDO = new EsHalfPriceDO();
        if(!StringUtils.isBlank(halfPricestr)){
            halfPriceDO = JsonUtil.jsonToObject(halfPricestr, EsHalfPriceDO.class);
            return DubboResult.success(halfPriceDO);
        }else{
            EsHalfPrice halfPrice = this.getById(id);
            if(halfPrice==null){
                return DubboResult.success(null);
            }
            BeanUtils.copyProperties(halfPrice, halfPriceDO);
            return DubboResult.success(halfPriceDO);
        }

    }

    @Override
    public DubboResult<EsHalfPriceDO> getSellerHalfPrice(Long id) {
        try{
            EsHalfPrice esHalfPrice = this.halfPriceMapper.selectById(id);
            if (esHalfPrice == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsHalfPriceDO esHalfPriceDO = new EsHalfPriceDO();
            BeanUtil.copyProperties(esHalfPrice, esHalfPriceDO);
            DubboPageResult<EsPromotionGoodsDO> result = this.promotionGoodsService.getPromotionGoods(id,PromotionTypeEnum.HALF_PRICE.name());
            if(result.isSuccess()){
                List<Long> skuIds = result.getData().getList().stream().map(EsPromotionGoodsDO::getSkuId).collect(Collectors.toList());
                DubboPageResult<EsGoodsSkuDO> goodsResult = this.esGoodsSkuService.getSkuByIds(skuIds.stream().toArray(Long[]::new));
                if(goodsResult.isSuccess()){
                    List<EsGoodsSkuDO> goodsSelectDOS = goodsResult.getData().getList();
                    esHalfPriceDO.setGoodsList(BeanUtil.copyList(goodsSelectDOS, ESGoodsSkuSelectDO.class));
                }
            }
            return DubboResult.success(esHalfPriceDO);
        } catch (ArgumentException ae) {
            logger.error("第二件半件表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("第二件半件表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsHalfPriceDO> getHalfPriceByTime(Long shopId) {
        try {
            long timeMillis = System.currentTimeMillis();
            QueryWrapper<EsHalfPrice> wrapper = new QueryWrapper<>();
            wrapper.lambda().lt(EsHalfPrice::getStartTime,timeMillis);
            wrapper.lambda().gt(EsHalfPrice::getEndTime,timeMillis);
            wrapper.lambda().gt(EsHalfPrice::getShopId,shopId);
            EsHalfPrice esHalfPrice = this.halfPriceMapper.selectOne(wrapper);
            EsHalfPriceDO esHalfPriceDO = new EsHalfPriceDO();
            if (esHalfPrice != null){
                BeanUtil.copyProperties(esHalfPrice, esHalfPriceDO);
            }
            return DubboResult.success(esHalfPriceDO);
        } catch (ArgumentException ae) {
            logger.error("单品立减查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("单品立减查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 验证此活动是否可进行编辑删除操作<br/>
     * 如有问题则抛出异常
     *
     * @param id 活动id
     */
    private void verifyStatus(Long id) {
        EsHalfPrice halfPrice = this.halfPriceMapper.selectById(id);
        long nowTime = System.currentTimeMillis();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if (halfPrice.getStartTime() < nowTime && halfPrice.getEndTime() > nowTime) {
            throw new ArgumentException(TradeErrorCode.ACTIVE_IS_STARTED.getErrorCode(),
                    TradeErrorCode.ACTIVE_IS_STARTED.getErrorMsg());
        }
    }

    /**
     * 全部商品的场合组装新的商品信息
     *
     * @param halfPriceDTO 第二件半价活动DTO
     * @author: libw 981087977@qq.com
     * @date: 2019/08/03 15:06:37
     * @return: void
     */
    private void allGoods(EsHalfPriceDTO halfPriceDTO) {
        // 是否是全部商品参与
        if (halfPriceDTO.getRangeType() == 1) {
            List<EsPromotionGoodsDTO> goodsList = new ArrayList<>();
            EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsList.add(goodsDTO);
            halfPriceDTO.setGoodsList(goodsList);
        }
    }
}
