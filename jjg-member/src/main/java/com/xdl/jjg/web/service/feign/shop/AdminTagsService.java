package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsAdminTagsDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-shop")
public interface AdminTagsService {

    /**
     * 根据id获取数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @GetMapping("/getAdminTagsById")
    DubboResult<EsAdminTagsDO> getAdminTags(Long id);
}
