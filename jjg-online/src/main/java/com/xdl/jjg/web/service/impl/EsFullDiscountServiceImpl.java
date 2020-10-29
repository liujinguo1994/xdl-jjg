package com.xdl.jjg.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.trade.model.domain.ESGoodsSelectDO;
import com.jjg.trade.model.domain.EsFullDiscountDO;
import com.jjg.trade.model.domain.EsPromotionGoodsDO;
import com.jjg.trade.model.dto.EsFullDiscountDTO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.EsFullDiscountVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsFullDiscount;
import com.xdl.jjg.manager.PromotionRuleManager;
import com.xdl.jjg.mapper.EsFullDiscountMapper;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.plugin.PromotionValid;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsFullDiscountService;
import com.xdl.jjg.web.service.IEsPromotionGoodsService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
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
 * 满减满赠表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsFullDiscountService.class, timeout = 50000)
public class EsFullDiscountServiceImpl extends ServiceImpl<EsFullDiscountMapper, EsFullDiscount> implements IEsFullDiscountService {

    private static Logger logger = LoggerFactory.getLogger(EsFullDiscountServiceImpl.class);

    @Autowired
    private EsFullDiscountMapper fullDiscountMapper;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private PromotionRuleManager promotionRuleManager;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    @Autowired
    private IEsPromotionGoodsService promotionGoodsService;

    @Autowired
    private GoodsService esGoodsService;

