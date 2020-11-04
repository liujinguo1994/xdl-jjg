package com.xdl.jjg.web.service.feign.member;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface MemberActiveInfoService {
}
