package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberTokenDO;
import com.jjg.member.model.dto.EsMemberTokenDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface MemberTokenService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberTokenDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult insertMemberToken(EsMemberTokenDTO memberTokenDTO);
    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberTokenDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult updateMemberToken(EsMemberTokenDTO memberTokenDTO);


    /**
     * 根据查询条件查询
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberTokenDTO  DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    DubboResult<EsMemberTokenDO> getMemberTokenInfo(EsMemberTokenDTO memberTokenDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult<EsMemberTokenDO> getMemberToken(Long id);
}
