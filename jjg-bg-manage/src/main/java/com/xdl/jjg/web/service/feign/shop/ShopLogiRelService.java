package com.xdl.jjg.web.service.feign.shop;

import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface ShopLogiRelService {

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    @DeleteMapping("/adminDeleteShopLogiRel")
    DubboResult adminDeleteShopLogiRel(@RequestParam("id") Long id);




}
