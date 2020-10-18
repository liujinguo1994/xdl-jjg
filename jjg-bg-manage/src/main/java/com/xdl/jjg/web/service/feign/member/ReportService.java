package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsReportDO;
import com.jjg.member.model.dto.ReportQueryParam;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface ReportService {



    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 10:37:16
     * @param reportQueryParam  举报查询
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsReportDO>
     */
    @GetMapping("/getReportList")
    DubboPageResult<EsReportDO> getReportList(@RequestBody ReportQueryParam reportQueryParam, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 14:40:10
     * @param dealContent    处理内容
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @PostMapping("/dealReport")
    DubboResult dealReport(@RequestParam("id") Long id,@RequestParam("state") String state,@RequestParam("dealContent") String dealContent);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 10:40:44
     * @param ids    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    @DeleteMapping("/deleteReport")
    DubboResult deleteReport(@RequestParam("ids") Integer[] ids);

}
