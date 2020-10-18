package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsComplaintDO;
import com.jjg.member.model.dto.ComplaintQueryParam;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface ComplaintService {
    /**
     * 根据查询条件查询列表分页
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param complaintQueryParam  complaintDTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    @GetMapping("/getComplaintListPage")
    DubboPageResult<EsComplaintDO> getComplaintListPage(@RequestBody ComplaintQueryParam complaintQueryParam,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:40:44
     * @param ids    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    @DeleteMapping("/deleteComplaint")
    DubboResult deleteComplaint(@RequestParam("ids") Integer[] ids);

    /**
     * 开始处理和已完成处理
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:40:10
     * @param state   状态
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    @PostMapping("/dealCompla")
    DubboResult dealCompla(@RequestParam("id") Long id,@RequestParam("state")  String state,@RequestParam("dealContent") String dealContent);

}
