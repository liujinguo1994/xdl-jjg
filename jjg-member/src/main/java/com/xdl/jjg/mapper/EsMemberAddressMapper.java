package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsMemberAddressDO;
import com.xdl.jjg.entity.EsMemberAddress;

import java.util.List;

/**
 * <p>
 * 收货地址表 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberAddressMapper extends BaseMapper<EsMemberAddress> {

    /**
     * 查询所有收货地址
     * @param memberId
     * @return
     */
    List<EsMemberAddressDO> getMemberAddressListByMemberId(Long memberId);

}
