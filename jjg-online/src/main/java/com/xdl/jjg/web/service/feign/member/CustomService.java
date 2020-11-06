package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.cache.EsCustomCO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface CustomService {

    /**
     * 查询所有的分类，父子关系
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/21 15:57:44
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCO>
     */
    @GetMapping("/getCategoryList")
    DubboPageResult<EsCustomCO> getCategoryList(@RequestParam("shopId") Long shopId);

}
