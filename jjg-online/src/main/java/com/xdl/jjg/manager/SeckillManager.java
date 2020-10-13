package com.xdl.jjg.manager;

import cn.hutool.core.date.DateUtil;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.trade.api.constant.cacheprefix.PromotionCacheKeys;
import com.shopx.trade.api.model.domain.EsSeckillApplyDO;
import com.shopx.trade.api.model.domain.EsSeckillTimetableDO;
import com.shopx.trade.api.model.domain.dto.EsSeckillApplyDTO;
import com.shopx.trade.api.model.domain.dto.EsSeckillTimelineGoodsDTO;
import com.shopx.trade.api.model.domain.vo.SeckillGoodsVO;
import com.shopx.trade.api.model.enums.SeckillGoodsApplyStatusEnum;
import com.shopx.trade.api.service.IEsSeckillApplyService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: SeckillManager
 * @Description: 限时购服务层
 * @Author: libw  981087977@qq.com
 * @Date: 6/17/2019 14:58
 * @Version: 1.0
 */
@Component
public class SeckillManager {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsSeckillApplyService seckillApplyService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsService goodsService;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获得限时购商品列表
     *
     * @param goodsId 商品ID
     * @author: libw 981087977@qq.com
     * @date: 2019/06/17 15:01:16
     * @return: com.shopx.common.model.result.ApiResponse
     */
    public SeckillGoodsVO getSeckillGoods(Long goodsId) {
        SeckillGoodsVO goodsVO = null;
        Map<String, String> map = this.getSeckillGoodsList();
        // 计算现在正在进行的场次
        int nowInSeckillTimeline = -1;
        DubboResult<EsSeckillTimetableDO> inSeckillNow = this.seckillApplyService.getInSeckillNow();
        if(inSeckillNow.isSuccess()){
            nowInSeckillTimeline = inSeckillNow.getData().getTimeline();
        }
        if(nowInSeckillTimeline == -1){
            return null;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<SeckillGoodsVO> list = JsonUtil.jsonToList(entry.getValue(), SeckillGoodsVO.class);
            if((""+nowInSeckillTimeline).equals(entry.getKey())){
                for (SeckillGoodsVO seckillGoods : list) {
                    if (seckillGoods.getGoodsId().equals(goodsId)) {
                        goodsVO = new SeckillGoodsVO();
                        BeanUtil.copyProperties(seckillGoods, goodsVO);
                        goodsVO.setSeckillStartTime(Integer.parseInt(entry.getKey()));
//                    countSeckillTime(goodsVO);
                        Integer remainQuantity = seckillGoods.getRemainQuantity();
                        remainQuantity = remainQuantity == null?seckillGoods.getSoldQuantity()-seckillGoods.getSoldNum():remainQuantity;
                        goodsVO.setRemainQuantity(remainQuantity);
                        goodsVO.setIsStart(inSeckillNow.getData().getState());
                        goodsVO.setSeckillPrice(seckillGoods.getSeckillPrice());
                        goodsVO.setDistanceEndTime((inSeckillNow.getData().getRemainTimestamp() - System.currentTimeMillis())/1000);
                        return goodsVO;
                    }
                }
            }
        }
        return goodsVO;
    }

