package com.xdl.jjg.web.service.impl;

import com.github.pagehelper.PageInfo;
import com.xdl.jjg.model.vo.AuctionRewardRecordVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/8 14:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JjgServiceBootstrap.class})
@WebAppConfiguration
public class WithdrawServiceImplTest {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private RewardService rewardService;

    @Test
    public void audit() {
        PageInfo<AuctionRewardRecordVO> pageInfo = rewardService.listManageRewardRecord(1, 10, null, null, null,null,null,null);

        System.out.println(pageInfo);
    }
}