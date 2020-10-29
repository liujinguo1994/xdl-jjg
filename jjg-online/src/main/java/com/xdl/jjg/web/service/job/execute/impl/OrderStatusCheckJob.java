package com.xdl.jjg.web.service.job.execute.impl;

import com.xdl.jjg.web.service.Task.IEsOrderTaskClientService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * 订单状态扫描
 * @author LIUJG
 * 2019-08-05 下午2:11
 */
@JobHandler(value="orderStatusCheckJob")
@Component
public class OrderStatusCheckJob extends IJobHandler{

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsOrderTaskClientService orderTaskClient;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, 开始执行订单状态扫描");

        /** 自动取消 */
        try {
            // 款到发货，新订单24小时未付款要自动取消
            this.orderTaskClient.cancelTask();
        } catch (Exception e) {
            logger.error("自动取消出错", e);
        }
        /** 自动确认收货 */
        try {
            // 发货之后10天要自动确认收货
            this.orderTaskClient.rogTask();
        } catch (Exception e) {
            logger.error("自动确认收货出错", e);
        }

        /** 自动完成天数 */
        try {
            // 确认收货7天后标记为完成
            this.orderTaskClient.completeTask();
        } catch (Exception e) {
            logger.error("订单7天后标记为完成出错", e);
        }

        /** 售后失效天数 */
        try {
            // 完成后一个月没有申请售后，标记为售后过期
            this.orderTaskClient.serviceTask();
        } catch (Exception e) {
            logger.error("订单标记为售后过期出错", e);
        }

        return SUCCESS;
    }

}
