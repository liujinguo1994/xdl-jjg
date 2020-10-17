package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsWarnStockDO;
import com.jjg.shop.model.dto.EsWarnStockDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsWarnStockService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param warnStockDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    DubboResult<EsWarnStockDO> insertWarnStock(EsWarnStockDTO warnStockDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param warnStockDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    DubboResult<EsWarnStockDO> updateWarnStock(EsWarnStockDTO warnStockDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    DubboResult<EsWarnStockDO> getWarnStock(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param warnStockDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsWarnStockDO>
     */
    DubboPageResult<EsWarnStockDO> getWarnStockList(EsWarnStockDTO warnStockDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsWarnStockDO>
     */
    DubboResult<EsWarnStockDO> deleteWarnStock(Long id);
}
