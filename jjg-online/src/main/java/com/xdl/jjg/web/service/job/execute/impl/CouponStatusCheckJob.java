package com.xdl.jjg.web.service.job.execute.impl;


import com.xdl.jjg.web.service.Task.IEsCouponTaskClientService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Description: 优惠券过期状态定时任务
 * <p>
 * Created by LJG on 2019/8/29 9:17
 */
@JobHandler(value="everyDayCouponStatusJobHandler")
@Component
public class CouponStatusCheckJob extends IJobHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsCouponTaskClientService couponTaskClient;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, 开始执行优惠券状态扫描");

        /** 优惠券状态扫描 */
        try {

            this.couponTaskClient.changeTask();
        } catch (Exception e) {
            logger.error("优惠券状态修改失败", e);
        }

        return SUCCESS;
    }
}
