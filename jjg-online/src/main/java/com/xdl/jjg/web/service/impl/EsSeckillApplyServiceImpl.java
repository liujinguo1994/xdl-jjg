package com.xdl.jjg.web.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.trade.model.domain.EsSeckillApplyDO;
import com.jjg.trade.model.domain.EsSeckillRangeDO;
import com.jjg.trade.model.domain.EsSeckillTimetableDO;
import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.dto.EsSeckillApplyDTO;
import com.jjg.trade.model.dto.EsSeckillTimelineGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.SeckillGoodsVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsPromotionGoods;
import com.xdl.jjg.entity.EsSeckill;
import com.xdl.jjg.entity.EsSeckillApply;
import com.xdl.jjg.manager.PromotionRuleManager;
import com.xdl.jjg.mapper.EsPromotionGoodsMapper;
import com.xdl.jjg.mapper.EsSeckillApplyMapper;
import com.xdl.jjg.mapper.EsSeckillMapper;
import com.xdl.jjg.redisson.annotation.DistributedLock;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.*;
import com.xdl.jjg.web.service.IEsSeckillApplyService;
import com.xdl.jjg.web.service.IEsSeckillRangeService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import com.xdl.jjg.web.service.job.execute.XXLHttpClient;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
@Service
public class EsSeckillApplyServiceImpl extends ServiceImpl<EsSeckillApplyMapper, EsSeckillApply> implements IEsSeckillApplyService {

    private static Logger logger = LoggerFactory.getLogger(EsSeckillApplyServiceImpl.class);

    private  static final  String  SECKILL_APPLY = "seckill_apply";

    @Autowired
    private EsSeckillApplyMapper seckillApplyMapper;

    @Autowired
    private PromotionRuleManager promotionRuleManager;

    @Autowired
    private EsSeckillMapper seckillMapper;

    @Autowired
    private EsPromotionGoodsMapper esPromotionGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Autowired
    private IEsSeckillApplyService iEsSeckillApplyService;

    @Autowired
    private IEsSeckillRangeService iEsSeckillRangeService;
    @Autowired
    private GoodsService esGoodsService;

