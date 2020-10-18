package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCompanyDO;
import com.jjg.member.model.dto.EsCompanyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface CompanyService {
    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:08:30
     * @param companyDTO  签约公司DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCompanyDO>
     */
    @GetMapping("/getCompanyList")
    DubboPageResult<EsCompanyDO> getCompanyList(@RequestBody EsCompanyDTO companyDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:30:35
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @DeleteMapping("/deleteCompany")
    DubboResult deleteCompany(@RequestParam("ids") Integer[] id);

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:01:30
     * @param companyDTO    签约公司DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @PostMapping("/insertCompany")
    DubboResult insertCompany(@RequestBody EsCompanyDTO companyDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:11:33
     * @param companyDTO    签约公司DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @PostMapping("/updateCompany")
    DubboResult updateCompany(@RequestBody EsCompanyDTO companyDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:25:40
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @GetMapping("/getCompany")
    DubboResult<EsCompanyDO> getCompany(@RequestParam("id") Long id);
}

