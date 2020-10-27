package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberAddressDO;
import com.xdl.jjg.response.service.DubboResult;

public interface MemberAddressService {

    /**
     * 查询默认收货地址
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> getDefaultMemberAddress(Long memberId);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> getMemberAddress(Long id);
}
