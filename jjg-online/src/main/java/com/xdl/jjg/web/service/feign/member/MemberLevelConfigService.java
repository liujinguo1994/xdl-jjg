package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface MemberLevelConfigService {

    /**
     * 根据成长值查询会员等级
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @GetMapping("/getMemberLevelByGrade")
    DubboResult<EsMemberLevelConfigDO> getMemberLevelByGrade(@RequestParam("grade") Integer grade);
}
