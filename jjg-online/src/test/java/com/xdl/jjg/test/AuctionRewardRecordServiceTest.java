package com.xdl.jjg.test;

import com.xdl.jjg.OnlineApplication;
import com.xdl.jjg.mapper.auction.AuctionExpandMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OnlineApplication.class})
public class AuctionRewardRecordServiceTest {

    @Autowired
    private AuctionRewardRecordService auctionRewardRecordService;
    @Autowired
    private AuctionExpandMapper auctionExpandMapper;
    @Test
    public void test1(){

//        Short type, Long accountId, Long auctionRecordId,Short rewardType;
//        auctionRewardRecordService.save((short)1,1L,1L,(short)1,new BigDecimal("100"));
    }


}
