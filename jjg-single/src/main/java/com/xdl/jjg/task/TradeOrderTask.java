package com.xdl.jjg.task;

import com.xdl.jjg.enums.NoTypeEnum;
import com.xdl.jjg.mapper.auction.AuctionRecordMapper;
import com.xdl.jjg.mapper.auction.AuctionRewardRecordMapper;
import com.xdl.jjg.mapper.order.TradeOrderMapper;
import com.xdl.jjg.mapper.user.SysUserAccountMapper;
import com.xdl.jjg.pojo.auction.AuctionRecord;
import com.xdl.jjg.pojo.auction.AuctionRecordExample;
import com.xdl.jjg.pojo.auction.AuctionRewardRecord;
import com.xdl.jjg.pojo.auction.AuctionRewardRecordExample;
import com.xdl.jjg.pojo.order.TradeOrder;
import com.xdl.jjg.pojo.order.TradeOrderExample;
import com.xdl.jjg.pojo.user.SysUserAccount;
import com.xdl.jjg.pojo.user.SysUserAccountExample;
import com.xdl.jjg.util.SnowFlakeProperties;
import com.xdl.jjg.web.service.RedisService;
import com.xdl.jjg.web.service.TradeOrderService;
import com.xdl.jjg.web.service.impl.SnowFlakeImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/8日
 * @Description 订单功能，确认收货和创建订单
 */
@Service
public class TradeOrderTask {

    @Autowired
    private SysUserAccountMapper userAccountMapper;
    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private AuctionRewardRecordMapper auctionRewardRecordMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AuctionRecordMapper auctionRecordMapper;
    @Autowired
    private SnowFlakeProperties snowFlakeProperties;

