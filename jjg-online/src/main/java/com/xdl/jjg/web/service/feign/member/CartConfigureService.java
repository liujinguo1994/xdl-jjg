package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCartConfigureDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface CartConfigureService {

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:20:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    DubboResult<EsCartConfigureDO> getCartConfigure();
}
