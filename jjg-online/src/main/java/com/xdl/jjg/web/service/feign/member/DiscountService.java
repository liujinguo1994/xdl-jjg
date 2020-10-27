package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsDiscountDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface DiscountService {


    DubboResult<EsDiscountDO> getDiscountByCompanyCodeAndCategoryId(String companyCode, Long categoryId);
}
