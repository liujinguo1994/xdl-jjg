package com.xdl.jjg.service.jobhandler;

import com.xdl.jjg.task.TradeOrderTask;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/8日
 * @Description 开始15天奖励金提现
 */
@JobHandler(value="ConfirmWithdrawalJob")
@Component
public class ConfirmWithdrawalJob extends IJobHandler {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TradeOrderTask TradeOrderTask;

	@Override
	public ReturnT<String> execute(String param) {

		log.info("开始15天奖励金提现********************，param："+param);
		TradeOrderTask.confirmWithdrawal();
		log.info("结束15天奖励金提现********************，param："+param);
		return SUCCESS;

	}

}
