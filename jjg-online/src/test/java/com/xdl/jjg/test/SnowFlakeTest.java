package com.xdl.jjg.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@EnableFeignClients
@ActiveProfiles("dev")
public class SnowFlakeTest {

    @Autowired
    private AuctionRewardRecordServiceImpl auctionRewardRecordService;

    @Test
    public void test1(){
        System.out.println("*************gg*******s");
        Object s = auctionRewardRecordService.getUserNo("USER374888958020300800","USER374909032886185984");

        System.out.println("********************s"+s);

    }


}
