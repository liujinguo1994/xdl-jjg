package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberPointHistoryDO;
import com.jjg.member.model.dto.EsMemberPointHistoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberPointHistoryService {


    /**
     * 后台-根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberPointHistoryDTO  会员积分明细DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberPointHistoryDO>
     */
    @GetMapping("/getMemberPointHistoryList")
    DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryList(@RequestBody EsMemberPointHistoryDTO memberPointHistoryDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
}
