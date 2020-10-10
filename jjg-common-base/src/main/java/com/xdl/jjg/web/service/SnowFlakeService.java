package com.xdl.jjg.web.service;

import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.web.service.impl.SnowFlakeServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sanqi
 * @version 1.0
 * @data 2019年07月24日
 * @description 获取自增id
 */
@FeignClient(value = "aite-single", fallback = SnowFlakeServiceHystrix.class)
@Repository
public interface SnowFlakeService {

    /**
     * 全局唯一编号
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/snow/flake/getNextId")
    RestResult getSingleSnowId();

    @RequestMapping(method = RequestMethod.GET, value = "/createWallet/restoreFromPrivateKey")
    RestResult restoreFromPrivateKey(@RequestParam("privateKey") String privateKey, @RequestParam("walletPSW") String walletPSW);

    @RequestMapping(method = RequestMethod.GET, value = "/createWallet/restoreFromMnemonic")
    RestResult restoreFromMnemonic(@RequestParam("mnemonic") String mnemonic, @RequestParam("walletPSW") String walletPSW);
}
