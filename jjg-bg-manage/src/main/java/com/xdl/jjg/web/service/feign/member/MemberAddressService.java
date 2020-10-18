package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberAddressDO;
import com.jjg.member.model.dto.EsMemberAddressDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberAddressService {

    /**
     * 根据查询条件查询列表
     *
     * @param memberAddressDTO 收货地址表DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    @GetMapping("/getMemberAddressList")
    DubboPageResult<EsMemberAddressDO> getMemberAddressList(@RequestBody EsMemberAddressDTO memberAddressDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
}
