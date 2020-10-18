package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsLogiCompanyDO;
import com.jjg.system.model.dto.EsLogiCompanyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsLogiCompanyService {

    /**
     * 插入数据
     *
     * @param logiCompanyDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     * @since 2019-06-04
     */
    DubboResult insertLogiCompany(EsLogiCompanyDTO logiCompanyDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param logiCompanyDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     * @since 2019-06-04
     */
    DubboResult updateLogiCompany(EsLogiCompanyDTO logiCompanyDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     * @since 2019-06-04
     */
    DubboResult<EsLogiCompanyDO> getLogiCompany(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param logiCompanyDTO DTO
     * @param pageSize       行数
     * @param pageNum        页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsLogiCompanyDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsLogiCompanyDO> getLogiCompanyList(EsLogiCompanyDTO logiCompanyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     * @since 2019-06-04
     */
    DubboResult deleteLogiCompany(Long id);

    /**
     * 查询物流公司列表
     */
    DubboPageResult<EsLogiCompanyDO> getLogiCompanyList();


    /**
     * 通过code获取物流公司
     *
     * @param code 物流公司code
     * @return 物流公司
     */
    DubboResult<EsLogiCompanyDO> getLogiByCode(String code);

    /**
     * 根据名字查询
     */
    DubboResult<EsLogiCompanyDO> getByName(String name);
}