    /**
     * 活动报名，以及秒杀商品数据添加
     * @param seckillApplyDTOList DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DistributedLock(value = SECKILL_APPLY,expireSeconds = 60)
    public DubboResult insertSeckillApply(List<EsSeckillApplyDTO> seckillApplyDTOList, Long shopId, Long seckillId) {
        try {
            if (CollectionUtils.isEmpty(seckillApplyDTOList)) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            // 通过 活动id获取到 活动单条活动数据，
            EsSeckill esSeckill = this.seckillMapper.selectById(seckillId);
            // 判断shopids 是否已经报名，
            if (esSeckill == null){
                throw new ArgumentException(TradeErrorCode.SECKILL_NOT_EXIST.getErrorCode(),TradeErrorCode.SECKILL_NOT_EXIST.getErrorMsg());
            }
            String shopIds = esSeckill.getShopIds();

            // 如果未报名 则拼接店铺id数据，
            if (shopIds != null && shopIds.length() != 0){
                String[] split = shopIds.split(",");
                boolean existenceUseList = ArrayUtil.isExistenceUseList(split, String.valueOf(shopId));
                if (!existenceUseList){
                    shopIds = shopIds+","+shopId;
                }
            }else {
                shopIds = String.valueOf(shopId);
            }
            esSeckill.setShopIds(shopIds);
            this.seckillMapper.updateById(esSeckill);
            // 处理秒杀商品数据，

            List<EsPromotionGoodsDTO> goodsList = new ArrayList<>();
            goodsList = seckillApplyDTOList.stream().map(esSeckillApplyDTO -> {
                //判断改商品是否在其他时刻参加
                QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsSeckillApply::getGoodsId,esSeckillApplyDTO.getGoodsId()).
                        eq(EsSeckillApply::getShopId,esSeckillApplyDTO.getShopId()).eq(EsSeckillApply::getSeckillId,seckillId);
                Integer count = seckillApplyMapper.selectCount(queryWrapper);
                if (count > 0){
                    throw new ArgumentException(TradeErrorCode.SECKILL_GOODS_EXIST.getErrorCode(),TradeErrorCode.SECKILL_GOODS_EXIST.getErrorMsg());
                }
                EsPromotionGoodsDTO esPromotionGoodsDTO = new EsPromotionGoodsDTO();
                esPromotionGoodsDTO.setGoodsId(esSeckillApplyDTO.getGoodsId());
                esPromotionGoodsDTO.setGoodsName(esSeckillApplyDTO.getGoodsName());
                esPromotionGoodsDTO.setActivityId(esSeckillApplyDTO.getSeckillId());
                esPromotionGoodsDTO.setPromotionType(PromotionTypeEnum.SECKILL.name());
                esPromotionGoodsDTO.setStartTime(esSeckillApplyDTO.getStartDay());
                esPromotionGoodsDTO.setEndTime(esSeckillApplyDTO.getEndTime());
                esPromotionGoodsDTO.setShopId(esSeckillApplyDTO.getShopId());
                esPromotionGoodsDTO.setSkuId(esSeckillApplyDTO.getSkuId());

                return esPromotionGoodsDTO;
            }).collect(Collectors.toList());

            Long startTime = goodsList.get(0).getStartTime().longValue();

            Long endTime = goodsList.get(0).getEndTime() == null ? esSeckill.getApplyEndTime() :goodsList.get(0).getEndTime().longValue();
            // 验证活动是否冲突
            // 判断该商品是否参加其他活动，除了满减以外，不可同时参加其他活动
            promotionRuleManager.verifyRule(goodsList,PromotionTypeEnum.SECKILL,startTime,endTime,seckillId);

            for (EsSeckillApplyDTO esSeckillApplyDTO: seckillApplyDTOList) {
                EsSeckillApply seckillApply = new EsSeckillApply();
                BeanUtil.copyProperties(esSeckillApplyDTO, seckillApply);
                seckillApply.setSalesNum(0);
                seckillApply.setState(0);
                this.seckillApplyMapper.insert(seckillApply);
            }

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("活动报名失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable t) {
            logger.error("插入失败", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param seckillApplyDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSeckillApply(EsSeckillApplyDTO seckillApplyDTO) {
        try {
            if (seckillApplyDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSeckillApply apply = this.seckillApplyMapper.selectById(seckillApplyDTO.getId());
            if(apply == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "限时抢购活动不存在");
            }
            long dateline = System.currentTimeMillis() / 1000;
            if (apply.getStartDay() < dateline) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "活动开始时间小于当前时间，不能审核");
            }
            if(seckillApplyDTO.getState() == 2 && StringUtils.isEmpty(seckillApplyDTO.getFailReason())){
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "驳回原因不能为空");
            }
            EsSeckillApply seckillApply = new EsSeckillApply();
            BeanUtil.copyProperties(seckillApplyDTO, seckillApply);
            QueryWrapper<EsSeckillApply> updateWrapper = new QueryWrapper<>();
            updateWrapper.lambda().eq(EsSeckillApply::getSeckillId, seckillApplyDTO.getSeckillId())
                        .eq(EsSeckillApply::getGoodsId,seckillApplyDTO.getGoodsId());
            this.seckillApplyMapper.update(seckillApply, updateWrapper);
            // 审核通过，保存商品信息到活动商品表
            long endTime = 0l;
            if(seckillApplyDTO.getState() == 1){
                EsPromotionGoods esPromotionGoods = new EsPromotionGoods();
                esPromotionGoods.setActivityId(apply.getSeckillId());
                esPromotionGoods.setShopId(apply.getShopId());
                esPromotionGoods.setGoodsId(apply.getGoodsId());
                esPromotionGoods.setPromotionType(PromotionTypeEnum.SECKILL.name());
                esPromotionGoods.setTitle("限时抢购");
                esPromotionGoods.setNum(apply.getSoldQuantity());
                esPromotionGoods.setPrice(apply.getMoney());

                //  获取活动的开始时间，获取活动的开始时间点
                // 这里开始时间为日期加时间点，结束时间需要考虑后续场次问题，暂时设置为当天结束
                String day = DateUtil.formatDate(DateUtil.date(apply.getStartDay()));
                DateTime startDate = DateUtil.parse(day + " " + (apply.getTimeLine()<10?"0":"") + apply.getTimeLine() + ":00:00");
                // 获取当前场次的结束时间
                DubboPageResult<EsSeckillRangeDO> dubboPageResult = iEsSeckillRangeService.getSeckillRangeBySeckillId(apply.getSeckillId());
                List<EsSeckillRangeDO> list = dubboPageResult.getData().getList();
                List<EsSeckillRangeDO> collect = list.stream().filter(esSeckillRangeDO -> esSeckillRangeDO.getRangeTime() > apply.getTimeLine()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)){
                    //获取时刻表中第一条时刻最小数据
                    Integer endLine = collect.stream().mapToInt(EsSeckillRangeDO::getRangeTime).min().getAsInt();
                    DateTime parse = DateUtil.parse(day + " " + (endLine < 10 ? "0" : "") + endLine + ":00:00");
                    endTime = parse.getTime();
                    esPromotionGoods.setEndTime(endTime);
                }else {
                    // 当天最后时分秒
                    DateTime parse = DateUtil.parse(day + " " +  "23:59:59");
                    endTime = parse.getTime();
                    esPromotionGoods.setEndTime(endTime);
                }
                esPromotionGoods.setStartTime(startDate.getTime());
                esPromotionGoodsMapper.insert(esPromotionGoods);
            }
            SeckillGoodsVO seckillGoods = new SeckillGoodsVO();
            //获取商品最新数据
            DubboResult<EsGoodsCO> result = esGoodsService.getEsGoods(apply.getGoodsId());
            seckillGoods.setGoodsId(apply.getGoodsId());
            seckillGoods.setGoodsName(apply.getGoodsName());
            seckillGoods.setOriginalPrice(apply.getOriginalPrice());
            if(result.isSuccess()) {
                seckillGoods.setGoodsId(result.getData().getId());
                seckillGoods.setGoodsName(result.getData().getGoodsName());
                // TODO 商品图片
                seckillGoods.setGoodsImage(result.getData().getOriginal());
                seckillGoods.setOriginalPrice(result.getData().getMoney());
            }
            seckillGoods.setSeckillPrice(apply.getMoney());
            seckillGoods.setSoldNum(0);
            seckillGoods.setSoldQuantity(apply.getSoldQuantity());
            seckillGoods.setShopId(apply.getShopId());
            seckillGoods.setSeckillStartTime(apply.getTimeLine());
            // 放入缓存
            iEsSeckillApplyService.addRedis(apply.getStartDay(),apply.getTimeLine(),seckillGoods);
            // 执行器名称
            String cartPromotionChangeJobHandler = "cartPromotionChangeJobHandler";
            // 第三方任务名称
            String promotionName = "秒杀过期定时任务";
            XXLHttpClient.addJob(addresses,seckillApplyDTO.getSeckillId(),PromotionTypeEnum.SECKILL.name(),endTime,promotionName,cartPromotionChangeJobHandler);

            return DubboResult.success();
        }catch (ArgumentException ae) {
            logger.error("更新失败",ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param seckillId
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/30 11:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    @Override
    public DubboResult<EsSeckillApplyDO> getSeckillApply(Long seckillId) {
        try {
            QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckillApply::getSeckillId, seckillId).eq(EsSeckillApply::getState,1);
            List<EsSeckillApply> applyList = this.seckillApplyMapper.selectList(queryWrapper);
            List<EsSeckillApplyDO> seckillApplyDOList = new ArrayList<>();
            if (CollectionUtils.isEmpty(applyList)) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            seckillApplyDOList = applyList.stream().map(seckillApply -> {
                EsSeckillApplyDO seckillApplyDO = new EsSeckillApplyDO();
                BeanUtil.copyProperties(seckillApply, seckillApplyDO);
                return seckillApplyDO;
            }).collect(Collectors.toList());
            return DubboResult.success(seckillApplyDOList);
        }  catch (ArgumentException ae) {
            logger.error("查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据审核状态 秒杀id 查询该活动下的商品查询列表
     *
     * @param seckillApplyDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillApplyDO>
     */
    @Override
    public DubboPageResult<EsSeckillApplyDO> getSeckillApplyList(EsSeckillApplyDTO seckillApplyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsSeckillApply::getState,seckillApplyDTO.getState()).eq(EsSeckillApply::getSeckillId,seckillApplyDTO.getSeckillId());
            Page<EsSeckillApply> page = new Page<>(pageNum, pageSize);
            IPage<EsSeckillApply> iPage = this.page(page, queryWrapper);
            List<EsSeckillApplyDO> seckillApplyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                seckillApplyDOList = iPage.getRecords().stream().map(seckillApply -> {
                    EsSeckillApplyDO seckillApplyDO = new EsSeckillApplyDO();
                    BeanUtil.copyProperties(seckillApply, seckillApplyDO);
                    return seckillApplyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(seckillApplyDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsSeckillApplyDO> getSellerSeckillApplyList(EsSeckillApplyDTO seckillApplyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda()
            .eq(EsSeckillApply::getShopId,seckillApplyDTO.getShopId())
                    .eq(EsSeckillApply::getSeckillId,seckillApplyDTO.getSeckillId());
            Page<EsSeckillApply> page = new Page<>(pageNum, pageSize);
            IPage<EsSeckillApply> iPage = this.page(page, queryWrapper);
            List<EsSeckillApplyDO> seckillApplyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                seckillApplyDOList = iPage.getRecords().stream().map(seckillApply -> {
                    EsSeckillApplyDO seckillApplyDO = new EsSeckillApplyDO();
                    BeanUtil.copyProperties(seckillApply, seckillApplyDO);
                    String stateText = "";
                    if(seckillApply.getState() == 0){
                        stateText ="申请中";
                    }else if(seckillApply.getState() == 1){
                        stateText ="已通过";
                    }else if(seckillApply.getState() == 2){
                        stateText ="已驳回";
                    }
                    seckillApplyDO.setStateText(stateText);
                    return seckillApplyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(seckillApplyDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSeckillApply(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSeckillApply> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSeckillApply::getId, id);
            this.seckillApplyMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据时间查询通过申请的活动
     *
     * @param seckillApplyDTO 数据限时购申请List
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 13:24:17
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    @Override
    public DubboPageResult getSeckillApplyPassList(EsSeckillApplyDTO seckillApplyDTO) {
        try {
            //当天已发布的限时抢购活动
            QueryWrapper<EsSeckill> seckillQrapper = new QueryWrapper<>();
            seckillQrapper.lambda().eq(EsSeckill::getStartDay, seckillApplyDTO.getStartDay());
            seckillQrapper.lambda().eq(EsSeckill::getState, 2);
            List<EsSeckill> seckillList = seckillMapper.selectList(seckillQrapper);
            if(CollectionUtils.isNotEmpty(seckillList)){
                QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsSeckillApply::getStartDay, seckillApplyDTO.getStartDay());
                queryWrapper.lambda().eq(EsSeckillApply::getState, seckillApplyDTO.getState());
                List<Long> seckillIds = seckillList.stream().map(EsSeckill::getId).distinct().collect(Collectors.toList());
                queryWrapper.lambda().in(EsSeckillApply::getSeckillId,seckillIds);

                List<EsSeckillApply> seckillApplyList = this.list(queryWrapper);
                List<EsSeckillApplyDO> seckillApplyDOList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(seckillApplyList)) {
                    seckillApplyDOList = seckillApplyList.stream().map(seckillApply -> {
                        EsSeckillApplyDO seckillApplyDO = new EsSeckillApplyDO();
                        BeanUtil.copyProperties(seckillApply, seckillApplyDO);
                        return seckillApplyDO;
                    }).collect(Collectors.toList());
                }
                return DubboPageResult.success(seckillApplyDOList);
            }
            return DubboPageResult.success(new ArrayList<>());
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public void addRedis(Long startTime, Integer timeLine,SeckillGoodsVO goodsVO) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String nyr = sf.format(startTime);
        //得到活动缓存的key
        String redisKey = PromotionCacheKeys.getSeckillKey(nyr);
        //查询活动商品
        String seckillGoodsCache = (String) this.redisTemplate.opsForValue().getAndSet(redisKey, timeLine.toString());
        // 判断是否为空 如果为空 则保存
        if (StringUtil.isEmpty(seckillGoodsCache)){
            List<SeckillGoodsVO> list = new ArrayList<>();
            SeckillGoodsVO seckillGoods = new SeckillGoodsVO();
            seckillGoods.setGoodsId(goodsVO.getGoodsId());
            seckillGoods.setGoodsName(goodsVO.getGoodsName());
            seckillGoods.setSkuId(goodsVO.getSkuId());
            seckillGoods.setOriginalPrice(goodsVO.getOriginalPrice());
            seckillGoods.setSeckillPrice(goodsVO.getSeckillPrice());
            seckillGoods.setSoldNum(goodsVO.getSoldNum());
            seckillGoods.setSoldQuantity(goodsVO.getSoldQuantity());
            seckillGoods.setGoodsImage(goodsVO.getGoodsImage());
            list.add(seckillGoods);
            String goods = JsonUtil.objectToJson(list);
            this.redisTemplate.opsForHash().put(redisKey,timeLine.toString(),goods);
        }else {
            // 如果不为空则重新获取缓存中的秒杀活动商品 add进去再保存缓存
            List<SeckillGoodsVO> seckillGoodsVOS = JsonUtil.jsonToList(seckillGoodsCache, SeckillGoodsVO.class);
            SeckillGoodsVO seckillGoods = new SeckillGoodsVO();
            seckillGoods.setGoodsId(goodsVO.getGoodsId());
            seckillGoods.setGoodsName(goodsVO.getGoodsName());
            seckillGoods.setSkuId(goodsVO.getSkuId());
            seckillGoods.setOriginalPrice(goodsVO.getOriginalPrice());
            seckillGoods.setSeckillPrice(goodsVO.getSeckillPrice());
            seckillGoods.setSoldNum(goodsVO.getSoldNum());
            seckillGoods.setSoldQuantity(goodsVO.getSoldQuantity());
            seckillGoods.setGoodsImage(goodsVO.getGoodsImage());
            seckillGoodsVOS.add(seckillGoods);
            String goods = JsonUtil.objectToJson(seckillGoodsVOS);
            this.redisTemplate.opsForHash().put(redisKey,timeLine.toString(),goods);
        }
    }

    @Override
    public DubboResult getSeckillApplyByShopId(List<Long> shopIds) {
        try{
            List<Long> seckillApplyDOIdList = new ArrayList<>();
            Long time = new Date().getTime();
            QueryWrapper<EsSeckillApply> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().le(EsSeckillApply::getStartDay, time)
                    .gt(EsSeckillApply::getEndTime, time)
                    .in(EsSeckillApply::getShopId, shopIds);
            List<EsSeckillApply> seckillApplyList = this.list(queryWrapper);
            if(CollectionUtils.isNotEmpty(seckillApplyList)){
                seckillApplyDOIdList = seckillApplyList.stream().map(e -> e.getShopId()).collect(Collectors.toList());
            }
            return  DubboResult.success(seckillApplyDOIdList);
        }catch (ArgumentException ae) {
            logger.error("查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<List<EsSeckillTimetableDO>> getSeckillTimetableToday() {
        long currentTimeMillis = System.currentTimeMillis();
        String today = DateUtil.today();
        List<Integer> temp = new ArrayList<>(); // 临时存储时刻表，用来去重
        List<EsSeckillTimetableDO> timetableDOS = this.baseMapper.selectList(Wrappers.<EsSeckillApply>lambdaQuery()
                .eq(EsSeckillApply::getStartDay, DateUtil.parse(today).getTime())
                .eq(EsSeckillApply::getState, 1)
        ).stream()
                .filter(seckill -> {
                    // 去重
                    boolean notContains = !temp.contains(seckill.getTimeLine());
                    if(notContains) temp.add(seckill.getTimeLine());
                    return notContains;
                })
                .sorted(Comparator.comparingLong(EsSeckillApply::getTimeLine))
                .map(seckill -> {
                    EsSeckillTimetableDO esSeckillTimetableDO = new EsSeckillTimetableDO();
                    esSeckillTimetableDO.setTimeline(seckill.getTimeLine());
                    esSeckillTimetableDO.setDay(DateUtil.formatDate(new Date(seckill.getStartDay())));
                    long seckillTimelineStart = DateUtil.parse(today + " " + (seckill.getTimeLine()<10?"0":"") +seckill.getTimeLine().toString() + ":00:00").getTime();
                    esSeckillTimetableDO.setState(seckillTimelineStart > currentTimeMillis?2:1); // 开始时间大于当前时间-未开始抢购,其他的一定为正在运行（因为已过期的会被筛选掉）
                    return esSeckillTimetableDO;
                }).collect(Collectors.toList());
        // 开始时间段筛选 如当前为4点 设置抢购时间点为1-3-5-7 则需要保留3-5-7
        List<Integer> tempTimeoutTimeline = new ArrayList<>();
        List<Integer> tempTimelineList = timetableDOS.stream().map(EsSeckillTimetableDO::getTimeline).collect(Collectors.toList());
        for (int i = 1; i < tempTimelineList.size(); i++) {
            long seckillTimelineStart = DateUtil.parse(today + " " + (tempTimelineList.get(i)<10?"0":"")  + tempTimelineList.get(i) + ":00:00").getTime();
            // 当前活动时间点小于当前时间，则上一个时间一定已过期
            if(seckillTimelineStart < currentTimeMillis){
                tempTimeoutTimeline.add(tempTimelineList.get(i-1));
            }
        }
        timetableDOS = timetableDOS.stream().filter(s -> !tempTimeoutTimeline.contains(s.getTimeline())).collect(Collectors.toList());
        // 设置秒杀剩余秒数
        for (int i = 1; i < timetableDOS.size(); i++) {
            EsSeckillTimetableDO lastSeckill = timetableDOS.get(i - 1);
            EsSeckillTimetableDO nowSeckill = timetableDOS.get(i);
            // 正在进行
            if(lastSeckill.getState() == 1){
                long seckillTimelineStart = DateUtil.parse(nowSeckill.getDay() + " " + (nowSeckill.getTimeline()<10?"0":"") + nowSeckill.getTimeline() + ":00:00").getTime();
                lastSeckill.setRemainTimestamp(seckillTimelineStart);
            } else {
                // 未开始，设置剩余开始时间
                long seckillTimelineStart = DateUtil.parse(lastSeckill.getDay() + " " + (lastSeckill.getTimeline()<10?"0":"") + lastSeckill.getTimeline() + ":00:00").getTime();
                lastSeckill.setRemainTimestamp(seckillTimelineStart);
            }
        }
        // 设置最后一个时刻的剩余秒数
        if(!timetableDOS.isEmpty()){
            int lastIndex = timetableDOS.size() - 1;
            EsSeckillTimetableDO lastSeckill = timetableDOS.get(lastIndex);
            if(lastSeckill.getState() == 1){// 正在进行（如果最后一个正在进行，说明之前的所有都过期了，也就是说，只有一个时间点）
                long time = DateUtils.addDays(DateUtil.parse(DateUtil.today()), 1).getTime();
                lastSeckill.setRemainTimestamp(time);
            }else{ // 等待开始
                long seckillTimelineStart = DateUtil.parse(lastSeckill.getDay() + " " + (lastSeckill.getTimeline()<10?"0":"") + lastSeckill.getTimeline() + ":00:00").getTime();
                lastSeckill.setRemainTimestamp(seckillTimelineStart);
            }
        }
        return DubboResult.success(timetableDOS);
    }

    @Override
    public DubboPageResult<EsSeckillApplyDO> seckillTimelineGoodsList(EsSeckillTimelineGoodsDTO esSeckillTimelineGoodsDTO, long pageNum, long pageSize) {
        IPage<EsSeckillApply> esSeckillApplyIPage = this.baseMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<EsSeckillApply>lambdaQuery()
                        .eq(EsSeckillApply::getState, 1)
                        .eq(EsSeckillApply::getTimeLine, esSeckillTimelineGoodsDTO.getTimeline())
                        .eq(EsSeckillApply::getStartDay, DateUtil.parse(esSeckillTimelineGoodsDTO.getDay()).getTime())
        );
        esSeckillApplyIPage.getRecords().forEach(seckill ->{
            DubboResult<EsGoodsCO> esGoods = esGoodsService.getEsGoods(seckill.getGoodsId());
            if(esGoods.isSuccess()){
                seckill.setOriginalPrice(esGoods.getData().getMoney());
                seckill.setImage(esGoods.getData().getOriginal());
            }
        });

        return DubboPageResult.success(esSeckillApplyIPage.getTotal(), BeanUtil.copyList(esSeckillApplyIPage.getRecords(),EsSeckillApplyDO.class));
    }

    @Override
    public DubboResult<EsSeckillTimetableDO> getInSeckillNow() {
        DubboResult<List<EsSeckillTimetableDO>> seckillTimetableToday = this.getSeckillTimetableToday();
        if (seckillTimetableToday.isSuccess()) {
            List<EsSeckillTimetableDO> collect = seckillTimetableToday.getData().stream().filter(s -> s.getState() == 1).collect(Collectors.toList());
            if(!collect.isEmpty()){
                return DubboResult.success(collect.get(0));
            }
        }
        return DubboResult.fail(TradeErrorCode.DATA_NOT_EXIST);
    }
}
