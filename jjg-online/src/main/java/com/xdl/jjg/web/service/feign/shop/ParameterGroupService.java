package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.ParameterGroupDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface ParameterGroupService {

    /**
     * 根据商品分类ID 获取参数组 参数
     * @param categoryId 商品分类ID
     * @return
     */
    DubboPageResult<ParameterGroupDO> getParameterGroupList(@RequestParam("categoryId") Long categoryId);
}
