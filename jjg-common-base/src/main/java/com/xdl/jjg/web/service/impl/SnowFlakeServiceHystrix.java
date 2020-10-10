package com.xdl.jjg.web.service.impl;


import com.xdl.jjg.response.service.ErrorCodeEnum;
import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.util.SnowFlake;
import com.xdl.jjg.web.service.SnowFlakeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/1日
 * @Description 获取自增id
 */
@Component
public class SnowFlakeServiceHystrix implements SnowFlakeService {
    private static Logger log = Logger.getLogger(SnowFlakeServiceHystrix.class);

    @Override
    public RestResult getSingleSnowId() {
        log.error("SnowFlakeS***erviceHystrix.getSingleSnowId():************进入断路由*********");
        return RestResult.ok(new SnowFlake(2,3).nextId());
    }

    @Override
    public RestResult restoreFromPrivateKey(String privateKey, String walletPSW) {
        log.error("进入熔断器");
        return RestResult.fail(ErrorCodeEnum.RES_SYS_ERROR.getErrorCode(), ErrorCodeEnum.RES_SYS_ERROR.getErrorMassage());
    }

    @Override
    public RestResult restoreFromMnemonic(String mnemonic, String walletPSW) {
        log.error("进入熔断器");
        return RestResult.fail(ErrorCodeEnum.RES_SYS_ERROR.getErrorCode(), ErrorCodeEnum.RES_SYS_ERROR.getErrorMassage());
    }
}
