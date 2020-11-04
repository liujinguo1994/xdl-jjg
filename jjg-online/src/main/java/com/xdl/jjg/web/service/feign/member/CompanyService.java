package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCompanyDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface CompanyService {

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:25:40
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult<EsCompanyDO> getCompany(@RequestParam("id") Long id);
    /**
     * 根据公司名称查询公司
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:25:40
     * @param companyCode    公司标志符
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult<EsCompanyDO> getCompanyByCode(@RequestParam("companyCode") String companyCode);



}
