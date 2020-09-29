package com.xdl.jjg.web.service.impl;

import com.xdl.jjg.util.ResponseUtil;
import com.xdl.jjg.web.service.TradeOrderService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/1日
 * @Description 获取自增id
 */
@Component
public class OrderServiceImpl implements TradeOrderService {


    private  Logger log = Logger.getLogger(this.getClass());


    @Override
    public Object confirm(@RequestParam List<String> orderIds){
        log.error("TradeOrderServiceImpl.confirm():************进入断路由************");
        return ResponseUtil.fail();
    }




}
