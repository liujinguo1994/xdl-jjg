package com.xdl.jjg.web.service.feign.member;

import com.jjg.system.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.service.DubboResult;

public interface ExpressPlatformService {


    /**
     * 查询物流信息
     *
     * @param id 物流公司id
     * @param nu 物流单号
     * @return 物流详细
     */
    DubboResult<ExpressDetailVO> getExpressFormDetail(Long id, String nu);
}
