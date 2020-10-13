package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.dto.EsHikOrderLogDTO;

/**
 * <p>
 * 海康订单日志
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
public interface IEsHikOrderLogService {

    /**
     *
     * 插入海康订单日志
     * @auther: yuanj 5958313259@qq.com
     * @date: 2020/05/09
     * @param hikOrderLogDTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult insertHikOrderLog(EsHikOrderLogDTO hikOrderLogDTO);



}
