package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.trade.model.domain.ESGoodsSkuSelectDO;
import com.jjg.trade.model.domain.EsMinusDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.dto.EsMinusDTO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.EsMinusVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsMinus;
import com.xdl.jjg.mapper.EsMinusMapper;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.plugin.PromotionValid;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsMinusService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
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
 * 单品立减表 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsMinusService.class, timeout = 50000)
public class EsMinusServiceImpl extends ServiceImpl<EsMinusMapper, EsMinus> implements IEsMinusService {

    private static Logger logger = LoggerFactory.getLogger(EsMinusServiceImpl.class);

    @Autowired
    private EsMinusMapper minusMapper;

    @Autowired
    private PromotionRuleManager promotionRuleManager;

    @Autowired
    private IEsPromotionGoodsService promotionGoodsService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsSkuService esGoodsSkuService;
    /**
     * 插入数据
     *
     * @param minusDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMinus(EsMinusDTO minusDTO) {
        try {
            PromotionValid.paramValid(minusDTO.getStartTime(), minusDTO.getEndTime(), minusDTO.getRangeType(),
                    minusDTO.getGoodsList());

            promotionRuleManager.verifyTime(minusDTO.getStartTime(), minusDTO.getEndTime(), PromotionTypeEnum.MINUS,
                    null, minusDTO.getShopId());

            //初步形成商品的DTO列表
            List<EsPromotionGoodsDTO> goodsListDTO = new ArrayList<>();
            //是否是全部商品参与
            if (minusDTO.getRangeType() == 1) {
                EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
                goodsDTO.setGoodsId(-1L);
                goodsDTO.setGoodsName("全部商品");
                goodsDTO.setThumbnail("path");
                goodsListDTO.add(goodsDTO);
                minusDTO.setGoodsList(goodsListDTO);
            }
            //检测活动规则
            promotionRuleManager.verifyRule(minusDTO.getGoodsList(), PromotionTypeEnum.MINUS, minusDTO.getStartTime()
                    , minusDTO.getEndTime(), minusDTO.getId());

            EsMinus minus = new EsMinus();
            BeanUtil.copyProperties(minusDTO, minus);
            minus.setIsDel(0);
            this.minusMapper.insert(minus);

            // 获取活动Id
            Long id = minus.getId();

            EsMinusVO minusVO = new EsMinusVO();
            BeanUtil.copyProperties(minus, minusVO);
            minusVO.setIsDel(0);

            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(minusVO.getStartTime());
            detailDTO.setEndTime(minusVO.getEndTime());
            detailDTO.setActivityId(minusVO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
            detailDTO.setTitle(minusVO.getTitle());
            detailDTO.setShopId(minus.getShopId());

            //将活动商品入库
            DubboResult result = this.promotionGoodsService.insertPromotionGoods(minusDTO.getGoodsList(),
                    detailDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }

            // 将活动放入缓存中
            jedisCluster.set(PromotionCacheKeys.getMinusKey(id), JSON.toJSONString(minusVO));

            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "单品立减过期定时任务";
            String s2 = XXLHttpClient.addJob(addresses,id,PromotionTypeEnum.MINUS.name(),minusVO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            ResponseEntityMsg responseEntity1 = JsonUtil.jsonToObject(s2, ResponseEntityMsg.class);
            // 第三方xxl_job返回的任务ID
            String content = responseEntity1.getContent();
            //  修改当前活动的定时任务关联的主键ID 用于后期活动删除操作
            EsMinus esMinus = minusMapper.selectById(id);
            esMinus.setJobId(Long.valueOf(content));
            minusMapper.updateById(esMinus);

            return DubboResult.success();
        } catch (ArgumentException e){
            return DubboResult.fail(e.getExceptionCode(), e.getMessage());
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param minusDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMinus(EsMinusDTO minusDTO) {
        try {
            // 验证活动状态
            this.verifyStatus(minusDTO.getId());

            // 判断是不是全部商品参加
            this.allGoods(minusDTO);

            promotionRuleManager.verifyRule(minusDTO.getGoodsList(), PromotionTypeEnum.FULL_DISCOUNT,
                    minusDTO.getStartTime(), minusDTO.getEndTime(), minusDTO.getId());
            EsMinus minus = minusMapper.selectById(minusDTO.getId());
            BeanUtil.copyProperties(minusDTO, minus);
            QueryWrapper<EsMinus> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMinus::getId, minusDTO.getId());
            minus.setUpdateTime(new Date().getTime());
            this.minusMapper.update(minus, queryWrapper);

            // 删除之前的活动与商品的对照关系
            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(minusDTO.getStartTime());
            detailDTO.setEndTime(minusDTO.getEndTime());
            detailDTO.setActivityId(minusDTO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
            detailDTO.setTitle(minusDTO.getTitle());
            detailDTO.setShopId(minusDTO.getShopId());
            // 将活动商品入库
            this.promotionGoodsService.updatePromotionGoods(minusDTO.getGoodsList(), detailDTO);
            jedisCluster.set(PromotionCacheKeys.getMinusKey(minusDTO.getId()), JSON.toJSONString(minusDTO));
            /*
             * 修改定时任务信息
             */
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "单品立减过期定时任务";
            Long jobId = minus.getJobId();
            XXLHttpClient.update(addresses,minusDTO.getId(),jobId,PromotionTypeEnum.MINUS.name(),minusDTO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    @Override
    public DubboResult getMinus(Long id) {
        try {
            QueryWrapper<EsMinus> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMinus::getId, id);
            EsMinus minus = this.minusMapper.selectOne(queryWrapper);
            EsMinusDO minusDO = new EsMinusDO();
            if (minus == null) {
                throw new ArgumentException(TradeErrorCode.MINUS_NOT_EXIST.getErrorCode(), TradeErrorCode.MINUS_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(minus, minusDO);
            return DubboResult.success(minusDO);
        } catch (ArgumentException ex) {
            logger.error("单品立减活动查询失败", ex);
            return DubboResult.fail(ex.getExceptionCode(), ex.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param minusDTO DTO
     * @param pageSize 行数
     * @param pageNum  页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMinusDO>
     */
    @Override
    public DubboPageResult getMinusList(EsMinusDTO minusDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMinus> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMinus::getShopId,minusDTO.getShopId());
            if (StringUtils.isNotEmpty(minusDTO.getTitle())){
                queryWrapper.lambda().like(EsMinus::getTitle,minusDTO.getTitle());
            }
            Page<EsMinus> page = new Page<>(pageNum, pageSize);
            IPage<EsMinus> iPage = this.page(page, queryWrapper);
            List<EsMinusDO> minusDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                minusDOList = iPage.getRecords().stream().map(minus -> {
                    EsMinusDO minusDO = new EsMinusDO();
                    BeanUtil.copyProperties(minus, minusDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < minus.getStartTime().longValue() ){
                        minusDO.setStatusText("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(minus.getStartTime().longValue() < nowTime && nowTime < minus.getEndTime() ){
                        minusDO.setStatusText("正在进行中");
                    }else{
                        minusDO.setStatusText("活动已失效");
                        minusDO.setStatus("expired");
                    }
                    return minusDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(minusDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMinus(Long id) {
        try {
            EsMinus esMinus = minusMapper.selectById(id);
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMinus> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMinus::getId, id);
            this.minusMapper.delete(deleteWrapper);
            this.jedisCluster.del(PromotionCacheKeys.getMinusKey(id));
            // 处理该活动商品表对应的 下架状态
            promotionGoodsService.deletePromotionGoods(id,PromotionTypeEnum.MINUS.name());

            // 发送活动下架mq
            CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
            cartPromotionChangeMsg.setActivityId(id);
            cartPromotionChangeMsg.setPromotionType(PromotionTypeEnum.MINUS.name());

            // 活动下架 删除对应的定时任务数据
            XXLHttpClient.deleteJob(addresses,esMinus.getJobId().intValue());
            mqProducer.send(promotion_change_topic,JSON.toJSONString(cartPromotionChangeMsg));

            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 通过活动ID查询活动
     *
     * @param id 活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 15:23:34
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult getMinusForCache(Long id) {
        String minusStr = this.jedisCluster.get(PromotionCacheKeys.getMinusKey(id));
        EsMinusDO minusDO = new EsMinusDO();
        if(!StringUtils.isBlank(minusStr)){
            minusDO =  JsonUtil.jsonToObject(this.jedisCluster.get(PromotionCacheKeys.getMinusKey(id)), EsMinusDO.class);
            return DubboResult.success(minusDO);
        }else{
            EsMinus minus = this.getById(id);
            if(minus == null){
                return DubboResult.success(null);
            }
            BeanUtils.copyProperties(minus, minusDO);
            return DubboResult.success(minusDO);
        }

    }

    @Override
    public DubboResult<EsMinusDO> getSellerMinus(Long id) {
        try{
            EsMinus esMinus = this.minusMapper.selectById(id);
            if (esMinus == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMinusDO esMinusDO = new EsMinusDO();
            BeanUtil.copyProperties(esMinus, esMinusDO);
            DubboPageResult<EsPromotionGoodsDO> result = this.promotionGoodsService.getPromotionGoods(id,PromotionTypeEnum.MINUS.name());
            if(result.isSuccess()){
                List<Long> skuIds = result.getData().getList().stream().map(EsPromotionGoodsDO::getSkuId).collect(Collectors.toList());
                DubboPageResult<EsGoodsSkuDO> goodsResult = this.esGoodsSkuService.getSkuByIds(skuIds.stream().toArray(Long[]::new));
                if(goodsResult.isSuccess()){
                    List<EsGoodsSkuDO> goodsDOS = goodsResult.getData().getList();
                    List<ESGoodsSkuSelectDO> esGoodsSkuSelectDOS = BeanUtil.copyList(goodsDOS, ESGoodsSkuSelectDO.class);
                    esMinusDO.setGoodsList(esGoodsSkuSelectDOS);
                }
            }
            return DubboResult.success(esMinusDO);
        } catch (ArgumentException ae) {
            logger.error("单品立减表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("单品立减表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsMinusDO> getMinusByTime(Long shopId) {
        try {
            long timeMillis = System.currentTimeMillis();
            QueryWrapper<EsMinus> wrapper = new QueryWrapper<>();
            wrapper.lambda().lt(EsMinus::getStartTime,timeMillis);
            wrapper.lambda().gt(EsMinus::getEndTime,timeMillis);
            wrapper.lambda().eq(EsMinus::getShopId,shopId);
            EsMinus esMinus = this.minusMapper.selectOne(wrapper);
            EsMinusDO minusDO = new EsMinusDO();
            if (esMinus != null){
                BeanUtil.copyProperties(esMinus, minusDO);
            }
            return DubboResult.success(minusDO);
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
        EsMinus minus = this.minusMapper.selectById(id);
        long nowTime = System.currentTimeMillis();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if (minus.getStartTime() < nowTime && minus.getEndTime() > nowTime) {
            throw new ArgumentException(TradeErrorCode.ACTIVE_IS_STARTED.getErrorCode(),
                    TradeErrorCode.ACTIVE_IS_STARTED.getErrorMsg());
        }
    }

    /**
     * 全部商品的场合组装新的商品信息
     *
     * @param minusDTO 单品立减活动DTO
     * @author: libw 981087977@qq.com
     * @date: 2019/08/03 15:06:37
     * @return: void
     */
    private void allGoods(EsMinusDTO minusDTO) {
        // 是否是全部商品参与
        if (minusDTO.getRangeType() == 1) {
            List<EsPromotionGoodsDTO> goodsList = new ArrayList<>();
            EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsList.add(goodsDTO);
            minusDTO.setGoodsList(goodsList);
        }
    }
}
