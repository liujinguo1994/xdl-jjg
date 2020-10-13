package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsRFMTradeDO;
import com.xdl.jjg.model.dto.EsRFMTradeDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * RFM 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsRFMService {

    /**
     * 查询所有会员下单信息
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboPageResult<EsRFMTradeDO>
     */
    DubboPageResult<EsRFMTradeDO> getOrderRfmInfo();

    /**
     * 修改会员表成长值
     * @param esRFMTradeDTOList DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     */
    DubboResult updateMemberGrowthValue(List<EsRFMTradeDTO> esRFMTradeDTOList);

}
