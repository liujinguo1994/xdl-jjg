package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCartNumDO;
import com.jjg.member.model.dto.EsCartDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface CartService {

    /**
     * 根据会员id查询 会员购物车项数量 及 购物车id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:20:44
     * @param memberId    memberId
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @GetMapping("/getByMemberId")
    DubboResult<EsCartNumDO> getByMemberId(@RequestParam("memberId") Long memberId);

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:39:30
     * @param cartDTO    购物车DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    @PostMapping("/insertCart")
    DubboResult<Long> insertCart(@RequestBody EsCartDTO cartDTO);
}
