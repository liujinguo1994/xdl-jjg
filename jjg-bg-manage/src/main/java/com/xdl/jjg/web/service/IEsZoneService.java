package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsZoneDO;
import com.xdl.jjg.model.dto.EsZoneDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 专区管理 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
public interface IEsZoneService {

    /**
     * 插入数据
     *
     * @param zoneDTO 专区管理DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     * @since 2020-05-12
     */
    DubboResult insertZone(EsZoneDTO zoneDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param zoneDTO 专区管理DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     * @since 2020-05-12
     */
    DubboResult updateZone(EsZoneDTO zoneDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     * @since 2020-05-12
     */
    DubboResult<EsZoneDO> getZone(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param zoneDTO  专区管理DTO
     * @param pageSize 行数
     * @param pageNum  页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsZoneDO>
     * @since 2020-05-12
     */
    DubboPageResult<EsZoneDO> getZoneList(EsZoneDTO zoneDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     * @since 2020-05-12
     */
    DubboResult deleteZone(Long id);

    /**
     * 根据专区名称获取数据
     */
    DubboResult<EsZoneDO> getByName(String zoneName);
}
