package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsComplaintDO;
import com.jjg.member.model.domain.EsComplaintOrderDO;
import com.jjg.member.model.dto.EsComplaintDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface ComplaintService {


    DubboResult insertComplaintBuyer(EsComplaintDTO esComplaintDTO);


    DubboResult updateComplaint(EsComplaintDTO esComplaintDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    DubboResult<EsComplaintDO> getComplaint(Long id);

    /**
     * 买家端-根据查询条件查询列表分页
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param memberId  memberId
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    DubboPageResult<EsComplaintOrderDO> getComplaintListBuyerPage(Long memberId, int pageSize, int pageNum);
}
