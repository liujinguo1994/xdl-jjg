package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsComplaintTypeConfigDO;
import com.jjg.member.model.dto.EsComplaintTypeConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface ComplaintTypeConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param complaintTypeConfigDTO    DTO
     *
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @PostMapping("/insertComplaintTypeConfig")
    DubboResult insertComplaintTypeConfig(@RequestBody EsComplaintTypeConfigDTO complaintTypeConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param complaintTypeConfigDTO   DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @PostMapping("/updateComplaintTypeConfig")
    DubboResult updateComplaintTypeConfig(@RequestBody EsComplaintTypeConfigDTO complaintTypeConfigDTO);


    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param complaintTypeConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintTypeConfigDO>
     */
    @GetMapping("/getComplaintTypeConfigList")
    DubboPageResult<EsComplaintTypeConfigDO> getComplaintTypeConfigList(@RequestBody EsComplaintTypeConfigDTO complaintTypeConfigDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    @DeleteMapping("/deleteComplaintTypeConfig")
    DubboResult deleteComplaintTypeConfig(@RequestParam("ids") Long id);



}
