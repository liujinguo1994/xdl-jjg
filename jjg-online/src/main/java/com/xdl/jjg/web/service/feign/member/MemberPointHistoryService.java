package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberPointHistoryDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface MemberPointHistoryService {
    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboPageResult<EsMemberPointHistoryDO> pointStatistics(Long memberId);
    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryDetail(Long memberId, Integer gradePointType, int pageSize, int pageNum);

}
