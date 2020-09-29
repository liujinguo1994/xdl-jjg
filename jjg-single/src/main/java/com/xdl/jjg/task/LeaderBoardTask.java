package com.xdl.jjg.task;

import com.xdl.jjg.mapper.auction.AuctionExpandMapper;
import com.xdl.jjg.mapper.leaderboard.SysAuctionLeaderboardExpandMapper;
import com.xdl.jjg.mapper.user.SysUserRelationshipMapper;
import com.xdl.jjg.model.vo.AuctionDetailVO;
import com.xdl.jjg.pojo.leaderboard.SysAuctionLeaderboard;
import com.xdl.jjg.pojo.leaderboard.SysAuctionLeaderboardDetail;
import com.xdl.jjg.pojo.user.SysUserRelationship;
import com.xdl.jjg.pojo.user.SysUserRelationshipExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/8日
 * @Description 加加榜单定时任务
 */
@Service
public class LeaderBoardTask {

    @Autowired
    private AuctionExpandMapper auctionExpandMapper;
    @Autowired
    private SysUserRelationshipMapper sysUserRelationshipMapper;
    @Autowired
    private SysAuctionLeaderboardExpandMapper sysAuctionLeaderboardExpandMapper;



    /**
     * 每天加加榜的生成
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void leaderBoardTop() {

        //取出昨天的最大值日期和自小日期
        LocalDate now = LocalDate.now().minusDays(1L);
        LocalDateTime startTime = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(now, LocalTime.MAX);
        //查询昨天出价者,上级id
        List<AuctionDetailVO> auctionVOList = auctionExpandMapper.countAuctionRecord(startTime, endTime);
        if (auctionVOList != null && !auctionVOList.isEmpty()) {

            //新增榜单明细
            Map<String, Integer> userTimesMap = auctionVOList.stream().collect(Collectors.toMap(p -> p.getUserNo(), p -> p.getCid()));
            //查询出价者上级
            List<String> uIds = auctionVOList.stream().map(p -> p.getSkus()).collect(Collectors.toList());
            SysUserRelationshipExample example = new SysUserRelationshipExample();
            example.createCriteria().andUserNoIn(uIds).andTypeEqualTo(1);
            List<SysUserRelationship> relationshipList = sysUserRelationshipMapper.selectByExample(example);
            Map<String, Integer> userTotalTimesMap = new HashMap<>();
            userTotalTimesMap.putAll(userTimesMap);
            relationshipList.forEach(p -> {
                String userId = p.getUserNo();
                String pidId = p.getUserPno();
                Integer userTimes = userTimesMap.get(userId);
                Integer totalPidTimes = userTotalTimesMap.get(pidId) == null ? 0 : userTotalTimesMap.get(pidId);
                userTotalTimesMap.put(pidId, totalPidTimes + userTimes);
            });

            List<SysAuctionLeaderboardDetail> leaderboardDetailList = new ArrayList<>();
            userTotalTimesMap.forEach((id, times) -> {
                Integer userTimes = userTimesMap.get(id) == null ? 0 : userTimesMap.get(id);
                SysAuctionLeaderboardDetail boardDetail = SysAuctionLeaderboardDetail
                        .builder()
                        .userNo(id)
                        .bidTimes(userTimes)
                        .coreFansBidTimes(times - userTimes)
                        .totalBidTimes(times)
                        .statisticalDate(now)
                        .updateTime(LocalDateTime.now())
                        .createTime(LocalDateTime.now())
                        .build();
                leaderboardDetailList.add(boardDetail);
            });

            sysAuctionLeaderboardExpandMapper.saveBatchLeaderBoardDetail(leaderboardDetailList);

        }


        //新增榜单操作
        List<SysAuctionLeaderboard> leaderBoardList = sysAuctionLeaderboardExpandMapper.listThirtyBoard(now.minusDays(30), now);
        LocalDateTime dateTime = LocalDateTime.now();
        Integer rank = 1;
        for (SysAuctionLeaderboard leaderBoard : leaderBoardList) {
            leaderBoard.setCreateTime(dateTime);
            leaderBoard.setUpdateTime(dateTime);
            leaderBoard.setRecordingDate(now);
            leaderBoard.setRank(rank++);
        }
        //新增榜单
        sysAuctionLeaderboardExpandMapper.saveBatchLeaderBoard(leaderBoardList);

    }



}
