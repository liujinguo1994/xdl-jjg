package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsComplaintDO;
import com.jjg.member.model.domain.EsComplaintOrderDO;
import com.jjg.member.model.dto.EsComplaintDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface ComplaintService {


    @PostMapping("/insertComplaintBuyer")
    DubboResult insertComplaintBuyer(@RequestBody EsComplaintDTO esComplaintDTO);


    @PostMapping("/updateComplaint")
    DubboResult updateComplaint(@RequestBody EsComplaintDTO esComplaintDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    @GetMapping("/getComplaint")
    DubboResult<EsComplaintDO> getComplaint(@RequestParam("id") Long id);

    /**
     * 买家端-根据查询条件查询列表分页
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param memberId  memberId
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    @GetMapping("/getComplaintListBuyerPage")
    DubboPageResult<EsComplaintOrderDO> getComplaintListBuyerPage(@RequestParam("memberId") Long memberId, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
}
