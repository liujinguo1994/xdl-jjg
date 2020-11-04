package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberTokenDO;
import com.jjg.member.model.dto.EsMemberTokenDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberTokenService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30getMemberToken
     * @param memberTokenDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @PostMapping("/insertMemberToken")
    DubboResult insertMemberToken(@RequestBody EsMemberTokenDTO memberTokenDTO);
    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberTokenDTO   DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @PostMapping("/updateMemberToken")
    DubboResult updateMemberToken(@RequestBody EsMemberTokenDTO memberTokenDTO);


    /**
     * 根据查询条件查询
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberTokenDTO  DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    @GetMapping("/")
    DubboResult<EsMemberTokenDO> getMemberTokenInfo(@RequestBody EsMemberTokenDTO memberTokenDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    @GetMapping("/")
    DubboResult<EsMemberTokenDO> getMemberToken(@RequestParam("id") Long id);
}
