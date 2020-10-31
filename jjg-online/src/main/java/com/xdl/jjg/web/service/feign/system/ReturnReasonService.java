package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsReturnReasonDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface ReturnReasonService {

    /**
     * 根据售后类型获取原因列表
     * 售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货退款,CHANGE_GOODS:换货,REPAIR_GOODS:维修)
     */
    DubboPageResult<EsReturnReasonDO> getByType(String refundType);
}
