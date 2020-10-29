package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.trade.model.domain.ESGoodsSkuSelectDO;
import com.jjg.trade.model.domain.EsGoodsDiscountDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.dto.EsGoodsDiscountDTO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.EsGoodsDiscountVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsGoodsDiscount;
import com.xdl.jjg.manager.PromotionRuleManager;
import com.xdl.jjg.mapper.EsGoodsDiscountMapper;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.plugin.PromotionValid;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsGoodsDiscountService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import com.xdl.jjg.web.service.job.ResponseEntityMsg;
import com.xdl.jjg.web.service.job.execute.XXLHttpClient;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 商品折扣活动表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-07-02 15:35:42
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsDiscountService.class, timeout = 50000)
public class EsGoodsDiscountServiceImpl extends ServiceImpl<EsGoodsDiscountMapper, EsGoodsDiscount> implements IEsGoodsDiscountService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsDiscountServiceImpl.class);

    @Autowired
    private EsGoodsDiscountMapper goodsDiscountMapper;

    @Autowired
    private PromotionRuleManager promotionRuleManager;

    @Autowired
    private IEsPromotionGoodsService promotionGoodsService;

    @Autowired
    private JedisCluster jedisCluster;

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    @Autowired
    private GoodsSkuService esGoodsSkuService;

    /**
     * 插入商品折扣活动表数据
     *
     * @param goodsDiscountDTO 商品折扣活动表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsDiscount(EsGoodsDiscountDTO goodsDiscountDTO) {
        try {
            PromotionValid.paramValid(goodsDiscountDTO.getStartTime(), goodsDiscountDTO.getEndTime(), goodsDiscountDTO.getRangeType(),
                    goodsDiscountDTO.getGoodsList());

            promotionRuleManager.verifyTime(goodsDiscountDTO.getStartTime(), goodsDiscountDTO.getEndTime(), PromotionTypeEnum.GOODS_DISCOUNT,
                    null, goodsDiscountDTO.getShopId());

            //初步形成商品的DTO列表
            List<EsPromotionGoodsDTO> goodsListDTO = new ArrayList<>();
            //是否是全部商品参与
            if (goodsDiscountDTO.getRangeType() == 1) {
                EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
                goodsDTO.setGoodsId(-1L);
                goodsDTO.setGoodsName("全部商品");
                goodsDTO.setThumbnail("path");
                goodsListDTO.add(goodsDTO);
                goodsDiscountDTO.setGoodsList(goodsListDTO);
            }
            //检测活动规则
            promotionRuleManager.verifyRule(goodsDiscountDTO.getGoodsList(), PromotionTypeEnum.MINUS, goodsDiscountDTO.getStartTime()
                    , goodsDiscountDTO.getEndTime(), goodsDiscountDTO.getId());

            EsGoodsDiscount goodsDiscount = new EsGoodsDiscount();
            BeanUtil.copyProperties(goodsDiscountDTO, goodsDiscount);
            goodsDiscount.setDiscount(goodsDiscountDTO.getDiscount()/100);
            goodsDiscount.setIsDel(0);
            this.goodsDiscountMapper.insert(goodsDiscount);

            // 获取活动Id
            Long id = goodsDiscount.getId();

            EsGoodsDiscountVO goodsDiscountVO = new EsGoodsDiscountVO();
            BeanUtil.copyProperties(goodsDiscount, goodsDiscountVO);
            goodsDiscountVO.setIsDel(0);

            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(goodsDiscountVO.getStartTime());
            detailDTO.setEndTime(goodsDiscountVO.getEndTime());
            detailDTO.setActivityId(goodsDiscountVO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.GOODS_DISCOUNT.name());
            detailDTO.setTitle(goodsDiscountVO.getTitle());
            detailDTO.setShopId(goodsDiscountVO.getShopId());
            //将活动商品入库
            DubboResult result = this.promotionGoodsService.insertPromotionGoods(goodsDiscountDTO.getGoodsList(),
                    detailDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }

            // 将活动放入缓存中
            jedisCluster.set(PromotionCacheKeys.getGoodsDiscount(id), JSON.toJSONString(goodsDiscountVO));

            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "商品打折过期定时任务";
            String s2 = XXLHttpClient.addJob(addresses,id,PromotionTypeEnum.GOODS_DISCOUNT.name(),goodsDiscountVO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            ResponseEntityMsg responseEntity1 = JsonUtil.jsonToObject(s2, ResponseEntityMsg.class);
            // 第三方xxl_job返回的任务ID
            String content = responseEntity1.getContent();
            //  修改当前活动的定时任务关联的主键ID 用于后期活动删除操作
            EsGoodsDiscount esGoodsDiscount = goodsDiscountMapper.selectById(id);
            esGoodsDiscount.setJobId(Long.valueOf(content));
            goodsDiscountMapper.updateById(esGoodsDiscount);

            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品折扣活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("商品折扣活动表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新商品折扣活动表数据
     *
     * @param goodsDiscountDTO 商品折扣活动表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodsDiscount(EsGoodsDiscountDTO goodsDiscountDTO, Long id) {
        try {
            // 验证活动状态
            this.verifyStatus(goodsDiscountDTO.getId());

            // 判断是不是全部商品参加
            this.allGoods(goodsDiscountDTO);

            promotionRuleManager.verifyRule(goodsDiscountDTO.getGoodsList(), PromotionTypeEnum.FULL_DISCOUNT,
                    goodsDiscountDTO.getStartTime(), goodsDiscountDTO.getEndTime(), goodsDiscountDTO.getId());

            EsGoodsDiscount goodsDiscount = goodsDiscountMapper.selectById(id);
            EsGoodsDiscountVO goodsDiscountVO = new EsGoodsDiscountVO();
            BeanUtil.copyProperties(goodsDiscountDTO, goodsDiscount);

            BeanUtil.copyProperties(goodsDiscount, goodsDiscountVO);
            goodsDiscountVO.setDiscount(goodsDiscountDTO.getDiscount()/100);
            goodsDiscountVO.setIsDel(0);
            QueryWrapper<EsGoodsDiscount> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsDiscount::getId, id);
            goodsDiscount.setDiscount(goodsDiscountDTO.getDiscount()/100);
            goodsDiscount.setUpdateTime(new Date().getTime());
            this.goodsDiscountMapper.update(goodsDiscount, queryWrapper);
            // 删除之前的活动与商品的对照关系
            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(goodsDiscountDTO.getStartTime());
            detailDTO.setEndTime(goodsDiscountDTO.getEndTime());
            detailDTO.setActivityId(goodsDiscountDTO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.GOODS_DISCOUNT.name());
            detailDTO.setTitle(goodsDiscountDTO.getTitle());
            detailDTO.setShopId(goodsDiscountDTO.getShopId());
            // 将活动商品入库
            this.promotionGoodsService.updatePromotionGoods(goodsDiscountDTO.getGoodsList(), detailDTO);
            jedisCluster.set(PromotionCacheKeys.getGoodsDiscount(goodsDiscountDTO.getId()), JSON.toJSONString(goodsDiscount));
            /*
             * 修改定时任务信息
             */
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "商品打折过期定时任务";
            Long jobId = goodsDiscount.getJobId();
            XXLHttpClient.update(addresses,id,jobId,PromotionTypeEnum.GOODS_DISCOUNT.name(),goodsDiscountDTO.getEndTime(),promotionName,cartPromotionChangeJobHandler);

            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品折扣活动表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品折扣活动表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取商品折扣活动表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    @Override
    public DubboResult<EsGoodsDiscountDO> getGoodsDiscount(Long id) {
        try {
            EsGoodsDiscount goodsDiscount = this.goodsDiscountMapper.selectById(id);
            if (goodsDiscount == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGoodsDiscountDO goodsDiscountDO = new EsGoodsDiscountDO();
            BeanUtil.copyProperties(goodsDiscount, goodsDiscountDO);
            return DubboResult.success(goodsDiscountDO);
        } catch (ArgumentException ae){
            logger.error("商品折扣活动表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品折扣活动表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询商品折扣活动表列表
     *
     * @param goodsDiscountDTO 商品折扣活动表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsDiscountDO>
     */
    @Override
    public DubboPageResult<EsGoodsDiscountDO> getGoodsDiscountList(EsGoodsDiscountDTO goodsDiscountDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsDiscount> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsGoodsDiscount::getShopId,goodsDiscountDTO.getShopId());
            if (StringUtils.isNotEmpty(goodsDiscountDTO.getTitle())){
                queryWrapper.lambda().like(EsGoodsDiscount::getTitle,goodsDiscountDTO.getTitle());
            }
            Page<EsGoodsDiscount> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsDiscount> iPage = this.page(page, queryWrapper);
            List<EsGoodsDiscountDO> goodsDiscountDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsDiscountDOList = iPage.getRecords().stream().map(goodsDiscount -> {
                    EsGoodsDiscountDO goodsDiscountDO = new EsGoodsDiscountDO();
                    BeanUtil.copyProperties(goodsDiscount, goodsDiscountDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < goodsDiscount.getStartTime().longValue() ){
                        goodsDiscountDO.setStatusText("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(goodsDiscount.getStartTime().longValue() < nowTime && nowTime < goodsDiscount.getEndTime() ){
                        goodsDiscountDO.setStatusText("正在进行中");
                    }else{
                        goodsDiscountDO.setStatusText("活动已失效");
                        goodsDiscountDO.setStatus("expired");
                    }
                    return goodsDiscountDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsDiscountDOList);
        } catch (ArgumentException ae){
            logger.error("商品折扣活动表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品折扣活动表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 从缓存里根据id获取商品打折活动id
     *
     * @param id 商品打折活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 16:21:21
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult getGoodsDiscountForCache(Long id) {
        String goodsDiscountForCache = this.jedisCluster.get(PromotionCacheKeys.getGoodsDiscount(id));
        EsGoodsDiscountVO goodsDiscountVO = new EsGoodsDiscountVO();
        if (StringUtils.isBlank(goodsDiscountForCache)){
            DubboResult goodsDiscountResult = this.getGoodsDiscount(id);
            if (!goodsDiscountResult.isSuccess()) {
                return goodsDiscountResult;
            }
            EsGoodsDiscountDO goodsDiscountDO = (EsGoodsDiscountDO) goodsDiscountResult.getData();
            BeanUtils.copyProperties(goodsDiscountDO, goodsDiscountVO);

            return DubboResult.success(goodsDiscountVO);
        }else {
            goodsDiscountVO = JsonUtil.jsonToObject(goodsDiscountForCache,
                    EsGoodsDiscountVO.class);
        }

        return DubboResult.success(goodsDiscountVO);
    }

    @Override
    public DubboResult<EsGoodsDiscountDO> getSellerGoodsDiscount(Long id) {
        try{
            EsGoodsDiscount goodsDiscount = this.goodsDiscountMapper.selectById(id);
            if (goodsDiscount == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGoodsDiscountDO esGoodsDiscountDO = new EsGoodsDiscountDO();
            BeanUtil.copyProperties(goodsDiscount, esGoodsDiscountDO);
            DubboPageResult<EsPromotionGoodsDO> result = this.promotionGoodsService.getPromotionGoods(id,PromotionTypeEnum.GOODS_DISCOUNT.name());
            if(result.isSuccess()){
                List<Long> skuIds = result.getData().getList().stream().map(EsPromotionGoodsDO::getSkuId).collect(Collectors.toList());
                DubboPageResult<EsGoodsSkuDO> goodsResult =   this.esGoodsSkuService.getSkuByIds(skuIds.stream().toArray(Long[]::new));
                if(goodsResult.isSuccess()){
                    List<EsGoodsSkuDO> goodsSkuSelectDOS = goodsResult.getData().getList();
                    List<ESGoodsSkuSelectDO> esGoodsSkuSelectDOS = BeanUtil.copyList(goodsSkuSelectDOS, ESGoodsSkuSelectDO.class);
                    esGoodsDiscountDO.setGoodsList(esGoodsSkuSelectDOS);
                }
            }
            return DubboResult.success(esGoodsDiscountDO);
        } catch (ArgumentException ae) {
            logger.error("单品立减表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("单品立减表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsGoodsDiscountDO> getGoodsDiscountByTime(Long shopId) {
        try {
            long timeMillis = System.currentTimeMillis();
            QueryWrapper<EsGoodsDiscount> wrapper = new QueryWrapper<>();
            wrapper.lambda().lt(EsGoodsDiscount::getStartTime,timeMillis);
            wrapper.lambda().gt(EsGoodsDiscount::getEndTime,timeMillis);
            wrapper.lambda().gt(EsGoodsDiscount::getShopId,shopId);
            EsGoodsDiscount esGoodsDiscount = this.goodsDiscountMapper.selectOne(wrapper);
            EsGoodsDiscountDO esGoodsDiscountDO = new EsGoodsDiscountDO();
            if (esGoodsDiscount != null){
                BeanUtil.copyProperties(esGoodsDiscount, esGoodsDiscountDO);
            }
            return DubboResult.success(esGoodsDiscountDO);
        } catch (ArgumentException ae) {
            logger.error("商品打折查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品打折查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除商品折扣活动表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsDiscount(Long id) {
        try {
            EsGoodsDiscount esGoodsDiscount = goodsDiscountMapper.selectById(id);
            this.goodsDiscountMapper.deleteById(id);
            this.jedisCluster.del(PromotionCacheKeys.getGoodsDiscount(id));
            // 处理该活动商品表对应的 下架状态
            promotionGoodsService.deletePromotionGoods(id,PromotionTypeEnum.GOODS_DISCOUNT.name());

            // 发送活动下架mq
            CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
            cartPromotionChangeMsg.setActivityId(id);
            cartPromotionChangeMsg.setPromotionType(PromotionTypeEnum.GOODS_DISCOUNT.name());

            // 活动下架 删除对应的定时任务数据
            XXLHttpClient.deleteJob(addresses,esGoodsDiscount.getJobId().intValue());
            mqProducer.send(promotion_change_topic,JSON.toJSONString(cartPromotionChangeMsg));
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("商品折扣活动表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品折扣活动表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 验证此活动是否可进行编辑删除操作<br/>
     * 如有问题则抛出异常
     *
     * @param id 活动id
     */
    private void verifyStatus(Long id) {
        EsGoodsDiscount goodsDiscount = this.goodsDiscountMapper.selectById(id);
        long nowTime = System.currentTimeMillis();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if (goodsDiscount.getStartTime() < nowTime && goodsDiscount.getEndTime() > nowTime) {
            throw new ArgumentException(TradeErrorCode.ACTIVE_IS_STARTED.getErrorCode(),
                    TradeErrorCode.ACTIVE_IS_STARTED.getErrorMsg());
        }
    }

    /**
     * 全部商品的场合组装新的商品信息
     *
     * @param goodsDiscountDTO 商品打折活动DTO
     * @author: libw 981087977@qq.com
     * @date: 2019/08/03 15:06:37
     * @return: void
     */
    private void allGoods(EsGoodsDiscountDTO goodsDiscountDTO) {
        // 是否是全部商品参与
        if (goodsDiscountDTO.getRangeType() == 1) {
            List<EsPromotionGoodsDTO> goodsList = new ArrayList<>();
            EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsList.add(goodsDTO);
            goodsDiscountDTO.setGoodsList(goodsList);
        }
    }
}
