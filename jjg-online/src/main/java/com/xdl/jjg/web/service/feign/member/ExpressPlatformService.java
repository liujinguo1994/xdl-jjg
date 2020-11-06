package com.xdl.jjg.web.service.feign.member;

import com.jjg.system.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface ExpressPlatformService {


    /**
     * 查询物流信息
     *
     * @param id 物流公司id
     * @param nu 物流单号
     * @return 物流详细
     */
    @GetMapping("/getExpressFormDetail")
    DubboResult<ExpressDetailVO> getExpressFormDetail(@RequestParam("id") Long id, @RequestParam("nu") String nu);
}
