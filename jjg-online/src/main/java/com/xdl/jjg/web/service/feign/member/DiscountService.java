package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsDiscountDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface DiscountService {


    @GetMapping("/getDiscountByCompanyCodeAndCategoryId")
    DubboResult<EsDiscountDO> getDiscountByCompanyCodeAndCategoryId(@RequestParam("companyCode") String companyCode,@RequestParam("categoryId")  Long categoryId);
}
