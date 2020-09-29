package com.xdl.jjg.service.jobhandler;

import com.xdl.jjg.task.AuctionTimeTask;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/15日
 * @Description TODO
 */
@JobHandler(value="DelAuctionTimeJob")
@Component
public class DelAuctionTimeJob extends IJobHandler {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private AuctionTimeTask auctionTimeTask;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnT<String> execute(String param) {

        log.info("开始删除活动场次和活动定时任务********************，param："+param);
        auctionTimeTask.listCategory();
        log.info("结束删除活动场次和活动定时任务********************，param："+param);
        return SUCCESS;

    }

}
