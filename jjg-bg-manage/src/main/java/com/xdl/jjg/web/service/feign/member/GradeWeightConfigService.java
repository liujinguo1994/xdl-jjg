package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsGradeWeightConfigDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-member")
public interface GradeWeightConfigService {

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGradeWeightConfigDO>
     */
    @GetMapping("/getGradeWeightConfigList")
    DubboPageResult<EsGradeWeightConfigDO> getGradeWeightConfigList();
}
