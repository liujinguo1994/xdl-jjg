package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberDepositDO;
import com.jjg.member.model.dto.EsMemberDepositDTO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface MemberDepositService {

    /**
     * 根据查询条件查询列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    DubboPageResult<EsMemberDepositDO> getMemberDepositList(EsMemberDepositDTO memberDepositDTO, int pageSize, int pageNum);
}
