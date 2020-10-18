package com.xdl.jjg.web.service.feign.trade;

import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface GoodsFreightService {

    /**
     * 系统模块
     * 分类绑定运费模板的接口
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:00
     */
    @PostMapping("/saveCatShipTemplate")
    DubboResult saveCatShipTemplate(@RequestParam("id") Long id,@RequestParam("shipTemplateId") Long shipTemplateId);
}