    @Value("${xxl.job.admin.addresses}")
    private String addresses;
    /**
     * 插入满减满赠表数据
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertFullDiscount(EsFullDiscountDTO fullDiscountDTO) {
        try {
            verifyFullDiscountParam(fullDiscountDTO);

            // 校验说动时间是否冲突
            promotionRuleManager.verifyTime(fullDiscountDTO.getStartTime(),fullDiscountDTO.getEndTime(),
                    PromotionTypeEnum.FULL_DISCOUNT,null, fullDiscountDTO.getShopId());

            // 判断是不是全部商品参加
            this.allGoods(fullDiscountDTO);

            // 校验活动参与规则
            promotionRuleManager.verifyRule(fullDiscountDTO.getGoodsList(), PromotionTypeEnum.FULL_DISCOUNT,
                    fullDiscountDTO.getStartTime(), fullDiscountDTO.getEndTime(), fullDiscountDTO.getId());

            EsFullDiscount fullDiscount = new EsFullDiscount();
            BeanUtils.copyProperties(fullDiscountDTO, fullDiscount);
            fullDiscount.setIsDel(0);
            fullDiscountMapper.insert(fullDiscount);

            // 获取活动Id
            Long id = fullDiscount.getId();

            EsFullDiscountVO fullDiscountVO = new EsFullDiscountVO();
            BeanUtil.copyProperties(fullDiscount, fullDiscountVO);

            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(fullDiscountVO.getStartTime());
            detailDTO.setEndTime(fullDiscountVO.getEndTime());
            detailDTO.setActivityId(fullDiscountVO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.FULL_DISCOUNT.name());
            detailDTO.setTitle(fullDiscountVO.getTitle());
            detailDTO.setShopId(fullDiscountVO.getShopId());

            //将活动商品入库
            DubboResult result = this.promotionGoodsService.insertPromotionGoods(fullDiscountDTO.getGoodsList(),
                    detailDTO);
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            }
            fullDiscountVO.setIsDel(0);
            // 将活动放入缓存中
            jedisCluster.set(PromotionCacheKeys.getFullDiscountKey(id), JSON.toJSONString(fullDiscountVO));
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "满减活动过期定时任务";
            String s2 = XXLHttpClient.addJob(addresses,id,PromotionTypeEnum.FULL_DISCOUNT.name(),fullDiscountVO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            ResponseEntityMsg responseEntity1 = JsonUtil.jsonToObject(s2, ResponseEntityMsg.class);
            // 第三方xxl_job返回的任务ID
            String content = responseEntity1.getContent();
            //  修改当前活动的定时任务关联的主键ID 用于后期活动删除操作
            EsFullDiscount esFullDiscount = fullDiscountMapper.selectById(id);
            esFullDiscount.setJobId(Long.valueOf(content));
            fullDiscountMapper.updateById(esFullDiscount);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("满减满赠表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("满减满赠表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新满减满赠表数据
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @param id              主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateFullDiscount(EsFullDiscountDTO fullDiscountDTO, Long id) {
        try {
            // 验证活动参数
            this.verifyFullDiscountParam(fullDiscountDTO);
            // 验证活动状态
            this.verifyStatus(id);

            // 判断是不是全部商品参加
            this.allGoods(fullDiscountDTO);

            promotionRuleManager.verifyRule(fullDiscountDTO.getGoodsList(), PromotionTypeEnum.FULL_DISCOUNT,
                    fullDiscountDTO.getStartTime(), fullDiscountDTO.getEndTime(), id);

            EsFullDiscount fullDiscount = fullDiscountMapper.selectById(id);
            BeanUtils.copyProperties(fullDiscountDTO, fullDiscount);

            // 更新活动信息
            QueryWrapper<EsFullDiscount> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFullDiscount::getId, id);
            fullDiscount.setUpdateTime(new Date().getTime());
            fullDiscount.setIsDel(0);
            this.fullDiscountMapper.update(fullDiscount, queryWrapper);

            // 删除之前的活动与商品的对照关系
            EsPromotionGoodsDTO detailDTO = new EsPromotionGoodsDTO();
            detailDTO.setStartTime(fullDiscountDTO.getStartTime());
            detailDTO.setEndTime(fullDiscountDTO.getEndTime());
            detailDTO.setActivityId(fullDiscountDTO.getId());
            detailDTO.setPromotionType(PromotionTypeEnum.FULL_DISCOUNT.name());
            detailDTO.setTitle(fullDiscountDTO.getTitle());
            detailDTO.setShopId(fullDiscountDTO.getShopId());
            // 将活动商品入库
            fullDiscountDTO.setIsDel(0);
            this.promotionGoodsService.updatePromotionGoods(fullDiscountDTO.getGoodsList(), detailDTO);
            jedisCluster.set(PromotionCacheKeys.getFullDiscountKey(id), JSON.toJSONString(fullDiscountDTO));
            /*
             * 修改定时任务信息
             */
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "满减活动过期定时任务";
            Long jobId = fullDiscount.getJobId();
            XXLHttpClient.update(addresses,id,jobId,PromotionTypeEnum.FULL_DISCOUNT.name(),fullDiscountDTO.getEndTime(),promotionName,cartPromotionChangeJobHandler);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("满减满赠表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取满减满赠表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    @Override
    public DubboResult<EsFullDiscountDO> getFullDiscount(Long id) {
        try {
            EsFullDiscount fullDiscount = this.fullDiscountMapper.selectById(id);
            if (fullDiscount == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "满减活动不存在！");
            }
            EsFullDiscountDO fullDiscountDO = new EsFullDiscountDO();
            BeanUtil.copyProperties(fullDiscount, fullDiscountDO);
            return DubboResult.success(fullDiscountDO);
        } catch (ArgumentException ae) {
            logger.error("满减满赠表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询满减满赠表列表
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFullDiscountDO>
     */
    @Override
    public DubboPageResult<EsFullDiscountDO> getFullDiscountList(EsFullDiscountDTO fullDiscountDTO, int pageSize, int pageNum) {
        QueryWrapper<EsFullDiscount> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda()
                    .eq(EsFullDiscount::getShopId,fullDiscountDTO.getShopId());
            if (StringUtils.isNotEmpty(fullDiscountDTO.getTitle())){
                queryWrapper.lambda().like(EsFullDiscount::getTitle,fullDiscountDTO.getTitle());
            }
            Page<EsFullDiscount> page = new Page<>(pageNum, pageSize);
            IPage<EsFullDiscount> iPage = this.page(page, queryWrapper);
            List<EsFullDiscountDO> fullDiscountDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                fullDiscountDOList = iPage.getRecords().stream().map(fullDiscount -> {
                    EsFullDiscountDO fullDiscountDO = new EsFullDiscountDO();
                    BeanUtil.copyProperties(fullDiscount, fullDiscountDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < fullDiscount.getStartTime().longValue() ){
                        fullDiscountDO.setStatusText("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(fullDiscount.getStartTime().longValue() < nowTime && nowTime < fullDiscount.getEndTime() ){
                        fullDiscountDO.setStatusText("正在进行中");
                    }else{
                        fullDiscountDO.setStatusText("活动已失效");
                        fullDiscountDO.setStatus("expired");
                    }
                    return fullDiscountDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(fullDiscountDOList);
        } catch (ArgumentException ae) {
            logger.error("满减满赠表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除满减满赠表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteFullDiscount(Long id) {
        try {
            EsFullDiscount esFullDiscount = fullDiscountMapper.selectById(id);
            this.fullDiscountMapper.deleteById(id);
            this.jedisCluster.del(PromotionCacheKeys.getFullDiscountKey(id));
            // 处理该活动商品表对应的 下架状态
            promotionGoodsService.deletePromotionGoods(id,PromotionTypeEnum.FULL_DISCOUNT.name());

            // 发送活动下架mq
            CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
            cartPromotionChangeMsg.setActivityId(id);
            cartPromotionChangeMsg.setPromotionType(PromotionTypeEnum.FULL_DISCOUNT.name());

            // 活动下架 删除对应的定时任务数据
            XXLHttpClient.deleteJob(addresses,esFullDiscount.getJobId().intValue());
            mqProducer.send(promotion_change_topic,JSON.toJSONString(cartPromotionChangeMsg));
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("满减满赠表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据活动id获取满减活动缓存
     *
     * @param id 活动ID
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 16:57:03
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public DubboResult getFullDiscountForCache(Long id) {
       String fullDiscountStr =  this.jedisCluster.get(PromotionCacheKeys.getFullDiscountKey(id));
        EsFullDiscountDO fullDiscountDO = new EsFullDiscountDO();
       if(!StringUtils.isBlank(fullDiscountStr)){
           fullDiscountDO = JsonUtil.jsonToObject(fullDiscountStr, EsFullDiscountDO.class);
           // 判断是够过期
           if (fullDiscountDO.getEndTime()<System.currentTimeMillis()){
               return DubboResult.success(null);
           }
           return DubboResult.success(fullDiscountDO);
       }else{
           EsFullDiscount esFullDiscount = this.fullDiscountMapper.selectById(id);
           if(esFullDiscount==null){
               return DubboResult.success(null);
           }
           BeanUtils.copyProperties(esFullDiscount, fullDiscountDO);
           return DubboResult.success(fullDiscountDO);
       }

    }

    @Override
    public DubboResult<EsFullDiscountDO> getSellerFullDiscount(Long id) {
        try{
            EsFullDiscount fullDiscount = this.fullDiscountMapper.selectById(id);
            if (fullDiscount == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsFullDiscountDO fullDiscountDO = new EsFullDiscountDO();
            BeanUtil.copyProperties(fullDiscount, fullDiscountDO);
            DubboPageResult<EsPromotionGoodsDO> result = this.promotionGoodsService.getPromotionGoods(id,PromotionTypeEnum.FULL_DISCOUNT.name());
            if(result.isSuccess()){
                List<Long> goodsIds = result.getData().getList().stream().map(EsPromotionGoodsDO::getGoodsId).collect(Collectors.toList());
                DubboPageResult<EsGoodsDO> goodsResult =   this.esGoodsService.getEsGoods(goodsIds.stream().toArray(Long[]::new));
                if(goodsResult.isSuccess()){
                    List<EsGoodsDO> goodsSelectDOS = goodsResult.getData().getList();
                    List<ESGoodsSelectDO> goodsSelectDOList = goodsSelectDOS.stream().map(esGoodsDO -> {
                        ESGoodsSelectDO esGoodsSelectDO = new ESGoodsSelectDO();
                        BeanUtil.copyProperties(esGoodsDO,esGoodsSelectDO);
                        esGoodsSelectDO.setGoodsId(esGoodsDO.getId());
                        esGoodsSelectDO.setOriginal(esGoodsDO.getOriginal());
                        return esGoodsSelectDO;
                    }).collect(Collectors.toList());
                    fullDiscountDO.setGoodsList(goodsSelectDOList);
                }
            }
            return DubboResult.success(fullDiscountDO);
        } catch (ArgumentException ae) {
            logger.error("满减满赠表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsFullDiscountDO> getFullDiscountByTime(Long shopId) {
        try {
            long timeMillis = System.currentTimeMillis();
            QueryWrapper<EsFullDiscount> wrapper = new QueryWrapper<>();
            wrapper.lambda().lt(EsFullDiscount::getStartTime,timeMillis);
            wrapper.lambda().gt(EsFullDiscount::getEndTime,timeMillis);
            wrapper.lambda().eq(EsFullDiscount::getShopId,shopId);
            EsFullDiscount fullDiscount;
            fullDiscount = this.fullDiscountMapper.selectOne(wrapper);
            EsFullDiscountDO fullDiscountDO = new EsFullDiscountDO();
            if (fullDiscount != null){
                BeanUtil.copyProperties(fullDiscount, fullDiscountDO);
            }
            return DubboResult.success(fullDiscountDO);
        } catch (ArgumentException ae) {
            logger.error("满减满赠表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("满减满赠表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 验证满优惠活动的参数信息
     *
     * @param fullDiscountDTO 活动参数校验
     */
    private void verifyFullDiscountParam(EsFullDiscountDTO fullDiscountDTO) {
        PromotionValid promotionValid = new PromotionValid();

        PromotionValid.paramValid(fullDiscountDTO.getStartTime(), fullDiscountDTO.getEndTime(),
                fullDiscountDTO.getRangeType(), fullDiscountDTO.getGoodsList());

        // 促销活动的优惠方式不能全部为空，至少要选择一项
        if (fullDiscountDTO.getIsFullMinus() == null && fullDiscountDTO.getIsFreeShip() == null && fullDiscountDTO.getIsSendGift() == null
                && fullDiscountDTO.getIsSendBonus() == null && fullDiscountDTO.getIsDiscount() == null) {

            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请选择优惠方式");
        }

        // 如果促销活动优惠详细是否包含满减不为空
        if (fullDiscountDTO.getIsFullMinus() != null && fullDiscountDTO.getIsFullMinus() == 1) {

            if (fullDiscountDTO.getMinusValue() == null || fullDiscountDTO.getMinusValue() == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请填写减多少元");
            }

            // 减少的现金必须小于优惠门槛
            if (fullDiscountDTO.getMinusValue() > fullDiscountDTO.getFullMoney()) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "减少的金额不能大于门槛金额");
            }
        }

        // 如果促销活动优惠详细是否包含满送赠品不为空
        if (fullDiscountDTO.getIsSendGift() != null && fullDiscountDTO.getIsSendGift() == 1) {
            // 赠品id不能为0
            if (fullDiscountDTO.getGiftId() == null || fullDiscountDTO.getGiftId() == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请选择赠品");
            }
        }

        // 如果促销活动优惠详细是否包含满送优惠券不为空
        if (fullDiscountDTO.getIsSendBonus() != null && fullDiscountDTO.getIsSendBonus() == 1) {
            // 优惠券id不能为0
            if (fullDiscountDTO.getBonusId() == null || fullDiscountDTO.getBonusId() == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请选择优惠券");
            }
        }

        // 如果促销活动优惠详细是否包含打折不为空
        if (fullDiscountDTO.getIsDiscount() != null && fullDiscountDTO.getIsDiscount() == 1) {
            // 打折的数值不能为空也不能为0
            if (fullDiscountDTO.getDiscountValue() == null || fullDiscountDTO.getDiscountValue() == 0) {

                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "请填写打折数值");
            }
            // 打折的数值应介于0-10之间
            if (fullDiscountDTO.getDiscountValue() >= 10 || fullDiscountDTO.getDiscountValue() <= 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "打折的数值应介于0到10之间");
            }
        }
    }

    /**
     * 验证此活动是否可进行编辑删除操作<br/>
     * 如有问题则抛出异常
     *
     * @param id 活动id
     */
    private void verifyStatus(Long id) {
        EsFullDiscount fullDiscount = this.fullDiscountMapper.selectById(id);
        long nowTime = System.currentTimeMillis();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if (fullDiscount.getStartTime() < nowTime && fullDiscount.getEndTime() > nowTime) {
            throw new ArgumentException(TradeErrorCode.ACTIVE_IS_STARTED.getErrorCode(),
                    TradeErrorCode.ACTIVE_IS_STARTED.getErrorMsg());
        }
    }

    /**
     * 全部商品的场合组装新的商品信息
     *
     * @param fullDiscountDTO 满减活动DTO
     * @author: libw 981087977@qq.com
     * @date: 2019/08/03 15:06:37
     * @return: void
     */
    private void allGoods(EsFullDiscountDTO fullDiscountDTO) {
        // 是否是全部商品参与
        if (fullDiscountDTO.getRangeType() == 1) {
            List<EsPromotionGoodsDTO> goodsList = new ArrayList<>();
            EsPromotionGoodsDTO goodsDTO = new EsPromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsList.add(goodsDTO);
            fullDiscountDTO.setGoodsList(goodsList);
        }
    }
}