    /**
     * 获取参加限时购的所有商品
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 11:22:25
     * @return: com.shopx.common.model.result.ApiResponse
     */
    private Map<String, String> getSeckillGoodsList() {

        //读取今天的时间
        long today = this.startOfTodDay();
        //从缓存读取限时抢购的活动的商品
        String redisKey = PromotionCacheKeys.getSeckillKey(toString(System.currentTimeMillis() / 1000, "yyyyMMdd"));
        Map<String, String> map = this.jedisCluster.hgetAll(redisKey);

        //如果redis中没有则从数据取
        if (map == null || map.isEmpty()) {
            map = new HashMap<>();

            //读取当天正在进行的活限时抢购活动的商品
            EsSeckillApplyDTO seckillApplyDTO = new EsSeckillApplyDTO();
            seckillApplyDTO.setStartDay(today);
            seckillApplyDTO.setState(SeckillGoodsApplyStatusEnum.PASS.getValue());
            DubboPageResult seckillApplyResult = this.seckillApplyService.getSeckillApplyPassList(seckillApplyDTO);
            if (!seckillApplyResult.isSuccess()) {
                throw new ArgumentException(seckillApplyResult.getCode(), seckillApplyResult.getMsg());
            }
            List<EsSeckillApplyDO> seckillApplyDOList = (List<EsSeckillApplyDO>) seckillApplyResult.getData().getList();
            List<SeckillGoodsVO> seckillGoodsVOs = new ArrayList<>();

            Map<Integer, List<SeckillGoodsVO>> collect = seckillApplyDOList.stream().map(applyDO -> {
                //查询商品
                DubboResult goodsResult = goodsService.getEsGoods(applyDO.getGoodsId());
                if (!goodsResult.isSuccess()) {
                    throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
                }
                EsGoodsCO goodsCache = (EsGoodsCO) goodsResult.getData();
                SeckillGoodsVO seckillGoods = new SeckillGoodsVO();
                seckillGoods.setSeckillStartTime(applyDO.getTimeLine());
                seckillGoods.setGoodsId(goodsCache.getId());
                seckillGoods.setGoodsName(goodsCache.getGoodsName());
                seckillGoods.setOriginalPrice(goodsCache.getMoney());
                seckillGoods.setSeckillPrice(applyDO.getMoney());
                Integer salesNum = applyDO.getSalesNum();
                salesNum = salesNum == null ? 0:salesNum;
                seckillGoods.setSoldNum(salesNum);
                seckillGoods.setSoldQuantity(applyDO.getSoldQuantity());
                seckillGoods.setRemainQuantity(applyDO.getSoldQuantity() - salesNum);
                seckillGoods.setGoodsImage(goodsCache.getOriginal());
                return seckillGoods;
            }).collect(Collectors.groupingBy(SeckillGoodsVO::getSeckillStartTime));

            for (Integer k : collect.keySet()) {
                List<SeckillGoodsVO> v = collect.get(k);
                String value = JsonUtil.objectToJson(v);
                String key = k.toString();
                map.put(key,value);
                this.jedisCluster.hset(redisKey, k.toString(),JsonUtil.objectToJson(v));
            }
        }
        return map;
    }

    /**
     * 计算限时抢购活动方法
     *
     * @param goodsVO 限时抢购商品类
     * @author: libw 981087977@qq.com
     * @date: 2019/06/17 20:19:32
     * @return: void
     * 2020年6月12日15:47:50 Kangll 已弃用
     */
    @Deprecated
    private void countSeckillTime(SeckillGoodsVO goodsVO) {

        //当前时间的小时数
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        //此商品限时抢购活动的 开始时刻
        int timeLine = goodsVO.getSeckillStartTime();

        //距离活动结束的时间
        long distanceEndTime = 0;

        //距离活动开始的时间
        long distanceStartTime = 0;


        //是否已经开始，1为开始
        int isStart = 0;

        //当前时间大于活动开始的时刻，说明已经开始，计算距离结束的时间
        if (hour >= timeLine) {
            //读取今天的结束时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            long endTime = getDateline(df.format(new Date()) + " 23:59:59") * 1000;

            //当前时间
            long currentTime = System.currentTimeMillis();
            distanceEndTime = endTime - currentTime;
            isStart = 1;
        } else {
            //当前时间
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //活动的开始时间
            long startTime = getDateline(df.format(new Date()) + " " + timeLine + ":00:00") * 1000;
            distanceStartTime = currentTime - startTime;
        }
        goodsVO.setSeckillStartTime(timeLine);
        goodsVO.setDistanceEndTime(distanceEndTime);
        goodsVO.setDistanceStartTime(distanceStartTime);
        goodsVO.setIsStart(isStart);
    }

    /**
     * 将日期格式的时间转成时间戳
     *
     * @param date    日期
     * @return
     */
    private Long getDateline(String date) {
        if ("".equals("" + date)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = new Date();
        try {
            newDate = sdf.parse(date);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newDate.getTime() / 1000;
    }

    /**
     * 当天的开始时间
     *
     * @return
     */
    private long startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date.getTime();
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