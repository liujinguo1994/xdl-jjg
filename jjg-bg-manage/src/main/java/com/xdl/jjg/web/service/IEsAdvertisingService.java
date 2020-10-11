package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsAdvertisingDO;
import com.xdl.jjg.model.dto.EsAdvertisingDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 广告位 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
public interface IEsAdvertisingService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-12
     * @param advertisingDTO    广告位DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    DubboResult insertAdvertising(EsAdvertisingDTO advertisingDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-12
     * @param advertisingDTO    广告位DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    DubboResult updateAdvertising(EsAdvertisingDTO advertisingDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-12
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    DubboResult<EsAdvertisingDO> getAdvertising(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2020-05-12
     * @param advertisingDTO  广告位DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsAdvertisingDO>
     */
    DubboPageResult<EsAdvertisingDO> getAdvertisingList(EsAdvertisingDTO advertisingDTO, int pageSize, int pageNum);


    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-12
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    DubboResult deleteAdvertising(Long id);


    /**
     * 根据位置查询广告位
     */
    DubboResult<EsAdvertisingDO> getListByLocation(String location);
}
