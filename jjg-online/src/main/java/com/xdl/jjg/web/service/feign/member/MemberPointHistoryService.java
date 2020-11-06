package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberPointHistoryDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface MemberPointHistoryService {
    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @GetMapping("/pointStatistics")
    DubboPageResult<EsMemberPointHistoryDO> pointStatistics(@RequestParam("memberId") Long memberId);
    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    @GetMapping("/getMemberPointHistoryDetail")
    DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryDetail(@RequestParam("memberId") Long memberId, @RequestParam("gradePointType") Integer gradePointType, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

}
