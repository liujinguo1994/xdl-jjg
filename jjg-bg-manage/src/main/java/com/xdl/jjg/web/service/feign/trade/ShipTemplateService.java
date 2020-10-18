package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsShipTemplateDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-online")
public interface ShipTemplateService {

    /**
     * 系统模块
     * 获取所有运费模板信息列表（给我提供个运费模板的下拉框数据 阮明）
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:00
     */
    @GetMapping("/getComboBox")
    DubboPageResult<EsShipTemplateDO> getComboBox();
}
