package com.xdl.jjg.web.service;

import com.xdl.jjg.web.service.impl.OrderServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sanqi
 * @version 1.0
 * @data 2019年07月24日
 * @description 获取自增id
 */
@FeignClient(value = "jjg-online", fallback = OrderServiceImpl.class)
@Repository
public interface TradeOrderService {



    /**
     * 确认订单收货
     *
     * @param orderSns
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/trade/order/list/confirm/receipt")
    Object confirm(@RequestParam(value = "orderSns") List<String> orderSns);


}
