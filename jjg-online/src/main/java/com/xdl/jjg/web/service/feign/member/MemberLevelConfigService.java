package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.xdl.jjg.response.service.DubboResult;

public interface MemberLevelConfigService {

    /**
     * 根据成长值查询会员等级
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    DubboResult<EsMemberLevelConfigDO> getMemberLevelByGrade(Integer grade);
}
