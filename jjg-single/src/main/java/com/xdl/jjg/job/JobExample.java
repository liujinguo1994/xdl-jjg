package com.xdl.jjg.job;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;


@Component
@JobHandler(value = "JobExampleHandler")
public class JobExample extends IJobHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());

    public ReturnT<String> execute(String s) throws Exception {
        logger.info("定时任务执行成功 已发送MQ");
        return null;
    }
}
