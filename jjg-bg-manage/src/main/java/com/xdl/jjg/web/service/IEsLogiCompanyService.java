package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.model.dto.EsLogiCompanyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsLogiCompanyService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param logiCompanyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    DubboResult insertLogiCompany(EsLogiCompanyDTO logiCompanyDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param logiCompanyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    DubboResult updateLogiCompany(EsLogiCompanyDTO logiCompanyDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    DubboResult<EsLogiCompanyDO> getLogiCompany(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param logiCompanyDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsLogiCompanyDO>
     */
    DubboPageResult<EsLogiCompanyDO> getLogiCompanyList(EsLogiCompanyDTO logiCompanyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    DubboResult deleteLogiCompany(Long id);

    /**
     * 查询物流公司列表
     */
    DubboPageResult<EsLogiCompanyDO> getLogiCompanyList();


    /**
     * 通过code获取物流公司
     * @param code 物流公司code
     * @return 物流公司
     */
    DubboResult<EsLogiCompanyDO> getLogiByCode(String code);

    /**
     * 根据名字查询
     */
    DubboResult<EsLogiCompanyDO> getByName(String name);
}