    /**
     * 15天的确认收货
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder() {
        LocalDateTime beginTime = LocalDateTime.of(LocalDate.now().minusDays(15), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now().minusDays(15), LocalTime.MAX);

        TradeOrderExample example = new TradeOrderExample();
        example.createCriteria()
                .andOrderStatusEqualTo(OrderStatusEnum.WAITING_RECEIPT.getCode())
                .andDeliveryTimeBetween(beginTime, endTime);
        List<TradeOrder> tradeOrderList = tradeOrderMapper.selectByExample(example);
        List<String> orderIds = tradeOrderList.stream().map(p -> p.getOrderSn()).collect(Collectors.toList());
        if (orderIds == null || orderIds.isEmpty()) {
            return;
        }
        tradeOrderService.confirm(orderIds);
    }


    @Transactional(rollbackFor = Exception.class)
    public void confirmWithdrawal() {
        LocalDateTime beginTime = LocalDateTime.of(LocalDate.now().minusDays(15), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now().minusDays(15), LocalTime.MAX);
        //查询订单15天前
        TradeOrderExample orderExample = new TradeOrderExample();
        orderExample.createCriteria()
                .andOrderStatusEqualTo(OrderStatusEnum.TRADE_SUCCESS.getCode())
                .andDeliveryTimeBetween(beginTime, endTime);
        List<TradeOrder> tradeOrderList = tradeOrderMapper.selectByExample(orderExample);
        List<String> recordNos = tradeOrderList.stream().map(p -> p.getAuctionRecordNo()).collect(Collectors.toList());
        if (recordNos == null || recordNos.isEmpty()) {
            return;
        }

        List<AuctionRecord> auctionRecordList = auctionRecordMapper.selectByExample(new AuctionRecordExample()
                .createCriteria().andAuctionRecordNoIn(recordNos).example());
        if (auctionRecordList == null || auctionRecordList.isEmpty()) {
            return;
        }
        List<String> auctionNos = auctionRecordList.stream().map(p -> p.getAuctionNo()).collect(Collectors.toList());

        AuctionRewardRecordExample rewardRecordExample = new AuctionRewardRecordExample();
        rewardRecordExample.createCriteria().andAuctionNoIn(auctionNos).andTypeEqualTo((short) 1);
        List<AuctionRewardRecord> auctionRewardRecordList = auctionRewardRecordMapper.selectByExample(rewardRecordExample);
        if (auctionRewardRecordList == null || auctionRewardRecordList.isEmpty()) {
            return;
        }

        AuctionRewardRecordExample recordExample = new AuctionRewardRecordExample();
        recordExample.createCriteria().andAuctionNoIn(auctionNos).andTypeEqualTo((short) 4);
        List<AuctionRewardRecord> ketiRewardRecordList = auctionRewardRecordMapper.selectByExample(recordExample);
        Map<String, AuctionRewardRecord> ketiRewardRecordListMap = ketiRewardRecordList.stream().collect(Collectors.toMap(p -> p.getAuctionRecordNo(), p -> p,(name1,name2)->name2));

        //用户id--奖励记录
        Map<String, List<AuctionRewardRecord>> auctionRecordMap = auctionRewardRecordList.stream().filter(p -> !ketiRewardRecordListMap.containsKey(p.getAuctionRecordNo())).collect(Collectors.groupingBy(p -> p.getUserNo()));

        //修改用户可提现金额
        for (Map.Entry<String, List<AuctionRewardRecord>> map : auctionRecordMap.entrySet()) {
            String userNo = map.getKey();
            List<AuctionRewardRecord> recordList = map.getValue();
            DoubleSummaryStatistics big = recordList.stream()
                    .collect(Collectors.summarizingDouble(p -> p.getIncomeAmount() == null ? 0 : p.getIncomeAmount().doubleValue()));
            Double sum = big.getSum();

            SysUserAccount userAccountVO = userAccountMapper.selectOneByExample(new SysUserAccountExample()
                    .createCriteria().andUserNoEqualTo(userNo).example());
            SysUserAccount sysUserAccount = SysUserAccount.builder()
                    .expectedReward(userAccountVO.getExpectedReward().subtract(new BigDecimal(sum)))
                    .cashWithdrawalAmount(userAccountVO.getCashWithdrawalAmount().add(new BigDecimal(sum)))
                    .userNo(userAccountVO.getUserNo())
                    .build();
            SysUserAccountExample example = new SysUserAccountExample();
            example.createCriteria().andUserNoEqualTo(sysUserAccount.getUserNo());
            sysUserAccount.setId(null);
            userAccountMapper.updateByExampleSelective(sysUserAccount, example);
            //删除用户缓存信息
            redisService.delKey("sysUserAccountVO:" + sysUserAccount.getUserNo());
        }

        //新增奖励-2.预期奖励-发货后15天，4.可提奖励-发货后15天）
        for (AuctionRewardRecord rewardRecord : auctionRewardRecordList) {
            if (ketiRewardRecordListMap.containsKey(rewardRecord.getAuctionRecordNo())) {
                continue;
            }
            AuctionRewardRecord yuqiAuctionRewardRecord = AuctionRewardRecord.builder().build();
            AuctionRewardRecord ketiAuctionRewardRecord = AuctionRewardRecord.builder().build();
            BeanUtils.copyProperties(rewardRecord, yuqiAuctionRewardRecord);
            BeanUtils.copyProperties(rewardRecord, ketiAuctionRewardRecord);
            SnowFlakeImpl snowFlake = new SnowFlakeImpl(snowFlakeProperties.getDatacenterId(), snowFlakeProperties.getMachineId());
            yuqiAuctionRewardRecord.setId(null);
            ketiAuctionRewardRecord.setId(null);
            yuqiAuctionRewardRecord.setRewardRecordNo(NoTypeEnum.AUCTION_REWARD_NO.getCode() + snowFlake.nextId());
            ketiAuctionRewardRecord.setRewardRecordNo(NoTypeEnum.AUCTION_REWARD_NO.getCode() + snowFlake.nextId());
            yuqiAuctionRewardRecord.setType((short) 2);
            ketiAuctionRewardRecord.setType((short) 4);
            auctionRewardRecordMapper.insertSelective(yuqiAuctionRewardRecord);
            auctionRewardRecordMapper.insertSelective(ketiAuctionRewardRecord);
        }

    }


}
