package com.xdl.jjg.task;

import com.xdl.jjg.web.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/15日
 * @Description TODO
 */
@Service
public class AuctionTimeTask {

    @Autowired
    private RedisService redisService;


    /**
     * 分类和活动场次查询
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void listCategory() {
        System.out.println("**********场次开始*************");
        Object categoryList = redisService.getList("categoryList");
        if (categoryList != null) {
            redisService.delKey("categoryList");
        }
        Object auctionTimeList = redisService.getList("auctionTimeList");
        if (auctionTimeList != null) {
            redisService.delKey("auctionTimeList");
        }

        System.out.println("************场次结束***********");
    }


}
