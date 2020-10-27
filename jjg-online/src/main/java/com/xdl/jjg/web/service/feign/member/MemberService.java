package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-member")
public interface MemberService {


    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMember(Long id);


    //根据登录态标识查询会员信息
    DubboResult<EsMemberDO> getMemberBySkey(String skey);

}
