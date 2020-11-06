package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.jjg.trade.model.domain.EsSeckillDO;
import com.jjg.trade.model.domain.EsSeckillTimetableDO;
import com.jjg.trade.model.dto.EsSeckillDTO;
import com.jjg.trade.model.vo.SeckillGoodsVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.constant.cacheprefix.PromotionCacheKeys;
import com.xdl.jjg.entity.EsSeckill;
import com.xdl.jjg.entity.EsSeckillRange;
import com.xdl.jjg.mapper.EsSeckillApplyMapper;
import com.xdl.jjg.mapper.EsSeckillMapper;
import com.xdl.jjg.mapper.EsSeckillRangeMapper;
import com.xdl.jjg.redisson.annotation.DistributedLock;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.ArrayUtil;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.DateUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsSeckillApplyService;
import com.xdl.jjg.web.service.IEsSeckillService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class EsSeckillServiceImpl extends ServiceImpl<EsSeckillMapper, EsSeckill> implements IEsSeckillService {

    private static Logger logger = LoggerFactory.getLogger(EsSeckillServiceImpl.class);

    @Autowired
    private EsSeckillMapper seckillMapper;

    @Autowired
    private EsSeckillRangeMapper esSeckillRangeMapper;

    @Autowired
    private IEsSeckillApplyService esSeckillApplyService;

    @Autowired
    private EsSeckillApplyMapper seckillApplyMapper;

    @Autowired
    private JedisCluster jedisCluster;

    //秒杀活动库存
    private  static final  String  SECKILL_KC = "seckill_kc";


    /**
     * 添加活动
     * @param seckillDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/29 15:23:30
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSeckill(EsSeckillDTO seckillDTO) {
        try {
            if (seckillDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            DubboResult dubboResult = this.checkSeckilDate(seckillDTO);
            if (!dubboResult.isSuccess()){
                throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
            }
            EsSeckill seckill = new EsSeckill();
            BeanUtil.copyProperties(seckillDTO, seckill);
            this.seckillMapper.insert(seckill);
            //保存秒杀时间点
            List<Integer> rangeList = seckillDTO.getRangeList();
            rangeList.forEach(integer -> {
                EsSeckillRange esSeckillRange = new EsSeckillRange();
                esSeckillRange.setSeckillId(seckill.getId());
                esSeckillRange.setRangeTime(integer);
                this.esSeckillRangeMapper.insert(esSeckillRange);
            });
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("添加失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public DubboResult checkSeckilDate(EsSeckillDTO seckillDTO) throws ParseException {
        try {
            Long startDay = seckillDTO.getStartDay();
            QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSeckill::getState,1,2);
            queryWrapper.lambda().le(EsSeckill::getStartDay,startDay).ge(EsSeckill::getStartDay,startDay);
            // 验证活动在同一时间内是否存在冲突。
            Integer num = this.seckillMapper.selectCount(queryWrapper);
            if(num > 0){
                throw new ArgumentException(TradeErrorCode.PROMOTION_EXIT_NOW.getErrorCode(),TradeErrorCode.PROMOTION_EXIT_NOW.getErrorMsg());
            }
            // 不可添加当天以及之前的时间点
            Long aLong = weeHours(new Date(), 1);
            if (startDay <= aLong){
                throw new ArgumentException(TradeErrorCode.CAN_NOT_ADD_THE_DAY.getErrorCode(),TradeErrorCode.CAN_NOT_ADD_THE_DAY.getErrorMsg());
            }
            return DubboResult.success();
        } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        }

        // TODO 限时抢购的抢购阶段表示的是从这个时间起，到活动时间的24点，不能取区间，例如，不支持10:00-11:00时间段。
    }

    /**
     * flag 0 获取当天开始时间
     * flag 1 获取当天结束时间
     * @param date
     * @param flag
     * @return
     */
    public static Long weeHours(Date date, int flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);

        if (flag == 0) {
            return cal.getTime().getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);

        }
        return cal.getTime().getTime();
    }
    /**
     * 根据条件更新数据
     *
     * @param seckillDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSeckill(EsSeckillDTO seckillDTO) {
        try {
            if (seckillDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
//            DubboResult dubboResult = this.checkSeckilDate(seckillDTO);
//            if (!dubboResult.isSuccess()){
//                throw new ArgumentException(dubboResult.getCode(),dubboResult.getMsg());
//            }
            EsSeckill seckill = new EsSeckill();
            BeanUtil.copyProperties(seckillDTO, seckill);
            QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckill::getId, seckillDTO.getId());
            QueryWrapper<EsSeckillRange> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(EsSeckillRange::getSeckillId,seckillDTO.getId());
            this.esSeckillRangeMapper.delete(queryWrapper2);
            //保存秒杀时间点
            List<Integer> rangeList = seckillDTO.getRangeList();
            rangeList.forEach(integer -> {
                EsSeckillRange esSeckillRange = new EsSeckillRange();
                esSeckillRange.setSeckillId(seckill.getId());
                esSeckillRange.setRangeTime(integer);
                this.esSeckillRangeMapper.insert(esSeckillRange);
            });

            this.seckillMapper.update(seckill, queryWrapper);
            return DubboResult.success();
        }  catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }   catch (Throwable th) {
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
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @Override
    public DubboResult<EsSeckillDO> getSeckill(Long id) {
        try {
            QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckill::getId, id);
            EsSeckill seckill = this.seckillMapper.selectOne(queryWrapper);
            // 查询 时刻
            QueryWrapper<EsSeckillRange> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsSeckillRange::getSeckillId,id);
            List<EsSeckillRange> rangeList = this.esSeckillRangeMapper.selectList(queryWrapper1);
            List<Integer> collect = rangeList.stream().filter(
                    esSeckillRange -> esSeckillRange.getSeckillId().equals(esSeckillRange.getSeckillId()))
                    .map(EsSeckillRange::getRangeTime).collect(Collectors.toList());
            EsSeckillDO seckillDO = new EsSeckillDO();
            if (seckill == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(seckill, seckillDO);
            seckillDO.setRangeList(collect);
            return DubboResult.success(seckillDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     * 系统后台
     * @param seckillDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillDO>
     */
    @Override
    public DubboPageResult<EsSeckillDO> getSeckillList(EsSeckillDTO seckillDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if(seckillDTO.getSeckillName() != null){
                queryWrapper.lambda().like(EsSeckill::getSeckillName,seckillDTO.getSeckillName());
            }
            Page<EsSeckill> page = new Page<>(pageNum, pageSize);
            IPage<EsSeckill> iPage = this.page(page, queryWrapper);
            List<EsSeckillDO> seckillDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                seckillDOList = iPage.getRecords().stream().map(seckill -> {
                    EsSeckillDO seckillDO = new EsSeckillDO();
                    BeanUtil.copyProperties(seckill, seckillDO);

                    String applyEndTime = DateUtils.format( new Date(seckill.getApplyEndTime()),DateUtils.DATE_PATTERN)+" 23:59:59";
                    if(new Date().getTime() > DateUtils.parseDate(applyEndTime).getTime()){
                        seckillDO.setState(3);
                    }
                    return seckillDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),seckillDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 卖家端
     * 根据查询条件查询限时抢购活动列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/08/23 13:42:53
     * @param seckillDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillDO>
     */
    @Override
    public DubboPageResult<EsSeckillDO> getSellerSeckillList(EsSeckillDTO seckillDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if(seckillDTO.getSeckillName() != null){
                queryWrapper.lambda().like(EsSeckill::getSeckillName,seckillDTO.getSeckillName());
            }
            if(seckillDTO.getState() != null){
                queryWrapper.lambda().like(EsSeckill::getState,seckillDTO.getState());
            }
            // 当前时间
            long timeMillis = System.currentTimeMillis();
            Page<EsSeckill> page = new Page<>(pageNum, pageSize);
            IPage<EsSeckill> iPage = this.page(page, queryWrapper);
            List<EsSeckillDO> seckillDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                seckillDOList = iPage.getRecords().stream().map(seckill -> {

                    EsSeckillDO seckillDO = new EsSeckillDO();
                    BeanUtil.copyProperties(seckill, seckillDO);
                    if (timeMillis > seckill.getApplyEndTime()){
                        seckillDO.setApplyStatus("已截止");
                    }else{
                        // 报名状态
                        String shopIds = seckill.getShopIds();
                        String[] split ={};
                        if(StringUtils.isNotEmpty(shopIds)){
                            split = shopIds.split(",");
                        }

                        boolean existenceUseSet = ArrayUtil.isExistenceUseSet(split, String.valueOf(seckillDTO.getShopIds()));
                        if (existenceUseSet){
                            seckillDO.setApplyStatus("已报名");
                        }else {
                            seckillDO.setApplyStatus("未报名");
                        }
                    }
                    return seckillDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),seckillDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSeckill(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSeckill> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSeckill::getId, id);
            this.seckillMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsSeckillDO> unloadSeckill(Long id) {
        try {
            QueryWrapper<EsSeckill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckill::getId, id);
            EsSeckill esSeckill = this.seckillMapper.selectById(id);
            if(esSeckill == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            //已下架修改成 已关闭状态
            esSeckill.setState(4);
            this.seckillMapper.updateById(esSeckill);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @DistributedLock(value = SECKILL_KC,expireSeconds = 60)
    public DubboResult addSoldNum(List<EsGoodsSkuQuantityDTO> quantityDTO) {
        try {
            String redisKey = PromotionCacheKeys.getSeckillKey(toString(System.currentTimeMillis() / 1000, "yyyyMMdd"));
            // 获取当前场次
            int timeline = -1;
            DubboResult<EsSeckillTimetableDO> inSeckillNow = esSeckillApplyService.getInSeckillNow();
            if (inSeckillNow.isSuccess()) {
                timeline = inSeckillNow.getData().getTimeline();
            }
            String hget = this.jedisCluster.hget(redisKey, timeline + "");
            List<SeckillGoodsVO> seckillGoodsVOS = JsonUtil.jsonToList(hget, SeckillGoodsVO.class);
            for (EsGoodsSkuQuantityDTO esGoodsSkuQuantityDTO : quantityDTO) {
                for (SeckillGoodsVO seckillGoodsVO : seckillGoodsVOS) {
                    if(seckillGoodsVO.getGoodsId().longValue() == esGoodsSkuQuantityDTO.getGoodsId().longValue()){
                        Integer soldNum = seckillGoodsVO.getSoldNum();
                        soldNum = soldNum==null?0:soldNum;
                        soldNum = soldNum+esGoodsSkuQuantityDTO.getGoodsNumber();
                        soldNum = soldNum>seckillGoodsVO.getSoldQuantity()?seckillGoodsVO.getSoldQuantity():soldNum;
                        seckillGoodsVO.setSoldNum(soldNum);
                        Integer remainQuantity = seckillGoodsVO.getRemainQuantity();
                        remainQuantity = remainQuantity == null?seckillGoodsVO.getSoldQuantity():remainQuantity;
                        remainQuantity = remainQuantity - esGoodsSkuQuantityDTO.getGoodsNumber();
                        remainQuantity = remainQuantity<0?0:remainQuantity;
                        seckillGoodsVO.setRemainQuantity(remainQuantity);
                        seckillApplyMapper.updateGoodsSkuQuantity(esGoodsSkuQuantityDTO.getGoodsNumber(),esGoodsSkuQuantityDTO.getGoodsId(),esGoodsSkuQuantityDTO.getActivityId());
                    }
                }
            }
            this.jedisCluster.hset(redisKey,timeline+"",JsonUtil.objectToJson(seckillGoodsVOS));
            return DubboResult.success();
        } catch (Exception e) {
            logger.error("秒杀活动表库存扣减失败", e);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 把日期转换成字符串型
     *
     * @param date
     * @param pattern
     * @return
     */
    public String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

    public String toString(Long time, String pattern) {
        if (time > 0) {
            if (time.toString().length() == 10) {
                time = time * 1000;
            }
            Date date = new Date(time);
            String str = toString(date, pattern);
            return str;
        }
        return "";
    }
}
