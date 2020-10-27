package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsSeckillRangeDO;
import com.jjg.trade.model.dto.EsSeckillRangeDTO;
import com.jjg.trade.model.vo.TimeLineVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsSeckillRange;
import com.xdl.jjg.mapper.EsSeckillRangeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSeckillRangeService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsSeckillRangeService.class, timeout = 50000)
public class EsSeckillRangeServiceImpl extends ServiceImpl<EsSeckillRangeMapper, EsSeckillRange> implements IEsSeckillRangeService {

    private static Logger logger = LoggerFactory.getLogger(EsSeckillRangeServiceImpl.class);

    @Autowired
    private EsSeckillRangeMapper seckillRangeMapper;

    /**
     * 插入数据
     *
     * @param seckillRangeDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSeckillRange(EsSeckillRangeDTO seckillRangeDTO) {
        try {
            if (seckillRangeDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSeckillRange seckillRange = new EsSeckillRange();
            BeanUtil.copyProperties(seckillRangeDTO, seckillRange);
            this.seckillRangeMapper.insert(seckillRange);
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
     * @param seckillRangeDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSeckillRange(EsSeckillRangeDTO seckillRangeDTO) {
        try {
            if (seckillRangeDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSeckillRange seckillRange = new EsSeckillRange();
            BeanUtil.copyProperties(seckillRangeDTO, seckillRange);
            QueryWrapper<EsSeckillRange> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckillRange::getId, seckillRangeDTO.getId());
            this.seckillRangeMapper.update(seckillRange, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    @Override
    public DubboResult getSeckillRange(Long id) {
        try {
            QueryWrapper<EsSeckillRange> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSeckillRange::getId, id);
            EsSeckillRange seckillRange = this.seckillRangeMapper.selectOne(queryWrapper);
            EsSeckillRangeDO seckillRangeDO = new EsSeckillRangeDO();
            if (seckillRange == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(seckillRange, seckillRangeDO);
            return DubboResult.success(seckillRangeDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param seckillRangeDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillRangeDO>
     */
    @Override
    public DubboPageResult getSeckillRangeList(EsSeckillRangeDTO seckillRangeDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSeckillRange> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSeckillRange> page = new Page<>(pageNum, pageSize);
            IPage<EsSeckillRange> iPage = this.page(page, queryWrapper);
            List<EsSeckillRangeDO> seckillRangeDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                seckillRangeDOList = iPage.getRecords().stream().map(seckillRange -> {
                    EsSeckillRangeDO seckillRangeDO = new EsSeckillRangeDO();
                    BeanUtil.copyProperties(seckillRange, seckillRangeDO);
                    return seckillRangeDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(seckillRangeDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSeckillRange(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSeckillRange> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSeckillRange::getId, id);
            this.seckillRangeMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult readTimeList(){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String result1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Date date1 = null;
        try {
            date1 = format.parse(result1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //日期转时间戳（毫秒）
        long today=date1.getTime();

//        String redisKey = PromotionCacheKeys.getSeckillKey(DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd"));
//        Map<Integer, List> map = this.cache.getHash(redisKey);
        Map<Integer, List> map = new HashMap<>();
        if(map.isEmpty()){

            List<EsSeckillRangeDO> esSeckillRangesList = this.seckillRangeMapper.selectSeckillRangeList(today);

            if(esSeckillRangesList.isEmpty()){
                return DubboPageResult.success(new ArrayList<TimeLineVO>());
            }

            for (EsSeckillRangeDO rangeDO:esSeckillRangesList){
                map.put(rangeDO.getRangeTime(),null);
            }
        }

        //读取系统时间的时刻
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        List<TimeLineVO> list = new ArrayList<TimeLineVO>();

        //距离时刻的集合
        List<Long> distanceTimeList = new ArrayList<>();

        //未开始的活动
        for (Map.Entry<Integer, List> entry : map.entrySet()) {
            //大于当前的小时数
            if(entry.getKey()>hour){

                String date = format.format(new Date());

                //当前时间的秒数
                long currentTime = System.currentTimeMillis() / 1000;

                //限时抢购的时刻
                String seckillTime = date+" "+entry.getKey();
                Date d = new Date();
                long timeLine = 0;
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
                try {
                    d = sf.parse(seckillTime);// 日期转换为时间戳
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeLine = d.getTime() / 1000;
                long distanceTime = timeLine-currentTime<0?0:timeLine-currentTime;
                distanceTimeList.add(distanceTime);

                TimeLineVO timeLineVO = new TimeLineVO();
                timeLineVO.setTimeText(entry.getKey()+"");
                timeLineVO.setDistanceTime(distanceTime);
                list.add(timeLineVO);
            }
        }

        //正在进行中的活动的时刻
        int currentTime = -1;

        //正在进行的活动读取
        for (Map.Entry<Integer, List> entry : map.entrySet()) {
            //如果有时间相等的则直接将当前时间，设为正在进行中活动的时刻
            if(entry.getKey()==hour){
                currentTime = hour;
                break;
            }

            //大于循环前面的时间,小于当前的时间
            if(entry.getKey()>currentTime && entry.getKey()<=hour){
                currentTime = entry.getKey();
            }
        }

        //距离时刻的数据
        Long[]  distanceTimes = new Long[distanceTimeList.size()];
        //排序
        distanceTimeList.toArray(distanceTimes);
        long distanceTime = distanceTimes.length>1?distanceTimes[0]:0;

        //如果当前时间大于等于0
        if(currentTime>=0){
            //正在进行中的活动
            TimeLineVO timeLine = new TimeLineVO();
            timeLine.setTimeText(currentTime+"");
            timeLine.setDistanceTime(0L);
            timeLine.setNextDistanceTime(distanceTime);
            list.add(0,timeLine);
        }

        return DubboPageResult.success(list);
    }

    @Override
    public DubboPageResult<EsSeckillRangeDO> getSeckillRangeBySeckillId(Long seckillId) {
        QueryWrapper<EsSeckillRange> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EsSeckillRange::getSeckillId,seckillId);

        List<EsSeckillRange> esSeckillRanges = this.seckillRangeMapper.selectList(wrapper);
        List<EsSeckillRangeDO> esSeckillRangeDOS = BeanUtil.copyList(esSeckillRanges, EsSeckillRangeDO.class);
        return DubboPageResult.success((long) esSeckillRanges.size(),esSeckillRangeDOS);
    }


}
