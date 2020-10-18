package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsComplaintReasonConfigDO;
import com.jjg.member.model.dto.EsComplaintReasonConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface ComplaintReasonConfigService {

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param complaintReasonConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintReasonConfigDO>
     */
    @GetMapping("/getComplaintReasonConfigList")
    DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigList(@RequestBody EsComplaintReasonConfigDTO complaintReasonConfigDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param complaintReasonConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @PostMapping("/insertComplaintReasonConfig")
    DubboResult insertComplaintReasonConfig(@RequestBody EsComplaintReasonConfigDTO complaintReasonConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param complaintReasonConfigDTO   DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @PostMapping("/updateComplaintReasonConfig")
    DubboResult updateComplaintReasonConfig(@RequestBody EsComplaintReasonConfigDTO complaintReasonConfigDTO);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    @DeleteMapping("/deleteComplaintReasonConfig")
    DubboResult deleteComplaintReasonConfig(@RequestParam("ids") Long id);
}
