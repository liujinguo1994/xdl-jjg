package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsHalfPriceDO;
import com.jjg.trade.model.dto.EsHalfPriceDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 第二件半价活动表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsHalfPriceService {

    /**
     * 插入数据
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    DubboResult insertHalfPrice(EsHalfPriceDTO halfPriceDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @param id           主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    DubboResult updateHalfPrice(EsHalfPriceDTO halfPriceDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    DubboResult<EsHalfPriceDO> getHalfPrice(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param halfPriceDTO 第二件半价活动表DTO
     * @param pageSize     行数
     * @param pageNum      页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsHalfPriceDO>
     */
    DubboPageResult<EsHalfPriceDO> getHalfPriceList(EsHalfPriceDTO halfPriceDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsHalfPriceDO>
     */
    DubboResult deleteHalfPrice(Long id);

    /**
     * 从缓存里根据id获取第二件半价活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 16:21:21
     * @param id    第二件半价活动id
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult getHalfPriceForCache(Long id);

    DubboResult<EsHalfPriceDO> getSellerHalfPrice(Long id);

    DubboResult<EsHalfPriceDO> getHalfPriceByTime(Long shopId);
}
