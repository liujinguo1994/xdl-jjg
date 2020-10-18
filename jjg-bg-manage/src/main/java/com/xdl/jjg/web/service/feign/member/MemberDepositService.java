package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberDepositDO;
import com.jjg.member.model.dto.EsQueryAdminMemberDepositDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberDepositService {

    /**
     * 根据查询会员余额明细列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    @GetMapping("/getAdminMemberDepositList")
    DubboPageResult<EsMemberDepositDO> getAdminMemberDepositList(@RequestBody EsQueryAdminMemberDepositDTO memberDepositDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
}
