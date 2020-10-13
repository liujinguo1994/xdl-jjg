package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsRegionsDO;
import com.xdl.jjg.model.dto.EsRegionsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsRegionsService {

    /**
     * 插入数据
     *
     * @param regionsDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     * @since 2019-06-04
     */
    DubboResult insertRegions(EsRegionsDTO regionsDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param regionsDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     * @since 2019-06-04
     */
    DubboResult updateRegions(EsRegionsDTO regionsDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     * @since 2019-06-04
     */
    DubboResult<EsRegionsDO> getRegions(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param regionsDTO DTO
     * @param pageSize   行数
     * @param pageNum    页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsRegionsDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsRegionsDO> getRegionsList(EsRegionsDTO regionsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     * @since 2019-06-04
     */
    DubboResult deleteRegions(Long id);

    //获取某地区的子地区
    DubboResult<List<EsRegionsDO>> getChildrenById(Long id);

    /**
     * 根据深度获取组织地区数据结构的数据
     */
    DubboPageResult<EsRegionsDO> getRegionByDepth(Integer depth);
}
