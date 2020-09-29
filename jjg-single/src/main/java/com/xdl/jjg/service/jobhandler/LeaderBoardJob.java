package com.xdl.jjg.service.jobhandler;

import com.xdl.jjg.task.LeaderBoardTask;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/8日
 * @Description 加加榜单定时任务
 */
@JobHandler(value = "LeaderBoardJob")
@Service
public class LeaderBoardJob extends IJobHandler {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private LeaderBoardTask leaderBoardTask;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnT<String> execute(String param) {

        log.info("开始加加榜定时任务********************，param：" + param);
        leaderBoardTask.leaderBoardTop();
        log.info("结束加加榜定时任务********************，param："+param);
        return SUCCESS;

    }

}
