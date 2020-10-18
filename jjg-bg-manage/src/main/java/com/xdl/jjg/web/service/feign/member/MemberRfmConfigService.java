package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberRfmConfigDO;
import com.jjg.member.model.dto.EsRfmConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "jjg-member")
public interface MemberRfmConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberRfmConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberRfmConfigDO>
     */
    @PostMapping("/insertMemberRfmConfig")
    DubboResult insertMemberRfmConfig(@RequestBody EsRfmConfigDTO memberRfmConfigDTO);
    /**
     * 根据查询所有天数间隔
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberRfmConfigDO>
     */
    @GetMapping("/getMemberRfmConfigListInfo")
    DubboPageResult<EsMemberRfmConfigDO> getMemberRfmConfigListInfo();

}
