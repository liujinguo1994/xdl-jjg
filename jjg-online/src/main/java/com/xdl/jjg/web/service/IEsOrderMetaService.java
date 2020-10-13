package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsOrderMetaDO;
import com.shopx.trade.api.model.domain.dto.EsOrderMetaDTO;

/**
 * <p>
 *  订单信息拓展表 服务类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface IEsOrderMetaService {

    /**
     *保存订单信息拓展表
     * @param esOrderMetaDTO
     * @return
     */
    DubboResult<EsOrderMetaDO> insertOrderMeta(EsOrderMetaDTO esOrderMetaDTO);

    /**
     * 删除订单信息拓展表
     * @param id
     * @return
     */
    DubboResult<EsOrderMetaDO> deleteOrderMeta(Integer id);

    /**
     * 删除订单信息
     * @param orderSn
     * @return
     */
    DubboResult<EsOrderMetaDO> deleteOrderMetaByOrderSn(String orderSn);
    /**
     * 修改订单拓展详情
     * @param esOrderMetaDTO
     * @return
     */
    DubboResult<EsOrderMetaDO> updateOrderMetaMessage(EsOrderMetaDTO esOrderMetaDTO);

    /**
     * 查询订单拓展表
     * @param id
     * @return
     */
    DubboResult<EsOrderMetaDO> getOrderMetaInfo(Long id);

    /**
     *  @Description: 通过 订单编号 和 赠品类型获取 赠品信息
     *  @author: LiuJG 344009799@qq.com
     *  @Date: 2019/8/6 11:07
     */
    DubboResult<EsOrderMetaDO> getOrderMetaByOrderSnAndMetaKey(String orderSn, String type);


}
