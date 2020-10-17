package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsGoodsQuantityLogDO;
import com.jjg.shop.model.dto.EsGoodsQuantityLogDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-16 09:52:06
 */
public interface IEsGoodsQuantityLogService {

    /**
     * 插入数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsQuantityLogDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsQuantityLogDO>
     */
    DubboResult insertGoodsQuantityLog(EsGoodsQuantityLogDTO goodsQuantityLogDTO);

   
    /**
     * 根据id获取数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param orderSn    订单编号
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsQuantityLogDO>
     */
    DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLog(String orderSn, Long skuId);

    DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLogByGoodsId(String orderSn, Long skuId);
}
